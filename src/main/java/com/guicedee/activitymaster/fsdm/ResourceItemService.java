package com.guicedee.activitymaster.fsdm;

import com.entityassist.RootEntity;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceData;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ResourceItemException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.Equals;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.EndOfTime;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;
import static jakarta.persistence.criteria.JoinType.INNER;


/**
	* A reactive service for managing resource items in the system.
	* <p>
	* This service implements the IResourceItemService interface and provides methods for creating,
	* finding, and managing resource items. All methods return Uni objects to support reactive
	* programming patterns.
	* <p>
	* Key features:
	* - All operations are non-blocking and return Uni objects
	* - Database operations are executed using reactive patterns
	* - Transactions are managed using ReactiveTransactionUtil
	* - Comprehensive logging is provided for debugging and monitoring
	* - In-memory caching is used for frequently accessed data
	* <p>
	* Usage example:
	* <pre>
	* resourceItemService.findByUUID(uuid)
	*     .chain(item -> {
	*         // Process the item
	*         return doSomethingElse(item);
	*     });
	* </pre>
	*/
@SuppressWarnings("unchecked")
@Log4j2
@Singleton
public class ResourceItemService
	implements IResourceItemService<ResourceItemService>
{
		// Local cache: key = enterpriseId + '|' + systemId + '|' + resourceItemTypeName → ResourceItemType UUID
		private final java.util.Map<String, java.util.UUID> resourceItemTypeKeyToId = new java.util.concurrent.ConcurrentHashMap<>();
		
		// UUID-based lookup to leverage Hibernate 2nd-level cache
		public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType<?, ?>> getResourceItemTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id)
		{
				return (io.smallrye.mutiny.Uni) session.find(com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemType.class, id);
		}
		
		@Inject
		private IClassificationService<?> classificationService;
		
		/**
			* Gets a new ResourceItem instance.
			* This is a lightweight operation that doesn't require a transaction.
			*
			* @return A Uni containing a new ResourceItem instance
			*/
		@Override
		public IResourceItem<?, ?> get()
		{
				log.trace("Getting new ResourceItem instance");
				return new ResourceItem();
		}
		
		/**
			* Gets a new ResourceItemData instance.
			* This is a lightweight operation that doesn't require a transaction.
			*
			* @return A Uni containing a new ResourceItemData instance
			*/
		@Override
		public IResourceData<?, ?, ?> getData()
		{
				log.trace("Getting new ResourceItemData instance");
				return new ResourceItemData();
		}
		
		/**
			* Gets a new ResourceItemType instance.
			* This is a lightweight operation that doesn't require a transaction.
			*
			* @return A Uni containing a new ResourceItemType instance
			*/
		@Override
		public IResourceItemType<?, ?> getType()
		{
				log.trace("Getting new ResourceItemType instance");
				return new ResourceItemType();
		}
		
		@Override
		public Uni<IResourceItemType<?, ?>> createType(Mutiny.Session session, String value, String description, ISystems<?, ?> system, UUID... identityToken)
		{
				log.trace("Creating resource type with value: {}, description: {}", value, description);
				return createType(session, value, null, description, system, identityToken);
		}
		
		@Override
		public Uni<IResourceItemType<?, ?>> createType(Mutiny.Session session, String value, UUID key, String description, ISystems<?, ?> system, UUID... identityToken)
		{
				log.debug("Creating resource type with value: {}, key: {}, description: {}", value, key, description);
				ResourceItemType xr = new ResourceItemType();
				var enterprise = system.getEnterprise();
				// First check if the resource type already exists
				return xr
												.builder(session)
												.withName(value)
												.inActiveRange()
												.inDateRange()
												.withEnterprise(enterprise)
												.getCount()
												.chain(count -> {
														if (count <= 0)
														{
																// Resource type doesn't exist, create a new one
																xr.setId(key);
																xr.setName(value);
																xr.setDescription(value);
																xr.setOriginalSourceSystemID(system.getId());
																xr.setSystemID(system);
																xr.setEnterpriseID(enterprise);
																IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
																
																return acService
																								.getActiveFlag(session, enterprise)
																								.chain(activeFlag -> {
																										xr.setActiveFlagID(activeFlag);
																										return session
																																		.persist(xr)
																																		.replaceWith(Uni
																																																.createFrom()
																																																.item(xr));
																								})
																								.map(persisted -> {
																										// Create default security
																										xr.createDefaultSecurity(session, system, identityToken);
																										return (IResourceItemType<?, ?>) xr;
																								});
														}
														else
														{
																// Resource type exists, find it
																ResourceItemType resourceItemType = new ResourceItemType();
																return resourceItemType
																								.builder(session)
																								.withEnterprise(enterprise)
																								.withName(value)
																								.inActiveRange()
																								.inDateRange()
																								.get()
																								.map(existingType -> {
																										if (existingType == null)
																										{
																												throw new ResourceItemException("Cannot find resource item type [%s]".formatted(value));
																										}
																										return (IResourceItemType<?, ?>) existingType;
																								});
														}
												});
		}
		
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
		}
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue, byte[] data,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
		}
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
		}
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue, byte[] data,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
		}
		
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
																																									LocalDateTime effectiveFromDate,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, system, identityToken);
		}
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
																																									LocalDateTime effectiveFromDate, byte[] data,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, data, system, identityToken);
		}
		
		
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
																																									LocalDateTime effectiveFromDate,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				return create(session, identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, null, system, identityToken);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue,
																																									UUID originalSourceSystemUniqueID,
																																									LocalDateTime effectiveFromDate, byte[] data,
																																									ISystems<?, ?> system, UUID... identityToken)
		{
				log.debug("Creating resource item - type: {}, value: {}", identityResourceType, resourceItemDataValue);
				var enterprise = system.getEnterprise();
				
				return findByUUID(session, key)
												.onFailure(NoResultException.class)
												.recoverWithUni(e -> {
// Step 1: Create the resource item
														ResourceItem xr = new ResourceItem();
														xr.setId(key);
														xr.setOriginalSourceSystemID(system.getId());
														xr.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
														xr.setEffectiveFromDate(convertToUTCDateTime(effectiveFromDate));
														xr.setSystemID(system);
														xr.setEnterpriseID(enterprise);
														IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
														
														return acService
																												.getActiveFlag(session, enterprise)
																												.chain(activeFlag -> {
																														xr.setActiveFlagID(activeFlag);
																														xr.setResourceItemDataType(resourceItemDataValue);
																														
																														// Persist the resource item
																														return session
																																						.persist(xr)
																																						.replaceWith(Uni
																																																				.createFrom()
																																																				.item(xr))
																																						.chain(persisted -> {
																																								// Create resource item data
																																								ResourceItemData rid = new ResourceItemData();
																																								rid.setResource(persisted);
																																								LocalDateTime now = RootEntity.getNow();
																																								rid.setEffectiveFromDate(convertToUTCDateTime(now));
																																								rid.setWarehouseCreatedTimestamp(convertToUTCDateTime(now));
																																								rid.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
																																								rid.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(now));
																																								rid.setActiveFlagID(activeFlag);
																																								rid.setOriginalSourceSystemID(system.getId());
																																								rid.setSystemID(system);
																																								rid.setEnterpriseID(system.getEnterpriseID());
																																								
																																								ResourceItemDataValue dataValue = new ResourceItemDataValue();
																																								dataValue.setId(persisted.getId());
																																								dataValue.setData(data);
																																								if (data == null)
																																								{
																																										dataValue.setData(new byte[0]);
																																								}
																																								rid.setDataValue(dataValue);
																																								
																																								// Persist the resource item data
																																								return session
																																																.persist(rid)
																																																.chain(() -> {
																																																		return session.persist(rid.getDataValue());
																																																})
																																																.replaceWith(Uni
																																																														.createFrom()
																																																														.item(rid))
																																																.map(persistedData -> {
																																																		// Create default security
																																																		persistedData.createDefaultSecurity(session, system, identityToken);
																																																		return persistedData;
																																																})
																																																.chain(_ -> {
																																																		// Step 3: Add resource item types
																																																		log.trace("Adding resource item type: {}", identityResourceType);
																																																		return addResourceItemTypeRelationship(session, persisted, identityResourceType, resourceItemDataValue, system, identityToken);
																																																})
																																																.replaceWith(persisted);
																																						});
																												})
																												.onFailure()
																												.invoke(cause -> {
																														log.error("Failed to create resource item", cause);
																												});
												});
		}
		
		@Override
		public Uni<Void> updateResourceData(Mutiny.Session session, byte[] data, UUID resourceItemId)
		{
				return session
												.find(ResourceItemDataValue.class, resourceItemId)
												.chain(resourceItem -> {
														if (resourceItem == null)
														{
																return Uni
																								.createFrom()
																								.failure(new IllegalStateException("Resource item not found: " + resourceItemId));
														}
														resourceItem.setData(data);
														return session.merge(resourceItem);
												})
												.replaceWithVoid();
		}
		
		/**
			* Helper method to add resource item type relationship
			*/
		@Override
		public Uni<Void> addResourceItemTypeRelationship(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String typeName, String value, ISystems<?, ?> system, UUID... identityToken)
		{
				log.trace("Adding resource item type relationship: {} for item: {}", typeName, resourceItem.getId());
				var enterprise = system.getEnterprise();
				return findResourceItemType(session, typeName, system, identityToken)
												.chain(resourceItemType -> {
														return classificationService
																						.find(session, DefaultClassifications.NoClassification.toString(), system, identityToken)
																						.chain(classification -> {
																								ResourceItemXResourceItemType relationship = new ResourceItemXResourceItemType();
																								relationship.setResourceItemID((ResourceItem) resourceItem);
																								relationship.setResourceItemTypeID((ResourceItemType) resourceItemType);
																								relationship.setClassificationID(classification);
																								relationship.setValue("");
																								relationship.setSystemID(system);
																								relationship.setEnterpriseID(enterprise);
																								
																								IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
																								return afs
																																.getActiveFlag(session, enterprise)
																																.chain(activeFlag -> {
																																		relationship.setActiveFlagID((IActiveFlag<?, ?>) activeFlag);
																																		relationship.setOriginalSourceSystemID(system.getId());
																																		LocalDateTime now = RootEntity.getNow();
																																		relationship.setEffectiveFromDate(convertToUTCDateTime(now));
																																		relationship.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
																																		relationship.setWarehouseCreatedTimestamp(convertToUTCDateTime(now));
																																		relationship.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(now));
																																		
																																		return session
																																										.persist(relationship)
																																										.replaceWith(Uni
																																																								.createFrom()
																																																								.item(relationship));
																																})
																																.chain(persisted -> {
																																		// Create default security
																																		return relationship
																																										.createDefaultSecurity(session, system, identityToken)
																																										.replaceWithVoid();
																																});
																						});
												});
		}
		
		@Override
		public Uni<IResourceItem<?, ?>> findByClassification(Mutiny.Session session, String resourceType,
																																																							String classification,
																																																							String value,
																																																							ISystems<?, ?> systems,
																																																							UUID... identityToken)
		{
				log.trace("Finding resource by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);
				
				// First get the classification using reactive pattern
				return classificationService
												.find(session, classification, systems, identityToken)
												.chain(clazz -> {
														if (clazz == null)
														{
																return Uni
																								.createFrom()
																								.nullItem();
														}
														
														try
														{
																ResourceItemXClassification res = new ResourceItemXClassification();
																ResourceItemXClassificationQueryBuilder builder = res.builder(session);
																
																builder.where(ResourceItemXClassification_.classificationID, Equals, (Classification) clazz);
																if (!Strings.isNullOrEmpty(value))
																{
																		builder.where(ResourceItemXClassification_.value, Equals, value);
																}
																
																JoinExpression<ResourceItem, ResourceItem, ResourceItemXClassification> resourceJoin = new JoinExpression<>();
																ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder(session);
																builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);
																
																ListJoin<ResourceItem, ResourceItemXResourceItemType> resourceItemTypesJoin = resourceJoin
																																																																																															.getGeneratedRoot()
																																																																																															.join(ResourceItem_.types, INNER);
																
																Join<ResourceItemXResourceItemType, ResourceItemType> resourceTypesJoin = resourceItemTypesJoin
																																																																																											.join(ResourceItemXResourceItemType_.resourceItemTypeID, INNER);
																
																resourceTypesJoin.on(builder
																																						.getCriteriaBuilder()
																																						.equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType));
																
																// Get the result from the builder using reactive pattern
																return builder
																								.get()
																								.chain(exists -> exists != null ?
																																										session.fetch(exists.getResourceItemID()) :
																																										Uni
																																											.createFrom()
																																											.failure(new ResourceItemException("Cannot find resource item for classification: %s".formatted(classification))));
														}
														catch (Exception e)
														{
																log.error("Error finding resource by classification - resourceType: {}, classification: {}, value: {}",
																										resourceType, classification, value, e);
																return Uni
																								.createFrom()
																								.failure(e);
														}
														
												});
		}
		
		
		@Override
		public Uni<List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>> findByClassificationAll(Mutiny.Session session, String resourceType,
																																																																																																														String classification,
																																																																																																														String value,
																																																																																																														ISystems<?, ?> systems,
																																																																																																														UUID... identityToken)
		{
				log.trace("Finding all resources by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);
				
				// First get the classification using reactive pattern
				return classificationService
												.find(session, classification, systems, identityToken)
												.chain(clazz -> {
														if (clazz == null)
														{
																return Uni
																								.createFrom()
																								.item(Collections.<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>emptyList());
														}
														ResourceItemXClassification res = new ResourceItemXClassification();
														ResourceItemXClassificationQueryBuilder builder = res.builder(session);
														
														builder.where(ResourceItemXClassification_.classificationID, Equals, (Classification) clazz);
														if (!Strings.isNullOrEmpty(value))
														{
																builder.where(ResourceItemXClassification_.value, Equals, value);
														}
														
														JoinExpression<ResourceItem, ResourceItem, ResourceItemXClassification> resourceJoin = new JoinExpression<>();
														ResourceItemQueryBuilder itemQueryBuilder = new ResourceItem().builder(session);
														builder.join(ResourceItemXClassification_.resourceItemID, itemQueryBuilder, JoinType.INNER, resourceJoin);
														
														ListJoin<ResourceItem, ResourceItemXResourceItemType> resourceItemTypesJoin = resourceJoin
																																																																																													.getGeneratedRoot()
																																																																																													.join(ResourceItem_.types, INNER);
														
														Join<ResourceItemXResourceItemType, ResourceItemType> resourceTypesJoin = resourceItemTypesJoin
																																																																																									.join(ResourceItemXResourceItemType_.resourceItemTypeID, INNER);
														
														resourceTypesJoin.on(builder
																																				.getCriteriaBuilder()
																																				.equal(resourceTypesJoin.get(ResourceItemType_.name), resourceType));
														
														return builder
																						.getAll()
																						.map(results -> {
																								@SuppressWarnings("unchecked")
																								List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>> castedResults =
																									(List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>) (List<?>) results;
																								return castedResults;
																						})
																						.onFailure()
																						.invoke(e ->
																															log.error("Error finding all resources by classification - resourceType: {}, classification: {}, value: {}",
																																									resourceType, classification, value, e));
														
												});
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public Uni<IResourceItem<?, ?>> findByUUID(Mutiny.Session session, UUID uuid)
		{
				log.trace("Finding resource by UUID: {}", uuid);
				ResourceItem res = new ResourceItem();
				return (Uni) res
																		.builder(session)
																		.where(ResourceItem_.id, Equals, uuid)
																		.inActiveRange()
																		.inDateRange()
																		.get();
				
		}
		
		
		@Override
		public Uni<IResourceItem<?, ?>> findByOriginalSourceUniqueID(Mutiny.Session session, UUID originalSourceUniqueID,
																																																															ISystems<?, ?> systems,
																																																															UUID... identityToken)
		{
				log.trace("Finding resource by original source unique ID: {}", originalSourceUniqueID);
				ResourceItem res = new ResourceItem();
				return (Uni) res
																		.builder(session)
																		.where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceUniqueID)
																		.inActiveRange()
																		.inDateRange()
																		.get();
				
		}
		
		@Override
		public Uni<IResourceItemType<?, ?>> findResourceItemType(Mutiny.Session session, String type, ISystems<?, ?> system, UUID... identityToken)
		{
				log.trace("Finding resource item type: {}", type);
				var enterprise = system.getEnterprise();
				java.util.UUID enterpriseId = null;
				java.util.UUID systemId = null;
				if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent)
				{
						enterpriseId = ent.getId();
				}
				if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys)
				{
						systemId = sys.getId();
				}
				String key = enterpriseId + "|" + systemId + "|" + type;
				java.util.UUID cachedId = resourceItemTypeKeyToId.get(key);
				if (cachedId != null)
				{
						log.trace("🔁 ResourceItemType cache hit for key '{}': {} — loading by UUID", key, cachedId);
						return (Uni) getResourceItemTypeById(session, cachedId)
																				.flatMap(found -> {
																						if (found != null)
																						{
																								return Uni
																																.createFrom()
																																.item(found);
																						}
																						// Stale mapping: remove and fallback to name+enterprise query
																						resourceItemTypeKeyToId.remove(key);
																						ResourceItemType xr = new ResourceItemType();
																						return (Uni) xr
																																				.builder(session)
																																				.withEnterprise(enterprise)
																																				.withName(type)
																																				.inActiveRange()
																																				//       .canRead(system, identityToken)
																																				.inDateRange()
																																				.get()
																																				.invoke(res -> {
																																						if (res != null && res.getId() != null)
																																						{
																																								resourceItemTypeKeyToId.put(key, (java.util.UUID) res.getId());
																																						}
																																				});
																				});
				}
				
				// Cold path
				ResourceItemType xr = new ResourceItemType();
				return (Uni) xr
																		.builder(session)
																		.withEnterprise(enterprise)
																		.withName(type)
																		.inActiveRange()
																		//       .canRead(system, identityToken)
																		.inDateRange()
																		.get()
																		.invoke(res -> {
																				if (res != null && res.getId() != null)
																				{
																						resourceItemTypeKeyToId.put(key, (java.util.UUID) res.getId());
																				}
																		});
		}
		
		@Override
		public Uni<List<IResourceItem<?, ?>>> findByResourceItemType(Mutiny.Session session, String type, ISystems<?, ?> systems, UUID... identityToken)
		{
				log.trace("Finding resources by type: {}", type);
				return findByResourceItemType(session, type, null, systems, identityToken);
		}
		
		
		@Override
		public Uni<List<IResourceItem<?, ?>>> findByResourceItemType(Mutiny.Session session, String type, String value, ISystems<?, ?> systems, UUID... identityToken)
		{
				log.trace("Finding resources by type: {} and value: {}", type, value);
				var enterprise = systems.getEnterprise();
				
				// Build query by joining ResourceItem to its types and filtering by type name without blocking
				ResourceItemQueryBuilder aqb = new ResourceItem().builder(session);
				aqb
					.withEnterprise(enterprise)
					.inActiveRange()
					.inDateRange()
				;
				
				com.entityassist.querybuilder.builders.JoinExpression<?, ?, ?> joinExpression = new com.entityassist.querybuilder.builders.JoinExpression<>();
				ResourceItemXResourceItemTypeQueryBuilder qb = new ResourceItemXResourceItemType().builder(session);
				qb
					.withEnterprise(enterprise)
					.inActiveRange()
					.inDateRange()
				;
				if (value != null)
				{
						qb.withValue(value);
				}
				// Filter by the ResourceItemType name without resolving the entity
				qb.where(qb.getAttribute("resourceItemTypeID.name"), Equals, type);
				
				aqb.join(ResourceItem_.types, qb, jakarta.persistence.criteria.JoinType.INNER, joinExpression);
				
				return (Uni) aqb
																		.getAll()
																		.onFailure()
																		.invoke(e -> log.error("Error finding resources by type: {} and value: {}", type, value, e));
		}
		
		/**
			* A utility method to help with testing the reactive behavior of this service.
			* This method creates a resource item, then finds it by UUID, and returns the result.
			* It demonstrates proper reactive composition using the chain pattern.
			*
			* @param session
			* @param identityResourceType  The resource type identity
			* @param resourceItemDataValue The resource item data value
			* @param system                The system
			* @param identityToken         The identity token
			* @return A Uni containing the found resource item
			*/
		@Override
		public Uni<IResourceItem<?, ?>> createAndFind(Mutiny.Session session, String identityResourceType, String resourceItemDataValue,
																																																ISystems<?, ?> system, UUID... identityToken)
		{
				log.trace("Creating and finding resource item - type: {}, value: {}", identityResourceType, resourceItemDataValue);
				
				// Generate a UUID for the new resource item
				UUID key = UUID.randomUUID();
				
				// Step 1: Create the resource item
				return create(session, identityResourceType, key, resourceItemDataValue, system, identityToken)
												// Step 2: Find the resource item by UUID
												.chain(resourceItem -> {
														log.trace("Resource item created, now finding by UUID: {}", key);
														return findByUUID(session, key);
												})
												// Step 3: Handle errors
												.onFailure()
												.recoverWithItem(cause -> {
														log.error("Error in createAndFind operation", cause);
														return null;
												});
		}
}

