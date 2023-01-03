package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.classifications.ResourceItemClassifications;
import com.guicedee.activitymaster.fsdm.client.types.classifications.ResourceItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -200, taskCount = 3)
public class ResourceItemsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Inject
	private IResourceItemService<?> resourceItemService;
	
	@Override
	public void update(IEnterprise<?,?> enterprise)
	{
		logProgress("Resource Items", "Loading Default Resource Items...", 1);
		resourceItemService.createType(ResourceItemTypes.Documents, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.JsonPacket, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.XMLPacket, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.PhysicalDocuments, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Invoices, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Statements, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.ElectronicDocuments, activityMasterSystem);
		
		logProgress("Resource Items", "Loading Base Image Types...", 1);
		
		resourceItemService.createType(ResourceItemTypes.Icon, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Logo, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Flag, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Banner, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Gravatar, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.Screenshot, activityMasterSystem);
		
		resourceItemService.createType(ResourceItemTypes.Background, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.StyleSheets, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.JavaScriptTemplates, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.HtmlTemplate, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.StringTemplate, activityMasterSystem);
		
		logProgress("Resource Items", "Loading Base Device Types...", 1);
		
		resourceItemService.createType(ResourceItemTypes.MobileDevice, activityMasterSystem);
		resourceItemService.createType(ResourceItemTypes.BrowserInformation, activityMasterSystem);
		
		service.create(ResourceItemClassifications.FileResourceItemClassifications, activityMasterSystem);
		service.create(ResourceItemClassifications.AddedANewDevice, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.HadNewConnectionDetails, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.Description, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.Extension, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.FileName, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.Size, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.UUID, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.Icon, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		service.create(ResourceItemClassifications.FileLocation, activityMasterSystem, ResourceItemClassifications.FileResourceItemClassifications);
		
		logProgress("Resource Items", "Loading Event Types...", 1);
		service.create(ResourceItemClassifications.EventDefaultResourceItemClassifications, activityMasterSystem);
		service.create(ResourceItemClassifications.Added, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		service.create(ResourceItemClassifications.Removed, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		service.create(ResourceItemClassifications.Updated, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
		service.create(ResourceItemClassifications.MovedTo, activityMasterSystem, ResourceItemClassifications.EventDefaultResourceItemClassifications);
	}
	
}
