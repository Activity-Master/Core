package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Actor;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Context;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Realm;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/** Caller-owned stateless transaction boundary for immutable Event movements. */
public final class TransactionService {
    private final String provider;
    private final UUID system;

    /** Verifies current FSDM token/domain access on the supplied stateless session. */
    public interface Authority {
        Uni<Void> currentActor(Mutiny.StatelessSession session, Actor actor, Context context);
        Uni<Void> event(Mutiny.StatelessSession session, Actor actor, Context context, UUID eventId);
        Uni<Void> arrangement(Mutiny.StatelessSession session, Actor actor, Context context, UUID arrangementId, int direction);
    }

    public record Call(Actor actor, Context context, Authority authority, UUID identityToken) {
        public Call(Actor actor, Context context, Authority authority) { this(actor, context, authority, null); }
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
            if (amount.signum() <= 0 || amount.scale() > 8
                    || unit == null || !unit.matches("[A-Z][A-Z0-9_]{0,15}"))
                throw new IllegalArgumentException("Positive representable amount and unit required");
            amount = amount.setScale(8);
            if (amount.precision() > 38) throw new IllegalArgumentException("Amount exceeds transaction precision");
        }
    }
    public record PostedLine(int number, UUID arrangementId, UUID transactionTypeId, int direction,
                             BigDecimal amount, String unit) { }
    public record Receipt(UUID eventId, UUID operationKey, List<PostedLine> lines) {
        public Receipt { lines = List.copyOf(lines); }
    }

    public TransactionService(String provider, UUID system) {
        if (provider == null || provider.isBlank()) throw new IllegalArgumentException("Provider required");
        this.provider = provider;
        this.system = Objects.requireNonNull(system);
    }

    /** Idempotently provisions one enterprise-owned accounting type and its warehouse security. */
    public Uni<TransactionType> ensureType(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise,
                                            ISystems<?, ?> ownerSystem, String code, short direction,
                                            UUID... identityTokens) {
        Objects.requireNonNull(session);
        Objects.requireNonNull(enterprise);
        Objects.requireNonNull(ownerSystem);
        if (!ownerSystem.getId().equals(system)) throw new IllegalArgumentException("System mismatch");
        if (code == null || !code.matches("[a-z][a-z0-9_.-]{0,79}") || (direction != -1 && direction != 1))
            throw new IllegalArgumentException("Transaction type code and direction required");
        UUID id = UUID.nameUUIDFromBytes(("transactions:" + enterprise.getId() + ":" + code)
                .getBytes(StandardCharsets.UTF_8));
        return session.createQuery("""
                        from TransactionType t where t.enterpriseID.id=:enterprise and t.code=:code
                        """, TransactionType.class)
                .setParameter("enterprise", enterprise.getId()).setParameter("code", code)
                .getResultList().chain(existing -> {
                    if (!existing.isEmpty()) {
                        TransactionType type = existing.getFirst();
                        if (type.getDirection() != direction)
                            return Uni.createFrom().failure(new IllegalStateException("Transaction type direction changed: " + code));
                        return Uni.createFrom().item(type);
                    }
                    TransactionType type = new TransactionType().setId(id).setCode(code)
                            .setDescription(code + " movement").setDirection(direction).setActive(true);
                    type.setEnterpriseID(enterprise).setSystemID(ownerSystem)
                            .setOriginalSourceSystemID(ownerSystem);
                    IActiveFlagService<?> flags = IGuiceContext.get(IActiveFlagService.class);
                    return flags.getActiveFlag(session, enterprise, identityTokens)
                            .chain(flag -> {
                                type.setActiveFlagID(flag);
                                com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService<?> security =
                                        IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);
                                return type.builder(session).persist(type)
                                        .chain(persisted -> security.resolveDefaultGroupFolderTokens(session, ownerSystem, identityTokens)
                                                .chain(groups -> persisted.createDefaultSecurity(session, ownerSystem,
                                                        enterprise, flag, groups, identityTokens)).replaceWith(persisted));
                            });
                });
    }

    /** Run inside the caller's withStatelessTransaction; each Event is posted once. */
    public Uni<Receipt> post(Mutiny.StatelessSession session, Call call, UUID eventId,
                             UUID operationKey, List<Line> requested) {
        Objects.requireNonNull(session);
        Objects.requireNonNull(call);
        Objects.requireNonNull(eventId);
        Objects.requireNonNull(operationKey);
        requested = List.copyOf(requested);
        if (requested.size() < 2 || requested.size() > 100)
            throw new IllegalArgumentException("Two to 100 lines required");
        List<Line> lines = requested;
        return authorize(session, call, "wallet.post")
                .chain(() -> call.authority().currentActor(session, call.actor(), call.context()))
                .chain(() -> call.authority().event(session, call.actor(), call.context(), eventId))
                .chain(() -> eventEnterprise(session, call.context(), eventId))
                .chain(enterprise -> existing(session, eventId).chain(previous -> {
                    if (previous != null) {
                        if (!previous.operationKey().equals(operationKey) || !same(previous.lines(), lines))
                            return Uni.createFrom().failure(new IllegalStateException("Transaction event conflict"));
                        return validateLines(session, call, eventId, enterprise, lines).replaceWith(previous);
                    }
                    return validateLines(session, call, eventId, enterprise, lines)
                            .chain(posted -> checkBalance(session, posted)
                                    .chain(() -> insert(session, call, eventId, operationKey, enterprise, posted)));
                }));
    }

    /** Posted entries, rather than Arrangement classifications, supply the balance. */
    public Uni<BigDecimal> balance(Mutiny.StatelessSession session, Call call, UUID arrangementId, String unit) {
        Objects.requireNonNull(session);
        Objects.requireNonNull(call);
        Objects.requireNonNull(arrangementId);
        if (unit == null || !unit.matches("[A-Z][A-Z0-9_]{0,15}"))
            throw new IllegalArgumentException("Unit required");
        return authorize(session, call, "wallet.read")
                .chain(() -> call.authority().currentActor(session, call.actor(), call.context()))
                .chain(() -> call.authority().arrangement(session, call.actor(), call.context(), arrangementId, 0))
                .chain(() -> scopeArrangement(session, call.context(), arrangementId, null))
                .chain(() -> balanceValue(session, arrangementId, unit));
    }

    /** Shared wallet boundary for actions which also create FSDM rows before posting. */
    public Uni<Void> checkAccess(Mutiny.StatelessSession session, Call call, String action) {
        if (!List.of("wallet.read", "wallet.create", "wallet.post", "wallet.transfer", "wallet.deposit", "wallet.withdrawal").contains(action))
            throw new IllegalArgumentException("Unknown wallet action");
        return authorize(session, call, action)
                .chain(() -> call.authority().currentActor(session, call.actor(), call.context()));
    }

    private Uni<Void> authorize(Mutiny.StatelessSession session, Call call, String action) {
        if (call.identityToken() == null) return denied();
        return session.createNativeQuery("select enterpriseid from dbo.systems where systemid=:system", UUID.class)
                .setParameter("system", system).getResultList()
                .chain(rows -> rows.size() == 1 ? new FsdmBehaviorAuthority().check(session, system,
                        rows.getFirst(), call.actor(), call.context(), call.identityToken(), provider, action) : denied());
    }

    private Uni<UUID> eventEnterprise(Mutiny.StatelessSession session, Context context, UUID eventId) {
        return session.createNativeQuery("""
                select e.enterpriseid from event.event e
                  join event.eventxeventtype x on x.eventid=e.eventid and x.enterpriseid=e.enterpriseid
                  join event.eventtype t on t.eventtypeid=x.eventtypeid and t.enterpriseid=e.enterpriseid
                where e.eventid=:event
                  and e.effectivefromdate<=statement_timestamp() and e.effectivetodate>statement_timestamp()
                  and t.eventtypename='Transaction Event' and x.effectivefromdate<=statement_timestamp()
                  and x.effectivetodate>statement_timestamp()
                for update of e for share of x,t
                """, UUID.class).setParameter("event", eventId).getResultList()
                .chain(rows -> {
                    if (rows.isEmpty()) return denied();
                    UUID enterprise = rows.getFirst();
                    return context.realm() == Realm.WORK && !enterprise.equals(context.ownerId())
                            ? denied() : Uni.createFrom().item(enterprise);
                });
    }

    private Uni<Receipt> existing(Mutiny.StatelessSession session, UUID eventId) {
        return session.createQuery("from Transaction t where t.eventId=:event order by t.lineNumber", Transaction.class)
                .setParameter("event", eventId).getResultList()
                .map(entries -> entries.isEmpty() ? null : new Receipt(eventId, entries.getFirst().getOperationKey(),
                        entries.stream().map(t -> new PostedLine(t.getLineNumber(), t.getArrangementId(),
                                t.getTransactionTypeId(), t.getDirection(), t.getAmount(), t.getUnit())).toList()));
    }

    private Uni<List<PostedLine>> validateLines(Mutiny.StatelessSession session, Call call, UUID eventId,
                                                  UUID enterprise, List<Line> lines) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (UUID arrangement : lines.stream().map(Line::arrangementId).distinct().sorted().toList()) {
            chain = chain.chain(() -> session.createNativeQuery("select arrangementid from arrangement.arrangement "
                            + "where arrangementid=:arrangement for update", UUID.class)
                    .setParameter("arrangement", arrangement).getResultList()
                    .chain(rows -> rows.isEmpty() ? denied() : Uni.createFrom().voidItem()));
        }
        List<PostedLine> posted = new ArrayList<>();
        for (int index = 0; index < lines.size(); index++) {
            int number = index + 1;
            Line line = lines.get(index);
            chain = chain.chain(() -> session.createNativeQuery("""
                            select direction from transactions.transaction_type where transaction_type_id=:type
                              and enterprise_id=:enterprise and active=true for share
                            """, Short.class)
                    .setParameter("type", line.transactionTypeId())
                    .setParameter("enterprise", enterprise).getResultList()
                    .chain(types -> {
                        if (types.isEmpty()) return denied();
                        int direction = types.getFirst();
                        return call.authority().arrangement(session, call.actor(), call.context(),
                                        line.arrangementId(), direction)
                                .chain(() -> scopeArrangement(session, call.context(), line.arrangementId(), enterprise))
                                .chain(() -> eventArrangement(session, eventId, line.arrangementId(), enterprise))
                                .invoke(() -> posted.add(new PostedLine(number, line.arrangementId(),
                                        line.transactionTypeId(), direction, line.amount(), line.unit())));
                    }));
        }
        return chain.replaceWith(() -> List.copyOf(posted));
    }

    private Uni<Void> scopeArrangement(Mutiny.StatelessSession session, Context context,
                                       UUID arrangementId, UUID enterprise) {
        String tenantPredicate = enterprise == null ? "" : "and a.enterpriseid=:enterprise";
        boolean work = context.realm() == Realm.WORK;
        String partyJoin = work ? "" : "join arrangement.arrangementxinvolvedparty p "
                + "on p.arrangementid=a.arrangementid and p.enterpriseid=a.enterpriseid "
                + "and p.involvedpartyid=:owner and p.effectivefromdate<=statement_timestamp() and p.effectivetodate>statement_timestamp()";
        String ownerPredicate = work ? "and a.enterpriseid=:owner" : "";
        String locks = work ? "for share of a,xt,t" : "for share of a,xt,t,p";
        var query = session.createNativeQuery("""
                select 1 from arrangement.arrangement a
                  join arrangement.arrangementxarrangementtype xt
                    on xt.arrangementid=a.arrangementid and xt.enterpriseid=a.enterpriseid
                  join arrangement.arrangementtype t
                    on t.arrangementtypeid=xt.arrangementtypeid and t.enterpriseid=a.enterpriseid
                  %s
                where a.arrangementid=:arrangement
                  %s
                  and a.effectivefromdate<=statement_timestamp() and a.effectivetodate>statement_timestamp()
                  and t.arrangementtypename in ('Wallet','Wallet Clearing')
                  and xt.effectivefromdate<=statement_timestamp() and xt.effectivetodate>statement_timestamp()
                  %s
                  %s
                """.formatted(partyJoin, tenantPredicate, ownerPredicate, locks), Integer.class)
                .setParameter("arrangement", arrangementId).setParameter("owner", context.ownerId());
        if (enterprise != null) query.setParameter("enterprise", enterprise);
        return query.getResultList().chain(rows -> rows.isEmpty() ? denied() : Uni.createFrom().voidItem());
    }

    private Uni<Void> eventArrangement(Mutiny.StatelessSession session, UUID eventId,
                                       UUID arrangementId, UUID enterprise) {
        return session.createNativeQuery("""
                select 1 from event.eventxarrangement where eventid=:event and arrangementid=:arrangement
                  and enterpriseid=:enterprise and effectivefromdate<=statement_timestamp() and effectivetodate>statement_timestamp()
                  for share
                """, Integer.class).setParameter("event", eventId)
                .setParameter("arrangement", arrangementId).setParameter("enterprise", enterprise)
                .getResultList().chain(rows -> rows.isEmpty() ? denied() : Uni.createFrom().voidItem());
    }

    private record BalanceKey(UUID arrangement, String unit) { }

    private Uni<Void> checkBalance(Mutiny.StatelessSession session, List<PostedLine> lines) {
        Map<BalanceKey, BigDecimal> changes = new HashMap<>();
        for (PostedLine line : lines)
            changes.merge(new BalanceKey(line.arrangementId(), line.unit()),
                    line.amount().multiply(BigDecimal.valueOf(line.direction())), BigDecimal::add);
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (var change : changes.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<BalanceKey, BigDecimal> e) -> e.getKey().arrangement())
                        .thenComparing(e -> e.getKey().unit())).toList()) {
            if (change.getValue().signum() >= 0) continue;
            BalanceKey key = change.getKey();
            chain = chain.chain(() -> session.createNativeQuery("""
                            select 1 from arrangement.arrangementxarrangementtype xt
                            join arrangement.arrangementtype t on t.arrangementtypeid=xt.arrangementtypeid
                              and t.enterpriseid=xt.enterpriseid
                            where xt.arrangementid=:arrangement and t.arrangementtypename='Wallet'
                              and xt.effectivefromdate<=statement_timestamp() and xt.effectivetodate>statement_timestamp()
                            """, Integer.class)
                    .setParameter("arrangement", key.arrangement()).getResultList()
                    .chain(wallet -> wallet.isEmpty() ? Uni.createFrom().voidItem()
                            : balanceValue(session, key.arrangement(), key.unit())
                            .chain(current -> current.add(change.getValue()).signum() >= 0
                                    ? Uni.createFrom().voidItem()
                                    : Uni.createFrom().failure(new IllegalStateException("Insufficient wallet balance")))));
        }
        return chain;
    }

    private Uni<BigDecimal> balanceValue(Mutiny.StatelessSession session, UUID arrangementId, String unit) {
        return session.createNativeQuery("""
                select coalesce(sum(signed_amount),0) from transactions.entry
                where arrangement_id=:arrangement and unit=:unit
                """, BigDecimal.class).setParameter("arrangement", arrangementId)
                .setParameter("unit", unit).getSingleResult();
    }

    private Uni<Receipt> insert(Mutiny.StatelessSession session, Call call, UUID eventId,
                                UUID operationKey, UUID enterprise, List<PostedLine> lines) {
        Enterprise owner = new Enterprise().setId(enterprise);
        return session.createNativeQuery("select systemid from dbo.systems where systemid=:system and enterpriseid=:enterprise", UUID.class)
                .setParameter("system", system).setParameter("enterprise", enterprise).getResultList()
                .chain(systems -> {
                    if (systems.isEmpty()) return denied();
                    Systems walletSystem = new Systems().setId(system).setEnterpriseID(owner);
                        IActiveFlagService<?> flags = IGuiceContext.get(IActiveFlagService.class);
                        return flags.getActiveFlag(session, owner)
                                .chain(flag -> role(session, enterprise, "WalletTransactionType", "TransactionXTransactionType")
                                        .chain(typeRole -> role(session, enterprise, "PostingActor", "TransactionXInvolvedParty")
                                                .chain(actorRole -> role(session, enterprise, "AccountingArrangement", "TransactionXArrangement")
                                                        .chain(arrangementRole -> role(session, enterprise, "ParentEvent", "TransactionXEvent")
                                                                .chain(eventRole -> persistEntries(session, eventId, call.actor().partyId(),
                                                                        owner, walletSystem, (ActiveFlag) flag, typeRole, actorRole,
                                                                        arrangementRole, eventRole, lines, operationKey, call.identityToken()))))));
                });
    }

    private Uni<Classification> role(Mutiny.StatelessSession session, UUID enterprise, String name, String concept) {
        return session.createQuery("""
                        select c.id from Classification c join c.concept d
                        where c.name=:name and d.name=:concept
                          and c.enterpriseID.id=:enterprise and c.systemID.id=:system
                        """, UUID.class)
                .setParameter("name", name).setParameter("concept", concept)
                .setParameter("enterprise", enterprise).setParameter("system", system)
                .getResultList().chain(rows -> rows.size() == 1 ? Uni.createFrom().item(new Classification().setId(rows.getFirst())) : denied());
    }

    private Uni<Receipt> persistEntries(Mutiny.StatelessSession session, UUID eventId, UUID actorId,
                                        Enterprise enterprise, Systems walletSystem, ActiveFlag flag,
                                        Classification typeRole, Classification actorRole,
                                        Classification arrangementRole, Classification eventRole,
                                        List<PostedLine> lines, UUID operationKey, UUID identityToken) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (PostedLine line : lines) {
            Transaction entry = new Transaction().setId(UUID.randomUUID()).setEventId(eventId)
                    .setOperationKey(operationKey).setLineNumber(line.number()).setArrangementId(line.arrangementId())
                    .setTransactionTypeId(line.transactionTypeId()).setDirection((short) line.direction())
                    .setAmount(line.amount()).setUnit(line.unit());
            TransactionXTransactionType type = new TransactionXTransactionType().setId(UUID.randomUUID())
                    .setTransaction(entry).setType(new TransactionType().setId(line.transactionTypeId()))
                    .setClassificationID(typeRole);
            TransactionXInvolvedParty actor = new TransactionXInvolvedParty().setId(UUID.randomUUID())
                    .setTransaction(entry).setInvolvedPartyID(new InvolvedParty().setId(actorId))
                    .setClassificationID(actorRole);
            TransactionXArrangement arrangement = new TransactionXArrangement().setId(UUID.randomUUID())
                    .setTransaction(entry).setArrangementID(new Arrangement().setId(line.arrangementId()))
                    .setClassificationID(arrangementRole);
            TransactionXEvent event = new TransactionXEvent().setId(UUID.randomUUID())
                    .setTransaction(entry).setEventID(new Event().setId(eventId))
                    .setClassificationID(eventRole);
            chain = chain.chain(() -> persistSecured(session, entry, enterprise, walletSystem, flag, identityToken))
                    .chain(() -> persistSecured(session, type, enterprise, walletSystem, flag, identityToken))
                    .chain(() -> persistSecured(session, actor, enterprise, walletSystem, flag, identityToken))
                    .chain(() -> persistSecured(session, arrangement, enterprise, walletSystem, flag, identityToken))
                    .chain(() -> persistSecured(session, event, enterprise, walletSystem, flag, identityToken));
        }
        return chain.replaceWith(new Receipt(eventId, operationKey, lines));
    }

    private <R extends WarehouseSCDTable<R, ?, ?, ?>> Uni<Void> persistSecured(
            Mutiny.StatelessSession session, R row, Enterprise enterprise, Systems walletSystem, ActiveFlag flag, UUID identityToken) {
        row.setEnterpriseID(enterprise).setSystemID(walletSystem)
                .setOriginalSourceSystemID(walletSystem).setActiveFlagID(flag);
        com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService<?> tokens =
                IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);
        Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?>> scope =
                identityToken == null ? Uni.createFrom().nullItem()
                        : tokens.getSecurityToken(session, identityToken, walletSystem, identityToken)
                                .onItem().ifNull().failWith(() -> new SecurityException("Wallet identity token unavailable"));
        return scope.chain(token -> row.builder(session).persist(row)
                .chain(() -> tokens.resolveDefaultGroupFolderTokens(session, walletSystem)
                        .chain(groups -> row.createScopeRestrictedSecurity(session, walletSystem,
                                enterprise, flag, groups, token)))).replaceWithVoid();
    }

    private static boolean same(List<PostedLine> stored, List<Line> requested) {
        if (stored.size() != requested.size()) return false;
        for (int index = 0; index < stored.size(); index++) {
            PostedLine old = stored.get(index);
            Line next = requested.get(index);
            if (!old.arrangementId().equals(next.arrangementId())
                    || !old.transactionTypeId().equals(next.transactionTypeId())
                    || old.amount().compareTo(next.amount()) != 0 || !old.unit().equals(next.unit())) return false;
        }
        return true;
    }

    private static <T> Uni<T> denied() {
        return Uni.createFrom().failure(new SecurityException("Transaction unavailable in this scope"));
    }
}
