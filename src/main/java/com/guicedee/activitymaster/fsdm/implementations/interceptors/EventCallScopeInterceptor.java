package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScopeProperties;
import com.guicedee.client.scopes.CallScopeSource;
import com.guicedee.client.scopes.CallScoper;
import io.smallrye.mutiny.Uni;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Establishes a call scope around methods annotated with {@link com.guicedee.activitymaster.fsdm.client.services.annotations.Event}.
 *
 * <p>This interceptor enters the call scope if one is not already present,
 * ensuring that event-related properties can be stored and propagated.</p>
 */
public class EventCallScopeInterceptor implements MethodInterceptor
{
    @Inject
    private CallScoper callScoper;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        boolean startedHere = false;
        if (!callScoper.isStartedScope())
        {
            callScoper.enter();
            startedHere = true;
        }

        try
        {
            CallScopeProperties csp = IGuiceContext.get(CallScopeProperties.class);
            if (csp.getSource() == CallScopeSource.Unknown)
            {
                csp.setSource(CallScopeSource.Event);
            }

            Object result = invocation.proceed();

            // If the result is a Uni, we don't exit the scope yet if it was started here?
            // Actually, for Uni, CallScopeUniInterceptor will handle the scope restoration
            // on subscription. The Uni created during invocation.proceed() has already
            // captured the current scope values.
            // So we can safely exit here if it's a Uni, as the snapshot is already taken.
            return result;
        }
        finally
        {
            if (startedHere)
            {
                callScoper.exit();
            }
        }
    }
}
