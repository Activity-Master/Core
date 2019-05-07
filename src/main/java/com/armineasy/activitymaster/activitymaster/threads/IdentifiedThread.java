package com.armineasy.activitymaster.activitymaster.threads;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.EnterpriseService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.system.ISecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.armineasy.activitymaster.activitymaster.DefaultEnterprise.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Data
@Accessors(chain=true)
@Log
public abstract class IdentifiedThread<J extends IdentifiedThread<J>>
		implements Callable<Object>
{
	private ActivityMasterConfiguration.ActivityMasterConfigurationDTO activityMasterConfigurationDTO;

	public void run()
	{
		startup();
		getIdentityToken();
		perform();
		notify();
	}

	protected void startup()
	{

	}

	@SuppressWarnings("unchecked")
	public J fromCurrentThread()
	{
		activityMasterConfigurationDTO = new ActivityMasterConfiguration.ActivityMasterConfigurationDTO()
				                                 .fromCurrentThread();
		return (J)this;
	}

	public abstract void perform();

	public SecurityToken getIdentityToken()
	{
		ActivityMasterConfiguration securityConfiguration = GuiceContext.get(ActivityMasterConfiguration.class);
		ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);

		config.setSecurityEnabled(false);
		config.setEnterpriseName(TestEnterprise);
		EnterpriseService service = get(EnterpriseService.class);
		Optional<Enterprise> enterpriseO = service.findEnterprise(TestEnterprise);

		Enterprise enterprise = enterpriseO.get();

		Systems systems = GuiceContext.get(ISystemsService.class)
		                              .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		SecurityToken token = GuiceContext.get(ISecurityTokenService.class)
		                                  .getSecurityToken(identityToken, enterprise);
		securityConfiguration.setToken(token);
		SecurityToken inToken = securityConfiguration.getToken();
		config.setSecurityEnabled(true);
		config.setAsyncEnabled(true);
		config.setDoubleCheckDisabled(true);
		return token;
	}

	@Override
	public Object call() throws Exception
	{
		run();
		return null;
	}
}