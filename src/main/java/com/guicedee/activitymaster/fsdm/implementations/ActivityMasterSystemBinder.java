package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class ActivityMasterSystemBinder extends AbstractModule implements IGuiceModule<ActivityMasterSystemBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IMasterSystem<?>> enterpriseServiceKey = Key.get(new TypeLiteral<IMasterSystem<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IMasterSystem<SystemsSystem>> enterpriseServiceKeyLegit = Key.get(new TypeLiteral<IMasterSystem<SystemsSystem>>() {});
		
		bind(enterpriseServiceKey).to(enterpriseServiceKeyLegit);
		bind(enterpriseServiceKeyLegit).to(SystemsSystem.class);
		bind(IMasterSystem.class).to(enterpriseServiceKey);

	}
}
