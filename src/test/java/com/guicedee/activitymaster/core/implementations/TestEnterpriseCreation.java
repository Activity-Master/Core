package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterService;
import com.guicedee.activitymaster.core.DefaultEnterprise;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import com.guicedee.logger.logging.LogColourFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Level;
import java.util.logging.Logger;

@ExtendWith(DefaultTestConfig.class)
public class TestEnterpriseCreation
{
	private static final Logger log = Logger.getLogger(TestEnterpriseCreation.class.getName());

	@Test
	public void testEnterpriseCreation()
	{
		System.setErr(System.out);
		IActivityMasterProgressMonitor soutMonitor = new IActivityMasterProgressMonitor()
		{
			int totalTasks = 0;
			int currentTasks = 0;

			@Override
			public IActivityMasterProgressMonitor progressUpdate(String source, String message)
			{
				TestEnterpriseCreation.log.info(source + " - " + message + " || " + currentTasks + "/" + totalTasks);
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
		LogFactory.configureConsoleColourOutput(Level.FINE);

		GuiceContext.get(ActivityMasterService.class)
		            .startNewEnterprise(DefaultEnterprise.TestEnterprise, "admin", "admin", soutMonitor);
	}
}
