package com.guicedee.activitymaster.fsdm.threads;

import com.guicedee.activitymaster.fsdm.systems.TimeSystem;

import java.util.Date;

public
class TimeLoaderThread implements Runnable
{
	private Date date;

	public TimeLoaderThread()
	{
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public TimeLoaderThread setDate(Date date)
	{
		this.date = date;
		return this;
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
