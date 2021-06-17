package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class ActivityMasterSystemBinder extends PrivateModule implements IGuiceModule<ActivityMasterSystemBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IActivityMasterSystem<?>> enterpriseServiceKey = Key.get(new TypeLiteral<IActivityMasterSystem<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IActivityMasterSystem<SystemsSystem>> enterpriseServiceKeyLegit = Key.get(new TypeLiteral<IActivityMasterSystem<SystemsSystem>>() {});
		
		bind(enterpriseServiceKey).to(enterpriseServiceKeyLegit);
		bind(enterpriseServiceKeyLegit).to(SystemsSystem.class);
		bind(IActivityMasterSystem.class).to(enterpriseServiceKey);
		
		expose(enterpriseServiceKey);
		expose(IActivityMasterSystem.class);
		
		
	}
}
