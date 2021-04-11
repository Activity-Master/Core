package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ClassificationService;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.guicedinjection.GuiceContext;

import static com.guicedee.activitymaster.fsdm.client.services.IRulesService.*;


public class RulesSystem
		extends ActivityMasterDefaultSystem<RulesSystem>
		implements IActivityMasterSystem<RulesSystem>
{
	
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
		ISystems<?,?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		/*service.create(ProductClassifications.Products, activityMasterSystem);
		service.create(ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup);
		service.create(ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup);*/
		logProgress("Rules System", "Loaded Rules Classifications...", 4);
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
