package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Actor;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Context;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Realm;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/** Posts low-level movements under an existing FSDM Event and Arrangements. */
final class TransactionSqlFixture {
    private final Pool pool;
    private final UUID system;

    /** Caller verifies current actor and FSDM row-token/domain access in this transaction. */
    public interface Authority {
        Future<Void> currentActor(SqlConnection connection, Actor actor, Context context);
        Future<Void> event(SqlConnection connection, Actor actor, Context context, UUID eventId);
        Future<Void> arrangement(SqlConnection connection, Actor actor, Context context, UUID arrangementId, int direction);
    }

    public record Call(Actor actor, Context context, Authority authority) {
        public Call {
            Objects.requireNonNull(actor);
            Objects.requireNonNull(context);
            Objects.requireNonNull(authority);
        }
    }
    public record Line(UUID arrangementId, UUID transactionTypeId, BigDecimal amount, String unit) {
        public Line {
            Objects.requireNonNull(arrangementId);
            Objects.requireNonNull(transactionTypeId);
            Objects.requireNonNull(amount);
            if (amount.signum() == 0 || amount.scale() > 8
                    || unit == null || !unit.matches("[A-Z][A-Z0-9_]{0,15}"))
                throw new IllegalArgumentException("Positive representable amount and unit required");
            if (amount.signum() < 0) throw new IllegalArgumentException("Amount must be positive; transaction type gives direction");
            amount = amount.setScale(8);
            if (amount.precision() > 38) throw new IllegalArgumentException("Amount exceeds transaction precision");
        }
    }
    public record PostedLine(int number, UUID arrangementId, UUID transactionTypeId, int direction,
                             BigDecimal amount, String unit) {}
    public record Receipt(UUID eventId, UUID operationKey, List<PostedLine> lines) {
        public Receipt { lines = List.copyOf(lines); }
    }

    TransactionSqlFixture(Pool existingPool, UUID registeredSystem) {
        pool = Objects.requireNonNull(existingPool);
        system = Objects.requireNonNull(registeredSystem);
    }

    /** An Event can be posted once. Identical retries return the existing lines. */
    public Uni<Receipt> post(Call call, UUID eventId, UUID operationKey, List<Line> lines) {
        Objects.requireNonNull(call);
        Objects.requireNonNull(eventId);
        Objects.requireNonNull(operationKey);
        lines = List.copyOf(lines);
        if (lines.size() < 2 || lines.size() > 100) throw new IllegalArgumentException("Two to 100 lines required");
        List<Line> requested = lines;
        return Uni.createFrom().completionStage(() -> pool.withTransaction(connection ->
                call.authority().currentActor(connection, call.actor(), call.context())
                        .compose(ignored -> call.authority().event(connection, call.actor(), call.context(), eventId))
                        .compose(ignored -> event(connection, call.context(), eventId)).compose(enterprise ->
                                existing(connection, eventId).compose(prior -> {
                                    if (prior != null) {
                                        if (!prior.operationKey().equals(operationKey) || !same(prior.lines(), requested))
                                            return Future.failedFuture(new IllegalStateException("Transaction event conflict"));
                                        return validateLines(connection, call, eventId, enterprise, requested).map(prior);
                                    }
                                    return validateLines(connection, call, eventId, enterprise, requested)
                                            .compose(posted -> checkFunds(connection, posted)
                                                    .compose(ignored -> insert(connection, call, eventId, operationKey, enterprise, posted)));
                                }))).toCompletionStage());
    }

    /** Authoritative sum of posted entries. Arrangement classifications may cache this as a projection. */
    public Uni<BigDecimal> balance(Call call, UUID arrangementId, String unit) {
        Objects.requireNonNull(call); Objects.requireNonNull(arrangementId);
        if (unit == null || !unit.matches("[A-Z][A-Z0-9_]{0,15}")) throw new IllegalArgumentException("Unit required");
        return Uni.createFrom().completionStage(() -> pool.withTransaction(connection ->
                call.authority().currentActor(connection, call.actor(), call.context())
                        .compose(ignored -> call.authority().arrangement(connection, call.actor(), call.context(), arrangementId, 0))
                        .compose(ignored -> scopeArrangement(connection, call.context(), arrangementId, null))
                        .compose(ignored -> connection.preparedQuery("""
                                SELECT coalesce(sum(signed_amount),0) AS balance
                                FROM transactions.entry WHERE arrangement_id=$1 AND unit=$2
                                """).execute(Tuple.of(arrangementId, unit)))
                        .map(rows -> rows.iterator().next().getBigDecimal("balance"))).toCompletionStage());
    }

    private Future<UUID> event(SqlConnection connection, Context context, UUID eventId) {
        return connection.preparedQuery("""
                SELECT e.enterpriseid FROM event.event e
                WHERE e.eventid=$1 AND e.effectivefromdate<=now() AND e.effectivetodate>now()
                  AND EXISTS (SELECT 1 FROM event.eventxeventtype x
                    JOIN event.eventtype t ON t.eventtypeid=x.eventtypeid AND t.enterpriseid=e.enterpriseid
                    WHERE x.eventid=e.eventid AND x.enterpriseid=e.enterpriseid
                      AND t.eventtypename='Transaction Event' AND x.effectivefromdate<=now()
                      AND x.effectivetodate>now())
                FOR UPDATE OF e
                """).execute(Tuple.of(eventId)).compose(rows -> {
            if (!rows.iterator().hasNext()) return denied();
            UUID enterprise = rows.iterator().next().getUUID("enterpriseid");
            if (context.realm() == Realm.WORK && !enterprise.equals(context.ownerId())) return denied();
            return Future.succeededFuture(enterprise);
        });
    }

    private Future<List<PostedLine>> validateLines(SqlConnection connection, Call call, UUID eventId,
                                                    UUID enterprise, List<Line> requested) {
        List<PostedLine> posted = new ArrayList<>();
        // Lock in a stable order across events so concurrent debits of one
        // arrangement cannot both spend the same opening balance.
        Future<Void> checked = Future.succeededFuture();
        for (UUID arrangement : requested.stream().map(Line::arrangementId).distinct().sorted().toList()) {
            checked = checked.compose(ignored -> connection.preparedQuery("""
                    SELECT arrangementid FROM arrangement.arrangement WHERE arrangementid=$1 FOR UPDATE
                    """).execute(Tuple.of(arrangement)).mapEmpty());
        }
        for (int index = 0; index < requested.size(); index++) {
            int number = index + 1;
            Line line = requested.get(index);
            checked = checked.compose(ignored -> connection.preparedQuery("""
                    SELECT direction FROM transactions.transaction_type
                    WHERE transaction_type_id=$1 AND enterprise_id=$2 AND active=true FOR SHARE
                    """).execute(Tuple.of(line.transactionTypeId(), enterprise)).compose(rows -> {
                if (!rows.iterator().hasNext()) return denied();
                int direction = rows.iterator().next().getShort("direction");
                return call.authority().arrangement(connection, call.actor(), call.context(), line.arrangementId(), direction)
                        .compose(ignored2 -> scopeArrangement(connection, call.context(), line.arrangementId(), enterprise))
                        .compose(ignored2 -> eventArrangement(connection, eventId, line.arrangementId(), enterprise))
                        .map(ignored2 -> {
                            posted.add(new PostedLine(number, line.arrangementId(), line.transactionTypeId(),
                                    direction, line.amount(), line.unit()));
                            return (Void) null;
                        });
            }));
        }
        return checked.map(ignored -> List.copyOf(posted));
    }

    private record BalanceKey(UUID arrangement, String unit) {}

    private Future<Void> checkFunds(SqlConnection connection, List<PostedLine> lines) {
        Map<BalanceKey, BigDecimal> changes = new HashMap<>();
        for (PostedLine line : lines) {
            changes.merge(new BalanceKey(line.arrangementId(), line.unit()),
                    line.amount().multiply(BigDecimal.valueOf(line.direction())), BigDecimal::add);
        }
        Future<Void> checked = Future.succeededFuture();
        for (var item : changes.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<BalanceKey, BigDecimal> entry) -> entry.getKey().arrangement())
                        .thenComparing(entry -> entry.getKey().unit())).toList()) {
            if (item.getValue().signum() >= 0) continue;
            BalanceKey key = item.getKey();
            checked = checked.compose(ignored -> connection.preparedQuery("""
                    SELECT 1 FROM arrangement.arrangementxarrangementtype xt
                    JOIN arrangement.arrangementtype t ON t.arrangementtypeid=xt.arrangementtypeid
                    WHERE xt.arrangementid=$1 AND t.arrangementtypename='Wallet'
                      AND xt.effectivefromdate<=now() AND xt.effectivetodate>now()
                    """).execute(Tuple.of(key.arrangement())).compose(walletTypes -> {
                if (!walletTypes.iterator().hasNext()) return Future.succeededFuture(); // clearing arrangement
                return connection.preparedQuery("""
                        SELECT coalesce(sum(signed_amount),0) AS balance FROM transactions.entry
                        WHERE arrangement_id=$1 AND unit=$2
                        """).execute(Tuple.of(key.arrangement(), key.unit())).compose(rows -> {
                    BigDecimal opening = rows.iterator().next().getBigDecimal("balance");
                    return opening.add(item.getValue()).signum() >= 0 ? Future.succeededFuture()
                            : Future.failedFuture(new IllegalStateException("Insufficient wallet balance"));
                });
            }));
        }
        return checked;
    }

    private Future<Void> scopeArrangement(SqlConnection connection, Context context, UUID arrangementId, UUID enterprise) {
        return connection.preparedQuery("""
                SELECT 1 FROM arrangement.arrangement a
                WHERE a.arrangementid=$1 AND ($2::uuid IS NULL OR a.enterpriseid=$2)
                  AND a.effectivefromdate<=now() AND a.effectivetodate>now()
                  AND EXISTS (SELECT 1 FROM arrangement.arrangementxarrangementtype xt
                    JOIN arrangement.arrangementtype t ON t.arrangementtypeid=xt.arrangementtypeid
                      AND t.enterpriseid=a.enterpriseid
                    WHERE xt.arrangementid=a.arrangementid AND xt.enterpriseid=a.enterpriseid
                      AND t.arrangementtypename IN ('Wallet','Wallet Clearing')
                      AND xt.effectivefromdate<=now() AND xt.effectivetodate>now())
                  AND ($3='WORK' AND a.enterpriseid=$4 OR $3<>'WORK' AND EXISTS (
                    SELECT 1 FROM arrangement.arrangementxinvolvedparty p
                    WHERE p.arrangementid=a.arrangementid AND p.involvedpartyid=$4
                      AND p.enterpriseid=a.enterpriseid
                      AND p.effectivefromdate<=now() AND p.effectivetodate>now()))
                FOR SHARE OF a
                """).execute(Tuple.of(arrangementId, enterprise, context.realm().name(), context.ownerId()))
                .compose(rows -> rows.iterator().hasNext() ? Future.succeededFuture() : denied());
    }

    private Future<Void> eventArrangement(SqlConnection connection, UUID eventId, UUID arrangementId, UUID enterprise) {
        return connection.preparedQuery("""
                SELECT 1 FROM event.eventxarrangement
                WHERE eventid=$1 AND arrangementid=$2 AND enterpriseid=$3
                  AND effectivefromdate<=now() AND effectivetodate>now() FOR SHARE
                """).execute(Tuple.of(eventId, arrangementId, enterprise))
                .compose(rows -> rows.iterator().hasNext() ? Future.succeededFuture() : denied());
    }

    private Future<Receipt> existing(SqlConnection connection, UUID eventId) {
        return connection.preparedQuery("""
                SELECT operation_key FROM transactions.entry WHERE event_id=$1 LIMIT 1
                """).execute(Tuple.of(eventId)).compose(batches -> {
            if (!batches.iterator().hasNext()) return Future.succeededFuture();
            UUID operation = batches.iterator().next().getUUID("operation_key");
            return connection.preparedQuery("""
                    SELECT line_no,arrangement_id,transaction_type_id,direction,amount,unit
                    FROM transactions.entry WHERE event_id=$1 ORDER BY line_no
                    """).execute(Tuple.of(eventId)).map(rows -> {
                List<PostedLine> lines = new ArrayList<>();
                rows.forEach(row -> lines.add(posted(row)));
                return new Receipt(eventId, operation, lines);
            });
        });
    }

    private Future<Receipt> insert(SqlConnection connection, Call call, UUID eventId, UUID operationKey,
                                   UUID enterprise, List<PostedLine> lines) {
        Future<Void> writes = Future.succeededFuture();
        for (PostedLine line : lines) {
            writes = writes.compose(next -> connection.preparedQuery("""
                    INSERT INTO transactions.entry(entry_id,event_id,line_no,arrangement_id,
                      transaction_type_id,direction,amount,unit,enterprise_id,operation_key)
                    VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10)
                    """).execute(Tuple.of(UUID.randomUUID(), eventId, line.number(), line.arrangementId(),
                    line.transactionTypeId(), line.direction(), line.amount(), line.unit(), enterprise, operationKey)).mapEmpty());
        }
        return writes.map(new Receipt(eventId, operationKey, lines));
    }

    private static boolean same(List<PostedLine> stored, List<Line> requested) {
        if (stored.size() != requested.size()) return false;
        for (int index = 0; index < stored.size(); index++) {
            PostedLine old = stored.get(index); Line next = requested.get(index);
            if (!old.arrangementId().equals(next.arrangementId()) || !old.transactionTypeId().equals(next.transactionTypeId())
                    || old.amount().compareTo(next.amount()) != 0 || !old.unit().equals(next.unit())) return false;
        }
        return true;
    }
    private static PostedLine posted(Row row) {
        return new PostedLine(row.getInteger("line_no"), row.getUUID("arrangement_id"),
                row.getUUID("transaction_type_id"), row.getShort("direction"),
                row.getBigDecimal("amount"), row.getString("unit"));
    }
    private static <T> Future<T> denied() { return Future.failedFuture(new SecurityException("Transaction unavailable in this scope")); }
}
