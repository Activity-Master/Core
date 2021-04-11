package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.EnterpriseService;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.services.providers.EnterpriseProvider;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class EnterpriseBinder extends PrivateModule implements IGuiceModule<EnterpriseBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IEnterprise<?,?>> enterpriseKey = Key.get(new TypeLiteral<IEnterprise<?,?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IEnterprise<Enterprise, EnterpriseQueryBuilder>> enterpriseKeyLegit = Key.get(new TypeLiteral<IEnterprise<Enterprise, EnterpriseQueryBuilder>>() {});
		bind(IEnterprise.class).to(enterpriseKey);
		bind(enterpriseKey).to(enterpriseKeyLegit);
		bind(enterpriseKeyLegit).toProvider(EnterpriseProvider.class);
		
		expose(enterpriseKey);
		expose(IEnterprise.class);
		
		@SuppressWarnings("Convert2Diamond")
		Key<IEnterpriseService<?>> enterpriseServiceKey = Key.get(new TypeLiteral<IEnterpriseService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IEnterpriseService<EnterpriseService>> enterpriseServiceKeyLegit = Key.get(new TypeLiteral<IEnterpriseService<EnterpriseService>>() {});
		
		bind(enterpriseServiceKey).to(enterpriseServiceKeyLegit);
		bind(enterpriseServiceKeyLegit).to(EnterpriseService.class);
		bind(IEnterpriseService.class).to(enterpriseServiceKey);
		
		expose(enterpriseServiceKey);
		expose(IEnterpriseService.class);
	}
}
