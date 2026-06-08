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
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceData;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ResourceItemException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemData;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemDataValue;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemType_;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXClassification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXClassification_;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemType;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemType_;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem_;
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
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.applicationEnterpriseName;
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
@Log4j2
@Singleton
public class ResourceItemService
        implements IResourceItemService<ResourceItemService> {
    // Local cache: key = enterpriseId + '|' + systemId + '|' + resourceItemTypeName → ResourceItemType UUID
    private final java.util.Map<String, java.util.UUID> resourceItemTypeKeyToId = new java.util.concurrent.ConcurrentHashMap<>();

    // UUID-based lookup to leverage Hibernate 2nd-level cache
    public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType<?, ?>> getResourceItemTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id) {
        //noinspection unchecked,rawtypes
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
    public IResourceItem<?, ?> get() {
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
    public IResourceData<?, ?, ?> getData() {
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
    public IResourceItemType<?, ?> getType() {
        log.trace("Getting new ResourceItemType instance");
        return new ResourceItemType();
    }

    @Override
    public Uni<IResourceItemType<?, ?>> createType(Mutiny.Session session, String value, String description, ISystems<?, ?> system, UUID... identityToken) {
        log.trace("Creating resource type with value: {}, description: {}", value, description);
        return createType(session, value, null, description, system, identityToken);
    }

    @Override
    public Uni<IResourceItemType<?, ?>> createType(Mutiny.Session session, String value, UUID key, String description, ISystems<?, ?> system, UUID... identityToken) {
        // Public create — world-readable (public/default security matrix).
        return createTypeInternal(session, value, key, description, system, null, identityToken);
    }

    /**
     * Opt-in <strong>scope-restricted</strong> resource-item-type create. Same as
     * {@link #createType(Mutiny.Session, String, UUID, String, ISystems, UUID...)} but secured with the restricted
     * matrix plus a <em>read</em> grant for {@code scopeToken}.
     */
    @Override
    public Uni<IResourceItemType<?, ?>> createTypeScopeRestricted(Mutiny.Session session, String value, UUID key, String description, ISystems<?, ?> system,
                                                                  com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                                  UUID... identityToken) {
        return createTypeInternal(session, value, key, description, system, scopeToken, identityToken);
    }

    private Uni<IResourceItemType<?, ?>> createTypeInternal(Mutiny.Session session, String value, UUID key, String description, ISystems<?, ?> system,
                                                            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                            UUID... identityToken) {
        log.debug("Creating resource type with value: {}, key: {}, description: {}", value, key, description);

        var enterprise = system.getEnterprise();

        ResourceItemType xr = new ResourceItemType();
        // First check if the resource type already exists
        return xr
                .builder(session)
                .withName(value)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                .getCount()
                .chain(count -> {
                    if (count <= 0) {
                        // Resource type doesn't exist, create a new one
                        xr.setId(key == null ? UUID.randomUUID() : key);
                        xr.setName(value);
                        xr.setDescription(value);
                        xr.setOriginalSourceSystemID(system.getId());
                        xr.setSystemID(system);
                        xr.setEnterpriseID(enterprise);
                        IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);

                        return acService
                                .getActiveFlag(session, enterprise, identityToken)
                                .chain(activeFlag -> {
                                    xr.setActiveFlagID(activeFlag);
                                    return session
                                            .persist(xr)
                                            .chain(session::flush)
                                            .replaceWith(Uni
                                                    .createFrom()
                                                    .item(xr));
                                })
                                .call(persisted ->
                                        // Apply the chosen security matrix (subscribed via call so it runs)
                                        scopeToken == null
                                                ? xr.createDefaultSecurity(session, system, identityToken)
                                                : xr.createScopeRestrictedSecurity(session, system, scopeToken, identityToken));
                    } else {
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
                                    if (existingType == null) {
                                        throw new ResourceItemException("Cannot find resource item type [%s]".formatted(value));
                                    }
                                    return (IResourceItemType<?, ?>) existingType;
                                });
                    }
                });
    }


    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue, byte[] data,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue, byte[] data,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, key, resourceItemDataValue, java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }


    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate, byte[] data,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, data, system, identityToken);
    }


    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue, UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate,
                                           ISystems<?, ?> system, UUID... identityToken) {
        return create(session, identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, null, system, identityToken);
    }

    @Override
    public Uni<IResourceItem<?, ?>> create(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue,
                                           UUID originalSourceSystemUniqueID,
                                           LocalDateTime effectiveFromDate, byte[] data,
                                           ISystems<?, ?> system, UUID... identityToken) {
        // Public create — world-readable (public/default security matrix).
        return createInternal(session, identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID,
                effectiveFromDate, data, system, null, identityToken);
    }

    /**
     * Opt-in <strong>scope-restricted</strong> resource-item create. Identical to
     * {@link #create(Mutiny.Session, String, UUID, String, UUID, LocalDateTime, byte[], ISystems, UUID...)} except
     * the resource item's data row and its type relationship are secured with the restricted matrix: only
     * Administrators / Systems / Applications / Plugins retain access, plus a <em>read</em> grant for
     * {@code scopeToken}. Because the applicable-token climb is child&rarr;parent, only identity tokens located at
     * the {@code scopeToken} node <em>or below it</em> may read.
     */
    @Override
    public Uni<IResourceItem<?, ?>> createScopeRestricted(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue,
                                                          UUID originalSourceSystemUniqueID,
                                                          LocalDateTime effectiveFromDate, byte[] data,
                                                          ISystems<?, ?> system,
                                                          com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                          UUID... identityToken) {
        return createInternal(session, identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID,
                effectiveFromDate, data, system, scopeToken, identityToken);
    }

    private Uni<IResourceItem<?, ?>> createInternal(Mutiny.Session session, String identityResourceType, UUID key, String resourceItemDataValue,
                                                    UUID originalSourceSystemUniqueID,
                                                    LocalDateTime effectiveFromDate, byte[] data,
                                                    ISystems<?, ?> system,
                                                    com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                    UUID... identityToken) {
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
                            .getActiveFlag(session, enterprise, identityToken)
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
                                            rid.setEnterpriseID(enterprise);

                                            ResourceItemDataValue dataValue = new ResourceItemDataValue();
                                            dataValue.setId(persisted.getId());
                                            dataValue.setData(data);
                                            if (data == null) {
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
                                                    .call(persistedData ->
                                                            // Apply the chosen security matrix (subscribed via call so it runs)
                                                            scopeToken == null
                                                                    ? persistedData.createDefaultSecurity(session, system, identityToken)
                                                                    : persistedData.createScopeRestrictedSecurity(session, system, scopeToken, identityToken))
                                                    .chain(_ -> {
                                                        // Step 3: Add resource item types
                                                        log.trace("Adding resource item type: {}", identityResourceType);
                                                        return addResourceItemTypeRelationshipInternal(session, persisted, identityResourceType, resourceItemDataValue, system, enterprise, scopeToken, identityToken);
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

    Uni<Integer> tryUpdate(Mutiny.Session session, UUID id, byte[] value, String systemName) {
        // First resolve the actual resourceitemdatavalueid via the entity graph
        return session.createQuery(
                        "SELECT dv.id FROM ResourceItemData rd JOIN rd.dataValue dv WHERE rd.id = :id OR rd.resource.id = :id", UUID.class)
                .setParameter("id", id)
                .getSingleResultOrNull()
                .onItem().ifNotNull().transformToUni(dataValueId -> {
                    // Use native SQL with FOR UPDATE SKIP LOCKED to perform a safe concurrent update
                    String sql = """
                            WITH tgt AS (
                              SELECT resourceitemdatavalueid
                              FROM resource.resourceitemdatavalue
                              WHERE resourceitemdatavalueid = :id
                              FOR UPDATE SKIP LOCKED
                            )
                            UPDATE resource.resourceitemdatavalue v
                            SET resourceitemdatavalue = :val
                            FROM tgt
                            WHERE v.resourceitemdatavalueid = tgt.resourceitemdatavalueid
                            """;
                    return session.createNativeQuery(sql)
                            .setParameter("id", dataValueId)
                            .setParameter("val", value)
                            .executeUpdate();
                })
                .onItem().ifNull().switchTo(() -> createMissingResourceDataForUpdate(session, id, value, systemName));
    }

    /**
     * When the ResourceItemDataValue is not found for the given id, check whether the ResourceItem exists.
     * <ul>
     *     <li>If the ResourceItem exists but has no ResourceItemData/DataValue → create them.</li>
     *     <li>If the ResourceItem itself is missing → create a default ResourceItem with the "Unknown" type,
     *         together with its ResourceItemData and ResourceItemDataValue.</li>
     * </ul>
     */
    private Uni<Integer> createMissingResourceDataForUpdate(Mutiny.Session session, UUID id, byte[] value, String systemName) {
        String resolvedSystemName = systemName != null ? systemName : com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;

        return SessionUtils.withActivityMaster(applicationEnterpriseName, resolvedSystemName, tuple -> {
            var createSession = tuple.getItem1();
            var createEnterprise = tuple.getItem2();
            var createSystem = tuple.getItem3();
            var createIdentityToken = tuple.getItem4();

            return createSession.find(ResourceItem.class, id)
                    .onItem().ifNotNull().transformToUni(existingItem ->
                            createResourceItemDataAndValueInternal(createSession, existingItem, value, createEnterprise, createSystem, createIdentityToken)
                                    .replaceWith(1))
                    .onItem().ifNull().switchTo(() ->
                            createDefaultResourceItemWithDataInternal(createSession, id, value, createEnterprise, createSystem, createIdentityToken)
                                    .replaceWith(1));
        });
    }

    /**
     * Creates a ResourceItemData and ResourceItemDataValue for an existing ResourceItem that is missing them.
     * Uses the session directly — must be called from within an existing withActivityMaster block.
     */
    private Uni<Void> createResourceItemDataAndValueInternal(Mutiny.Session session, ResourceItem resourceItem, byte[] data,
                                                             com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise,
                                                             ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Creating missing ResourceItemData and ResourceItemDataValue for ResourceItem: {}", resourceItem.getId());

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        return acService
                .getActiveFlag(session, enterprise, identityToken)
                .chain(activeFlag -> {
                    LocalDateTime now = RootEntity.getNow();
                    ResourceItemData rid = new ResourceItemData();
                    rid.setResource(resourceItem);
                    rid.setEffectiveFromDate(convertToUTCDateTime(now));
                    rid.setWarehouseCreatedTimestamp(convertToUTCDateTime(now));
                    rid.setEffectiveToDate(EndOfTime.atOffset(ZoneOffset.UTC));
                    rid.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(now));
                    rid.setActiveFlagID(activeFlag);
                    rid.setOriginalSourceSystemID(system.getId());
                    rid.setSystemID(system);
                    rid.setEnterpriseID(enterprise);

                    ResourceItemDataValue dataValue = new ResourceItemDataValue();
                    dataValue.setId(resourceItem.getId());
                    dataValue.setData(data != null ? data : new byte[0]);
                    rid.setDataValue(dataValue);

                    return session.persist(rid)
                            .chain(() -> session.persist(dataValue))
                            .chain(session::flush)
                            .replaceWithVoid();
                });
    }

    /**
     * Creates a default ResourceItem (with the "Unknown" type), along with its ResourceItemData and ResourceItemDataValue,
     * when no ResourceItem exists for the requested id.
     * Uses the session directly — must be called from within an existing withActivityMaster block.
     */
    private Uni<IResourceItem<?, ?>> createDefaultResourceItemWithDataInternal(Mutiny.Session session, UUID id, byte[] data,
                                                                               com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise,
                                                                               ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Creating default ResourceItem with 'Unknown' type for missing id: {}", id);

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        return acService
                .getActiveFlag(session, enterprise, identityToken)
                .chain(activeFlag -> {
                    ResourceItem xr = new ResourceItem();
                    xr.setId(id);
                    xr.setOriginalSourceSystemID(system.getId());
                    xr.setOriginalSourceSystemUniqueID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    xr.setEffectiveFromDate(convertToUTCDateTime(RootEntity.getNow()));
                    xr.setSystemID(system);
                    xr.setEnterpriseID(enterprise);
                    xr.setActiveFlagID(activeFlag);
                    xr.setResourceItemDataType(com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes.Unknown.toString());

                    return session.persist(xr)
                            .replaceWith(xr)
                            .chain(persisted -> createResourceItemDataAndValueInternal(session, persisted, data, enterprise, system, identityToken)
                                    .replaceWith(persisted))
                            .chain(persisted -> addResourceItemTypeRelationshipInternal(
                                    session, persisted,
                                    com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes.Unknown.toString(),
                                    com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes.Unknown.toString(),
                                    system, enterprise, identityToken)
                                    .replaceWith(persisted))
                            .map(persisted -> (IResourceItem<?, ?>) persisted);
                });
    }

    @Override
    public Uni<Void> updateResourceData(Mutiny.Session session, byte[] data, UUID resourceItemId, String systemName) {
        return tryUpdate(session, resourceItemId, data, systemName)
                .replaceWithVoid();
    }

    /**
     * Helper method to add resource item type relationship
     */
    @Override
    public Uni<Void> addResourceItemTypeRelationship(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String typeName, String value, ISystems<?, ?> system, UUID... identityToken) {
        log.trace("Adding resource item type relationship: {} for item: {}", typeName, resourceItem.getId());

        var enterprise = system.getEnterprise();
        return addResourceItemTypeRelationshipInternal(session, resourceItem, typeName, value, system, enterprise, identityToken);
    }

    /**
     * Internal method that uses the session directly — called from within an existing withActivityMaster block.
     */
    private Uni<Void> addResourceItemTypeRelationshipInternal(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String typeName, String value,
                                                              ISystems<?, ?> system, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise, UUID... identityToken) {
        return addResourceItemTypeRelationshipInternal(session, resourceItem, typeName, value, system, enterprise, null, identityToken);
    }

    /**
     * Internal method that uses the session directly — called from within an existing withActivityMaster block.
     * When {@code scopeToken} is non-null the relationship is secured with the restricted matrix instead of the
     * public/default matrix.
     */
    private Uni<Void> addResourceItemTypeRelationshipInternal(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String typeName, String value,
                                                              ISystems<?, ?> system, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise,
                                                              com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                              UUID... identityToken) {
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
                                        .getActiveFlag(session, enterprise, identityToken)
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
                                            // Apply the chosen security matrix to the relationship row
                                            return (scopeToken == null
                                                    ? relationship.createDefaultSecurity(session, system, identityToken)
                                                    : relationship.createScopeRestrictedSecurity(session, system, scopeToken, identityToken))
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
                                                         UUID... identityToken) {
        log.trace("Finding resource by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);

        // First get the classification using reactive pattern
        return classificationService
                .find(session, classification, systems, identityToken)
                .chain(clazz -> {
                    if (clazz == null) {
                        return Uni
                                .createFrom()
                                .nullItem();
                    }

                    try {
                        ResourceItemXClassification res = new ResourceItemXClassification();
                        ResourceItemXClassificationQueryBuilder builder = res.builder(session);

                        builder.where(ResourceItemXClassification_.classificationID, Equals, (Classification) clazz);
                        if (!Strings.isNullOrEmpty(value)) {
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
                    } catch (Exception e) {
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
                                                                                                                UUID... identityToken) {
        log.trace("Finding all resources by classification - resourceType: {}, classification: {}, value: {}", resourceType, classification, value);

        // First get the classification using reactive pattern
        return classificationService
                .find(session, classification, systems, identityToken)
                .chain(clazz -> {
                    if (clazz == null) {
                        return Uni
                                .createFrom()
                                .item(Collections.<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>>emptyList());
                    }
                    ResourceItemXClassification res = new ResourceItemXClassification();
                    ResourceItemXClassificationQueryBuilder builder = res.builder(session);

                    builder.where(ResourceItemXClassification_.classificationID, Equals, (Classification) clazz);
                    if (!Strings.isNullOrEmpty(value)) {
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
    public Uni<IResourceItem<?, ?>> findByUUID(Mutiny.Session session, UUID uuid) {
        log.trace("Finding resource by UUID: {}", uuid);
        ResourceItem res = new ResourceItem();
        //noinspection unchecked,rawtypes
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
                                                                 UUID... identityToken) {
        log.trace("Finding resource by original source unique ID: {}", originalSourceUniqueID);
        ResourceItem res = new ResourceItem();
        //noinspection unchecked,rawtypes
        return (Uni) res
                .builder(session)
                .where(ResourceItem_.originalSourceSystemUniqueID, Equals, originalSourceUniqueID)
                .inActiveRange()
                .inDateRange()
                .get();

    }

    @Override
    public Uni<IResourceItemType<?, ?>> findResourceItemType(Mutiny.Session session, String type, ISystems<?, ?> system, UUID... identityToken) {
        log.trace("Finding resource item type by name (ID-first): {}", type);
        // Resolve ResourceItemType UUID by name using cached native resolver, then load entity by UUID
        //noinspection unchecked,rawtypes
        return (io.smallrye.mutiny.Uni) resolveResourceItemTypeIdByName(session, system.getEnterpriseID(), type)
                .flatMap(id -> getResourceItemTypeById(session, id))
                .map(result -> (IResourceItemType<?, ?>) result)
                .onFailure()
                .invoke(error -> log.error("Error finding resource item type (ID-first): {}", type, error));
    }

    @Override
    public Uni<List<IResourceItem<?, ?>>> findByResourceItemType(Mutiny.Session session, String type, ISystems<?, ?> systems, UUID... identityToken) {
        log.trace("Finding resources by type: {}", type);
        return findByResourceItemType(session, type, null, systems, identityToken);
    }


    @Override
    public Uni<List<IResourceItem<?, ?>>> findByResourceItemType(Mutiny.Session session, String type, String value, ISystems<?, ?> systems, UUID... identityToken) {
        log.trace("Finding resources by type: {} and value: {}", type, value);
        var enterprise = systems.getEnterprise();

        // Resolve the ResourceItemType entity by name first, then filter the type relationship by its ID.
        // (The metamodel attribute lookup is flat — it cannot navigate the dotted "resourceItemTypeID.name"
        // path — so we compare against the resolved entity exactly like findByClassification does.)
        return findResourceItemType(session, type, systems, identityToken)
                .onFailure(jakarta.persistence.NoResultException.class)
                .recoverWithItem((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType<?, ?>) null)
                .chain(resourceItemType -> {
                    if (resourceItemType == null) {
                        return Uni.createFrom().item(Collections.<IResourceItem<?, ?>>emptyList());
                    }

                    // Build query by joining ResourceItem to its types and filtering by the resolved type ID.
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
                    if (value != null) {
                        qb.withValue(value);
                    }
                    // Filter by the resolved ResourceItemType entity (no dotted-path attribute resolution).
                    qb.where(ResourceItemXResourceItemType_.resourceItemTypeID, Equals, (ResourceItemType) resourceItemType);

                    aqb.join(ResourceItem_.types, qb, jakarta.persistence.criteria.JoinType.INNER, joinExpression);

                    //noinspection unchecked
                    return (Uni<List<IResourceItem<?, ?>>>) (Uni<?>) aqb
                            .getAll()
                            .onFailure()
                            .invoke(e -> log.error("Error finding resources by type: {} and value: {}", type, value, e));
                });
    }

}

