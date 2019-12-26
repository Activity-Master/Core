package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.services.system.ITimeSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

public class TimeSystemTest
{

	@Test
	public void testLoadTimeRange()
	{
		LogFactory.configureConsoleColourOutput(Level.FINE);
		LogFactory.configureDefaultLogHiding();
		ITimeSystem timeSystem = GuiceContext.get(ITimeSystem.class);
		timeSystem.loadTimeRange(1990, 2020,null);
	}

	@Test
	public void testCreateTime()
	{
		LogFactory.configureConsoleColourOutput(Level.FINE);
		LogFactory.configureDefaultLogHiding();
		ITimeSystem timeSystem = GuiceContext.get(ITimeSystem.class);
		timeSystem.createTime();
	}
}
