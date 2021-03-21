package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;

public interface IActivityMasterService
{
	void loadSystems(String enterpriseName, IActivityMasterProgressMonitor progressMonitor);
	
	void loadUpdates(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
	void runScript(String script);

	void updatePartitionBases();
	
}
