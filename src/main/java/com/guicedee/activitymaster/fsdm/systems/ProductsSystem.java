package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IProductService.*;


public class ProductsSystem
		extends ActivityMasterDefaultSystem<ProductsSystem>
		implements IActivityMasterSystem<ProductsSystem>
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	private ISystemsService<?> systemsService;
	
	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(enterprise, getSystemName(), getSystemDescription());
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise));
		return iSystems;
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
		service.create(ProductClassifications.Products, activityMasterSystem);
		service.create(ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products);
		service.create(ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup);
		logProgress("Products System", "Loaded Product Classifications...", 4);
	}

	@Override
	public int totalTasks()
	{
		return 0;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 11;
	}

	@Override
	public String getSystemName()
	{
		return ProductSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing Products";
	}
}
