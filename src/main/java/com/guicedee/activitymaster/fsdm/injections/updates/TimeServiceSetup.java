package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import com.guicedee.activitymaster.fsdm.systems.TimeSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

@SortedUpdate(sortOrder = Integer.MAX_VALUE - 200, taskCount = 1)
@Log4j2
public class TimeServiceSetup implements ISystemUpdate
{
	@Inject
	private TimeSystem timeSystem;

	@Override
	public Uni<Boolean> update(IEnterprise<?, ?> enterprise)
	{
		log.info("Starting time service setup");
		logProgress("Time Service", "Loading Time Specifications", 1);
		/*timeSystem.loadTimeRange(LocalDate.now()
		                                  .getYear(), LocalDate.now()
		                                        .getYear());*/
		return Uni.createFrom().item(true);
	}

}
