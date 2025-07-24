package com.guicedee.activitymaster.fsdm.systems;

import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;

public class EnterpriseSystem
		extends ActivityMasterDefaultSystem<EnterpriseSystem>
		implements IActivityMasterSystem<EnterpriseSystem>
{
	@Override
	public ISystems<?,?> registerSystem(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		return null;
	}
	
	
	@Override
	public void createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
	
	}
	
	@Override
	public int totalTasks()
	{
		return 0;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE;
	}
	
	@Override
	public String getSystemName()
	{
		return EnterpriseSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for handling enterprises";
	}
}
