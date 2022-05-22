package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsQueryBuilder;

public class SystemsProvider implements Provider<ISystems<Systems, SystemsQueryBuilder>>
{
	@Inject
	private Provider<IEnterprise<?, ?>> enterprise;
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	private String systemName;
	
	public SystemsProvider()
	{
	}
	
	public SystemsProvider(String systemName)
	{
		this.systemName = systemName;
	}
	
	@Override
	public ISystems<Systems, SystemsQueryBuilder> get()
	{
		if (EnterpriseProvider.loadedEnterprise == null || EnterpriseProvider.loadedEnterprise.isFake())
		{
			return new Systems();
		}
		var s = (ISystems<Systems, SystemsQueryBuilder>) systemsService.get()
		                                                              .findSystem(enterprise.get(),
				                                                              systemName, null);
		return s;
	}
	
}
