package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;

public interface IActivityMasterService
{
	void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);
}
