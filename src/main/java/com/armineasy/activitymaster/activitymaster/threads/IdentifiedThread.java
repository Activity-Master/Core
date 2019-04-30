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
public abstract class IdentifiedThread
		implements Callable<Object>, Runnable
{

	@Override
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
		securityConfiguration.getToken()
		                     .set(token);
		SecurityToken inToken = securityConfiguration.getToken()
		                                             .get();
		config.setSecurityEnabled(true);
		config.setAsyncEnabled(true);
		return token;
	}

	@Override
	public Object call() throws Exception
	{
		run();
		return null;
	}
}