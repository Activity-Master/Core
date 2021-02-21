package com.guicedee.activitymaster.core.implementations;

import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ServletScopes;
import com.guicedee.activitymaster.core.*;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.system.ISecurityTokenService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.*;
import java.util.concurrent.Callable;

import static com.guicedee.activitymaster.core.DefaultEnterprise.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

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
		config.setEnterpriseName(TestEnterprise.classificationName());
		EnterpriseService service = get(EnterpriseService.class);
		Optional<IEnterprise<?>> enterpriseO = service.findEnterprise(TestEnterprise);
		IEnterprise<?> enterprise = enterpriseO.get();

		ISystems<?> systems = GuiceContext.get(ISystemsService.class)
		                               .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		SecurityToken token = (SecurityToken) GuiceContext.get(ISecurityTokenService.class)
		                                                  .getSecurityToken(identityToken, systems);
		securityConfiguration.setToken(token);
		ISecurityToken<?> inToken = securityConfiguration.getToken();

		config.setSecurityEnabled(false);

		return token;

	}

	@Override
	public Object call() throws Exception
	{
		run();
		return null;
	}
}
