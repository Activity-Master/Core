package com.guicedee.activitymaster.fsdm.systems;

import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import org.junit.jupiter.api.Test;

public class TimeSystemTest
{

	@Test
	public void testLoadTimeRange()
	{
	//	LogFactory.configureConsoleColourOutput(Level.FINE);
	//	LogFactory.configureDefaultLogHiding();
		ITimeSystem timeSystem = com.guicedee.client.IGuiceContext.get(ITimeSystem.class);
		timeSystem.loadTimeRange(1990, 2020);
	}

	@Test
	public void testCreateTime()
	{
	//	LogFactory.configureConsoleColourOutput(Level.FINE);
	//	LogFactory.configureDefaultLogHiding();
		ITimeSystem timeSystem = com.guicedee.client.IGuiceContext.get(ITimeSystem.class);
		timeSystem.createTime();
	}
}
