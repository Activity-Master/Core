package com.guicedee.activitymaster.fsdm.services.system;

import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterProgressMonitor;

import java.util.Date;

public interface ITimeSystem
{
	void loadTimeRange(int startYear, int endYearo);

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
