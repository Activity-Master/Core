package com.guicedee.activitymaster.core;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Strings;
import com.google.inject.*;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.exceptions.EnterpriseException;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.activitymaster.core.threads.TransactionalIdentifiedThread;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

@Singleton
public class ActivityMasterConfiguration
{
	private final ThreadLocal<ISecurityToken<?>> token = ThreadLocal.withInitial(SecurityToken::new);
	private final ThreadLocal<Boolean> securities = ThreadLocal.withInitial(() -> true);
	
	private ISystems<?> applicationSystem;
	private UUID applicationSystemUUID;
	private IEnterprise<?> enterprise;
	private String applicationEnterpriseName;
	
	private Set<IActivityMasterSystem<?>> allSystems;
	
	@Inject
	private Provider<IEnterpriseService<?>> enterpriseService;
	@Inject
	private Injector injector;
	
	public ActivityMasterConfiguration()
	{
		// no config required
	}
	
	public IEnterprise<?> getEnterprise()
	{
		if (enterprise == null && !Strings.isNullOrEmpty(applicationEnterpriseName))
		{
			try
			{
				enterprise = enterpriseService.get()
				                              .getEnterprise(applicationEnterpriseName);
			}catch (EnterpriseException ee)
			{
			
			}
		}
		return enterprise;
	}
	
	public String getEnterpriseName()
	{
		return applicationEnterpriseName;
	}
	
	public void setEnterpriseName(String enterpriseName)
	{
		applicationEnterpriseName = enterpriseName;
		if (enterprise == null)
		{
			enterprise = GuiceContext.get(IEnterpriseService.class)
			                         .getEnterprise(enterpriseName);
		}
	}
	
	public ActivityMasterConfiguration setEnterprise(IEnterprise<?> enterprise)
	{
		this.enterprise = enterprise;
		return this;
	}
	
	public boolean isSecurityEnabled()
	{
		return securities.get();
	}
	
	public void setSecurityEnabled(boolean securityEnabled)
	{
		securities.set(securityEnabled);
	}
	
	public ISystems<?> getApplicationSystem()
	{
		return applicationSystem;
	}
	
	public ActivityMasterConfiguration setApplicationSystem(ISystems<?> applicationSystem)
	{
		this.applicationSystem = applicationSystem;
		return this;
	}
	
	public UUID getApplicationSystemUUID()
	{
		return applicationSystemUUID;
	}
	
	public ActivityMasterConfiguration setApplicationSystemUUID(UUID applicationSystemUUID)
	{
		this.applicationSystemUUID = applicationSystemUUID;
		return this;
	}
	
	public String getApplicationEnterpriseName()
	{
		return applicationEnterpriseName;
	}
	
	public ActivityMasterConfiguration setApplicationEnterpriseName(String applicationEnterpriseName)
	{
		this.applicationEnterpriseName = applicationEnterpriseName;
		if (applicationEnterpriseName != null)
		{
			try
			{
				enterprise = enterpriseService.get().getEnterprise(applicationEnterpriseName);
			}catch (EnterpriseException e)
			{
			}
		}
		return this;
	}
	
	public ISecurityToken<?> getToken()
	{
		return token.get();
	}
	
	public ISecurityToken<?> setToken(ISecurityToken<?> token)
	{
		this.token.set(token);
		return this.token.get();
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
	}
	
	public Set<IActivityMasterSystem<?>> getAllSystems()
	{
		if (allSystems == null)
		{
			allSystems = new TreeSet<>();
			Set<IActivityMasterSystem> systems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
			for (IActivityMasterSystem<?> system : systems)
			{
				allSystems.add(injector.getInstance(system.getClass()));
			}
		}
		return allSystems;
	}
	
	@Getter
	@Setter
	@Accessors(chain = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
	public static class ActivityMasterConfigurationDTO
	{
		private String enterpriseName;
		private ISecurityToken<?> token;
		private Boolean securities;
		
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
			return this;
		}
	}
}
