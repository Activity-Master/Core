package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

public interface IActivityMasterService
{
	void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);
	
	void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
	void runScript(String script);

	void updatePartitionBases();
	
}
