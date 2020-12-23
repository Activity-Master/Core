package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.time.Days;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
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
