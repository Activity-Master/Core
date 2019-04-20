package com.armineasy.activitymaster.activitymaster.services;

public interface IProgressable
{
	default void logProgress(String source, String message, Integer currentTask, Integer totalTasks, IActivityMasterProgressMonitor progressMonitor)
	{
		if (progressMonitor != null)
		{
			if (currentTask != null)
			{
				progressMonitor.setCurrentTask(progressMonitor.getCurrentTask() + currentTask);
			}
			if (totalTasks != null)
			{
				progressMonitor.setTotalTasks(progressMonitor.getTotalTasks() + totalTasks);
			}
			progressMonitor.progressUpdate(source, message);
		}
	}

	default void logProgress(String source, String message, Integer currentTask, IActivityMasterProgressMonitor progressMonitor)
	{
		if (progressMonitor != null)
		{
			if (currentTask != null)
			{
				progressMonitor.setCurrentTask(progressMonitor.getCurrentTask() + currentTask);
			}
			progressMonitor.progressUpdate(source, message);
		}
	}

	default void logProgress(String source, String message, IActivityMasterProgressMonitor progressMonitor)
	{
		if (progressMonitor != null)
		{
			progressMonitor.progressUpdate(source, message);
		}
	}
}
