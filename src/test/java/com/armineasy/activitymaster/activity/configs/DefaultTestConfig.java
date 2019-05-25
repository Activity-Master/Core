package com.armineasy.activitymaster.activity.configs;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.ActivityMasterService;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDBModule;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.EnterpriseService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IEnterpriseService;
import com.armineasy.activitymaster.activitymaster.services.system.ISecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ServletScopes;
import com.jwebmp.guicedhazelcast.HazelcastConfigHandler;
import com.jwebmp.guicedhazelcast.implementations.HazelcastPreStartup;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.btm.implementation.BTMAutomatedTransactionHandler;
import com.jwebmp.guicedservlets.BasicServlet;
import com.jwebmp.guicedservlets.GuicedServletKeys;
import com.jwebmp.guicedservlets.RequestScopedObject;
import com.jwebmp.guicedservlets.mocks.MockRequest;
import com.jwebmp.guicedservlets.mocks.MockResponse;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.logger.logging.LogColourFormatter;
import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.armineasy.activitymaster.activitymaster.DefaultEnterprise.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Log
public class DefaultTestConfig
		implements BeforeEachCallback, AfterEachCallback
{
	private static final String FirefoxHeaderAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; ServiceUI 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18356";

	private RequestScoper.CloseableScope scoper;
	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception
	{
		BTMAutomatedTransactionHandler.setActive(true);
		HazelcastConfigHandler.startLocal = true;
		ActivityMasterDBModule.persistenceUnitName = "ActivityMasterUT";

		//HazelcastConfigHandler.startLocal = true;
		LogFactory.configureConsoleColourOutput(Level.FINE);
		LogColourFormatter.setRenderBlack(false);
		GuiceContext.inject();

		scoper = ServletScopes.scopeRequest(new HashMap<>())
		                                                   .open();
		RequestScopedObject obj = GuiceContext.get(RequestScopedObject.class);
		BasicServlet servlet = GuiceContext.get(BasicServlet.class);

		MockResponse resp = (MockResponse) GuiceContext.get(GuicedServletKeys.getHttpServletResponseKey());
		MockRequest req = (MockRequest) GuiceContext.get(GuicedServletKeys.getHttpServletRequestKey());

		req.setHeader("User-Agent", FirefoxHeaderAgent);

		get(ActivityMasterConfiguration.class).setEnterpriseName(TestEnterprise);
		EnterpriseService service = get(EnterpriseService.class);
		Optional<Enterprise> enterpriseO = service.findEnterprise(TestEnterprise);
		Enterprise enterprise = null;
		if(enterpriseO.isEmpty())
		{
			enterpriseO = Optional.ofNullable(get(ActivityMasterService.class)
					                                  .startNewEnterprise(TestEnterprise,
					                                                      "admin", "admin", getSoutMonitor()));
		}
		enterprise = enterpriseO.get();

		ActivityMasterConfiguration securityConfiguration =GuiceContext.get(ActivityMasterConfiguration.class);

		ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
		config.setSecurityEnabled(false);
		config.setEnterpriseName(TestEnterprise);

		ISystems systems = GuiceContext.get(ISystemsService.class)
		                               .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		SecurityToken token = GuiceContext.get(ISecurityTokenService.class)
		                                  .getSecurityToken(identityToken, enterprise);

		GuiceContext.get(ActivityMasterService.class).loadSystems(TestEnterprise,null);
		securityConfiguration.setToken(token);
		SecurityToken inToken = securityConfiguration.getToken();
		config.setSecurityEnabled(true);
		config.setAsyncEnabled(true);
		config.setDoubleCheckDisabled(true);
		defaultWaitUnit = TimeUnit.HOURS;
		defaultWaitTime = 1;
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception
	{
		scoper.close();
		GuiceContext.destroy();
	}

	private IActivityMasterProgressMonitor getSoutMonitor()
	{
		IActivityMasterProgressMonitor soutMonitor = new IActivityMasterProgressMonitor()
		{
			int totalTasks = 0;
			int currentTasks = 0;

			@Override
			public void progressUpdate(String source, String message)
			{
				log.info(source + " - " + message + " || " + currentTasks + "/" + totalTasks);
			}

			@Override
			public Integer getCurrentTask()
			{
				return currentTasks;
			}

			@Override
			public void setCurrentTask(Integer i)
			{
				currentTasks = i;
			}

			@Override
			public Integer getTotalTasks()
			{
				return totalTasks;
			}

			@Override
			public void setTotalTasks(Integer i)
			{
				totalTasks = i;
			}
		};
		LogColourFormatter.setRenderBlack(false);
		FileHandler fh;
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("C:/temp/EnterpriseCreateLogFile.log",false);
			fh.setLevel(Level.FINE);
			Logger.getLogger("").addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			// the following statement is used to log any messages
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return soutMonitor;
	}

}
