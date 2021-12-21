package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.servlet.*;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RequestScopedThread extends Thread
{
	private static final Logger log = Logger.getLogger(RequestScopedThread.class.getName());
	private RequestScoper.CloseableScope scoper;
	
	@Override
	public final void run()
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
