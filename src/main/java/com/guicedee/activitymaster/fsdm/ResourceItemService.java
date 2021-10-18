package com.guicedee.activitymaster.fsdm;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static jakarta.persistence.criteria.JoinType.*;


public class ResourceItemService
		implements IResourceItemService<ResourceItemService>
{
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	public IResourceItem<?,?> get()
	{
		return new ResourceItem();
	}
	
	@Override
	public IResourceItemType<?,?> getType()
	{
		return new ResourceItemType();
	}
	
	@Override
	public IResourceItemType<?, ?> createType(String value, String description, ISystems<?,?> system, UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		boolean exists = xr.builder()
		                   .withName(value)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(value);
			xr.setDescription(value);
			xr.setOriginalSourceSystemID(system);
			xr.setSystemID(system);
			xr.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			
			xr.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return findResourceItemType(value, system, identityToken);
		}
		return xr;
	}
	
	
	@Override
	public IResourceItem<?, ?> create(String identityResourceType, String resourceItemDataValue,
	                                  ISystems<?,?> system, UUID... identityToken)
	{
		return create(identityResourceType, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), system, identityToken);
	}
	
	@Override
	public IResourceItem<?, ?> create(String identityResourceType, String resourceItemDataValue, String originalSourceSystemUniqueID,
	                                  LocalDateTime effectiveFromDate,
	                                  ISystems<?,?> system, UUID... identityToken)
	{
		if (!Strings.isNullOrEmpty(originalSourceSystemUniqueID))
		{
			if (new ResourceItem().builder()
			                      .where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceSystemUniqueID)
			                      .withValue(resourceItemDataValue)
			                      .withType(identityResourceType, null, system, identityToken)
			                      .inDateRange()
			                      .inActiveRange()
			                      .getCount() > 0)
			{
				return new ResourceItem().builder()
				                         .where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceSystemUniqueID)
				                         .withValue(resourceItemDataValue)
				                         .inDateRange()
				                         .withType(identityResourceType, null, system, identityToken)
				                         .inActiveRange()
				                         .get()
				                         .orElseThrow();
			}
		}
		ResourceItem xr = new ResourceItem();
		
		boolean exists = xr.builder()
		                   .withValue(resourceItemDataValue)
		                   .inActiveRange()
		                   .withType(identityResourceType, null, system, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		if (exists)
		{
			return xr.builder()
			         .withValue(resourceItemDataValue)
			         .inActiveRange()
			         .inDateRange()
			         .withType(identityResourceType, null, system, identityToken)
			         .withEnterprise(enterprise)
			         .get()
			         .orElseThrow();
		}
		xr.setOriginalSourceSystemID(system);
		xr.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		xr.setEffectiveFromDate(effectiveFromDate);
		xr.setSystemID(system);
		xr.setEnterpriseID(enterprise);
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
		xr.setActiveFlagID(activeFlag);
		xr.setResourceItemDataType(resourceItemDataValue);
		xr.persist();
		
		xr.createDefaultSecurity(system, identityToken);
		
		xr.addResourceItemTypes(identityResourceType, null, DefaultClassifications.NoClassification.toString(), system, identityToken);
		
		return xr;
	}
	
	
	@Override
	public IResourceItem<?, ?> findByClassification(String resourceType,
	                                                String classification,
	                                                String value,
	                                                ISystems<?,?> systems,
	                                                UUID... identityToken)
	{
		ResourceItemXClassification res = new ResourceItemXClassification();
		ResourceItemXClassificationQueryBuilder builder = res.builder();
		
		Classification clazz = (Classification) classificationService.find(classification, systems, identityToken);
		
		builder.where(ResourceItemXClassification_.classificationID, Equals, clazz);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(ResourceItemXClassification_.value, Equals, value);
		}
		
		JoinExpression<ResourceItem, ResourceItem, ResourceItemXClassification> resourceJoin = new JoinExpression<>();
		ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder();
		builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);
		
		ListJoin<ResourceItem, ResourceItemXResourceItemType> resourceItemTypesJoin = resourceJoin.getGeneratedRoot()
		                                                                                          .join(ResourceItem_.types, INNER);
		
		Join<ResourceItemXResourceItemType, ResourceItemType> resourceTypesJoin = resourceItemTypesJoin
				.join(ResourceItemXResourceItemType_.resourceItemTypeID, INNER);
		
		resourceTypesJoin.on(builder.getCriteriaBuilder()
		                            .equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType));
		
		Optional<ResourceItemXClassification> exists = builder.get();
		return exists.map(ResourceItemXClassification::getResourceItemID)
		             .orElse(null);
	}
	
	@Override
	public List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?,?>,?>> findByClassificationAll(String resourceType,
	                                                                                             String classification,
	                                                                                             String value,
	                                                                                             ISystems<?,?> systems,
	                                                                                             UUID... identityToken)
	{
		ResourceItemXClassification res = new ResourceItemXClassification();
		ResourceItemXClassificationQueryBuilder builder = res.builder();
		
		Classification clazz = (Classification) classificationService.find(classification, systems, identityToken);
		
		builder.where(ResourceItemXClassification_.classificationID, Equals, clazz);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(ResourceItemXClassification_.value, Equals, value);
		}
		
		JoinExpression<ResourceItem, ResourceItem, ResourceItemXClassification> resourceJoin = new JoinExpression<>();
		ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder();
		builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);
		
		ListJoin<ResourceItem, ResourceItemXResourceItemType> resourceItemTypesJoin = resourceJoin.getGeneratedRoot()
		                                                                                          .join(ResourceItem_.types, INNER);
		
		Join<ResourceItemXResourceItemType, ResourceItemType> resourceTypesJoin = resourceItemTypesJoin
				.join(ResourceItemXResourceItemType_.resourceItemTypeID, INNER);
		
		resourceTypesJoin.on(builder.getCriteriaBuilder()
		                            .equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType));
		
		return (List) builder.getAll();
	}
	
	@Override
	public IResourceItem<?, ?> findByUUID(@CacheKey UUID uuid)
	{
		ResourceItem res = new ResourceItem();
		ResourceItemQueryBuilder builder = res.builder();
		builder.where(ResourceItem_.id, Equals, uuid);
		builder.inActiveRange();
		builder.inDateRange();
		Optional<ResourceItem> exists = builder.get();
		return exists.orElse(null);
	}
	
	@Override
	public IResourceItem<?, ?> findByOriginalSourceUniqueID(@CacheKey String originalSourceUniqueID,
	                                                        @CacheKey ISystems<?,?> systems,
	                                                        @CacheKey UUID... identityToken)
	{
		ResourceItem res = new ResourceItem();
		ResourceItemQueryBuilder builder = res.builder();
		builder.where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceUniqueID);
		builder.inActiveRange();
		builder.inDateRange();
		Optional<ResourceItem> exists = builder.get();
		return exists.orElse(null);
	}
	
	@Override
	public byte[] getDataForResourceItemValue(IRelationshipValue<IResourceItem<?, ?>, IResourceData<?, ?>, ?> data)
	{
		ResourceItemData d = (ResourceItemData) data.getSecondary();
		return d.getResourceItemData();
	}
	
	@Override
	@CacheResult(cacheName = "FindResourceItemTypeString")
	public IResourceItemType<?, ?> findResourceItemType(@CacheKey String type, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .withEnterprise(enterprise)
		                                      .withName(type)
		                                      .inActiveRange()
		                                      //       .canRead(system, identityToken)
		                                      .inDateRange()
		                                      .get();
		return exists.orElseThrow();
	}
	
	@Override
	public List<IResourceItem<?, ?>> findByResourceItemType(@CacheKey String type, @CacheKey ISystems<?,?> systems, @CacheKey UUID... identityToken)
	{
		return findByResourceItemType(type, null, systems, identityToken);
	}
	
	@Override
	public List<IResourceItem<?, ?>> findByResourceItemType(@CacheKey String type, String value, @CacheKey ISystems<?,?> systems, @CacheKey UUID... identityToken)
	{
		return new ResourceItemXResourceItemType().builder()
		                                          .withEnterprise(enterprise)
		                                          .inActiveRange()
		                                          .inDateRange()
		                                          .canRead(systems, identityToken)
		                                          .withType(type, systems, identityToken)
		                                          .withValue(value)
		                                          .getAll()
		                                          .stream()
		                                          .map(ResourceItemXResourceItemType::getResourceItemID)
		                                          .collect(Collectors.toList());
	}
}
