package com.guicedee.activitymaster.core.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.client.services.systems.*;

import static com.guicedee.activitymaster.core.SystemsService.*;

@SortedUpdate(sortOrder = 0, taskCount = 1)
public class ProductsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Override
	public void update(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Products System", "Loaded Product Classifications...", 1, progressMonitor);
		service.create(ProductClassifications.Products, activityMasterSystem);
		service.create(ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products);
		service.create(ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup);
	}
	
}
