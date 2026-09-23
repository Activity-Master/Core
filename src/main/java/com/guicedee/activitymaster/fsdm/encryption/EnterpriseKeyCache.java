package com.guicedee.activitymaster.fsdm.encryption;

import java.time.Duration;
import java.util.*;

/** No I/O here. Populate only after committed wrapped keys have been unwrapped. */
public final class EnterpriseKeyCache
{
    private static final Map<UUID, Ring> RINGS = new LinkedHashMap<>(16, .75f, true);
    private static final long TTL = Duration.ofMinutes(15).toNanos();

    private EnterpriseKeyCache() { }

    private record Ring(String active, Map<String, byte[]> keys, long expires)
    {
        void destroy() { keys.values().forEach(key -> Arrays.fill(key, (byte) 0)); }
    }

    static synchronized void install(UUID enterprise, String active, Map<String, byte[]> keys)
    {
        Objects.requireNonNull(enterprise);
        if (!keys.containsKey(active)) throw new IllegalStateException("No active enterprise data key");
        keys.forEach((id, key) -> {
            if (!id.matches("[a-f0-9]{32}") || key.length != 32)
                throw new IllegalStateException("Invalid enterprise data key");
        });
        Map<String, byte[]> copy = new LinkedHashMap<>();
        keys.forEach((id, key) -> copy.put(id, key.clone()));
        evict(enterprise);
        while (RINGS.size() >= 256) evict(RINGS.keySet().iterator().next());
        RINGS.put(enterprise, new Ring(active, copy, System.nanoTime() + TTL));
    }

    public static synchronized void evict(UUID enterprise)
    {
        Ring old = RINGS.remove(enterprise);
        if (old != null) old.destroy();
    }

    private static Ring require(UUID enterprise)
    {
        if (enterprise == null) throw new IllegalStateException("Assign enterprise before encrypting or querying values");
        Ring ring = RINGS.get(enterprise);
        if (ring == null || System.nanoTime() - ring.expires >= 0)
        {
            evict(enterprise);
            throw new IllegalStateException("Prepare enterprise encryption keys before accessing protected values");
        }
        return ring;
    }

    public static synchronized boolean contains(UUID enterprise)
    {
        if (enterprise == null) return false;
        Ring ring = RINGS.get(enterprise);
        if (ring != null && System.nanoTime() - ring.expires >= 0) evict(enterprise);
        return RINGS.containsKey(enterprise);
    }

    public static synchronized String activeId(UUID enterprise) { return require(enterprise).active; }
    public static synchronized Set<String> keyIds(UUID enterprise) { return Set.copyOf(require(enterprise).keys.keySet()); }
    public static synchronized byte[] key(UUID enterprise, String id)
    {
        byte[] key = require(enterprise).keys.get(id);
        if (key == null) throw new IllegalStateException("Enterprise data key is unavailable");
        return key.clone();
    }
}
