package com.armineasy.activitymaster.activitymaster.services;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.jwebmp.guicedinjection.interfaces.IDefaultService;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

public interface IActivityMasterSystem<J extends IActivityMasterSystem<J>>
		extends IDefaultService<J>, IProgressable
{
	void createDefaults(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor);

	int totalTasks();

	default void postUpdate(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{

	}
}
