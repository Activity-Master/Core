package com.armineasy.activitymaster.activitymaster.services;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

public interface IActivityMasterSystem<J extends IActivityMasterSystem<J>>
		extends IDefaultService<J>, IProgressable
{
	void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);

	int totalTasks();

	default void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{

	}
}
