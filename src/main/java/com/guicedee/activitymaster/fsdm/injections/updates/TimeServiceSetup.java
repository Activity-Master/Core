package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import com.guicedee.activitymaster.fsdm.systems.TimeSystem;

import java.time.LocalDate;

@SortedUpdate(sortOrder = Integer.MAX_VALUE - 200, taskCount = 1)
public class TimeServiceSetup implements ISystemUpdate
{
	@Inject
	private TimeSystem timeSystem;
	
	@Override
	public void update(IEnterprise<?, ?> enterprise)
	{
		logProgress("Time Service", "Loading Time Specifications", 1);
		timeSystem.loadTimeRange(LocalDate.now()
		                                  .getYear(), LocalDate.now()
		                                        .getYear());
	}
	
}
