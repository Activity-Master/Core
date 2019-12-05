package com.guicedee.activitymaster.core.services;

public interface IActivityMasterProgressMonitor
{
	/**
	 * Feeds progress information into the monitor
	 *
	 * @param source
	 * @param message
	 */
	public void progressUpdate(String source, String message);

	public Integer getCurrentTask();

	public void setCurrentTask(Integer i);

	public Integer getTotalTasks();

	public void setTotalTasks(Integer i);
}
