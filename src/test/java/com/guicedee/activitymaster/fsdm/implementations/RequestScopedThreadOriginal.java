package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ServletScopes;
import com.guicedee.activitymaster.fsdm.EnterpriseService;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.*;
import java.util.concurrent.Callable;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.*;

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
		config.setApplicationEnterpriseName(TestEnterprise.name());
		EnterpriseService service = GuiceContext.get(EnterpriseService.class);
		Optional<IEnterprise<?,?>> enterpriseO = service.findEnterprise(TestEnterprise.name());
		IEnterprise<?,?> enterprise = enterpriseO.get();

		ISystems<?,?> systems = GuiceContext.get(ISystemsService.class)
		                               .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		SecurityToken token = (SecurityToken) GuiceContext.get(ISecurityTokenService.class)
		                                                  .getSecurityToken(identityToken, systems);
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
