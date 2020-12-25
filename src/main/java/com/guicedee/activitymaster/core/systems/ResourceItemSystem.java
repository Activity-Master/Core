package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.ResourceItemService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.ResourceItemClassifications;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.ResourceItemTypes;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.guicedinjection.GuiceContext;

@Singleton
public class ResourceItemSystem
		extends ActivityMasterDefaultSystem<ResourceItemSystem>
		implements IActivityMasterSystem<ResourceItemSystem>
{
	
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		
		ResourceItemService service = GuiceContext.get(ResourceItemService.class);
		
		service.createType(ResourceItemTypes.Documents, activityMasterSystem);
		service.createType(ResourceItemTypes.JsonPacket, activityMasterSystem);
		service.createType(ResourceItemTypes.XMLPacket, activityMasterSystem);
		service.createType(ResourceItemTypes.PhysicalDocuments, activityMasterSystem);
		service.createType(ResourceItemTypes.Invoices, activityMasterSystem);
		service.createType(ResourceItemTypes.Statements, activityMasterSystem);
		service.createType(ResourceItemTypes.ElectronicDocuments, activityMasterSystem);
		
		service.createType(ResourceItemTypes.Icon, activityMasterSystem);
		service.createType(ResourceItemTypes.Logo, activityMasterSystem);
		service.createType(ResourceItemTypes.Flag, activityMasterSystem);
		service.createType(ResourceItemTypes.Banner, activityMasterSystem);
		service.createType(ResourceItemTypes.Gravatar, activityMasterSystem);
		service.createType(ResourceItemTypes.Screenshot, activityMasterSystem);
		
		service.createType(ResourceItemTypes.Background, activityMasterSystem);
		service.createType(ResourceItemTypes.StyleSheets, activityMasterSystem);
		service.createType(ResourceItemTypes.JavaScriptTemplates, activityMasterSystem);
		service.createType(ResourceItemTypes.HtmlTemplate, activityMasterSystem);
		
		service.createType(ResourceItemTypes.MobileDevice, activityMasterSystem);
		service.createType(ResourceItemTypes.BrowserInformation, activityMasterSystem);
		
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		classificationService.create(ResourceItemClassifications.FileResourceItemClassifications, activityMasterSystem);
		classificationService.create(ResourceItemClassifications.AddedANewDevice, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.HadNewConnectionDetails, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Description, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Extension, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Size, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.UUID, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Icon, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.FileLocation, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		
		classificationService.create(ResourceItemClassifications.EventDefaultResourceItemClassifications, activityMasterSystem);
		classificationService.create(ResourceItemClassifications.Added, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Removed, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Updated, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.MovedTo, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		
		
		
	}
	
	@Override
	public int totalTasks()
	{
		return 0;
	}
	
	@Override
	public String getSystemName()
	{
		return "Resource Items System";
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for managing Resource Items";
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 10;
	}
}
