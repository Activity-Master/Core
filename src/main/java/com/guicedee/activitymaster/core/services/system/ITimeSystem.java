package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;

import java.util.Date;

public interface ITimeSystem
{
	void loadTimeRange(int startYear, int endYear, IActivityMasterProgressMonitor progressMonitoro);

	/**
	 * True if available
	 *
	 * @param date
	 *
	 * @return
	 */
	boolean getDay(Date date);

	void createTime();
}
