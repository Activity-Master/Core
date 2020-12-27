package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterService;
import com.guicedee.activitymaster.core.DefaultEnterprise;
import com.guicedee.activitymaster.core.services.ConsoleLogActivityMasterProgressMaster;
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
		LogColourFormatter.setRenderBlack(false);
		LogFactory.configureConsoleColourOutput(Level.FINE);

		//done in the before each
		/*GuiceContext.get(ActivityMasterService.class)
		            .startNewEnterprise(DefaultEnterprise.TestEnterprise, "admin", "admin", new ConsoleLogActivityMasterProgressMaster());*/
	}
}
