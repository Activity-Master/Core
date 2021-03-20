package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.client.services.administration.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;

public interface IActivityMasterService
{
	void loadSystems(String enterpriseName, IActivityMasterProgressMonitor progressMonitor);
	
	void loadUpdates(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
	void runScript(String script);

	void updatePartitionBases();
	
}
