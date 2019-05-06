package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.*;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IResourceTypeValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationService;
import com.armineasy.activitymaster.activitymaster.services.system.IResourceItemService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.jwebmp.entityassist.querybuilder.builders.JoinExpression;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.persistence.criteria.JoinType;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.entityassist.enumerations.Operand.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;
import static javax.persistence.criteria.JoinType.*;

@Singleton
public class ResourceItemService
		implements IResourceItemService
{
	public ResourceItemType createType(IResourceTypeValue<?> value, Systems system, UUID... identityToken)
	{
		Enterprise enterprise = system.getEnterpriseID();

		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .findByName(value.classificationName())
		                                      .inActiveRange(enterprise, identityToken)
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
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(get(ISystemsService.class)
					                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public ResourceItem create(IResourceTypeValue<?> identityResourceType,
	                           Systems system, UUID... identityToken)
	{
		ResourceItem xr = new ResourceItem();
		xr.setResourceItemUUID(UUID.randomUUID());
		xr.setOriginalSourceSystemID(system);
		xr.setOriginalSourceSystemUniqueID("");
		xr.setSystemID(system);
		xr.setEnterpriseID(system.getEnterpriseID());
		xr.setActiveFlagID(system.getActiveFlagID());
		xr.persist();

		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(system, identityToken);
		}

		xr.addResourceItemType(findResourceItemType(identityResourceType, system, identityToken), "", identityToken);

		return xr;
	}

	@CacheResult(cacheName = "ResourceItemFindByClassification")
	public ResourceItem findByClassification(@CacheKey IResourceTypeValue<?> resourceType,
	                                         @CacheKey IResourceItemClassification<?> classification,
	                                         @CacheKey String value,
	                                         @CacheKey Systems systems,
	                                         @CacheKey UUID... identityToken)
	{
		ResourceItemXClassification res = new ResourceItemXClassification();
		ResourceItemXClassificationQueryBuilder builder = res.builder();

		IClassificationService classificationService = get(IClassificationService.class);
		Classification clazz = classificationService.find(classification, systems.getEnterpriseID(), identityToken);

		builder.where(ResourceItemXClassification_.classificationID, Equals, clazz);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(ResourceItemXClassification_.value, Equals, value);
		}

		JoinExpression resourceJoin = new JoinExpression();
		ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder();
		itemQueryBuilder.inActiveRange(systems.getEnterpriseID());
		itemQueryBuilder.inDateRange();
		builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);

		JoinExpression typesJoinExpression = new JoinExpression();
		ResourceItemXResourceItemTypeQueryBuilder types = new ResourceItemXResourceItemType().builder();
		types.inActiveRange(systems.getEnterpriseID());
		types.inDateRange();
		itemQueryBuilder.join(ResourceItem_.types, types, INNER, typesJoinExpression);

		JoinExpression itemTypeExpression = new JoinExpression();
		ResourceItemTypeQueryBuilder itemType = new ResourceItemType().builder();
		itemType.inActiveRange(systems.getEnterpriseID());
		itemType.inDateRange();
		types.join(ResourceItemXResourceItemType_.resourceItemTypeID, itemType, INNER, itemTypeExpression);

		itemType.where(ResourceItemType_.name, Equals, resourceType.classificationName());

		Optional<ResourceItemXClassification> exists = builder.get();
		return exists.map(ResourceItemXClassification::getResourceItemID)
		             .orElse(null);
	}

	@CacheResult(cacheName = "FindResourceItemType")
	public ResourceItemType findResourceItemType(@CacheKey IResourceTypeValue<?> type, @CacheKey Systems systems, @CacheKey UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .findByName(type.classificationName())
		                                      .inActiveRange(systems.getEnterpriseID(), identityToken)
		                                      .canRead(systems.getEnterpriseID(), identityToken)
		                                      .inDateRange()
		                                      .get();
		return exists.orElse(null);
	}

}
