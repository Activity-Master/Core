package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.core.ClassificationService;
import com.guicedee.activitymaster.core.SystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import static com.guicedee.activitymaster.client.services.IRulesService.*;


public class RulesSystem
		extends ActivityMasterDefaultSystem<RulesSystem>
		implements IActivityMasterSystem<RulesSystem>
{
	
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
		ISystems<?,?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		/*service.create(ProductClassifications.Products, activityMasterSystem);
		service.create(ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup);*/
		logProgress("Rules System", "Loaded Rules Classifications...", 4, progressMonitor);
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
		return RulesSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for managing Rules";
	}
}
