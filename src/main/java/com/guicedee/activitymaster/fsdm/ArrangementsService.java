package com.guicedee.activitymaster.fsdm;

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
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
import lombok.extern.log4j.Log4j2;


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
public class ArrangementsService
        implements IArrangementsService<ArrangementsService>
{
    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private IEnterprise<?, ?> enterprise;

    @Inject
    private Vertx vertx;

    @Override
    public IArrangement<?, ?> get()
    {
        log.debug("Getting new Arrangement instance");
        return new Arrangement();
    }


    @Override
    public Uni<IArrangement<?, ?>> create(String type,
                                          String arrangementTypeClassification,
                                          String arrangementTypeValue,
                                          ISystems<?, ?> system,
                                          UUID... identityToken)
    {
        log.debug("Creating arrangement - type: {}, classification: {}, value: {}", type, arrangementTypeClassification, arrangementTypeValue);
        return create(type, null, arrangementTypeClassification, arrangementTypeValue, system, identityToken);
    }


    @Override
    public Uni<IArrangement<?, ?>> create(
            String type,
            UUID key,
            String arrangementTypeClassification,
            String arrangementTypeValue,
            ISystems<?, ?> system,
            UUID... identityToken)
    {
        log.debug("Creating arrangement - type: {}, key: {}, classification: {}, value: {}",
                type, key, arrangementTypeClassification, arrangementTypeValue);

        return (Uni) ReactiveTransactionUtil.withTransaction(session -> {
            // Step 1: Create the arrangement
            Arrangement arrangement = new Arrangement();
            arrangement.setId(key != null ? key : UUID.randomUUID());
            arrangement.setSystemID(system);
            arrangement.setOriginalSourceSystemID(system.getId());
            arrangement.setEnterpriseID(system.getEnterpriseID());
            IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);

            return activeFlagService.getActiveFlag(system.getEnterprise())
                           .chain(activeFlag -> {
                               arrangement.setActiveFlagID(activeFlag);
                               return arrangement.persist();
                           })
                           .chain(persisted -> {
                               // Step 2: Create default security
                               persisted.createDefaultSecurity(system, identityToken);
                               return find(type, system);
                           })
                           .chain(arrangementType -> {
                               // Step 3: Add arrangement type
                               return arrangement.addOrUpdateArrangementType(
                                               arrangementTypeClassification,
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
        });
    }

    @Override
    public Uni<IArrangementType<?, ?>> createArrangementType(String type, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Creating arrangement type: {}", type);
        return createArrangementType(type, null, system, identityToken);
    }

    @Override
    ////@CacheResult(cacheName = "ArrangementTypes")
    //
    public Uni<IArrangementType<?, ?>> createArrangementType( String type, java.util.UUID key,  ISystems<?, ?> system,  java.util.UUID... identityToken)
    {
        log.debug("Creating arrangement type: {}, key: {}", type, key);

        return ReactiveTransactionUtil.withTransaction(session -> {
            ArrangementType xr = new ArrangementType();
            xr.setId(key);
            xr.setName(type);
            xr.setDescription(type);
            xr.setSystemID(system);
            xr.setOriginalSourceSystemID(system.getId());
            xr.setEnterpriseID(enterprise);
            IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);

            return acService.getActiveFlag(enterprise)
                           .chain(activeFlag -> {
                               xr.setActiveFlagID(activeFlag);
                               return xr.persist();
                           })
                           .map(persisted -> {
                               // Create default security
                               persisted.createDefaultSecurity(system, identityToken);
                               return (IArrangementType<?, ?>) persisted;
                           })
                           .onFailure()
                           .invoke(error ->
                                           log.error("Failed to create arrangement type: {}", type, error)
                           );
        })
                       .map(result -> result);
    }


    @Override
    public Uni<IArrangementType<?, ?>> findArrangementType(String type, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangement type: {}", type);
        return ReactiveTransactionUtil.withTransaction(session -> {
            ArrangementType xr = new ArrangementType();
            return xr.builder()
                           .withName(type)
                           .inActiveRange()
                           .inDateRange()
                           .withEnterprise(enterprise)
                           .get()
                           .onItem()
                           .ifNull()
                           .failWith(() ->
                                             new ArrangementException("Unable to find arrangement type - " + type))
                           .map(result -> (IArrangementType<?, ?>) result)
                           .onFailure()
                           .invoke(error ->
                                           log.error("Error finding arrangement type: {}", type, error));
        })
                       .map(result ->result);
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findInvolvedPartyArrangements(IInvolvedParty<?, ?> ip, String arrType, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding involved party arrangements for IP: {}, type: {}", ip.getId(), arrType);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new ArrangementXInvolvedParty()
                           .builder()
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
                               log.debug("Found {} arrangements for involved party", result.size());
                               return result;
                           })
                           .onFailure()
                           .invoke(error ->
                                           log.error("Error finding involved party arrangements: {}", error.getMessage(), error));
        });
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassification(String classificationName, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification - name: {}, value: {}", classificationName, value);

        // First get the classification using reactive pattern
        return classificationService.find(classificationName, systems, identityToken)
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               ArrangementQueryBuilder aqb = new Arrangement().builder();
                               aqb.withEnterprise(enterprise)
                                       .inActiveRange()
                                       .inDateRange()
                               ;
                               JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                               ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                                  log.debug("Found {} arrangements for classification {}", result.size(), classificationName);
                                                  return result;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationGT(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification GT - type: {}, value: {}", arrType, value);

        // First get the classification using reactive pattern
        return classificationService.find(arrType, systems, identityToken)
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               ArrangementQueryBuilder aqb = new Arrangement().builder();
                               aqb.withEnterprise(enterprise)
                                       .inActiveRange()
                                       .inDateRange()
                               ;
                               JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                               ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                                   .builder()
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
                                                  log.debug("Found {} arrangements for classification GT {}", result.size(), arrType);
                                                  return result;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification GT: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationGTE(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification GTE - type: {}, value: {}", arrType, value);

        // First get the classification using reactive pattern
        return classificationService.find(arrType, systems, identityToken)
                       .chain(classification -> {
                           if (classification == null)
                           {
                               return Uni.createFrom()
                                              .item(Collections.<IArrangement<?, ?>>emptyList());
                           }

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               ArrangementQueryBuilder aqb = new Arrangement().builder();
                               aqb.withEnterprise(enterprise)
                                       .inActiveRange()
                                       .inDateRange()
                               ;
                               JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                               ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                                   .builder()
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
                                                  log.debug("Found {} arrangements for classification GTE {}", result.size(), arrType);
                                                  return result;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification GTE: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationGTEWithIP(String arrangementType, String classificationName,
                                                                                   IInvolvedParty<?, ?> withInvolvedParty,
                                                                                   String ipClassification,
                                                                                   IArrangement<?, ?> withParent,
                                                                                   IResourceItem<?, ?> resourceItem,
                                                                                   String resourceItemClassification,
                                                                                   String value, ISystems<?, ?> system,
                                                                                   java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification GTE with IP - type: {}, classification: {}, value: {}",
                arrangementType, classificationName, value);

        // First get the classification using reactive pattern if classificationName is provided
        Uni<IClassification<?, ?>> classificationUni;
        if (classificationName != null)
        {
            classificationUni = classificationService.find(classificationName, system, identityToken);
        }
        else
        {
            classificationUni = Uni.createFrom()
                                        .failure(new ArrangementException("Classification name not provided"));
        }

        return (Uni) classificationUni.chain(classification -> {
                    return ReactiveTransactionUtil.withTransaction(session -> {
                        ArrangementQueryBuilder aqb = new Arrangement().builder();
                        aqb.withEnterprise(enterprise)
                                .inActiveRange()
                                .inDateRange()
                        ;

                        if (classificationName != null && classification != null)
                        {
                            JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                            ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                            .builder()
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
                                            .builder()
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
                                            .builder()
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
                                            .builder()
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
                                       .map(result->result);
                    });
                })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification GTE with IP: {}", error.getMessage(), error));


    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationLT(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification LT - type: {}, value: {}", arrType, value);

        // First get the classification using reactive pattern
        return (Uni) classificationService.find(arrType, systems, identityToken)
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

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               ArrangementQueryBuilder aqb = new Arrangement().builder();
                               aqb.withEnterprise(enterprise)
                                       .inActiveRange()
                                       .inDateRange()
                               ;
                               JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                               ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                                   .builder()
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
                               return aqb.getAll().map(result->result);
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification LT: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassificationLTE(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification LTE - type: {}, value: {}", arrType, value);

        // First get the classification using reactive pattern
        return classificationService.find(arrType, systems, identityToken)
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

                           return ReactiveTransactionUtil.withTransaction(session -> {
                               ArrangementQueryBuilder aqb = new Arrangement().builder();
                               aqb.withEnterprise(enterprise)
                                       .inActiveRange()
                                       .inDateRange()
                               ;
                               JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                               ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                                   .builder()
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
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by classification LTE: {}", error.getMessage(), error));
    }

    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByClassification(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by classification - type: {}, withParent: {}, value: {}", 
                arrType, withParent != null ? withParent.getId() : "null", value);

        // First get the classification using reactive pattern
        return classificationService.find(arrType, systems, identityToken)
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

                    return ReactiveTransactionUtil.withTransaction(session -> {
                        ArrangementQueryBuilder aqb = new Arrangement().builder();
                        aqb.withEnterprise(enterprise)
                                .inActiveRange()
                                .inDateRange()
                        ;
                        JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();

                        ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
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
                                            .builder()
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
                                    log.debug("Found {} arrangements for classification {} with parent", 
                                            result.size(), arrType);
                                    return result;
                                });
                    });
                })
                .onFailure()
                .invoke(error ->
                        log.error("Error finding arrangements by classification with parent: {}", error.getMessage(), error));
    }


    @Override
    public Uni<IArrangement<?, ?>> findArrangementByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangement by resource item: {}, classification: {}, value: {}", 
                resourceItem.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return (Uni) classificationService.find(classificationName, system, identityToken)
                .chain(classification -> {
                    return ReactiveTransactionUtil.withTransaction(session -> {
                        return new ArrangementXResourceItem().builder()
                                .inActiveRange()
                                .inDateRange()
                                .withEnterprise(enterprise)
                                .withClassification(classification)
                                .withValue(value)
                                .where(ArrangementXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
                                .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                .get()
                                .map(result -> {
                                    log.debug("Found arrangement for resource item: {}, classification: {}", 
                                            resourceItem.getId(), finalClassificationName);
                                    return result != null ? result.getArrangementID() : null;
                                });
                    });
                })
                .onFailure().invoke(error ->
                        log.error("Error finding arrangement by resource item: {}", error.getMessage(), error));
    }


    @Override
    public Uni<IArrangement<?, ?>> findArrangementByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangement by involved party: {}, classification: {}, value: {}", 
                involvedParty.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return (Uni)classificationService.find(classificationName, system, identityToken)
                .chain(classification -> {
                    return ReactiveTransactionUtil.withTransaction(session -> {
                        return new ArrangementXInvolvedParty().builder()
                                .inActiveRange()
                                .inDateRange()
                                .withEnterprise(enterprise)
                                .withClassification(classification)
                                .withValue(value)
                                .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                                .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
                                .get()
                                .map(result -> {
                                    log.debug("Found arrangement for involved party: {}, classification: {}", 
                                            involvedParty.getId(), finalClassificationName);
                                    return result.getArrangementID();
                                });
                    });
                })
                .onFailure().invoke(error ->
                        log.error("Error finding arrangement by involved party: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByRulesType(IRulesType<?, ?> ruleType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by rules type: {}, classification: {}, value: {}", ruleType.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return classificationService.find(classificationName, system, identityToken)
                       .chain(classification -> {
                           return ReactiveTransactionUtil.withTransaction(session -> {
                               return new ArrangementXRulesType().builder()
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
                                                  log.debug("Found {} arrangements for rules type: {}, classification: {}",
                                                          arrangements.size(), ruleType.getId(), finalClassificationName);
                                                  return arrangements;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by rules type: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by involved party: {}, classification: {}, value: {}, startDate: {}",
                involvedParty.getId(), classificationName, value, startDate);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return classificationService.find(classificationName, system, identityToken)
                       .chain(classification -> {
                           return ReactiveTransactionUtil.withTransaction(session -> {
                               return new ArrangementXInvolvedParty().builder()
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
                                                  log.debug("Found {} arrangements for involved party: {}, classification: {}",
                                                          arrangements.size(), involvedParty.getId(), finalClassificationName);
                                                  return arrangements;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by involved party: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, LocalDateTime endDate, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by involved party: {}, classification: {}, value: {}, startDate: {}, endDate: {}",
                involvedParty.getId(), classificationName, value, startDate, endDate);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return classificationService.find(classificationName, system, identityToken)
                       .chain(classification -> {
                           return ReactiveTransactionUtil.withTransaction(session -> {
                               return new ArrangementXInvolvedParty().builder()
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
                                                  log.debug("Found {} arrangements for involved party: {}, classification: {}, with date range",
                                                          arrangements.size(), involvedParty.getId(), finalClassificationName);
                                                  return arrangements;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by involved party with date range: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangements by involved party: {}, classification: {}, value: {}",
                involvedParty.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return classificationService.find(classificationName, system, identityToken)
                       .chain(classification -> {
                           return ReactiveTransactionUtil.withTransaction(session -> {
                               return new ArrangementXInvolvedParty().builder()
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
                                                  log.debug("Found {} arrangements for involved party: {}, classification: {}",
                                                          arrangements.size(), involvedParty.getId(), finalClassificationName);
                                                  return arrangements;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding arrangements by involved party: {}", error.getMessage(), error));
    }


    @Override
    public Uni<List<IInvolvedParty<?, ?>>> findArrangementInvolvedParties(IArrangement<?, ?> arrangement, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding involved parties for arrangement: {}, classification: {}, value: {}",
                arrangement.getId(), classificationName, value);

        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }

        final String finalClassificationName = classificationName;

        return classificationService.find(classificationName, system, identityToken)
                       .chain(classification -> {
                           return ReactiveTransactionUtil.withTransaction(session -> {
                               return new ArrangementXInvolvedParty().builder()
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
                                                  log.debug("Found {} involved parties for arrangement: {}, classification: {}",
                                                          involvedParties.size(), arrangement.getId(), finalClassificationName);
                                                  return involvedParties;
                                              });
                           });
                       })
                       .onFailure()
                       .invoke(error ->
                                       log.error("Error finding involved parties for arrangement: {}", error.getMessage(), error));
    }


    /// /@CacheResult(cacheName = "ArrangementArrangementTypeString")
    @Override
    public Uni<IArrangementType<?, ?>> find( String idType,  ISystems<?, ?> system,  java.util.UUID... identityToken)
    {
        log.debug("Finding arrangement type by name: {}", idType);
        return ReactiveTransactionUtil.withTransaction(session -> {
            ArrangementType xr = new ArrangementType();
            return xr.builder()
                           .withName(idType)
                           .inActiveRange()
                           .inDateRange()
                           .withEnterprise(enterprise)
                           //   .canRead(system, tokens)
                           .get()
                           .onItem()
                           .ifNull()
                           .failWith(() ->
                                             new ArrangementException("Cannot find active or visible arrangement type - " + idType))
                           .map(result -> (IArrangementType<?, ?>) result)
                           .onFailure()
                           .invoke(error ->
                                           log.error("Error finding arrangement type by name: {}", idType, error));
        })
                       .map(arrangementType -> arrangementType);
    }


    @Override
    ////@CacheResult
    public Uni<IArrangement<?, ?>> find( java.util.UUID id, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding arrangement by ID: {}", id);
        return ReactiveTransactionUtil.withTransaction(session -> {
            Arrangement xr = new Arrangement();
            return xr.builder()
                           .where(Arrangement_.id, Equals, id)
                           .get()
                           .onItem()
                           .ifNull()
                           .failWith(() ->
                                             new ArrangementException("Cannot find active or visible arrangement with ID " + id))
                           .map(result -> (IArrangement<?, ?>) result)
                           .onFailure()
                           .invoke(error ->
                                           log.error("Error finding arrangement by ID: {}", id, error));
        })
                       .map(arrangement -> arrangement);
    }


    @Override
    ////@CacheResult
    public Uni<IArrangement<?, ?>> find( UUID id)
    {
        log.debug("Finding arrangement by ID (no system): {}", id);
        return ReactiveTransactionUtil.withTransaction(session -> {
                    Arrangement xr = new Arrangement();
                    return xr.builder()
                                   .where(Arrangement_.id, Equals, id)
                                   .get()
                                   .onItem()
                                   .ifNull()
                                   .failWith(() ->
                                                     new ArrangementException("Cannot find arrangement with ID " + id))
                                   .map(result -> (IArrangement<?, ?>) result)
                                   .onFailure()
                                   .invoke(error ->
                                                   log.error("Error finding arrangement by ID (no system): {}", id, error));
                })
                       .map(result -> result);
    }


    @Override
    public Uni<List<IArrangement<?, ?>>> findAll(String arrangementType, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Finding all arrangements of type: {}", arrangementType);

        return find(arrangementType, system, identityToken)
                       .chain(type -> ReactiveTransactionUtil.withTransaction(session -> {
                           return new ArrangementXArrangementType().builder()
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
                                          })
                                          .onFailure()
                                          .invoke(error ->
                                                          log.error("Error finding all arrangements of type: {}", arrangementType, error));
                       }));
    }

    @Override
    public Uni<IArrangement<?, ?>> completeArrangement(IArrangement<?, ?> arrangement, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("Completing arrangement: {}", arrangement.getId());

        return ReactiveTransactionUtil.withTransaction(session -> {
            Arrangement arr = (Arrangement) arrangement;
            arr.expireIn(Duration.ZERO);
            return arr.update()
                           .map(updated -> (IArrangement<?, ?>) updated)
                           .onFailure()
                           .invoke(error ->
                                           log.error("Error completing arrangement: {}", error.getMessage(), error));
        })
                       .map(result -> result);
    }
}
