package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagQueryBuilder;

public class ActiveFlagProvider implements Provider<IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>>
{
	@Inject
	private Provider<IEnterprise<?, ?>> enterprise;
	
	@Inject
	private Provider<IActiveFlagService<?>> activeFlagService;
	
	private final com.entityassist.enumerations.ActiveFlag flag;
	
	public ActiveFlagProvider(com.entityassist.enumerations.ActiveFlag flag)
	{
		this.flag = flag;
	}
	
	@Override
	public IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder> get()
	{
		if (enterprise.get()
		              .isFake())
		{
			return new ActiveFlag();
		}
		//noinspection unchecked
		return (IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>) activeFlagService.get()
		                                                                          .findFlagByName(flag, enterprise.get());
		
	}
}
