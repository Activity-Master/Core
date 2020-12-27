package com.guicedee.activitymaster.core;

import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.activitymaster.core.threads.TransactionalIdentifiedThread;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.JobService;

import java.util.Objects;

@Singleton
public class ActivityMasterConfiguration
{
	private static final ThreadLocal<IEnterpriseName<?>> enterpriseName = ThreadLocal.withInitial(() -> DefaultEnterprise.TestEnterprise);
	private static final ThreadLocal<ISecurityToken<?>> token = ThreadLocal.withInitial(SecurityToken::new);
	private static final ThreadLocal<Boolean> securities = ThreadLocal.withInitial(() -> true);
	private static final ThreadLocal<Boolean> async = ThreadLocal.withInitial(() -> false);
	
	private static final ThreadLocal<Boolean> creatingNew = ThreadLocal.withInitial(() -> false);
	
	public ActivityMasterConfiguration()
	{
		JobService.getInstance()
		          .setMaxQueueCount("SecurityTokenStore", 200);
	}

	public IEnterprise<?> getEnterprise()
	{
		return GuiceContext.get(IEnterpriseService.class)
		                   .getEnterprise(getEnterpriseName());
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
	
	public static ThreadLocal<Boolean> getCreatingNew()
	{
		return creatingNew;
	}
	
	public ISecurityToken<?> getToken()
	{
		return token.get();
	}

	public ISecurityToken<?> setToken(ISecurityToken<?> token)
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

	public static class ActivityMasterConfigurationDTO
	{
		private IEnterpriseName<?> enterpriseName;
		private ISecurityToken<?> token;
		private Boolean securities;
		private Boolean async;
		private Boolean doubleCheck;

		public ActivityMasterConfigurationDTO()
		{
		}

		public ActivityMasterConfigurationDTO fromCurrentThread()
		{
			enterpriseName = ActivityMasterConfiguration.get()
			                                            .getEnterpriseName();
			token = ActivityMasterConfiguration.get()
			                                   .getToken();
			securities = ActivityMasterConfiguration.get()
			                                        .isSecurityEnabled();
			async = ActivityMasterConfiguration.get()
			                                   .isAsyncEnabled();
			doubleCheck = ActivityMasterConfiguration.get()
			                                         .isDoubleCheckDisabled();
			return this;
		}

		public IEnterpriseName<?> getEnterpriseName()
		{
			return this.enterpriseName;
		}

		public ISecurityToken<?> getToken()
		{
			return this.token;
		}

		public Boolean getSecurities()
		{
			return this.securities;
		}

		public Boolean getAsync()
		{
			return this.async;
		}

		public Boolean getDoubleCheck()
		{
			return this.doubleCheck;
		}

		public void setEnterpriseName(IEnterpriseName<?> enterpriseName)
		{
			this.enterpriseName = enterpriseName;
		}

		public void setToken(ISecurityToken<?> token)
		{
			this.token = token;
		}

		public void setSecurities(Boolean securities)
		{
			this.securities = securities;
		}

		public void setAsync(Boolean async)
		{
			this.async = async;
		}

		public void setDoubleCheck(Boolean doubleCheck)
		{
			this.doubleCheck = doubleCheck;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}
			ActivityMasterConfigurationDTO that = (ActivityMasterConfigurationDTO) o;
			return Objects.equals(getEnterpriseName(), that.getEnterpriseName()) &&
			       Objects.equals(getToken(), that.getToken());
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(getEnterpriseName(), getToken());
		}

		@Override
		public String toString()
		{
			return "ActivityMasterConfigurationDTO{" +
			       "enterpriseName=" + enterpriseName +
			       ", token=" + token +
			       '}';
		}
	}
}
