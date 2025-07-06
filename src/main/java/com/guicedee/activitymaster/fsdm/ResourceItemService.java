package com.guicedee.activitymaster.fsdm;

import com.entityassist.RootEntity;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
//import com.guicedee.guicedpersistence.lambda.TransactionalBiConsumer;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ResourceItemException;
import com.guicedee.guicedpersistence.lambda.TransactionalBiConsumer;
import com.guicedee.guicedpersistence.lambda.TransactionalCallable;
import com.guicedee.guicedpersistence.lambda.TransactionalConsumer;
import com.guicedee.guicedpersistence.lambda.TransactionalSupplier;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import jakarta.persistence.criteria.*;
import lombok.extern.log4j.Log4j2;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static jakarta.persistence.criteria.JoinType.*;


/**
 * A reactive service for managing resource items in the system.
 * 
 * This service implements the IResourceItemService interface and provides methods for creating,
 * finding, and managing resource items. All methods return Future objects to support reactive
 * programming patterns.
 * 
 * Key features:
 * - All operations are non-blocking and return Future objects
 * - Database operations are executed in worker threads using vertx.executeBlocking
 * - Transactions are managed using TransactionalCallable
 * - Comprehensive logging is provided for debugging and monitoring
 * - In-memory caching is used for frequently accessed data
 * 
 * Usage example:
 * <pre>
 * resourceItemService.findByUUID(uuid)
 *     .compose(item -> {
 *         // Process the item
 *         return doSomethingElse(item);
 *     })
 *     .onSuccess(result -> {
 *         // Handle success
 *     })
 *     .onFailure(cause -> {
 *         // Handle failure
 *     });
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
     * @return A Future containing a new ResourceItem instance
     */
    public Future<IResourceItem<?, ?>> get()
    {
        log.debug("Getting new ResourceItem instance");
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            return new ResourceItem();
        }, false), false);
    }

    /**
     * Gets a new ResourceItemData instance.
     * This is a lightweight operation that doesn't require a transaction.
     * 
     * @return A Future containing a new ResourceItemData instance
     */
    @Override
    public Future<IResourceData<?, ?, ?>> getData()
    {
        log.debug("Getting new ResourceItemData instance");
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            return new ResourceItemData();
        }, false), false);
    }

    /**
     * Gets a new ResourceItemType instance.
     * This is a lightweight operation that doesn't require a transaction.
     * 
     * @return A Future containing a new ResourceItemType instance
     */
    @Override
    public Future<IResourceItemType<?, ?>> getType()
    {
        log.debug("Getting new ResourceItemType instance");
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            return new ResourceItemType();
        }, false), false);
    }

    @Override
    public Future<IResourceItemType<?, ?>> createType(String value, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating resource type with value: {}, description: {}", value, description);
        return createType(value, null, description, system, identityToken);
    }

    @Override
    public Future<IResourceItemType<?, ?>> createType(String value, java.util.UUID key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating resource type with value: {}, key: {}, description: {}", value, key, description);
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            ResourceItemType xr = new ResourceItemType();
            boolean exists = xr.builder()
                    .withName(value)
                    .inActiveRange()
                    .inDateRange()
                    .withEnterprise(enterprise)
                    .getCount() > 0;

            if (!exists)
            {
                xr.setId(key);
                xr.setName(value);
                xr.setDescription(value);
                xr.setOriginalSourceSystemID(system.getId());
                xr.setSystemID(system);
                xr.setEnterpriseID(enterprise);
                IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
                IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
                xr.setActiveFlagID(activeFlag);
                xr.persist();

                xr.createDefaultSecurity(system, identityToken);
                return xr;
            }
            else
            {
                // We need to handle this differently since findResourceItemType now returns a Future
                ResourceItemType resourceItemType = new ResourceItemType();
                Optional<ResourceItemType> existingType = resourceItemType.builder()
                        .withEnterprise(enterprise)
                        .withName(value)
                        .inActiveRange()
                        .inDateRange()
                        .get();
                return existingType.orElseThrow(() -> new ResourceItemException("Cannot find resource item type [%s]".formatted(value)));
            }
        }, true), true);
    }


    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {

        return create(identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue,java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }


    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, data, system, identityToken);
    }


    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, null, system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue,
                                              UUID originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating resource item - type: {}, value: {}", identityResourceType, resourceItemDataValue);

        // Step 1: Create the resource item
        Future<ResourceItem> initialFuture = vertx.executeBlocking(TransactionalCallable.of(() -> {
            ResourceItem xr = new ResourceItem();
            xr.setId(key);
            xr.setOriginalSourceSystemID(system.getId());
            xr.setOriginalSourceSystemUniqueID(originalSourceSystemUniqueID);
            xr.setEffectiveFromDate(QueryBuilderSCD.convertToUTCDateTime(effectiveFromDate));
            xr.setSystemID(system);
            xr.setEnterpriseID(enterprise);
            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
            IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
            xr.setActiveFlagID(activeFlag);
            xr.setResourceItemDataType(resourceItemDataValue);
            xr.persist();

            ResourceItemData rid = new ResourceItemData();
            rid.setResource(xr);
            rid.setEffectiveFromDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
            rid.setWarehouseCreatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
            rid.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
            rid.setWarehouseLastUpdatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow()));
            rid.setResourceItemData("".getBytes());
            rid.setActiveFlagID(activeFlag);
            rid.setOriginalSourceSystemID(system.getId());
            rid.setSystemID(system);
            rid.setEnterpriseID(system.getEnterpriseID());
            rid.persist();

            rid.createDefaultSecurity(system, identityToken);
            return xr;
        }, true), true);

        // Step 2: Update data if provided
        Future<ResourceItem> dataUpdatedFuture = initialFuture.compose(resourceItem -> {
            if (data != null) {
                log.debug("Updating resource item data");
                return vertx.executeBlocking(TransactionalCallable.of(() -> {
                    resourceItem.updateData(data, system, identityToken);
                    return resourceItem;
                }, true), true);
            } else {
                return Future.succeededFuture(resourceItem);
            }
        });

        // Step 3: Add resource item types
        Future<ResourceItem> finalFuture = dataUpdatedFuture.compose(resourceItem -> {
            log.debug("Adding resource item type: {}", identityResourceType);
            return vertx.executeBlocking(TransactionalCallable.of(() -> {
                resourceItem.addResourceItemTypes(identityResourceType, null, 
                    DefaultClassifications.NoClassification.toString(), system, identityToken);
                return resourceItem;
            }, true), true);
        });

        // Handle errors throughout the chain
        finalFuture.onFailure(cause -> {
            log.error("Failed to create resource item", cause);
        });

        return (Future) finalFuture;
    }

    @Override
    public Future<IResourceItem<?, ?>> findByClassification(String resourceType,
                                                    String classification,
                                                    String value,
                                                    ISystems<?, ?> systems,
                                                    java.util.UUID... identityToken)
    {
        log.debug("Finding resource by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            try {
                ResourceItemXClassification res = new ResourceItemXClassification();
                ResourceItemXClassificationQueryBuilder builder = res.builder();

                // Note: classificationService.find is synchronous, but we're handling it within the executeBlocking context
                Classification clazz = (Classification) classificationService.find(classification, systems, identityToken);
                if (clazz == null) {
                    log.warn("Classification not found: {}", classification);
                    return null;
                }

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
            } catch (Exception e) {
                log.error("Error finding resource by classification - resourceType: {}, classification: {}, value: {}", 
                    resourceType, classification, value, e);
                throw e;
            }
        }, true), true);
    }


    @Override
    public Future<List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>> findByClassificationAll(String resourceType,
                                                                                                           String classification,
                                                                                                           String value,
                                                                                                           ISystems<?, ?> systems,
                                                                                                           java.util.UUID... identityToken)
    {
        log.debug("Finding all resources by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            try {
                ResourceItemXClassification res = new ResourceItemXClassification();
                ResourceItemXClassificationQueryBuilder builder = res.builder();

                // Note: classificationService.find is synchronous, but we're handling it within the executeBlocking context
                Classification clazz = (Classification) classificationService.find(classification, systems, identityToken);
                if (clazz == null) {
                    log.warn("Classification not found: {}", classification);
                    return Collections.emptyList();
                }

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
            } catch (Exception e) {
                log.error("Error finding all resources by classification - resourceType: {}, classification: {}, value: {}", 
                    resourceType, classification, value, e);
                throw e;
            }
        }, true), true);
    }


    @Override
    public Future<IResourceItem<?, ?>> findByUUID(@CacheKey UUID uuid)
    {
        log.debug("Finding resource by UUID: {}", uuid);
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            ResourceItem res = new ResourceItem();
            ResourceItemQueryBuilder builder = res.builder();
            builder.where(ResourceItem_.id, Equals, uuid);
            builder.inActiveRange();
            builder.inDateRange();
            Optional<ResourceItem> exists = builder.get();
            return exists.orElse(null);
        }, true), true);
    }


    @Override
    public Future<IResourceItem<?, ?>> findByOriginalSourceUniqueID(@CacheKey UUID originalSourceUniqueID,
                                                            @CacheKey ISystems<?, ?> systems,
                                                            @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resource by original source unique ID: {}", originalSourceUniqueID);
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            ResourceItem res = new ResourceItem();
            ResourceItemQueryBuilder builder = res.builder();
            builder.where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceUniqueID);
            builder.inActiveRange();
            builder.inDateRange();
            Optional<ResourceItem> exists = builder.get();
            return exists.orElse(null);
        }, true), true);
    }

    @Override
    public Future<byte[]> getDataForResourceItemValue(IRelationshipValue<IResourceItem<?, ?>, IResourceData<?, ?, ?>, ?> data)
    {
        log.debug("Getting data for resource item value");
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            ResourceItemData d = (ResourceItemData) data.getSecondary();
            return d.getResourceItemData();
        }, false), false);
    }


    // Cache for resource item types (type name -> ResourceItemType)
    private final Map<String, ResourceItemType> resourceItemTypeCache = new HashMap<>();

    @Override
    public Future<IResourceItemType<?, ?>> findResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resource item type: {}", type);

        // Check if we have this type in our cache
        ResourceItemType cachedType = resourceItemTypeCache.get(type);
        if (cachedType != null) {
            log.debug("Resource item type found in cache: {}", type);
            return Future.succeededFuture(cachedType);
        }

        // Not in cache, need to query the database
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            ResourceItemType xr = new ResourceItemType();
            Optional<ResourceItemType> exists = xr.builder()
                    .withEnterprise(enterprise)
                    .withName(type)
                    .inActiveRange()
                    //       .canRead(system, identityToken)
                    .inDateRange()
                    .get();

            ResourceItemType result = exists.orElseThrow(() -> 
                new ResourceItemException("Cannot find resource item type [%s]".formatted(type)));

            // Store in cache for future use
            resourceItemTypeCache.put(type, result);
            return result;
        }, true), true);
    }

    @Override
    public Future<List<IResourceItem<?, ?>>> findByResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resources by type: {}", type);
        return findByResourceItemType(type, null, systems, identityToken);
    }


    @Override
    public Future<List<IResourceItem<?, ?>>> findByResourceItemType(@CacheKey String type, String value, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
    {
        log.debug("Finding resources by type: {} and value: {}", type, value);
        return vertx.executeBlocking(TransactionalCallable.of(() -> {
            try {
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
            } catch (Exception e) {
                log.error("Error finding resources by type: {} and value: {}", type, value, e);
                throw e;
            }
        }, true), true);
    }

    /**
     * A utility method to help with testing the reactive behavior of this service.
     * This method creates a resource item, then finds it by UUID, and returns the result.
     * It demonstrates proper reactive composition using the compose pattern.
     * 
     * @param identityResourceType The resource type identity
     * @param resourceItemDataValue The resource item data value
     * @param system The system
     * @param identityToken The identity token
     * @return A Future containing the found resource item
     */
    public Future<IResourceItem<?, ?>> createAndFind(String identityResourceType, String resourceItemDataValue,
                                                    ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating and finding resource item - type: {}, value: {}", identityResourceType, resourceItemDataValue);

        // Generate a UUID for the new resource item
        UUID key = UUID.randomUUID();

        // Step 1: Create the resource item
        return create(identityResourceType, key, resourceItemDataValue, system, identityToken)
            // Step 2: Find the resource item by UUID
            .compose(resourceItem -> {
                log.debug("Resource item created, now finding by UUID: {}", key);
                return findByUUID(key);
            })
            // Step 3: Handle errors
            .recover(cause -> {
                log.error("Error in createAndFind operation", cause);
                return Future.failedFuture(cause);
            });
    }
}
