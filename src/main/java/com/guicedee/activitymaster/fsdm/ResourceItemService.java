package com.guicedee.activitymaster.fsdm;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static jakarta.persistence.criteria.JoinType.*;


public class ResourceItemService
		implements IResourceItemService<ResourceItemService>
{

	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	@com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB
	private EntityManager entityManager;
	
	public IResourceItem<?, ?> get()
	{
		return new ResourceItem();
	}
	
	@Override
	public IResourceData<?, ?> getData()
	{
		return new ResourceItemData();
	}
	
	@Override
	public IResourceItemType<?, ?> getType()
	{
		return new ResourceItemType();
	}
	
	@Override
	public IResourceItemType<?, ?> createType(String value, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createType(value, null, description, system, identityToken);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IResourceItemType<?, ?> createType(String value, java.lang.String key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		boolean exists = xr.builder(entityManager)
		                   .withName(value)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(system.getEnterpriseID())
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setId(key);
			xr.setName(value);
			xr.setDescription(value);
			xr.setOriginalSourceSystemID(system);
			xr.setSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			xr.setActiveFlagID(activeFlag);
			xr.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			
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
	                                  ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return create(identityResourceType, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), system, identityToken);
	}
	
	@Override
	public IResourceItem<?, ?> create(String identityResourceType, java.lang.String key, String resourceItemDataValue,
	                                  ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return create(identityResourceType, key, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), system, identityToken);
	}
	
	
	@Override
	public IResourceItem<?, ?> create(String identityResourceType, String resourceItemDataValue, String originalSourceSystemUniqueID,
	                                  LocalDateTime effectiveFromDate,
	                                  ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, system, identityToken);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IResourceItem<?, ?> create(String identityResourceType, java.lang.String key, String resourceItemDataValue, String originalSourceSystemUniqueID,
	                                  LocalDateTime effectiveFromDate,
	                                  ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		ResourceItem xr = new ResourceItem();
		xr.setId(key);
		xr.setOriginalSourceSystemID(system);
		xr.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
		xr.setEffectiveFromDate(QueryBuilderSCD.convertToUTCDateTime(effectiveFromDate));
		xr.setSystemID(system);
		xr.setEnterpriseID(system.getEnterpriseID());
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
		xr.setActiveFlagID(activeFlag);
		xr.setResourceItemDataType(resourceItemDataValue);
		xr.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		
		xr.createDefaultSecurity(system, identityToken);
		
		xr.addResourceItemTypes(identityResourceType, null, DefaultClassifications.NoClassification.toString(), system, identityToken);
		
		ResourceItemData rid = new ResourceItemData();
		rid.setResource(xr);
		rid.setEffectiveFromDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		rid.setWarehouseCreatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		rid.setEffectiveToDate(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
		rid.setWarehouseLastUpdatedTimestamp(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
		rid.setResourceItemData("".getBytes());
		rid.setActiveFlagID(activeFlag);
		rid.setOriginalSourceSystemID(system);
		rid.setSystemID(system);
		rid.setEnterpriseID(system.getEnterpriseID());
		rid.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		
		rid.createDefaultSecurity(system, identityToken);
		
		return xr;
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IResourceItem<?, ?> findByClassification(String resourceType,
	                                                String classification,
	                                                String value,
	                                                ISystems<?, ?> systems,
	                                                java.util.UUID... identityToken)
	{
		ResourceItemXClassification res = new ResourceItemXClassification();
		ResourceItemXClassificationQueryBuilder builder = res.builder(entityManager);
		
		Classification clazz = (Classification) classificationService.find(classification, systems, identityToken);
		
		builder.where(ResourceItemXClassification_.classificationID, Equals, clazz);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(ResourceItemXClassification_.value, Equals, value);
		}
		
		JoinExpression<ResourceItem, ResourceItem, ResourceItemXClassification> resourceJoin = new JoinExpression<>();
		ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder(entityManager);
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
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>> findByClassificationAll(String resourceType,
	                                                                                                       String classification,
	                                                                                                       String value,
	                                                                                                       ISystems<?, ?> systems,
	                                                                                                       java.util.UUID... identityToken)
	{
		ResourceItemXClassification res = new ResourceItemXClassification();
		ResourceItemXClassificationQueryBuilder builder = res.builder(entityManager);
		
		Classification clazz = (Classification) classificationService.find(classification, systems, identityToken);
		
		builder.where(ResourceItemXClassification_.classificationID, Equals, clazz);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(ResourceItemXClassification_.value, Equals, value);
		}
		
		JoinExpression<ResourceItem, ResourceItem, ResourceItemXClassification> resourceJoin = new JoinExpression<>();
		ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder(entityManager);
		builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);
		
		ListJoin<ResourceItem, ResourceItemXResourceItemType> resourceItemTypesJoin = resourceJoin.getGeneratedRoot()
		                                                                                          .join(ResourceItem_.types, INNER);
		
		Join<ResourceItemXResourceItemType, ResourceItemType> resourceTypesJoin = resourceItemTypesJoin
				.join(ResourceItemXResourceItemType_.resourceItemTypeID, INNER);
		
		resourceTypesJoin.on(builder.getCriteriaBuilder()
		                            .equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType));
		
		return (List) builder.getAll();
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IResourceItem<?, ?> findByUUID(@CacheKey UUID uuid)
	{
		ResourceItem res = new ResourceItem();
		ResourceItemQueryBuilder builder = res.builder(entityManager);
		builder.where(ResourceItem_.id, Equals, uuid.toString());
		builder.inActiveRange();
		builder.inDateRange();
		Optional<ResourceItem> exists = builder.get();
		return exists.orElse(null);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IResourceItem<?, ?> findByOriginalSourceUniqueID(@CacheKey String originalSourceUniqueID,
	                                                        @CacheKey ISystems<?, ?> systems,
	                                                        @CacheKey java.util.UUID... identityToken)
	{
		ResourceItem res = new ResourceItem();
		ResourceItemQueryBuilder builder = res.builder(entityManager);
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
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	@CacheResult(cacheName = "FindResourceItemTypeString")
	public IResourceItemType<?, ?> findResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		Optional<ResourceItemType> exists = xr.builder(entityManager)
		                                      .withEnterprise(system.getEnterpriseID())
		                                      .withName(type)
		                                      .inActiveRange()
		                                      //       .canRead(system, identityToken)
		                                      .inDateRange()
		                                      .get();
		return exists.orElseThrow();
	}
	
	@Override
	public boolean doesResourceItemTypExist(String type,ISystems<?, ?> system,java.util.UUID... identityToken)
	{
		ResourceItemType xr = new ResourceItemType();
		return xr.builder(entityManager)
		                                      .withEnterprise(system.getEnterpriseID())
		                                      .withName(type)
		                                      .inActiveRange()
		                                      //       .canRead(system, identityToken)
		                                      .inDateRange()
		                                      .getCount() > 0;
	}
	
	@Override
	public List<IResourceItem<?, ?>> findByResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
	{
		return findByResourceItemType(type, null, systems, identityToken);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IResourceItem<?, ?>> findByResourceItemType(@CacheKey String type, String value, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
	{
		return new ResourceItemXResourceItemType().builder(entityManager)
		                                          .withEnterprise(systems.getEnterpriseID())
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
