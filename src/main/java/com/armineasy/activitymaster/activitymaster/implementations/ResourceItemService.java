package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNameType;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemData;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IResourceTypeValue;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.system.IResourceItemService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.armineasy.activitymaster.activitymaster.systems.ResourceItemSystem;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class ResourceItemService implements IResourceItemService
{
	public ResourceItemType createType(IResourceTypeValue<?> value, Systems system, UUID... identityToken)
	{
		Enterprise enterprise = system.getEnterpriseID();

		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .findByName(value.classificationName())
		                                      .inActiveRange(enterprise)
		                                      .inDateRange()
		                                      .get();
		if (exists.isEmpty())
		{
			xr.setName(value.classificationName());
			xr.setDescription(value.classificationDescription());
			xr.setOriginalSourceSystemID(system);
			xr.setSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			xr.setActiveFlagID(system.getActiveFlagID());
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			xr.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
			                                     .getActivityMaster(xr.getEnterpriseID(), identityToken));
		}
		return xr;
	}

	@Transactional(entityManagerAnnotation =  ActivityMasterDB.class)
	@Override
	public ResourceItem create(IResourceTypeValue<?> identityResourceType,
	                           Systems system, UUID... identityToken)
	{
		Enterprise enterprise = system.getEnterpriseID();

		ResourceItem xr = new ResourceItem();
		xr.setResourceItemUUID(UUID.randomUUID());
		xr.setOriginalSourceSystemID(system);
		xr.setOriginalSourceSystemUniqueID("");
		xr.setSystemID(system);
		xr.setEnterpriseID(system.getEnterpriseID());
		xr.setActiveFlagID(system.getActiveFlagID());
		xr.persist();

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			xr.createDefaultSecurity(system);
		}
		return xr;
	}


}
