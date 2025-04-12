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

    public IResourceItem<?, ?> get()
    {
        return new ResourceItem();
    }

    @Override
    public IResourceData<?, ?, ?> getData()
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

    public IResourceItemType<?, ?> createType(String value, java.util.UUID key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
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
            xr.setId(key);
            xr.setName(value);
            xr.setDescription(value);
            xr.setOriginalSourceSystemID(system);
            xr.setSystemID(system);
            xr.setEnterpriseID(enterprise);
            IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
            IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
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
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {

        return create(identityResourceType, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, "", com.entityassist.RootEntity.getNow(), data, system, identityToken);
    }


    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, String originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, String resourceItemDataValue, String originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, null, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, data, system, identityToken);
    }


    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue, String originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return create(identityResourceType, key, resourceItemDataValue, originalSourceSystemUniqueID, effectiveFromDate, null, system, identityToken);
    }

    @Override
    public Future<IResourceItem<?, ?>> create(String identityResourceType, java.util.UUID key, String resourceItemDataValue,
                                              String originalSourceSystemUniqueID,
                                              LocalDateTime effectiveFromDate, byte[] data,
                                              ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        Future<ResourceItem> future = vertx.executeBlocking(TransactionalCallable.of(() -> {
                    ResourceItem xr = new ResourceItem();
                    xr.setId(key);
                    xr.setOriginalSourceSystemID(system);
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
                    rid.setOriginalSourceSystemID(system);
                    rid.setSystemID(system);
                    rid.setEnterpriseID(system.getEnterpriseID());
                    rid.persist();

                    rid.createDefaultSecurity(system, identityToken);
                    return xr;
                }, true), true)
                .onComplete(ri -> {
                    if (ri.failed() || ri.result() == null)
                    {
                        log.error("Failed to create resource item", ri.cause());
                    }
                    else
                    {
                        if (data != null)
                        {
                            vertx.executeBlocking(TransactionalCallable.of(() -> {
                                var ris = ri.result();
                                ris.updateData(data, system, identityToken);
                                return ris;
                            }, true), true);
                        }
                    }
                })
                .onComplete(ri -> {
                    if (ri.failed() || ri.result() == null)
                    {
                        log.error("Failed to create resource item", ri.cause());
                    }
                    else
                    {
                        vertx.executeBlocking(TransactionalCallable.of(() -> {
                            ri.result().addResourceItemTypes(identityResourceType, null, DefaultClassifications.NoClassification.toString(), system, identityToken);
                            return ri;
                        }, true), true);
                    }
                });
        return (Future) future;
    }

    @Override
    public IResourceItem<?, ?> findByClassification(String resourceType,
                                                    String classification,
                                                    String value,
                                                    ISystems<?, ?> systems,
                                                    java.util.UUID... identityToken)
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
    public List<IRelationshipValue<IResourceItem<?, ?>, IClassification<?, ?>, ?>> findByClassificationAll(String resourceType,
                                                                                                           String classification,
                                                                                                           String value,
                                                                                                           ISystems<?, ?> systems,
                                                                                                           java.util.UUID... identityToken)
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
                                                            @CacheKey ISystems<?, ?> systems,
                                                            @CacheKey java.util.UUID... identityToken)
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
    public byte[] getDataForResourceItemValue(IRelationshipValue<IResourceItem<?, ?>, IResourceData<?, ?, ?>, ?> data)
    {
        ResourceItemData d = (ResourceItemData) data.getSecondary();
        return d.getResourceItemData();
    }


    @Override
    @CacheResult(cacheName = "FindResourceItemTypeString")
    public IResourceItemType<?, ?> findResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
    {
        ResourceItemType xr = new ResourceItemType();
        Optional<ResourceItemType> exists = xr.builder()
                .withEnterprise(enterprise)
                .withName(type)
                .inActiveRange()
                //       .canRead(system, identityToken)
                .inDateRange()
                .get();
        return exists.orElseThrow(() -> new ResourceItemException("Cannot find resource item type [%s]".formatted(type)));
    }

    @Override
    public List<IResourceItem<?, ?>> findByResourceItemType(@CacheKey String type, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
    {
        return findByResourceItemType(type, null, systems, identityToken);
    }


    @Override
    public List<IResourceItem<?, ?>> findByResourceItemType(@CacheKey String type, String value, @CacheKey ISystems<?, ?> systems, @CacheKey java.util.UUID... identityToken)
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
