package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import org.hibernate.reactive.mutiny.Mutiny;

/**
 * Guice module for configuring Hibernate Reactive with EntityAssist.
 * This module registers the reactive session factory and session providers.
 */
public class EntityAssistReactiveModule extends AbstractModule implements IGuiceModule<EntityAssistReactiveModule> {

    @Override
    protected void configure() {
        bind(Mutiny.SessionFactory.class).toProvider(ReactiveSessionFactoryProvider.class).in(Singleton.class);
        bind(Mutiny.Session.class).toProvider(ReactiveSessionProvider.class);
    }
}
