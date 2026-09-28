package com.guicedee.activitymaster.fsdm.transactions;

import java.util.UUID;

/** Verified actor and owner context used when authorizing FSDM movements. */
public final class ActivityScope {
    private ActivityScope() { }

    public enum Realm { PERSONAL, SOCIAL, WORK }

    public record Actor(UUID partyId, boolean registered) {
        public Actor {
            if (partyId == null || !registered) throw new IllegalArgumentException("Registered actor required");
        }
    }

    public record Context(Realm realm, UUID ownerId) {
        public Context {
            if (realm == null || ownerId == null) throw new IllegalArgumentException("Context required");
        }
    }
}
