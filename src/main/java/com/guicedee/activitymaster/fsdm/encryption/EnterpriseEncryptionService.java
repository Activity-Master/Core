package com.guicedee.activitymaster.fsdm.encryption;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import org.hibernate.reactive.mutiny.Mutiny;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

/** Trusted API: caller authorizes enterprise access and owns the session/transaction. */
public final class EnterpriseEncryptionService
{
    private final EnterpriseKeyWrapper wrapper;
    private final EnterpriseKeyStore store;
    private static final SecureRandom RANDOM = new SecureRandom();

    public EnterpriseEncryptionService(EnterpriseKeyWrapper wrapper)
    {
        this(wrapper, new EnterpriseKeyStore());
    }

    public EnterpriseEncryptionService(EnterpriseKeyWrapper wrapper, EnterpriseKeyStore store)
    {
        this.wrapper = Objects.requireNonNull(wrapper);
        this.store = Objects.requireNonNull(store);
    }

    /** Requires a transaction; duplicate provisioning fails at the unique active-key index. */
    public Uni<String> provision(Mutiny.Session session, UUID enterprise)
    {
        return create(session, Objects.requireNonNull(enterprise), false);
    }

    /** Drain writers first; after commit invalidate/prepare the ring on every node. */
    public Uni<String> rotate(Mutiny.Session session, UUID enterprise)
    {
        return create(session, Objects.requireNonNull(enterprise), true);
    }

    private Uni<String> create(Mutiny.Session session, UUID enterprise, boolean rotate)
    {
        return Uni.createFrom().deferred(() -> {
            byte[] dek = new byte[32];
            RANDOM.nextBytes(dek);
            return onCallerContext(() -> wrapper.wrap(dek))
                    .chain(wrapped -> {
                        EnterpriseDataKey row = new EnterpriseDataKey(enterprise, wrapper.currentKeyReference(),
                                Base64.getEncoder().encodeToString(wrapped));
                        Uni<?> deactivate = rotate
                                ? store.deactivate(session, enterprise)
                                : Uni.createFrom().voidItem();
                        return deactivate.chain(() -> store.insert(session, row)).replaceWith(row.getId());
                    })
                    .eventually(() -> Arrays.fill(dek, (byte) 0));
        });
    }

    /** Normal entry-point preparation; administrative refreshes must use prepare explicitly. */
    public Uni<Void> ensurePrepared(Mutiny.Session session, UUID enterprise)
    {
        Objects.requireNonNull(enterprise);
        return Uni.createFrom().deferred(() -> EnterpriseKeyCache.contains(enterprise)
                ? Uni.createFrom().voidItem() : prepare(session, enterprise));
    }

    /** Use a fresh session after provisioning commits. No cache publication on partial failure. */
    public Uni<Void> prepare(Mutiny.Session session, UUID enterprise)
    {
        Objects.requireNonNull(enterprise);
        return Uni.createFrom().deferred(() -> {
            Map<String, byte[]> decoded = new LinkedHashMap<>();
            return store.load(session, enterprise)
                    .chain(rows -> {
                        if (rows.stream().anyMatch(row -> !enterprise.equals(row.getEnterpriseId())))
                            return Uni.createFrom().failure(new IllegalStateException("Key store returned a different enterprise"));
                        List<EnterpriseDataKey> active = rows.stream().filter(EnterpriseDataKey::isActive).toList();
                        if (active.size() != 1)
                            return Uni.createFrom().failure(new IllegalStateException("Enterprise must have exactly one active encryption key"));
                        Uni<Void> load = Uni.createFrom().voidItem();
                        for (EnterpriseDataKey row : rows)
                        {
                            load = load.chain(() -> onCallerContext(() -> wrapper.unwrap(row.getKeyReference(), Base64.getDecoder().decode(row.getWrappedKey())))
                                    .invoke(key -> decoded.put(row.getId(), key)).replaceWithVoid());
                        }
                        return load.invoke(() -> EnterpriseKeyCache.install(enterprise, active.getFirst().getId(), decoded));
                    })
                    .onFailure().invoke(() -> EnterpriseKeyCache.evict(enterprise))
                    .eventually(() -> decoded.values().forEach(key -> Arrays.fill(key, (byte) 0)));
        });
    }

    private static <T> Uni<T> onCallerContext(Supplier<Uni<T>> operation)
    {
        return Uni.createFrom().deferred(() -> {
            var context = Vertx.currentContext();
            Uni<T> result = operation.get();
            return context == null ? result : result.emitOn(command -> context.runOnContext(ignored -> command.run()));
        });
    }
}


