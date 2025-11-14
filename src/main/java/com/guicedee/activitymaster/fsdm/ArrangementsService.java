package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * 
 * [✓] One action per Mutiny.Session at a time
 *     - All operations on a session are sequential
 *     - No parallel operations on the same session
 * 
 * [✓] Pass Mutiny.Session through the chain
 *     - All methods accept session as parameter
 *     - Session is passed to all dependent operations
 * 
 * [✓] No await() usage
 *     - Using reactive chains instead of blocking operations
 * 
 * [!] Synchronous execution of reactive chains
 *     - Most reactive chains execute synchronously
 *     - The create and createArrangementType methods call createDefaultSecurity
 *       without properly chaining its result, potentially continuing before
 *       the security setup is complete
 * 
 * [✓] No parallel operations on a session
 *     - Not using Uni.combine().all().unis() with operations that share the same session
 * 
 * [✓] No session/transaction creation in libraries
 *     - Sessions are passed in from the caller
 *     - No sessionFactory.withTransaction() in methods
 * 
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ArrangementException;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.DESC;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.EndOfTime;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;

@SuppressWarnings({"rawtypes", "unchecked"})
@Log4j2
@Singleton
public class ArrangementsService
        implements IArrangementsService<ArrangementsService>
{
    // Local cache: key = enterpriseId + '|' + systemId + '|' + arrangementTypeName → ArrangementType UUID
    private final java.util.Map<String, java.util.UUID> arrangementTypeKeyToId = new java.util.concurrent.ConcurrentHashMap<>();

    // UUID-based lookup to leverage Hibernate 2nd-level cache
    public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType<?, ?>> getArrangementTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id) {
        return (io.smallrye.mutiny.Uni) session.find(com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementType.class, id);
    }
    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private Vertx vertx;

    @Override
    public IArrangement<?, ?> get()
    {
        log.trace("Getting new Arrangement instance");
        return new Arrangement();
    }


    @Override
    public Uni<IArrangement<?, ?>> create(Mutiny.Session session, String type,
                                          String arrangementTypeClassification,
                                          String arrangementTypeValue,
                                          ISystems<?, ?> system,
                                          UUID... identityToken)
    {
        log.debug("Creating arrangement - type: {}, classification: {}, value: {}", type, arrangementTypeClassification, arrangementTypeValue);
        return create(session, type, null, arrangementTypeClassification, arrangementTypeValue, system, identityToken);
    }


    @Override
    public Uni<IArrangement<?, ?>> create(
            Mutiny.Session session, String type,
            UUID key,
            String arrangementTypeClassification,
            String arrangementTypeValue,
            ISystems<?, ?> system,
            UUID... identityToken)
    {
        log.debug("Creating arrangement - type: {}, key: {}, classification: {}, value: {}",
                type, key, arrangementTypeClassification, arrangementTypeValue);
        // Step 1: Create the arrangement
        Arrangement arrangement = new Arrangement();
        arrangement.setId(key != null ? key : UUID.randomUUID());
        arrangement.setSystemID(system);
        arrangement.setOriginalSourceSystemID(system.getId());
        arrangement.setEnterpriseID(system.getEnterpriseID());
        IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);
        var enterprise = system.getEnterprise();
        return (Uni) activeFlagService.getActiveFlag(session, system.getEnterprise())
                       .chain(activeFlag -> {
                           arrangement.setActiveFlagID(activeFlag);
                           return session.persist(arrangement).replaceWith(Uni.createFrom().item(arrangement));
                       })
                       .chain(persisted -> {
                           // Step 2: Create default security with proper chaining
                           return persisted.createDefaultSecurity(session, system, identityToken)
                               .onItem().invoke(() -> log.trace("Security setup completed successfully for arrangement"))
                               .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for arrangement", error))
                               .onFailure().recoverWithItem(() -> null) // Continue even if security setup fails
                               .chain(() -> find(session, type, system));
                       })
                       .chain(arrangementType -> {
                           // Step 3: Add arrangement type
                           return arrangement.addOrUpdateArrangementType(
                                           session, arrangementTypeClassification,
                                           arrangementType,
                                           arrangementTypeValue,
                                           arrangementTypeValue,
                                           system,
                                           identityToken
                                   )
                                          .map(result -> arrangement);
                       })
                       .map(result -> (IArrangement<?, ?>) result)
                       .onFailure()
                       .invoke(error ->
                                       log.error("Failed to create arrangement", error)
                       );

    }

    @Override
    public Uni<IArrangementType<?, ?>> createArrangementType(Mutiny.Session session, String type, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Creating arrangement type: {}", type);
        return createArrangementType(session, type, null, system, identityToken);
    }

    @Override
    ////@CacheResult(cacheName = "ArrangementTypes")
    //
    public Uni<IArrangementType<?, ?>> createArrangementType(Mutiny.Session session, String type, UUID key, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Creating arrangement type: {}, key: {}", type, key);
        ArrangementType xr = new ArrangementType();
        xr.setId(key);
        xr.setName(type);
        xr.setDescription(type);
        xr.setSystemID(system);
        xr.setOriginalSourceSystemID(system.getId());
        var enterprise = system.getEnterprise();
        xr.setEnterpriseID(enterprise);
        IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);

        return (Uni)acService.getActiveFlag(session, enterprise)
                       .chain(activeFlag -> {
                           xr.setActiveFlagID(activeFlag);
                           return session.persist(xr).replaceWith(Uni.createFrom().item(xr));
                       })
                       .chain(persisted -> {
                           // Create default security with proper chaining
                           return persisted.createDefaultSecurity(session, system, identityToken)
                               .onItem().invoke(() -> log.trace("Security setup completed successfully for arrangement type"))
                               .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for arrangement type", error))
                               .onFailure().recoverWithItem(() -> null) // Continue even if security setup fails
                               .map(result -> (IArrangementType<?, ?>) persisted);
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Failed to create arrangement type: {}", type, error)
                       );

    }


    @Override
    public Uni<IArrangementType<?, ?>> findArrangementType(Mutiny.Session session, String type, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangement type: {}", type);
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
            systemId = sys.getId();
        }
        String key = enterpriseId + "|" + systemId + "|" + type;
        java.util.UUID cachedId = arrangementTypeKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 ArrangementType cache hit for key '{}': {} — loading by UUID", key, cachedId);
            return (Uni) getArrangementTypeById(session, cachedId)
                .flatMap(found -> {
                    if (found != null) {
                        return Uni.createFrom().item(found);
                    }
                    arrangementTypeKeyToId.remove(key);
                    ArrangementType xr = new ArrangementType();
                    return (Uni) xr.builder(session)
                                     .withName(type)
                                     .inActiveRange()
                                     .inDateRange()
                                     .withEnterprise(enterprise)
                                     .get()
                                     .invoke(res -> {
                                         if (res != null && res.getId() != null) {
                                             arrangementTypeKeyToId.put(key, (java.util.UUID) res.getId());
                                         }
                                     })
                                     .onItem()
                                     .ifNull()
                                     .failWith(() -> new ArrangementException("Unable to find arrangement type - " + type))
                                     .map(result -> (IArrangementType<?, ?>) result);
                });
        }
        ArrangementType xr = new ArrangementType();
        return (Uni) xr.builder(session)
                             .withName(type)
                             .inActiveRange()
                             .inDateRange()
                             .withEnterprise(enterprise)
                             .get()
                             .invoke(res -> {
                                 if (res != null && res.getId() != null) {
                                     arrangementTypeKeyToId.put(key, (java.util.UUID) res.getId());
                                 }
                             })
                             .onItem()
                             .ifNull()
                             .failWith(() -> new ArrangementException("Unable to find arrangement type - " + type))
                             .map(result -> (IArrangementType<?, ?>) result)
                             .onFailure()
                             .invoke(error -> log.error("Error finding arrangement type: {}", type, error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findInvolvedPartyArrangements(Mutiny.Session session, IInvolvedParty<?, ?> ip, String arrType, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding involved party arrangements for IP: {}, type: {}", ip.getId(), arrType);
        var enterprise = systems.getEnterprise();
        return new ArrangementXInvolvedParty()
                       .builder(session)
                       .withEnterprise(enterprise)
                       .findLink(null, (InvolvedParty) ip, null)
                       .withValue(arrType)
                       .inActiveRange()
                       .inDateRange()
                       .getAll()
                       .map(xips -> {
                           List<IArrangement<?, ?>> result = xips.stream()
                                                                     .map(ArrangementXInvolvedParty::getArrangementID)
                                                                     .filter(a -> convertToUTCDateTime(com.entityassist.RootEntity.getNow())
                                                                                          .isBefore(a.getEffectiveToDate()))
                                                                     .collect(Collectors.toList())
                                   ;
                           log.trace("Found {} arrangements for involved party", result.size());
                           return result;
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding involved party arrangements: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassification(Mutiny.Session session, String classificationName, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding arrangements by classification - name: {}, value: {}", classificationName, value);
        var enterprise = systems.getEnterprise();
        // First get the classification using reactive pattern
        return classificationService.find(session, classificationName, systems, identityToken)
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                           aqb.withEnterprise(enterprise)
                                   .inActiveRange()
                                   .inDateRange()
                           ;
                           JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                           ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                           qb.withEnterprise(enterprise)
                                   .withClassification((Classification) classification)
                                   .withValue(value)
                                   .inActiveRange()
                                   .inDateRange()
                           ;

                           aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

                           aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                           // Get the result from the builder using reactive pattern
                           return aqb.getAll()
                                          .map(arrangementList -> {
                                              List<IArrangement<?, ?>> result = new ArrayList<>(arrangementList);
                                              log.trace("Found {} arrangements for classification {}", result.size(), classificationName);
                                              return result;
                                          });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationGT(Mutiny.Session session, String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding arrangements by classification GT - type: {}, value: {}", arrType, value);
      var enterprise = systems.getEnterprise();
        // First get the classification using reactive pattern
        return classificationService.find(session, arrType, systems, identityToken)
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                           aqb.withEnterprise(enterprise)
                                   .inActiveRange()
                                   .inDateRange()
                           ;
                           JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                           ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                           qb.withEnterprise(enterprise)
                                   .withClassification((Classification) classification)
                                   .withValue(GreaterThan, value)
                                   .inActiveRange()
                                   .inDateRange()
                           ;

                           aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

                           if (withParent != null)
                           {
                               JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
                               ArrangementXArrangementQueryBuilder builder =
                                       new ArrangementXArrangement()
                                               .builder(session)
                                               .inActiveRange()
                                               .inDateRange()
                                               .where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent)
                                       ;
                               aqb.join(Arrangement_.arrangementXArrangementList,
                                       builder,
                                       JoinType.INNER, joinExpression);
                           }

                           aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                           // Get the result from the builder using reactive pattern
                           return aqb.getAll()
                                          .map(arrangementList -> {
                                              List<IArrangement<?, ?>> result = new ArrayList<>(arrangementList);
                                              log.trace("Found {} arrangements for classification GT {}", result.size(), arrType);
                                              return result;
                                          });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification GT: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationGTE(Mutiny.Session session, String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding arrangements by classification GTE - type: {}, value: {}", arrType, value);
      var enterprise = systems.getEnterprise();
        // First get the classification using reactive pattern
        return classificationService.find(session, arrType, systems, identityToken)
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }
                           ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                           aqb.withEnterprise(enterprise)
                                   .inActiveRange()
                                   .inDateRange()
                           ;
                           JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                           ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                           qb.withEnterprise(enterprise)
                                   .withClassification((Classification) classification)
                                   .withValue(GreaterThanEqualTo, value)
                                   .inActiveRange()
                                   .inDateRange()
                           ;

                           aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

                           if (withParent != null)
                           {
                               JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
                               ArrangementXArrangementQueryBuilder builder =
                                       new ArrangementXArrangement()
                                               .builder(session)
                                               .inActiveRange()
                                               .inDateRange()
                                               .where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent)
                                       ;
                               aqb.join(Arrangement_.arrangementXArrangementList,
                                       builder,
                                       JoinType.INNER, joinExpression);
                           }

                           aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                           // Get the result from the builder using reactive pattern
                           return aqb.getAll()
                                          .map(arrangementList -> {
                                              List<IArrangement<?, ?>> result = new ArrayList<>(arrangementList);
                                              log.trace("Found {} arrangements for classification GTE {}", result.size(), arrType);
                                              return result;
                                          });

                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification GTE: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationGTEWithIP(Mutiny.Session session, String arrangementType, String classificationName,
                                                                                   IInvolvedParty<?, ?> withInvolvedParty,
                                                                                   String ipClassification,
                                                                                   IArrangement<?, ?> withParent,
                                                                                   IResourceItem<?, ?> resourceItem,
                                                                                   String resourceItemClassification,
                                                                                   String value, ISystems<?, ?> system,
                                                                                   UUID... identityToken)
    {
        log.trace("Finding arrangements by classification GTE with IP - type: {}, classification: {}, value: {}",
                arrangementType, classificationName, value);

        // First get the classification using reactive pattern if classificationName is provided
        Uni<IClassification<?, ?>> classificationUni;
        if (classificationName != null)
        {
            classificationUni = classificationService.find(session, classificationName, system, identityToken);
        }
        else
        {
            classificationUni = Uni.createFrom()
                                        .failure(new ArrangementException("Classification name not provided"));
        }
        var enterprise = system.getEnterprise();
        return (Uni) classificationUni.chain(classification -> {
                    ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                    aqb.withEnterprise(enterprise)
                            .inActiveRange()
                            .inDateRange()
                    ;

                    if (classificationName != null && classification != null)
                    {
                        JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                        ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                        qb.withEnterprise(enterprise)
                                .withClassification((Classification) classification)
                                .withValue(GreaterThanEqualTo, value)
                                .inActiveRange()
                                .inDateRange()
                        ;
                        aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
                    }

                    if (withInvolvedParty != null)
                    {
                        JoinExpression<Arrangement, InvolvedParty, ?> joinExpression = new JoinExpression<>();
                        String ipClass = ipClassification;
                        if (Strings.isNullOrEmpty(ipClass))
                        {
                            ipClass = NoClassification.toString();
                        }
                        ArrangementXInvolvedPartyQueryBuilder builder =
                                new ArrangementXInvolvedParty()
                                        .builder(session)
                                        .inActiveRange()
                                        .withClassification(ipClass, system)
                                        .inDateRange()
                                        .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) withInvolvedParty)
                                ;
                        aqb.join(Arrangement_.parties,
                                builder,
                                JoinType.INNER, joinExpression);
                    }

                    if (resourceItem != null)
                    {
                        JoinExpression<Arrangement, ResourceItem, ?> joinExpression = new JoinExpression<>();
                        String resourceClass = resourceItemClassification;
                        if (Strings.isNullOrEmpty(resourceClass))
                        {
                            resourceClass = NoClassification.toString();
                        }
                        ArrangementXResourceItemQueryBuilder builder =
                                new ArrangementXResourceItem()
                                        .builder(session)
                                        .inActiveRange()
                                        .withClassification(resourceClass, system)
                                        .inDateRange()
                                        .where(ArrangementXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
                                ;
                        aqb.join(Arrangement_.resources,
                                builder,
                                JoinType.INNER, joinExpression);
                    }

                    if (!Strings.isNullOrEmpty(arrangementType))
                    {
                        JoinExpression<Arrangement, ArrangementType, ?> joinExpressionAt = new JoinExpression<>();
                        ArrangementXArrangementTypeQueryBuilder builderAT =
                                new ArrangementXArrangementType()
                                        .builder(session)
                                        .inActiveRange()
                                        .withType(arrangementType, system, identityToken)
                                        .inDateRange()
                                ;
                        aqb.join(Arrangement_.types,
                                builderAT,
                                JoinType.INNER, joinExpressionAt);
                    }

                    if (withParent != null)
                    {
                        JoinExpression<Arrangement, Arrangement, ?> joinExpressionParentJoin = new JoinExpression<>();
                        ArrangementXArrangementQueryBuilder builderParent =
                                new ArrangementXArrangement()
                                        .builder(session)
                                        .inActiveRange()
                                        .inDateRange()
                                        .where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent)
                                ;
                        aqb.join(Arrangement_.arrangementXArrangementList,
                                builderParent,
                                JoinType.INNER, joinExpressionParentJoin);
                    }

                    aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                    // Get the result from the builder using reactive pattern
                    return aqb.getAll()
                                   .map(result -> result);
                })
                             .onFailure()
                             .invoke(error ->
                                             log.error("Error finding arrangements by classification GTE with IP: {}", error.getMessage(), error));


    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationLT(Mutiny.Session session, String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding arrangements by classification LT - type: {}, value: {}", arrType, value);
      var enterprise = systems.getEnterprise();
        // First get the classification using reactive pattern
        return (Uni) classificationService.find(session, arrType, systems, identityToken)
                             .onItem()
                             .ifNull()
                             .continueWith(() -> {
                                 log.warn("Classification not found: {}", arrType);
                                 return null;
                             })
                             .chain(classification -> {
                                 if (classification == null)
                                 {
                                     return Uni.createFrom()
                                                    .item(Collections.<IArrangement<?, ?>>emptyList());
                                 }
                                 ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                                 aqb.withEnterprise(enterprise)
                                         .inActiveRange()
                                         .inDateRange()
                                 ;
                                 JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                                 ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                                 qb.withEnterprise(enterprise)
                                         .withClassification((Classification) classification)
                                         .withValue(LessThan, value)
                                         .inActiveRange()
                                         .inDateRange()
                                 ;

                                 aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

                                 if (withParent != null)
                                 {
                                     JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
                                     ArrangementXArrangementQueryBuilder builder =
                                             new ArrangementXArrangement()
                                                     .builder(session)
                                                     .inActiveRange()
                                                     .inDateRange()
                                                     .where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent)
                                             ;
                                     aqb.join(Arrangement_.arrangementXArrangementList,
                                             builder,
                                             JoinType.INNER, joinExpression);
                                 }

                                 aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                                 // Get the result from the builder using reactive pattern
                                 return aqb.getAll()
                                                .map(result -> result);
                             })
                             .onFailure()
                             .invoke(error ->
                                             log.error("Error finding arrangements by classification LT: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationLTE(Mutiny.Session session, String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding arrangements by classification LTE - type: {}, value: {}", arrType, value);
      var enterprise = systems.getEnterprise();
        // First get the classification using reactive pattern
        return classificationService.find(session, arrType, systems, identityToken)
                       .onItem()
                       .ifNull()
                       .continueWith(() -> {
                           log.warn("Classification not found: {}", arrType);
                           return null;
                       })
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                           aqb.withEnterprise(enterprise)
                                   .inActiveRange()
                                   .inDateRange()
                           ;
                           JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                           ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                           qb.withEnterprise(enterprise)
                                   .withClassification((Classification) classification)
                                   .withValue(LessThanEqualTo, value)
                                   .inActiveRange()
                                   .inDateRange()
                           ;

                           aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

                           if (withParent != null)
                           {
                               JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
                               ArrangementXArrangementQueryBuilder builder =
                                       new ArrangementXArrangement()
                                               .builder(session)
                                               .inActiveRange()
                                               .inDateRange()
                                               .where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent)
                                       ;
                               aqb.join(Arrangement_.arrangementXArrangementList,
                                       builder,
                                       JoinType.INNER, joinExpression);
                           }

                           aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                           // Get the result from the builder using reactive pattern
                           return aqb.getAll()
                                          .map(arrangementList -> {
                                              List<IArrangement<?, ?>> result = new ArrayList<>(arrangementList);
                                              log.debug("Found {} arrangements for classification LTE {}", result.size(), arrType);
                                              return result;
                                          });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification LTE: {}", error.getMessage(), error));
    }

    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassification(Mutiny.Session session, String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding arrangements by classification - type: {}, withParent: {}, value: {}",
                arrType, withParent != null ? withParent.getId() : "null", value);
      var enterprise = systems.getEnterprise();
        // First get the classification using reactive pattern
        return classificationService.find(session, arrType, systems, identityToken)
                       .onItem()
                       .ifNull()
                       .continueWith(() -> {
                           log.warn("Classification not found: {}", arrType);
                           return null;
                       })
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           ArrangementQueryBuilder aqb = new Arrangement().builder(session);
                           aqb.withEnterprise(enterprise)
                                   .inActiveRange()
                                   .inDateRange()
                           ;
                           JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                           ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder(session);
                           qb.withEnterprise(enterprise)
                                   .withClassification((Classification) classification)
                                   .withValue(value)
                                   .inActiveRange()
                                   .inDateRange()
                           ;

                           aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

                           if (withParent != null)
                           {
                               JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
                               ArrangementXArrangementQueryBuilder builder =
                                       new ArrangementXArrangement()
                                               .builder(session)
                                               .inActiveRange()
                                               .inDateRange()
                                               .where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent)
                                       ;
                               if (!Strings.isNullOrEmpty(value))
                               {
                                   builder.where(ArrangementXClassification_.value, Equals, value);
                               }

                               aqb.join(Arrangement_.arrangementXArrangementList,
                                       builder,
                                       JoinType.INNER, joinExpression);
                           }

                           aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);

                           // Get the result from the builder using reactive pattern
                           return aqb.getAll()
                                          .map(arrangementList -> {
                                              List<IArrangement<?, ?>> result = new ArrayList<>(arrangementList);
                                              log.trace("Found {} arrangements for classification {} with parent",
                                                      result.size(), arrType);
                                              return result;
                                          });

                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification with parent: {}", error.getMessage(), error));
    }


    @Override
    public Uni<IArrangement<?, ?>> findArrangementByResourceItem(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangement by resource item: {}, classification: {}, value: {}",
                resourceItem.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        final String finalClassificationName = classificationName;

        return (Uni) classificationService.find(session, classificationName, system, identityToken)
                             .chain(classification -> {
                                 return new ArrangementXResourceItem().builder(session)
                                                .inActiveRange()
                                                .inDateRange()
                                                .withEnterprise(enterprise)
                                                .withClassification(classification)
                                                .withValue(value)
                                                .where(ArrangementXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
                                                .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                                .get()
                                                .map(result -> {
                                                    log.trace("Found arrangement for resource item: {}, classification: {}",
                                                            resourceItem.getId(), finalClassificationName);
                                                    return result != null ? result.getArrangementID() : null;
                                                });
                             })
                             .onFailure()
                             .invoke(error ->
                                             log.error("Error finding arrangement by resource item: {}", error.getMessage(), error));
    }


    @Override
    public Uni<IArrangement<?, ?>> findArrangementByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangement by involved party: {}, classification: {}, value: {}",
                involvedParty.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;
        var enterprise = system.getEnterprise();
        return (Uni) classificationService.find(session, classificationName, system, identityToken)
                             .chain(classification -> {

                                 return new ArrangementXInvolvedParty().builder(session)
                                                .inActiveRange()
                                                .inDateRange()
                                                .withEnterprise(enterprise)
                                                .withClassification(classification)
                                                .withValue(value)
                                                .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                                                .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                                .get()
                                                .map(result -> {
                                                    log.trace("Found arrangement for involved party: {}, classification: {}",
                                                            involvedParty.getId(), finalClassificationName);
                                                    return result.getArrangementID();
                                                });

                             })
                             .onFailure()
                             .invoke(error ->
                                             log.error("Error finding arrangement by involved party: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByRulesType(Mutiny.Session session, IRulesType<?, ?> ruleType, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangements by rules type: {}, classification: {}, value: {}", ruleType.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;
        var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                       .chain(classification -> {
                           return new ArrangementXRulesType().builder(session)
                                          .inActiveRange()
                                          .inDateRange()
                                          .withEnterprise(enterprise)
                                          .withClassification(classification)
                                          .withValue(value)
                                          .where(ArrangementXRulesType_.rulesTypeID, Equals, (RulesType) ruleType)
                                          .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                          .getAll()
                                          .map(results -> {
                                              List<IArrangement<?, ?>> arrangements = results.stream()
                                                                                              .map(ArrangementXRulesType::getArrangement)
                                                                                              .collect(Collectors.toList())
                                                      ;
                                              log.trace("Found {} arrangements for rules type: {}, classification: {}",
                                                      arrangements.size(), ruleType.getId(), finalClassificationName);
                                              return arrangements;
                                          });

                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by rules type: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangements by involved party: {}, classification: {}, value: {}, startDate: {}",
                involvedParty.getId(), classificationName, value, startDate);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;
      var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                       .chain(classification -> {
                           return new ArrangementXInvolvedParty().builder(session)
                                          .inActiveRange()
                                          .inDateRange(startDate, EndOfTime)
                                          .withEnterprise(enterprise)
                                          .withClassification(classification)
                                          .withValue(value)
                                          .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                                          .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                          .getAll()
                                          .map(all -> {
                                              List<IArrangement<?, ?>> arrangements = all.stream()
                                                                                              .map(ArrangementXInvolvedParty::getArrangementID)
                                                                                              .collect(Collectors.toList())
                                                      ;
                                              log.trace("Found {} arrangements for involved party: {}, classification: {}",
                                                      arrangements.size(), involvedParty.getId(), finalClassificationName);
                                              return arrangements;
                                          });

                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by involved party: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, LocalDateTime endDate, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangements by involved party: {}, classification: {}, value: {}, startDate: {}, endDate: {}",
                involvedParty.getId(), classificationName, value, startDate, endDate);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;
      var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                       .chain(classification -> {
                           return new ArrangementXInvolvedParty().builder(session)
                                          .inActiveRange()
                                          .inDateRange(startDate, endDate)
                                          .withEnterprise(enterprise)
                                          .withClassification(classification)
                                          .withValue(value)
                                          .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                                          .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                          .getAll()
                                          .map(all -> {
                                              List<IArrangement<?, ?>> arrangements = all.stream()
                                                                                              .map(ArrangementXInvolvedParty::getArrangementID)
                                                                                              .collect(Collectors.toList())
                                                      ;
                                              log.trace("Found {} arrangements for involved party: {}, classification: {}, with date range",
                                                      arrangements.size(), involvedParty.getId(), finalClassificationName);
                                              return arrangements;
                                          });

                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by involved party with date range: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangements by involved party: {}, classification: {}, value: {}",
                involvedParty.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;
      var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                       .chain(classification -> {
                           return new ArrangementXInvolvedParty().builder(session)
                                          .inActiveRange()
                                          .inDateRange()
                                          .withEnterprise(enterprise)
                                          .withClassification(classification)
                                          .withValue(value)
                                          .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                                          .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                          .getAll()
                                          .map(arxip -> {
                                              List<IArrangement<?, ?>> arrangements = arxip.stream()
                                                                                              .<IArrangement<?, ?>>map(ArrangementXInvolvedParty::getArrangementID)
                                                                                              .collect(Collectors.toList())
                                                      ;
                                              log.trace("Found {} arrangements for involved party: {}, classification: {}",
                                                      arrangements.size(), involvedParty.getId(), finalClassificationName);
                                              return arrangements;
                                          });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by involved party: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IInvolvedParty<?, ?>>> findArrangementInvolvedParties(Mutiny.Session session, IArrangement<?, ?> arrangement, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding involved parties for arrangement: {}, classification: {}, value: {}",
                arrangement.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;
      var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                       .chain(classification -> {
                           return new ArrangementXInvolvedParty().builder(session)
                                          .inActiveRange()
                                          .inDateRange()
                                          .withEnterprise(enterprise)
                                          .withClassification(classification)
                                          .withValue(value)
                                          .where(ArrangementXInvolvedParty_.arrangementID, Equals, (Arrangement) arrangement)
                                          .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                          .getAll()
                                          .map(arxip -> {
                                              List<IInvolvedParty<?, ?>> involvedParties = arxip.stream()
                                                                                                   .<IInvolvedParty<?, ?>>map(ArrangementXInvolvedParty::getInvolvedPartyID)
                                                                                                   .collect(Collectors.toList())
                                                      ;
                                              log.trace("Found {} involved parties for arrangement: {}, classification: {}",
                                                      involvedParties.size(), arrangement.getId(), finalClassificationName);
                                              return involvedParties;
                                          });

                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding involved parties for arrangement: {}", error.getMessage(), error));
    }


    /// /@CacheResult(cacheName = "ArrangementArrangementTypeString")
    @Override
    public @NotNull Uni<IArrangementType<?, ?>> find(Mutiny.Session session, String idType, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangement type by name: {}", idType);
        var enterprise = system.getEnterprise();
        ArrangementType xr = new ArrangementType();
        return (Uni) xr.builder(session)
                             .withName(idType)
                             .inActiveRange()
                             .inDateRange()
                             .withEnterprise(enterprise)
                             //   .canRead(system, tokens)
                             .get();
    }


    @Override
    ////@CacheResult
    public @NotNull Uni<IArrangement<?, ?>> find(Mutiny.Session session, UUID id, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding arrangement by ID: {}", id);
        Arrangement xr = new Arrangement();
        return (Uni)xr.builder(session)
                       .where(Arrangement_.id, Equals, id)
                       .get();
    }


    @Override
    ////@CacheResult
    public @NotNull Uni<IArrangement<?, ?>> find(Mutiny.Session session, UUID id)
    {
        log.trace("Finding arrangement by ID (no system): {}", id);
        Arrangement xr = new Arrangement();
        return (Uni)xr.builder(session)
                       .where(Arrangement_.id, Equals, id)
                       .get();
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findAll(Mutiny.Session session, String arrangementType, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding all arrangements of type: {}", arrangementType);

        return find(session, arrangementType, system, identityToken)
                       .chain(type -> {
                           return new ArrangementXArrangementType().builder(session)
                                          .inActiveRange()
                                          .inDateRange()
                                          .canRead(system, identityToken)
                                          .findLink(null, (ArrangementType) type, null)
                                          .getAll()
                                          .map(arrs -> {
                                              List<IArrangement<?, ?>> arrOut = new ArrayList<>();
                                              for (ArrangementXArrangementType arr : arrs)
                                              {
                                                  arrOut.add(arr.getArrangement());
                                              }
                                              return arrOut;
                                          });

                       });
    }

    @Override
    public Uni<IArrangement<?, ?>> completeArrangement(Mutiny.Session session, IArrangement<?, ?> arrangement, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Completing arrangement: {}", arrangement.getId());
        Arrangement arr = (Arrangement) arrangement;
        return (Uni) arr.expire(Duration.ZERO);

    }
}

