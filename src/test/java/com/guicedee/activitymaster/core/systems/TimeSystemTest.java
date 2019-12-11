package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.services.system.ITimeSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

class TimeSystemTest
{

	@Test
	void loadTimeRange()
	{
		LogFactory.configureConsoleColourOutput(Level.FINE);
		LogFactory.configureDefaultLogHiding();
		ITimeSystem timeSystem = GuiceContext.get(ITimeSystem.class);
		timeSystem.loadTimeRange(1992, 2019);
		//timeSystem.createTime();
	}
}
