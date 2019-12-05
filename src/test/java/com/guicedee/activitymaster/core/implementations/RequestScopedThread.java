package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.threads.TransactionalIdentifiedThread;
import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ScopingException;
import com.google.inject.servlet.ServletScopes;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RequestScopedThread extends TransactionalIdentifiedThread
{
	private static final Logger log = Logger.getLogger(RequestScopedThread.class.getName());
	private RequestScoper.CloseableScope scoper;
	@Override
	public final void perform()
	{
		try
		{
			scoper = ServletScopes.scopeRequest(new HashMap<>())
			                      .open();
		}catch (ScopingException T)
		{
			log.log(Level.FINER, "Unable to scope", T);
		}
		catch (Throwable T)
		{
			log.log(Level.SEVERE, "Unable to scope", T);
		}
		performRequestScoped();
		scoper.close();
	}

	public abstract void performRequestScoped();
}
