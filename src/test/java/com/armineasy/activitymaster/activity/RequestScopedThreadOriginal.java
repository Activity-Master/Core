package com.armineasy.activitymaster.activity;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.ActivityMasterService;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.EnterpriseService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IEnterpriseService;
import com.armineasy.activitymaster.activitymaster.services.system.ISecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ServletScopes;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.armineasy.activitymaster.activitymaster.DefaultEnterprise.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

public abstract class RequestScopedThreadOriginal
		implements Callable<Object>, Runnable
{
	RequestScoper.CloseableScope scoper;
	@Override
	public void run()
	{
		startup();
		getIdentityToken();
		perform();
		scoper.close();
	}

	private void startup()
	{
		scoper = ServletScopes.scopeRequest(new HashMap<>())
		                      .open();
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public abstract void perform();

	public SecurityToken getIdentityToken()
	{
		ActivityMasterConfiguration securityConfiguration =GuiceContext.get(ActivityMasterConfiguration.class);

		ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
		config.setSecurityEnabled(false);
		config.setEnterpriseName(TestEnterprise);
		EnterpriseService service = get(EnterpriseService.class);
		Optional<Enterprise> enterpriseO = service.findEnterprise(TestEnterprise);
		Enterprise enterprise = enterpriseO.get();

		ISystems systems = GuiceContext.get(ISystemsService.class)
		                               .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		SecurityToken token = GuiceContext.get(ISecurityTokenService.class)
		                                  .getSecurityToken(identityToken, enterprise);
		securityConfiguration.setToken(token);
		SecurityToken inToken = securityConfiguration.getToken();

		config.setSecurityEnabled(true);

		return token;

	}

	@Override
	public Object call() throws Exception
	{
		run();
		return null;
	}
}
