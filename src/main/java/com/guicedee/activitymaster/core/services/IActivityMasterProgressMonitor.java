package com.guicedee.activitymaster.core.services;

public interface IActivityMasterProgressMonitor
{
	/**
	 * Feeds progress information into the monitor
	 *
	 * @param source
	 * @param message
	 */
	public IActivityMasterProgressMonitor progressUpdate(String source, String message);

	public Integer getCurrentTask();

	public IActivityMasterProgressMonitor setCurrentTask(Integer i);

	public Integer getTotalTasks();

	public IActivityMasterProgressMonitor setTotalTasks(Integer i);
}
