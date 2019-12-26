package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.ResourceItemService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.ResourceItemClassifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.ResourceItemTypes;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResourceItemSystem
		implements IActivityMasterSystem<ResourceItemSystem>
{

	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	private static final Map<IEnterprise<?>, Systems> systemsMap = new HashMap<>();

	@SuppressWarnings("Duplicates")
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);

		ResourceItemService service = GuiceContext.get(ResourceItemService.class);

		service.createType(ResourceItemTypes.Documents, activityMasterSystem);
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
		service.createType(ResourceItemTypes.OperatingSystem, activityMasterSystem);
		service.createType(ResourceItemTypes.OperatingSystemFamily, activityMasterSystem);
		service.createType(ResourceItemTypes.BrowserInformation, activityMasterSystem);
		service.createType(ResourceItemTypes.BrowserDeviceCategory, activityMasterSystem);
		service.createType(ResourceItemTypes.BrowserDeviceIcon, activityMasterSystem);
		service.createType(ResourceItemTypes.BrowserDeviceName, activityMasterSystem);

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
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 10;
	}

	@Override
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		final String systemName = "Resource Items System";
		final String systemDesc = "The system for managing Resource Items";
		Systems sys = (Systems) GuiceContext.get(SystemsService.class)
		                                    .findSystem(enterprise, systemName);
		UUID securityToken = null;
		if (sys == null)
		{
			sys = (Systems) GuiceContext.get(SystemsService.class)
			                                    .create(enterprise, systemName, systemDesc, systemName);

			securityToken = GuiceContext.get(ISystemsService.class)
			                            .registerNewSystem(enterprise, sys);
		}
		else
		{
			securityToken = GuiceContext.get(SystemsService.class)
			                            .getSecurityIdentityToken(sys);
		}
		systemTokens.put(enterprise, securityToken);
		systemsMap.put(enterprise, sys);
	}

	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{

	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}

	public static Map<IEnterprise<?>, Systems> getSystemsMap()
	{
		return systemsMap;
	}
}
