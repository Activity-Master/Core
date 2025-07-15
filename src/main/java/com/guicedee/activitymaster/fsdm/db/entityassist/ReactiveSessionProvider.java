package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.google.inject.Provider;
import com.google.inject.Inject;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Provider for Hibernate Reactive's Mutiny.Session.
 * This provider creates and manages sessions for reactive database operations.
 * 
 * Note: This implementation uses a proxy to handle the asynchronous nature of session creation.
 * In a fully reactive application, you would typically not use this provider directly,
 * but instead use ReactiveTransactionUtil.withTransaction() to execute operations within a transaction.
 */
public class ReactiveSessionProvider implements Provider<Mutiny.Session> {

    @Inject
    private Mutiny.SessionFactory sessionFactory;

    @Override
    public Mutiny.Session get() {
        // Create a proxy that will lazily get the session when needed
        return createSessionProxy();
    }

    private Mutiny.Session createSessionProxy() {
        return (Mutiny.Session) Proxy.newProxyInstance(
            Mutiny.Session.class.getClassLoader(),
            new Class<?>[] { Mutiny.Session.class },
            new LazySessionProxyHandler()
        );
    }

    /**
     * Proxy handler that lazily gets the session when a method is called.
     * This is a workaround for the asynchronous nature of session creation.
     * In practice, you should use ReactiveTransactionUtil.withTransaction() instead
     * of directly accessing the session.
     */
    private class LazySessionProxyHandler implements InvocationHandler {
        private Mutiny.Session session;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (session == null) {
                // Get the session synchronously (not recommended for production use)
                CompletableFuture<Mutiny.Session> future = new CompletableFuture<>();
                sessionFactory.openSession()
                    .subscribe().with(
                        future::complete,
                        future::completeExceptionally
                    );
                try {
                    session = future.get(); // This blocks until the session is available
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Failed to get reactive session", e);
                }
            }

            return method.invoke(session, args);
        }
    }
}
