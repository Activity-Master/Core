package com.armineasy.activitymaster.activitymaster.services;

public interface IProgressable
{
	default void logProgress(String source, String message, Integer currentTask, Integer totalTasks, IActivityMasterProgressMonitor progressMonitor)
	{
		message = cleanName(message);
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
		message = cleanName(message);
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
		message = cleanName(message);
		if (progressMonitor != null)
		{
			progressMonitor.progressUpdate(source, message);
		}
	}

	default String cleanName(String name)
	{
		if (name.indexOf("$$EnhancerByGuice$$") > 0)
		{
			name = name.substring(0, name.indexOf("$$EnhancerByGuice$$"));
		}
		name = name.replaceAll("com\\.armineasy\\.activitymaster\\.activitymaster\\.systems\\.", "");
		name = name.replaceAll("com\\.armineasy\\.activitymaster\\.activitymaster\\.", "");
		return name;
	}
}
