package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.client.services.IProductService.*;
import static com.guicedee.activitymaster.core.SystemsService.*;


public class ProductsSystem
		extends ActivityMasterDefaultSystem<ProductsSystem>
		implements IActivityMasterSystem<ProductsSystem>
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?, ?> iSystems = systemsService.get()
		                                        .create(enterprise, getSystemName(), getSystemDescription());
		systemsService.get()
		              .registerNewSystem(enterprise, getSystem(enterprise));
		return iSystems;
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		service.create(ProductClassifications.Products, activityMasterSystem);
		service.create(ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products);
		service.create(ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup);
		logProgress("Products System", "Loaded Product Classifications...", 4, progressMonitor);
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
