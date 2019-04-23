package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.google.inject.Singleton;
import lombok.Getter;

@Singleton
public class ActivityMasterConfiguration
{
	public static ThreadLocal<IEnterpriseName<?>> enterpriseName = ThreadLocal.withInitial(() -> DefaultEnterprise.TestEnterprise);
	@Getter
	private final ThreadLocal<SecurityToken> token = ThreadLocal.withInitial(SecurityToken::new);

	public static Double version = 0.1;
	public static boolean requiresUpdate = false;

	private static final ThreadLocal<Boolean> securities = ThreadLocal.withInitial(() -> true);
	private static final ThreadLocal<Boolean> async = ThreadLocal.withInitial(() -> false);

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

}
