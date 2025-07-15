package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.google.inject.Provider;
import com.google.inject.Inject;
import io.vertx.core.Vertx;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.mutiny.Mutiny;

/**
 * Provider for Hibernate Reactive's Mutiny.SessionFactory.
 * This provider creates and manages the session factory for reactive operations.
 */
@Deprecated(forRemoval = true)
public class ReactiveSessionFactoryProvider implements Provider<Mutiny.SessionFactory>
{

    @Inject
    private Vertx vertx;

    @Override
    public Mutiny.SessionFactory get()
    {
        // Create the session factory from the persistence unit
        return Persistence.createEntityManagerFactory("ActivityMaster")
                       .unwrap(Mutiny.SessionFactory.class);
    }
}
