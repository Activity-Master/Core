package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.exceptions.ActiveFlagException;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;

public class ActiveFlagProvider implements Provider<IActiveFlag<ActiveFlag>>
{
	@Inject
	private Provider<IEnterprise<?>> enterprise;
	
	@Inject
	private Provider<IActiveFlagService<?>> activeFlagService;
	
	private final com.entityassist.enumerations.ActiveFlag flag;
	
	public ActiveFlagProvider(com.entityassist.enumerations.ActiveFlag flag)
	{
		this.flag = flag;
	}
	
	@Override
	public IActiveFlag<ActiveFlag> get()
	{
		if (enterprise.get().isFake())
		{
			return new ActiveFlag();
		}
		try
		{
			//noinspection unchecked
			return (IActiveFlag<ActiveFlag>) activeFlagService.get().findFlagByName(flag, enterprise.get());
		}catch (ActiveFlagException e)
		{
			return new ActiveFlag();
		}
	}
}
