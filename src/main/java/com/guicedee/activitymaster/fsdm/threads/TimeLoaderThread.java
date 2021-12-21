package com.guicedee.activitymaster.fsdm.threads;

import com.guicedee.activitymaster.fsdm.systems.TimeSystem;
import lombok.Data;

import java.util.Date;

@Data
public
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
	public void run()
	{
		new TimeSystem().populateTransformationTables(date, -3);
	}
}
