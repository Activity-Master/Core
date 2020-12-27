package com.guicedee.activitymaster.core.services;

import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.guicedinjection.interfaces.IDefaultService;

public interface IActivityMasterSystem<J extends IActivityMasterSystem<J>>
		extends IDefaultService<J>, IProgressable
{
	void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);

	int totalTasks();

	default void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	}
}
