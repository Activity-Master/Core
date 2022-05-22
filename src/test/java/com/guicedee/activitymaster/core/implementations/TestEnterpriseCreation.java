package com.guicedee.activitymaster.fsdm.implementations;

import com.guicedee.activitymaster.fsdm.DefaultEnterprise;
import com.guicedee.activitymaster.fsdm.EnterpriseService;
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
		GuiceContext.get(EnterpriseService.class)
		            .startNewEnterprise(DefaultEnterprise.TestEnterprise.toString(), "admin", "admin"
				            );
	}
}
