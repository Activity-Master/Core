package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.guicedee.activitymaster.fsdm.EnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class EnterpriseBinder extends AbstractModule implements IGuiceModule<EnterpriseBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IEnterpriseService<?>> enterpriseServiceKey = Key.get(new TypeLiteral<IEnterpriseService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IEnterpriseService<EnterpriseService>> enterpriseServiceKeyLegit = Key.get(new TypeLiteral<IEnterpriseService<EnterpriseService>>() {});
		
		bind(enterpriseServiceKey).to(enterpriseServiceKeyLegit);
		bind(enterpriseServiceKeyLegit).to(EnterpriseService.class);
		bind(IEnterpriseService.class).to(enterpriseServiceKey);
	}
}
