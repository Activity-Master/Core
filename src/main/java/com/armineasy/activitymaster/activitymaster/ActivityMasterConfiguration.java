package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.threads.TransactionalIdentifiedThread;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.JobService;
import lombok.Data;
import lombok.Getter;

import java.util.concurrent.Callable;

@Singleton
public class ActivityMasterConfiguration
{
	private static final ThreadLocal<IEnterpriseName<?>> enterpriseName = ThreadLocal.withInitial(() -> DefaultEnterprise.TestEnterprise);
	private static final ThreadLocal<SecurityToken> token = ThreadLocal.withInitial(SecurityToken::new);
	private static final ThreadLocal<Boolean> securities = ThreadLocal.withInitial(() -> true);
	private static final ThreadLocal<Boolean> async = ThreadLocal.withInitial(() -> false);

	public static Double version = 0.1;
	public static boolean requiresUpdate = false;

	public ActivityMasterConfiguration()
	{
		JobService.getInstance().setMaxQueueCount("SecurityTokenStore",200);
	}

	public IEnterpriseName<?> getEnterpriseName()
	{
		return enterpriseName.get();
	}

	public void setEnterpriseName(IEnterpriseName<?> securityEnabled)
	{
		enterpriseName.set(securityEnabled);
	}

	public boolean isSecurityEnabled()
	{
		return securities.get();
	}

	public void setSecurityEnabled(boolean securityEnabled)
	{
		securities.set(securityEnabled);
	}

	public boolean isAsyncEnabled()
	{
		return async.get();
	}

	public void setAsyncEnabled(boolean securityEnabled)
	{
		async.set(securityEnabled);
	}

	public boolean isDoubleCheckDisabled()
	{
		return false;
	}

	public void setDoubleCheckDisabled(boolean securityEnabled)
	{

	}

	public SecurityToken getToken()
	{
		return token.get();
	}

	public SecurityToken setToken(SecurityToken token)
	{
		ActivityMasterConfiguration.token.set(token);
		return ActivityMasterConfiguration.token.get();
	}

	public static ActivityMasterConfiguration get()
	{
		return GuiceContext.get(ActivityMasterConfiguration.class);
	}

	public TransactionalIdentifiedThread spawnNewTransactionThread()
	{
		TransactionalIdentifiedThread identifiedThread = GuiceContext.get(TransactionalIdentifiedThread.class)
		                                                             .fromCurrentThread();

		return identifiedThread;
	}

	public void configureThread(ActivityMasterConfigurationDTO dto)
	{
		setEnterpriseName(dto.getEnterpriseName());
		setSecurityEnabled(dto.getSecurities());
		setToken(dto.getToken());
		setAsyncEnabled(dto.getAsync());
		setDoubleCheckDisabled(dto.getDoubleCheck());
	}

	@Data
	public static class ActivityMasterConfigurationDTO
	{
		private IEnterpriseName<?> enterpriseName;
		private SecurityToken token;
		private Boolean securities;
		private Boolean async;
		private Boolean doubleCheck;

		public ActivityMasterConfigurationDTO fromCurrentThread()
		{
			enterpriseName = ActivityMasterConfiguration.get().getEnterpriseName();
			token = ActivityMasterConfiguration.get().getToken();
			securities = ActivityMasterConfiguration.get().isSecurityEnabled();
			async = ActivityMasterConfiguration.get().isAsyncEnabled();
			doubleCheck = ActivityMasterConfiguration.get().isDoubleCheckDisabled();
			return this;
		}
	}
}
