package com.guicedee.activitymaster.core.services;

import com.guicedee.activitymaster.client.services.administration.IActivityMasterProgressMonitor;
import lombok.extern.java.Log;

@Log
public class ConsoleLogActivityMasterProgressMaster implements IActivityMasterProgressMonitor
{
	int totalTasks = 0;
	int currentTasks = 0;
	
	@Override
	public IActivityMasterProgressMonitor progressUpdate(String source, String message)
	{
		log.info(source + " - " + message + " || " + currentTasks + "/" + totalTasks);
		return this;
	}
	
	@Override
	public Integer getCurrentTask()
	{
		return currentTasks;
	}
	
	@Override
	public IActivityMasterProgressMonitor setCurrentTask(Integer i)
	{
		currentTasks = i;
		return this;
	}
	
	@Override
	public Integer getTotalTasks()
	{
		return totalTasks;
	}
	
	@Override
	public IActivityMasterProgressMonitor setTotalTasks(Integer i)
	{
		totalTasks = i;
		return this;
	}
}
