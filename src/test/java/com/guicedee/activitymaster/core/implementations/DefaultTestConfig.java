package com.guicedee.activitymaster.core.implementations;

import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.ServletScopes;
import com.guicedee.activitymaster.ActivityMasterTestBinder;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.ActivityMasterService;
import com.guicedee.activitymaster.core.db.ActivityMasterDBModule;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ISecurityTokenService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.btm.implementation.BTMAutomatedTransactionHandler;
import com.guicedee.logger.LogFactory;
import com.guicedee.logger.logging.LogColourFormatter;
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

import static com.guicedee.activitymaster.core.DefaultEnterprise.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class DefaultTestConfig
		implements BeforeEachCallback, AfterEachCallback
{
	private static final String FirefoxHeaderAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; ServiceUI 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18356";
	private static final Logger log = Logger.getLogger(DefaultTestConfig.class.getName());

	private RequestScoper.CloseableScope scoper;

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception
	{
		GuiceContext.instance()
		            .loadIGuiceModules()
		            .add(new ActivityMasterTestBinder());
		HazelcastProperties.setStartLocal(true);
		HazelcastProperties.setGroupName("fsdm");
		HazelcastProperties.setInstanceName("fsdm");
		if (HazelcastProperties.getAddress() == null) {
			HazelcastProperties.setAddress("127.0.0.1");
		}
		GuiceContext.instance().getConfig()
				.setIncludeModuleAndJars(true)
				.setClasspathScanning(true)
				.setMethodInfo(true)
				.setFieldInfo(true)
				.setAnnotationScanning(true)
				.setPathScanning(true);

		LogFactory.configureConsoleColourOutput(Level.FINE);
	//	LogColourFormatter.setRenderBlack(false);
		LogFactory.configureDefaultLogHiding();
		GuiceContext.inject();

		scoper = ServletScopes.scopeRequest(new HashMap<>())
		                      .open();
/*		RequestScopedObject obj = GuiceContext.get(RequestScopedObject.class);
		BasicServlet servlet = GuiceContext.get(BasicServlet.class);

		MockResponse resp = (MockResponse) GuiceContext.get(GuicedServletKeys.getHttpServletResponseKey());
		MockRequest req = (MockRequest) GuiceContext.get(GuicedServletKeys.getHttpServletRequestKey());

		req.setHeader("User-Agent", FirefoxHeaderAgent);*/

		get(ActivityMasterConfiguration.class).setEnterpriseName(TestEnterprise);
		EnterpriseService service = get(EnterpriseService.class);
		Optional<IEnterprise<?>> enterpriseO = service.findEnterprise(TestEnterprise);
		IEnterprise<?> enterprise = null;
		if (enterpriseO.isEmpty())
		{
			enterpriseO = Optional.ofNullable(get(ActivityMasterService.class)
					                                  .startNewEnterprise(TestEnterprise,
					                                                      "admin", "admin", getSoutMonitor()));
		}
		enterprise = enterpriseO.get();

		ActivityMasterConfiguration securityConfiguration = GuiceContext.get(ActivityMasterConfiguration.class);

		ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
		config.setSecurityEnabled(false);
		config.setEnterpriseName(TestEnterprise);

		ISystems<?> systems = GuiceContext.get(ISystemsService.class)
		                                  .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		
		ISecurityToken<?> token = GuiceContext.get(ISecurityTokenService.class)
		                                      .getSecurityToken(identityToken, systems);

		GuiceContext.get(ActivityMasterService.class)
		            .loadSystems(TestEnterprise, null);
		securityConfiguration.setToken(token);
		ISecurityToken<?> inToken = securityConfiguration.getToken();
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
			public IActivityMasterProgressMonitor progressUpdate(String source, String message)
			{
				DefaultTestConfig.log.info(source + " - " + message + " || " + currentTasks + "/" + totalTasks);
				return this;
			}

			@Override
			public Integer getCurrentTask()
			{
				return currentTasks;
			}

			@Override
			public IActivityMasterProgressMonitor setCurrentTask(Integer i)
			{
				currentTasks = i;
				return this;
			}

			@Override
			public Integer getTotalTasks()
			{
				return totalTasks;
			}

			@Override
			public IActivityMasterProgressMonitor setTotalTasks(Integer i)
			{
				totalTasks = i;
				return this;
			}
		};
		LogColourFormatter.setRenderBlack(false);
		FileHandler fh;
		try
		{
			// This block configure the logger with handler and formatter
			fh = new FileHandler("C:/temp/EnterpriseCreateLogFile.log", false);
			fh.setLevel(Level.FINE);
			Logger.getLogger("")
			      .addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			// the following statement is used to log any messages
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
		return soutMonitor;
	}

}
