package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IRulesService;
import com.guicedee.activitymaster.fsdm.RulesService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class RulesBinder extends AbstractModule implements IGuiceModule<RulesBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IRulesService<?>> genericKey = Key.get(new TypeLiteral<IRulesService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IRulesService<RulesService>> realKey
				= Key.get(new TypeLiteral<IRulesService<RulesService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(RulesService.class);
		bind(IRulesService.class).to(genericKey);

	}
}
