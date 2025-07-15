package com.guicedee.activitymaster.fsdm;

import com.entityassist.RootEntity;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import com.guicedee.activitymaster.fsdm.client.services.exceptions.ResourceItemException;
//import com.guicedee.activitymaster.fsdm.db.entityassist.TransactionalCallable;
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
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.persistence.criteria.*;
import lombok.extern.log4j.Log4j2;

import javax.cache.annotation.CacheKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

//import static com.guicedee.activitymaster.fsdm.db.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;
import static com.guicedee.activitymaster.fsdm.db.entityassist.SCDEntity.EndOfTime;
import static jakarta.persistence.criteria.JoinType.*;


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
 *     })
 *     .subscribe().with(
 *         result -> {
 *             // Handle success
 *         },
 *         cause -> {
 *             // Handle failure
 *         }
 *     );
 * </pre>
 */
@Log4j2
public class ResourceItemService
        implements IResourceItemService<ResourceItemService>
{
    @Inject
    private IEnterprise<?, ?> enterprise;

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private Vertx vertx;

    /**
     * Gets a new ResourceItem instance.
     * This is a lightweight operation that doesn't require a transaction.
     *
     * @return A Uni containing a new ResourceItem instance
     */
    @Override
    public Uni<IResourceItem<?, ?>> get()
    {
        log.debug("Getting new ResourceItem instance");
        return Uni.createFrom()
                       .item(new ResourceItem());
    }

    /**
     * Gets a new ResourceItemData instance.
     * This is a lightweight operation that doesn't require a transaction.
     *
     * @return A Uni containing a new ResourceItemData instance
     */
    @Override
    public Uni<IResourceData<?, ?, ?>> getData()
    {
        log.debug("Getting new ResourceItemData instance");
        return Uni.createFrom()
                       .item(new ResourceItemData());
    }

    /**
     * Gets a new ResourceItemType instance.
     * This is a lightweight operation that doesn't require a transaction.
     *
     * @return A Uni containing a new ResourceItemType instance
     */
    @Override
    public Uni<IResourceItemType<?, ?>> getType()
    {
        log.debug("Getting new ResourceItemType instance");
        return Uni.createFrom()
                       .item(new ResourceItemType());
    }

    @Override
    public Uni<IResourceItemType<?, ?>> createType(String value, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating resource type with value: {}, description: {}", value, description);
        return createType(value, null, description, system, identityToken);
    }

    @Override
    public Uni<IResourceItemType<?, ?>> createType(String value, java.util.UUID key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating resource type with value: {}, key: {}, description: {}", value, key, description);

        return ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItemType xr = new ResourceItemType();

            // First check if the resource type already exists
            return xr.builder()
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

                                   return acService.getActiveFlag(enterprise)
                                                  .chain(activeFlag -> {
                                                      xr.setActiveFlagID(activeFlag);
                                                      return xr.persist();
                                                  })
                                                  .map(persisted -> {
                                                      // Create default security
                                                      xr.createDefaultSecurity(system, identityToken);
                                                      return (IResourceItemType<?, ?>) xr;
                                                  });
                               }
                               else
                               {
                                   // Resource type exists, find it
                                   ResourceItemType resourceItemType = new ResourceItemType();
                                   return resourceItemType.builder()
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
        });
    }


    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, byte[] data,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue, byte[] data,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }


    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate, byte[] data,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, data, system, identityToken);
    }


    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, null, system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue,
                                           UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate, byte[] data,
                                           ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating resource item - type: {}, value: {}", identityResourceType, resourceItemDataValue);

        // Step 1: Create the resource item
        var result = ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItem xr = new ResourceItem();
            xr.setId(key);
            xr.setOriginalSourceSystemID(system.getId());
            xr.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
            xr.setEffectiveFromDate(convertToUTCDateTime(effectiveFromDate));
            xr.setSystemID(system);
            xr.setEnterpriseID(enterprise);
            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

            return acService.getActiveFlag(enterprise)
                           .chain(activeFlag -> {
                               xr.setActiveFlagID(activeFlag);
                               xr.setResourceItemDataType(resourceItemDataValue);

                               // Persist the resource item
                               return xr.persist()
                                              .chain(persisted -> {
                                                  // Create resource item data
                                                  ResourceItemData rid = new ResourceItemData();
                                                  rid.setResource(persisted);
                                                  LocalDateTime now = RootEntity.getNow();
                                                  rid.setEffectiveFromDate(convertToUTCDateTime(now));
                                                  rid.setWarehouseCreatedTimestamp(convertToUTCDateTime(now));
                                                  rid.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
                                                  rid.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(now));
                                                  rid.setResourceItemData("".getBytes());
                                                  rid.setActiveFlagID(activeFlag);
                                                  rid.setOriginalSourceSystemID(system.getId());
                                                  rid.setSystemID(system);
                                                  rid.setEnterpriseID(system.getEnterpriseID());

                                                  // Persist the resource item data
                                                  return rid.persist()
                                                                 .map(persistedData -> {
                                                                     // Create default security
                                                                     persistedData.createDefaultSecurity(system, identityToken);
                                                                     return persistedData;
                                                                 })
                                                                 .chain(resourceItem -> {
                                                                     // Step 3: Add resource item types
                                                                     log.debug("Adding resource item type: {}", identityResourceType);
                                                                     return addResourceItemTypeRelationship(persisted, identityResourceType, system, identityToken);
                                                                 })
                                                                 .chain(resourceItem -> {
                                                                     // Step 2: Update data if provided
                                                                     if (data != null)
                                                                     {
                                                                         log.debug("Updating resource item data");
                                                                         // Find the appropriate method to update data
                                                                         // This might need to be adjusted based on the actual implementation
                                                                         return findResourceItemDataForUpdate(persisted, data, system, identityToken)
                                                                                        .map(updated -> persisted);
                                                                     }
                                                                     else
                                                                     {
                                                                         return Uni.createFrom()
                                                                                        .item(persisted);
                                                                     }
                                                                 });
                                              });
                           })
                           .onFailure()
                           .invoke(cause -> {
                               log.error("Failed to create resource item", cause);
                           });
        });

        return (Uni)result;
    }

    /**
     * Helper method to update resource item data
     */
    private Uni<Void> findResourceItemDataForUpdate(ResourceItem resourceItem, byte[] data, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Updating data for resource item: {}", resourceItem.getId());

        // Find the existing data relationship
        return ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItemData rid = new ResourceItemData();
            return rid.builder()
                           .withResource(resourceItem)
                           .inActiveRange()
                           .inDateRange()
                           .get()
                           .chain(existingData -> {
                               if (existingData == null)
                               {
                                   // Create new data if none exists
                                   ResourceItemData newData = new ResourceItemData();
                                   newData.setResource(resourceItem);
                                   LocalDateTime now = RootEntity.getNow();
                                   newData.setEffectiveFromDate(convertToUTCDateTime(now));
                                   newData.setWarehouseCreatedTimestamp(convertToUTCDateTime(now));
                                   newData.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
                                   newData.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(now));
                                   newData.setResourceItemData(data);
                                   newData.setSystemID(system);
                                   newData.setEnterpriseID(system.getEnterpriseID());
                                   IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
                                   return afs.getActiveFlag(enterprise)
                                                  .chain(activeFlag -> {
                                                      newData.setActiveFlagID(activeFlag);
                                                      newData.setOriginalSourceSystemID(system.getId());
                                                      return newData.persist();
                                                  })
                                                  .map(persisted -> null); // Return Void
                               }
                               else
                               {
                                   // Update existing data
                                   existingData.setResourceItemData(data);
                                   existingData.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(RootEntity.getNow()));
                                   return existingData.persist()
                                                  .map(persisted -> null); // Return Void
                               }
                           });
        });
    }

    /**
     * Helper method to add resource item type relationship
     */
    private Uni<Void> addResourceItemTypeRelationship(ResourceItem resourceItem, String typeName, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Adding resource item type relationship: {} for item: {}", typeName, resourceItem.getId());

        return findResourceItemType(typeName, system, identityToken)
                       .chain(resourceItemType -> {
                           ResourceItemXResourceItemType relationship = new ResourceItemXResourceItemType();
                           relationship.setResourceItemID(resourceItem);
                           relationship.setResourceItemTypeID((ResourceItemType) resourceItemType);
                           relationship.setValue(DefaultClassifications.NoClassification.toString());
                           relationship.setSystemID(system);
                           relationship.setEnterpriseID(enterprise);

                           IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
                           return afs
                                          .getActiveFlag(enterprise)
                                          .chain(activeFlag -> {
                                              relationship.setActiveFlagID((IActiveFlag<?, ?>) activeFlag);
                                              relationship.setOriginalSourceSystemID(system.getId());
                                              LocalDateTime now = RootEntity.getNow();
                                              relationship.setEffectiveFromDate(convertToUTCDateTime(now));
                                              relationship.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
                                              relationship.setWarehouseCreatedTimestamp(convertToUTCDateTime(now));
                                              relationship.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(now));

                                              return relationship.persist();
                                          })
                                          .map(persisted -> {
                                              // Create default security
                                              relationship.createDefaultSecurity(system, identityToken);
                                              return null; // Return Void
                                          });
                       });
    }

    @Override
    public Uni<IResourceItem<?, ?>> findByClassification(String resourceType,
                                                         String classification,
                                                         String value,
                                                         ISystems<?, ?> systems,
                                                         java.util.UUID... identityToken)
    {
        log.debug("Finding resource by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);

        // First get the classification using reactive pattern
        return classificationService.find(classification, systems, identityToken)
                       .onItem()
                       .ifNull()
                       .continueWith(() -> {
                           log.warn("Classification not found: {}", classification);
                           return null;
                       })
                       .chain(clazz -> {
                           if (clazz == null)
                           {
                               return Uni.createFrom()
                                              .nullItem();
                           }

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               try
                               {
                                   ResourceItemXClassification res = new ResourceItemXClassification();
                                   ResourceItemXClassificationQueryBuilder builder = res.builder();

                                   builder.where(ResourceItemXClassification_.classificationID, Equals, (Classification) clazz);
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

                                   // Get the result from the builder using reactive pattern
                                   return builder.get()
                                                  .map(exists -> exists != null ? exists.getResourceItemID() : null);
                               }
                               catch (Exception e)
                               {
                                   log.error("Error finding resource by classification - resourceType: {}, classification: {}, value: {}",
                                           resourceType, classification, value, e);
                                   return Uni.createFrom()
                                                  .failure(e);
                               }
                           });
                       });
    }


    @Override
    public Uni<List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>> findByClassificationAll(String resourceType,
                                                                                                                String classification,
                                                                                                                String value,
                                                                                                                ISystems<?, ?> systems,
                                                                                                                java.util.UUID... identityToken)
    {
        log.debug("Finding all resources by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);

        // First get the classification using reactive pattern
        return classificationService.find(classification, systems, identityToken)
                       .onItem()
                       .ifNull()
                       .continueWith(() -> {
                           log.warn("Classification not found: {}", classification);
                           return null;
                       })
                       .chain(clazz -> {
                           if (clazz == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>emptyList());
                           }

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               ResourceItemXClassification res = new ResourceItemXClassification();
                               ResourceItemXClassificationQueryBuilder builder = res.builder();

                               builder.where(ResourceItemXClassification_.classificationID, Equals, (Classification) clazz);
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

                               return builder.getAll()
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
                       });
    }


    @Override
    public Uni<IResourceItem<?, ?>> findByUUID(@CacheKey UUID uuid)
    {
        log.debug("Finding resource by UUID: {}", uuid);
        return ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItem res = new ResourceItem();
            return res.builder()
                           .where(ResourceItem_.id, Equals, uuid)
                           .inActiveRange()
                           .inDateRange()
                           .get()
                           .map(item -> (IResourceItem<?, ?>) item);
        });
    }


    @Override
    public Uni<IResourceItem<?, ?>> findByOriginalSourceUniqueID(@CacheKey UUID originalSourceUniqueID,
                                                                 @CacheKey ISystems<?, ?> systems,
                                                                 @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resource by original source unique ID: {}", originalSourceUniqueID);
        return ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItem res = new ResourceItem();
            return res.builder()
                           .where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceUniqueID)
                           .inActiveRange()
                           .inDateRange()
                           .get()
                           .map(item -> (IResourceItem<?, ?>) item);
        });
    }

    @Override
    public Uni<byte[]> getDataForResourceItemValue(IRelationshipValue<IResourceItem<?, ?>, IResourceData<?, ?, ?>, ?> data)
    {
        log.debug("Getting data for resource item value");
        return ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItemData d = (ResourceItemData) data.getSecondary();
            return Uni.createFrom()
                           .item(d.getResourceItemData());
        });
    }


    // Cache for resource item types (type name -> ResourceItemType)
    private final Map<String, ResourceItemType> resourceItemTypeCache = new HashMap<>();

    @Override
    public Uni<IResourceItemType<?, ?>> findResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resource item type: {}", type);

        // Check if we have this type in our cache
        ResourceItemType cachedType = resourceItemTypeCache.get(type);
        if (cachedType != null)
        {
            log.debug("Resource item type found in cache: {}", type);
            return Uni.createFrom()
                           .item(cachedType);
        }

        // Not in cache, need to query the database
        return ReactiveTransactionUtil.withTransaction(session -> {
            ResourceItemType xr = new ResourceItemType();
            return xr.builder()
                           .withEnterprise(enterprise)
                           .withName(type)
                           .inActiveRange()
                           //       .canRead(system, identityToken)
                           .inDateRange()
                           .get()
                           .onItem()
                           .ifNull()
                           .failWith(() ->
                                             new ResourceItemException("Cannot find resource item type [%s]".formatted(type)))
                           .onItem()
                           .invoke(result -> {
                               // Store in cache for future use
                               resourceItemTypeCache.put(type, result);
                           })
                           .map(result -> (IResourceItemType<?, ?>) result);
        });
    }

    @Override
    public Uni<List<IResourceItem<?, ?>>> findByResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resources by type: {}", type);
        return findByResourceItemType(type, null, systems, identityToken);
    }


    @Override
    public Uni<List<IResourceItem<?, ?>>> findByResourceItemType(@CacheKey String type, String value, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resources by type: {} and value: {}", type, value);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new ResourceItemXResourceItemType().builder()
                           .withEnterprise(enterprise)
                           .inActiveRange()
                           .inDateRange()
                           .canRead(systems, identityToken)
                           .withType(type, systems, identityToken)
                           .withValue(value)
                           .getAll()
                           .map(results -> {
                               List<IResourceItem<?, ?>> resourceItems = new ArrayList<>();
                               for (ResourceItemXResourceItemType result : results)
                               {
                                   resourceItems.add(result.getResourceItemID());
                               }
                               return resourceItems;
                           })
                           .onFailure()
                           .invoke(e ->
                                           log.error("Error finding resources by type: {} and value: {}", type, value, e));
        });
    }

    /**
     * A utility method to help with testing the reactive behavior of this service.
     * This method creates a resource item, then finds it by UUID, and returns the result.
     * It demonstrates proper reactive composition using the chain pattern.
     *
     * @param identityResourceType  The resource type identity
     * @param resourceItemDataValue The resource item data value
     * @param system                The system
     * @param identityToken         The identity token
     * @return A Uni containing the found resource item
     */
    public Uni<IResourceItem<?, ?>> createAndFind(String identityResourceType, String resourceItemDataValue,
                                                  ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating and finding resource item - type: {}, value: {}", identityResourceType, resourceItemDataValue);

        // Generate a UUID for the new resource item
        UUID key = UUID.randomUUID();

        // Step 1: Create the resource item
        return create(identityResourceType, key, resourceItemDataValue, system, identityToken)
                       // Step 2: Find the resource item by UUID
                       .chain(resourceItem -> {
                           log.debug("Resource item created, now finding by UUID: {}", key);
                           return findByUUID(key);
                       })
                       // Step 3: Handle errors
                       .onFailure()
                       .recoverWithItem(cause -> {
                           log.error("Error in createAndFind operation", cause);
                           return null;
                       });
    }
}
