package com.guicedee.activitymaster.fsdm.db.entityassist;

import com.google.inject.Key;
import com.guicedee.client.CallScoper;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * A transactional callable wrapper that ensures the provided {@link Callable} runs within a transactional context.
 * This class allows for automatic transaction management and optional scope transfers while enabling the callable
 * to return results.
 * <p>
 * This implementation uses Hibernate Reactive with Vertx 5 for non-blocking transaction management.
 *
 * @param <T> The generic type parameter for the result of the {@link Callable}.
 */
@Log
@Deprecated(forRemoval = true)
public class TransactionalCallable<T> implements Callable<T> {

    /**
     * The {@link Callable} instance to be executed.
     */
    private Callable<T> callable;

    /**
     * The stack trace captured during instantiation to assist in debugging if errors occur.
     */
    private Exception stackTrace;

    /**
     * Contextual values to be transferred during execution.
     */
    private Map<Key<?>, Object> values;

    /**
     * Default timeout for transaction operations in seconds
     */
    private static final int DEFAULT_TRANSACTION_TIMEOUT_SECONDS = 60;

    /**
     * Configurable timeout for transaction operations in seconds
     */
    @Getter
    @Setter
    private int transactionTimeoutSeconds = DEFAULT_TRANSACTION_TIMEOUT_SECONDS;

    /**
     * Flag to determine if the transaction should be retried on failure
     */
    @Getter
    @Setter
    private boolean retryOnFailure = false;

    /**
     * Maximum number of retry attempts
     */
    @Getter
    @Setter
    private int maxRetries = 3;

    /**
     * Delay between retry attempts in milliseconds
     */
    @Getter
    @Setter
    private long retryDelayMs = 500;

    /**
     * Default constructor that initializes the stack trace for debugging purposes.
     */
    public TransactionalCallable() {
        this.stackTrace = new Exception();
        this.values = new HashMap<>();
    }

    /**
     * Creates a new {@link TransactionalCallable} with the specified {@link Callable}.
     *
     * @param supplier The {@link Callable} to be executed within a transactional context.
     * @param <T>      The generic type parameter for the result of the callable.
     * @return An instance of {@link TransactionalCallable}.
     */
    public static <T> TransactionalCallable<T> of(Callable<T> supplier) {
        return of(supplier, false);
    }

    /**
     * Creates a new {@link TransactionalCallable} with the specified {@link Callable}, optionally transferring the scope.
     *
     * @param supplier      The {@link Callable} to be executed within a transactional context.
     * @param transferScope If {@code true}, transfers the current scope's context values to the callable.
     * @param <T>           The generic type parameter for the result of the callable.
     * @return An instance of {@link TransactionalCallable}.
     */
    public static <T> TransactionalCallable<T> of(Callable<T> supplier, boolean transferScope) {
        var tc = IGuiceContext.get(TransactionalCallable.class);
        tc.callable = supplier;
        if (transferScope) {
            var cs = IGuiceContext.get(CallScoper.class);
            tc.values = cs.getValues();
        }
        return tc;
    }

    /**
     * Creates a new {@link TransactionalCallable} with the specified {@link Callable}, optionally transferring the scope.
     * The transferTransaction parameter is maintained for API compatibility but is ignored in this implementation
     * as transaction management is handled by Hibernate Reactive.
     *
     * @param supplier           The {@link Callable} to be executed within a transactional context.
     * @param transferScope      If {@code true}, transfers the current scope's context values to the callable.
     * @param transferTransaction Ignored in this implementation (maintained for API compatibility)
     * @param <T>                The generic type parameter for the result of the callable.
     * @return An instance of {@link TransactionalCallable}.
     */
    public static <T> TransactionalCallable<T> of(Callable<T> supplier, boolean transferScope, boolean transferTransaction) {
        // transferTransaction is ignored in this implementation as transaction management is handled by Hibernate Reactive
        return of(supplier, transferScope);
    }

    /**
     * Creates a new {@link TransactionalCallable} with the specified {@link Callable} and configures it with retry options.
     *
     * @param supplier      The {@link Callable} to be executed within a transactional context.
     * @param transferScope If {@code true}, transfers the current scope's context values to the callable.
     * @param retryOnFailure If {@code true}, the transaction will be retried on failure.
     * @param maxRetries    The maximum number of retry attempts.
     * @param retryDelayMs  The delay between retry attempts in milliseconds.
     * @param <T>           The generic type parameter for the result of the callable.
     * @return An instance of {@link TransactionalCallable}.
     */
    public static <T> TransactionalCallable<T> ofWithRetry(Callable<T> supplier, boolean transferScope, 
                                                          boolean retryOnFailure, int maxRetries, long retryDelayMs) {
        var tc = of(supplier, transferScope);
        tc.setRetryOnFailure(retryOnFailure);
        tc.setMaxRetries(maxRetries);
        tc.setRetryDelayMs(retryDelayMs);
        return tc;
    }

    /**
     * Creates a new {@link TransactionalCallable} with the specified {@link Callable} and configures it with a custom timeout.
     *
     * @param supplier      The {@link Callable} to be executed within a transactional context.
     * @param transferScope If {@code true}, transfers the current scope's context values to the callable.
     * @param timeoutSeconds The timeout for the transaction in seconds.
     * @param <T>           The generic type parameter for the result of the callable.
     * @return An instance of {@link TransactionalCallable}.
     */
    public static <T> TransactionalCallable<T> ofWithTimeout(Callable<T> supplier, boolean transferScope, int timeoutSeconds) {
        var tc = of(supplier, transferScope);
        tc.setTransactionTimeoutSeconds(timeoutSeconds);
        return tc;
    }

    /**
     * Executes the callable within a Hibernate Reactive transaction.
     * This method is used internally by the call() method.
     *
     * @return The result of the callable execution
     * @throws Exception If an error occurs during execution
     */
    private T runOnTransaction() throws Exception {
        // Get the Hibernate Reactive SessionFactory from the context
        Mutiny.SessionFactory sessionFactory = IGuiceContext.get(Mutiny.SessionFactory.class);
        if (sessionFactory == null) {
            throw new IllegalStateException("Mutiny.SessionFactory not found in the context. Make sure Hibernate Reactive is properly configured.");
        }

        int attempts = 0;
        Exception lastException = null;

        while (attempts <= (retryOnFailure ? maxRetries : 0)) {
            try {
                // Execute the callable within a transaction
                Uni<T> result = sessionFactory.withTransaction((session, tx) -> {
                    try {
                        T callResult = callable.call();
                        return Uni.createFrom().item(callResult);
                    } catch (Exception e) {
                        return Uni.createFrom().failure(e);
                    }
                });

                // Wait for the result with a timeout
                return result.await().atMost(Duration.ofSeconds(transactionTimeoutSeconds));
            } catch (Exception e) {
                lastException = e;
                attempts++;

                if (retryOnFailure && attempts <= maxRetries) {
                    log.warning("Transaction failed, retrying (" + attempts + "/" + maxRetries + "): " + e.getMessage());
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw e;
                    }
                } else {
                    log.severe("Error executing transaction: " + e.getMessage());
                    throw e;
                }
            }
        }

        // This should never be reached, but just in case
        throw lastException != null ? lastException : new RuntimeException("Transaction failed after retries");
    }

    /**
     * Executes the wrapped {@link Callable} within a transactional context.
     * Ensures transactions are managed automatically and resets state after execution.
     *
     * @return The result of the executed {@link Callable}.
     * @throws Exception If an exception occurs during execution, it is propagated with debugging information.
     */
    @Override
    public T call() throws Exception {
        if (this.callable == null) {
            log.warning("Callable is null, returning null");
            return null;
        }

        boolean started = false;
        try {
            // Transfer the scope's context values if present
            if (values != null && !values.isEmpty()) {
                CallScoper scoper = IGuiceContext.get(CallScoper.class);
                scoper.setValues(values);
                scoper.enter();
                started = true;
            }

            // Execute the callable within a transaction
            return runOnTransaction();
        } catch (Throwable e) {
            if (stackTrace != null) {
                e.addSuppressed(stackTrace);
            }
            log.severe("Transaction failed: " + e.getMessage());
            throw e;
        } finally {
            if (started) {
                try {
                    IGuiceContext.get(CallScoper.class).exit();
                } catch (Exception ex) {
                    log.warning("Error exiting call scope: " + ex.getMessage());
                }
            }
            // Reset the internal state to prevent memory leaks
            values = null;
            callable = null;
        }
    }

    /**
     * Creates a Future that executes this callable within a Vertx context.
     * This method is useful when you want to integrate with Vertx's reactive programming model directly.
     *
     * @param vertx The Vertx instance to use for execution
     * @return A Future that will complete with the result of the callable execution
     */
    public Future<T> asFuture(Vertx vertx) {
        return vertx.executeBlocking(this, false);
    }

    /**
     * Creates a Future that executes this callable within a Vertx context.
     * This method is useful when you want to integrate with Vertx's reactive programming model directly.
     *
     * @param vertx The Vertx instance to use for execution
     * @param ordered Whether the execution should be ordered
     * @return A Future that will complete with the result of the callable execution
     */
    public Future<T> asFuture(Vertx vertx, boolean ordered) {
        return vertx.executeBlocking(this, ordered);
    }

    /**
     * Sets the {@link Callable} to be executed.
     *
     * @param callable The {@link Callable} instance to be executed.
     * @return The current {@link TransactionalCallable} instance for method chaining.
     */
    public TransactionalCallable<T> setCallable(Callable<T> callable) {
        this.callable = callable;
        return this;
    }

    /**
     * Configures this TransactionalCallable to retry on failure.
     *
     * @param maxRetries The maximum number of retry attempts.
     * @param retryDelayMs The delay between retry attempts in milliseconds.
     * @return The current {@link TransactionalCallable} instance for method chaining.
     */
    public TransactionalCallable<T> withRetry(int maxRetries, long retryDelayMs) {
        this.retryOnFailure = true;
        this.maxRetries = maxRetries;
        this.retryDelayMs = retryDelayMs;
        return this;
    }

    /**
     * Configures this TransactionalCallable with a custom timeout.
     *
     * @param timeoutSeconds The timeout for the transaction in seconds.
     * @return The current {@link TransactionalCallable} instance for method chaining.
     */
    public TransactionalCallable<T> withTimeout(int timeoutSeconds) {
        this.transactionTimeoutSeconds = timeoutSeconds;
        return this;
    }

    /**
     * Disables retry on failure.
     *
     * @return The current {@link TransactionalCallable} instance for method chaining.
     */
    public TransactionalCallable<T> withoutRetry() {
        this.retryOnFailure = false;
        return this;
    }
}
