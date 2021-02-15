package com.guicedee.activitymaster.core;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static jakarta.persistence.criteria.JoinType.*;


public class ResourceItemService
		implements IResourceItemService<ResourceItemService>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?> activeFlag;
	
	@Inject
	private IEnterprise<?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Override
	public IResourceItemType<?> createType(IResourceType<?> value, ISystems<?> system, UUID... identityToken)
	{
		return createType(value.classificationName(), value.classificationDescription(), system, identityToken);
	}
	
	@Override
	public IResourceItemType<?> createType(String value, String description, ISystems<?> system, UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		boolean exists = xr.builder()
		                   .withName(value)
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(value);
			xr.setDescription(value);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				xr.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			return findResourceItemType(value, system, identityToken);
		}
		return xr;
	}
	
	
	@Override
	public IResourceItem<?> create(IResourceType<?> identityResourceType, String resourceItemDataValue,
	                               ISystems<?> system, UUID... identityToken)
	{
		return create(identityResourceType.classificationName(), resourceItemDataValue, system, identityToken);
	}
	
	@Override
	public IResourceItem<?> create(String identityResourceType, String resourceItemDataValue,
	                               ISystems<?> system, UUID... identityToken)
	{
		return create(identityResourceType, resourceItemDataValue, "", LocalDateTime.now(), system, identityToken);
	}
	
	@Override
	public IResourceItem<?> create(String identityResourceType, String resourceItemDataValue, String originalSourceSystemUniqueID,
	                               LocalDateTime effectiveFromDate,
	                               ISystems<?> system, UUID... identityToken)
	{
		if (!Strings.isNullOrEmpty(originalSourceSystemUniqueID))
		{
			if (new ResourceItem().builder()
			                      .where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceSystemUniqueID)
			                      .withValue(resourceItemDataValue)
			                      .withType(identityResourceType,null,system,identityToken)
			                      .inDateRange()
			                      .inActiveRange(enterprise, identityToken)
			                      .getCount() > 0)
			{
				return new ResourceItem().builder()
				                         .where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceSystemUniqueID)
				                         .withValue(resourceItemDataValue)
				                         .inDateRange()
				                         .withType(identityResourceType,null,system,identityToken)
				                         .inActiveRange(enterprise, identityToken)
				                         .get()
				                         .orElseThrow();
			}
		}
		ResourceItem xr = new ResourceItem();
		
		boolean exists = xr.builder()
		                   .withValue(resourceItemDataValue)
		                   .inActiveRange(enterprise, identityToken)
		                   .withType(identityResourceType,null,system,identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		if (exists)
		{
			return xr.builder()
			         .withValue(resourceItemDataValue)
			         .inActiveRange(enterprise, identityToken)
			         .inDateRange()
			         .withType(identityResourceType,null,system,identityToken)
			         .withEnterprise(enterprise)
			         .get()
			         .orElseThrow();
		}
		xr.setResourceItemUUID(UUID.randomUUID());
		xr.setOriginalSourceSystemID((Systems) system);
		xr.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		xr.setEffectiveFromDate(effectiveFromDate);
		xr.setSystemID((Systems) system);
		xr.setEnterpriseID((Enterprise) enterprise);
		xr.setActiveFlagID(activeFlag);
		xr.setResourceItemDataType(resourceItemDataValue);
		xr.persist();
		
		if (get(ActivityMasterConfiguration.class)
				.isSecurityEnabled())
		{
			xr.createDefaultSecurity(system, identityToken);
		}
		IResourceType<?> resourceItemType = (IResourceType<?>) createType(identityResourceType, identityResourceType, system, identityToken);
		
		
		xr.addResourceItemTypes(resourceItemType, null, Classifications.NoClassification.classificationName() , system, identityToken);
		
		return xr;
	}
	
	@Override
	public IResourceItem<?> findByClassification(IResourceType<?> resourceType,
	                                             IResourceItemClassification<?> classification,
	                                             String value,
	                                             ISystems<?> systems,
	                                             UUID... identityToken)
	{
		return findByClassification(resourceType, classification.classificationName(), value, systems, identityToken);
	}
	
	@Override
	public IResourceItem<?> findByClassification(IResourceType<?> resourceType,
	                                             String classification,
	                                             String value,
	                                             ISystems<?> systems,
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
		                            .equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType.classificationName()));
		
		Optional<ResourceItemXClassification> exists = builder.get();
		return exists.map(ResourceItemXClassification::getResourceItemID)
		             .orElse(null);
	}
	
	@Override
	public IResourceItem<?> findByUUID(@CacheKey UUID uuid,
	                                   @CacheKey ISystems<?> systems,
	                                   @CacheKey UUID... identityToken)
	{
		ResourceItem res = new ResourceItem();
		ResourceItemQueryBuilder builder = res.builder();
		builder.where(ResourceItem_.resourceItemUUID, Equals, uuid);
		builder.inActiveRange(enterprise, identityToken);
		builder.inDateRange();
		
		Optional<ResourceItem> exists = builder.get();
		return exists.orElse(null);
	}
	
	@Override
	public IResourceItem<?> findByOriginalSourceUniqueID(@CacheKey String originalSourceUniqueID,
	                                                     @CacheKey ISystems<?> systems,
	                                                     @CacheKey UUID... identityToken)
	{
		ResourceItem res = new ResourceItem();
		ResourceItemQueryBuilder builder = res.builder();
		builder.where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceUniqueID);
		builder.inActiveRange(enterprise, identityToken);
		builder.inDateRange();
		Optional<ResourceItem> exists = builder.get();
		return exists.orElse(null);
	}
	
	@Override
	public byte[] getDataForResourceItemValue(IRelationshipValue<IResourceItem<?>, IResourceData<?>, ?> data)
	{
		ResourceItemData d = (ResourceItemData) data.getSecondary();
		return d.getResourceItemData();
	}
	
	@Override
	@CacheResult(cacheName = "FindResourceItemType")
	public IResourceItemType<?> findResourceItemType(@CacheKey IResourceType<?> type, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return findResourceItemType(type.classificationName(), system, identityToken);
	}
	
	
	@Override
	@CacheResult(cacheName = "FindResourceItemTypeString")
	public IResourceItemType<?> findResourceItemType(@CacheKey String type, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder()
		                                      .withEnterprise(enterprise)
		                                      .withName(type)
		                                      .inActiveRange(enterprise, identityToken)
		                                      .canRead(system, identityToken)
		                                      .inDateRange()
		                                      .get();
		return exists.orElseThrow();
	}
	
	@Override
	public List<IResourceItem<?>> findByResourceItemType(@CacheKey String type, @CacheKey ISystems<?> systems, @CacheKey UUID... identityToken)
	{
		return findByResourceItemType(type,null, systems, identityToken);
	}
	
	@Override
	public List<IResourceItem<?>> findByResourceItemType(@CacheKey String type,String value, @CacheKey ISystems<?> systems, @CacheKey UUID... identityToken)
	{
		return new ResourceItemXResourceItemType().builder()
		                                          .withEnterprise(enterprise)
		                                          .inActiveRange(enterprise, identityToken)
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
