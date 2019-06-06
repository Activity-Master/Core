package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.ResourceItemService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.ResourceItemClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.jwebmp.guicedinjection.GuiceContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.ResourceItemClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.ResourceItemTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.ResourceItemTypes.Icon;

public class ResourceItemSystem
		implements IActivityMasterSystem<ResourceItemSystem>
{

	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	private static final Map<IEnterprise<?>, Systems> systemEnterprises = new HashMap<>();

	@SuppressWarnings("Duplicates")
	@Override
	public void createDefaults(IEnterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		ResourceItemService service = GuiceContext.get(ResourceItemService.class);

		service.createType(Documents, activityMasterSystem);
		service.createType(PhysicalDocuments, activityMasterSystem);
		service.createType(Invoices, activityMasterSystem);
		service.createType(Statements, activityMasterSystem);
		service.createType(ElectronicDocuments, activityMasterSystem);

		service.createType(Icon, activityMasterSystem);
		service.createType(Logo, activityMasterSystem);
		service.createType(Flag, activityMasterSystem);
		service.createType(Banner, activityMasterSystem);
		service.createType(Gravatar, activityMasterSystem);
		service.createType(Screenshot, activityMasterSystem);

		service.createType(Background, activityMasterSystem);
		service.createType(StyleSheets, activityMasterSystem);
		service.createType(JavaScriptTemplates, activityMasterSystem);
		service.createType(HtmlTemplate, activityMasterSystem);

		service.createType(MobileDevice, activityMasterSystem);
		service.createType(OperatingSystem, activityMasterSystem);
		service.createType(OperatingSystemFamily, activityMasterSystem);
		service.createType(BrowserInformation, activityMasterSystem);
		service.createType(BrowserDeviceCategory, activityMasterSystem);
		service.createType(BrowserDeviceIcon, activityMasterSystem);
		service.createType(BrowserDeviceName, activityMasterSystem);


		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		classificationService.create(FileResourceItemClassifications, activityMasterSystem);
		classificationService.create(AddedANewDevice, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(HadNewConnectionDetails, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(Description, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(Extension, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(Size, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(UUID, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(ResourceItemClassifications.Icon, activityMasterSystem, FileResourceItemClassifications);
		classificationService.create(FileLocation, activityMasterSystem, FileResourceItemClassifications);

		classificationService.create(EventDefaultResourceItemClassifications, activityMasterSystem);
		classificationService.create(Added, activityMasterSystem, EventDefaultResourceItemClassifications);
		classificationService.create(Removed, activityMasterSystem, EventDefaultResourceItemClassifications);
		classificationService.create(Updated, activityMasterSystem, EventDefaultResourceItemClassifications);
		classificationService.create(MovedTo, activityMasterSystem, EventDefaultResourceItemClassifications);

	}

	@Override
	public int totalTasks()
	{
		return 0;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 10;
	}

	@Override
	public void postUpdate(IEnterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Resource Items System",
		                                        "The system for managing Resource Items", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
		systemEnterprises.put(enterprise, newSystem);
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}

	public static Map<IEnterprise<?>, Systems> getSystemEnterprises()
	{
		return systemEnterprises;
	}
}
