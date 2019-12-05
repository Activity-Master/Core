package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;

public interface IActivityMasterService
{
	void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);
}
