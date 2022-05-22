package com.guicedee.activitymaster.fsdm.implementations;

import com.guicedee.activitymaster.ActivityMasterTestBinder;
import com.guicedee.activitymaster.fsdm.*;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import com.guicedee.logger.logging.LogColourFormatter;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class DefaultTestConfig
		implements BeforeEachCallback, AfterEachCallback
{
	private static final String FirefoxHeaderAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; ServiceUI 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18356";
	private static final Logger log = Logger.getLogger(DefaultTestConfig.class.getName());

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

/*		RequestScopedObject obj = GuiceContext.get(RequestScopedObject.class);
		BasicServlet servlet = GuiceContext.get(BasicServlet.class);

		MockResponse resp = (MockResponse) GuiceContext.get(GuicedServletKeys.getHttpServletResponseKey());
		MockRequest req = (MockRequest) GuiceContext.get(GuicedServletKeys.getHttpServletRequestKey());

		req.setHeader("User-Agent", FirefoxHeaderAgent);*/
		
		ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
		
		config.setApplicationEnterpriseName(TestEnterprise.name());
		EnterpriseService service = get(EnterpriseService.class);
		Optional<IEnterprise<?,?>> enterpriseO = service.findEnterprise(TestEnterprise.name());
		IEnterprise<?,?> enterprise = null;
		if (enterpriseO.isEmpty())
		{
			enterpriseO = Optional.ofNullable(service.startNewEnterprise(TestEnterprise.name(),
					                                                      "admin", "admin"));
		}
		enterprise = enterpriseO.get();

		ActivityMasterConfiguration securityConfiguration = GuiceContext.get(ActivityMasterConfiguration.class);

		
		config.setSecurityEnabled(false);
		config.setApplicationEnterpriseName(TestEnterprise.name());

		ISystems<?,?> systems = GuiceContext.get(ISystemsService.class)
		                               .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(systems);
		
		GuiceContext.get(ActivityMasterService.class)
		            .loadSystems(TestEnterprise.name());

		config.setSecurityEnabled(false);
		defaultWaitUnit = TimeUnit.HOURS;
		defaultWaitTime = 1;
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception
	{
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
