package com.armineasy.activitymaster.activity;

import com.armineasy.activitymaster.activitymaster.threads.TransactionalIdentifiedThread;
import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ScopingException;
import com.google.inject.servlet.ServletScopes;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.logging.Level;

@Log
public abstract class RequestScopedThread extends TransactionalIdentifiedThread
{
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
