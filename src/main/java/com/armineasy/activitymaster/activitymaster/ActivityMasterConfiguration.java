package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISecurityToken;
import com.armineasy.activitymaster.activitymaster.services.system.IEnterpriseService;
import com.armineasy.activitymaster.activitymaster.threads.TransactionalIdentifiedThread;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.JobService;

@Singleton
public class ActivityMasterConfiguration
{
	private static final ThreadLocal<IEnterpriseName<?>> enterpriseName = ThreadLocal.withInitial(() -> DefaultEnterprise.TestEnterprise);
	private static final ThreadLocal<ISecurityToken<?>> token = ThreadLocal.withInitial(SecurityToken::new);
	private static final ThreadLocal<Boolean> securities = ThreadLocal.withInitial(() -> true);
	private static final ThreadLocal<Boolean> async = ThreadLocal.withInitial(() -> false);

	public static Double version = 0.1;
	public static boolean requiresUpdate = false;

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

		public boolean equals(final Object o)
		{
			if (o == this)
			{
				return true;
			}
			if (!(o instanceof ActivityMasterConfigurationDTO))
			{
				return false;
			}
			final ActivityMasterConfigurationDTO other = (ActivityMasterConfigurationDTO) o;
			if (!other.canEqual((Object) this))
			{
				return false;
			}
			final Object this$enterpriseName = this.getEnterpriseName();
			final Object other$enterpriseName = other.getEnterpriseName();
			if (this$enterpriseName == null ? other$enterpriseName != null : !this$enterpriseName.equals(other$enterpriseName))
			{
				return false;
			}
			final Object this$token = this.getToken();
			final Object other$token = other.getToken();
			if (this$token == null ? other$token != null : !this$token.equals(other$token))
			{
				return false;
			}
			final Object this$securities = this.getSecurities();
			final Object other$securities = other.getSecurities();
			if (this$securities == null ? other$securities != null : !this$securities.equals(other$securities))
			{
				return false;
			}
			final Object this$async = this.getAsync();
			final Object other$async = other.getAsync();
			if (this$async == null ? other$async != null : !this$async.equals(other$async))
			{
				return false;
			}
			final Object this$doubleCheck = this.getDoubleCheck();
			final Object other$doubleCheck = other.getDoubleCheck();
			if (this$doubleCheck == null ? other$doubleCheck != null : !this$doubleCheck.equals(other$doubleCheck))
			{
				return false;
			}
			return true;
		}

		protected boolean canEqual(final Object other)
		{
			return other instanceof ActivityMasterConfigurationDTO;
		}

		public int hashCode()
		{
			final int PRIME = 59;
			int result = 1;
			final Object $enterpriseName = this.getEnterpriseName();
			result = result * PRIME + ($enterpriseName == null ? 43 : $enterpriseName.hashCode());
			final Object $token = this.getToken();
			result = result * PRIME + ($token == null ? 43 : $token.hashCode());
			final Object $securities = this.getSecurities();
			result = result * PRIME + ($securities == null ? 43 : $securities.hashCode());
			final Object $async = this.getAsync();
			result = result * PRIME + ($async == null ? 43 : $async.hashCode());
			final Object $doubleCheck = this.getDoubleCheck();
			result = result * PRIME + ($doubleCheck == null ? 43 : $doubleCheck.hashCode());
			return result;
		}

		public String toString()
		{
			return "ActivityMasterConfiguration.ActivityMasterConfigurationDTO(enterpriseName=" + this.getEnterpriseName() + ", token=" + this.getToken() + ", securities=" +
			       this.getSecurities() + ", async=" + this.getAsync() + ", doubleCheck=" + this.getDoubleCheck() + ")";
		}
	}
}
