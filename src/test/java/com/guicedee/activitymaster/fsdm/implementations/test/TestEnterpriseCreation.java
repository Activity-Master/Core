package com.guicedee.activitymaster.fsdm.implementations.test;

import com.guicedee.activitymaster.fsdm.DefaultEnterprise;
import com.guicedee.activitymaster.fsdm.EnterpriseService;
import com.guicedee.activitymaster.fsdm.implementations.DefaultTestConfig;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(DefaultTestConfig.class)
@Log4j2
public class TestEnterpriseCreation
{
	@Test
	public void testEnterpriseCreation()
	{
		System.setErr(System.out);
	//	LogColourFormatter.setRenderBlack(false);
	//	LogFactory.configureConsoleColourOutput(Level.FINE);

		//done in the before each
		com.guicedee.client.IGuiceContext.get(EnterpriseService.class)
		            .startNewEnterprise(DefaultEnterprise.TestEnterprise.toString(), "admin", "admin"
				            );
	}
}
