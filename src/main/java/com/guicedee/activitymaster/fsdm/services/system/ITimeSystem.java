package com.guicedee.activitymaster.fsdm.services.system;

import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterProgressMonitor;
import io.smallrye.mutiny.Uni;

import java.util.Date;

public interface ITimeSystem
{
	Uni<Void> loadTimeRange(int startYear, int endYear);

	/**
	 * True if available
	 *
	 * @param date
	 *
	 * @return
	 */
	boolean getDay(Date date);

	Uni<Void> createTime();
}
