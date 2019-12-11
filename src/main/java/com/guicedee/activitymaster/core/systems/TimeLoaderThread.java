package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import lombok.Data;

import java.util.Date;

@Data
class TimeLoaderThread implements Runnable
{
	private Date date;

	public TimeLoaderThread()
	{
	}

	public TimeLoaderThread(Date date)
	{
		this.date = date;
	}

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void run()
	{
		new TimeSystem().populateTransformationTables(date, -3);
	}
}
