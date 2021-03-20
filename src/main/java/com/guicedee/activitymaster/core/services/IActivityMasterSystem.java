package com.guicedee.activitymaster.core.services;

import com.guicedee.activitymaster.client.services.administration.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.administration.IProgressable;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.guicedinjection.interfaces.IDefaultService;

import java.util.UUID;

public interface IActivityMasterSystem<J extends IActivityMasterSystem<J>>
		extends IDefaultService<J>, IProgressable
{
	void registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
	void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor);

	int totalTasks();

	default void postStartup(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	}
	
	ISystems<?,?> getSystem(String enterpriseName);
	
	UUID getSystemToken(String enterpriseName);
	
	boolean hasSystemInstalled(IEnterprise<?,?> enterprise);
	
	String getSystemName();
	
	String getSystemDescription();
}
