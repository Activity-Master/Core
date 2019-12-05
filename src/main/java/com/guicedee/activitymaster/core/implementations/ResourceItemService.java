package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static javax.persistence.criteria.JoinType.*;

@Singleton
public class ResourceItemService
		implements IResourceItemService<ResourceItemService>
{
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IResourceItemType<?> createType(IResourceType<?> value, ISystems<?> system, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterpriseID();

		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .findByName(value.classificationName())
		                                      .inActiveRange(enterprise, identityToken)
		                                      .inDateRange()
		                                      .withEnterprise(system.getEnterprise())
		                                      .get();
		if (exists.isEmpty())
		{
			xr.setName(value.classificationName());
			xr.setDescription(value.classificationDescription());
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID((ActiveFlag)get(IActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
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
	public IResourceItem<?> create(IResourceType<?> identityResourceType, String mimeType,
	                               ISystems<?> system, UUID... identityToken)
	{
		ResourceItem xr = new ResourceItem();
		xr.setResourceItemUUID(UUID.randomUUID());
		xr.setOriginalSourceSystemID((Systems) system);
		xr.setOriginalSourceSystemUniqueID("");
		xr.setSystemID((Systems) system);
		xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
		xr.setActiveFlagID(((Systems)system).getActiveFlagID());
		xr.setResourceItemDataType(mimeType);
		xr.persist();

		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(system, identityToken);
		}

		xr.add(identityResourceType,"Created With", system, identityToken);

		return xr;
	}

	@SuppressWarnings("unchecked")
	@CacheResult(cacheName = "ResourceItemFindByClassification")
	@Override
	public IResourceItem<?> findByClassification(@CacheKey IResourceType<?> resourceType,
	                                          @CacheKey IResourceItemClassification<?> classification,
	                                          @CacheKey String value,
	                                          @CacheKey ISystems<?> systems,
	                                          @CacheKey UUID... identityToken)
	{
		ResourceItemXClassification res = new ResourceItemXClassification();
		ResourceItemXClassificationQueryBuilder builder = res.builder();

		IClassificationService classificationService = get(IClassificationService.class);
		Classification clazz = (Classification) classificationService.find(classification, systems.getEnterpriseID(), identityToken);

		builder.where(ResourceItemXClassification_.classificationID, Equals, clazz);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(ResourceItemXClassification_.value, Equals, value);
		}

		JoinExpression resourceJoin = new JoinExpression();
		ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder();
		builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);

		Join resourceItemTypesJoin = resourceJoin.getGeneratedRoot()
		                                         .join(ResourceItem_.types, INNER);

		Join resourceTypesJoin = resourceItemTypesJoin
		                                         .join(ResourceItemXResourceItemType_.resourceItemTypeID, INNER);

		resourceTypesJoin.on(builder.getCriteriaBuilder()
		                            .equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType.classificationName()));

		Optional<ResourceItemXClassification> exists = builder.get();
		return exists.map(ResourceItemXClassification::getResourceItemID)
		             .orElse(null);
	}

	@Override
	public byte[] getDataForResourceItemValue(IRelationshipValue<IResourceItem<?>, IResourceData<?>, ?> data)
	{
		ResourceItemData d = (ResourceItemData) data.getSecondary();
		return d.getResourceItemData();
	}

	@Override
	@CacheResult(cacheName = "FindResourceItemType")
	public IResourceItemType<?> findResourceItemType(@CacheKey IResourceType<?> type, @CacheKey ISystems<?> systems, @CacheKey UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .withEnterprise(systems.getEnterprise())
		                                      .findByName(type.classificationName())
		                                      .inActiveRange(systems.getEnterpriseID(), identityToken)
		                                      .canRead(systems.getEnterpriseID(), identityToken)
		                                      .inDateRange()
		                                      .get();
		return exists.orElse(null);
	}


}
