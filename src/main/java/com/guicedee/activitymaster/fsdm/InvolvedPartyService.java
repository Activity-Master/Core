package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.InvolvedPartyException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;

@Log4j2
public class InvolvedPartyService implements IInvolvedPartyService<InvolvedPartyService> {
    @Inject
    private IEnterprise<?, ?> enterprise;

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private Vertx vertx;

    @Override
    public IInvolvedParty<?, ?> get() {
        log.debug("Getting new InvolvedParty instance");
        return new InvolvedParty();
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByID(UUID id) {
        log.debug("Finding InvolvedParty by ID: {}", id);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new InvolvedParty().builder()
                    .find(id)
                    .get()
                    .onItem().ifNull().failWith(() -> new InvolvedPartyException("The IP does not exist - " + id))
                    .map(involvedParty -> (IInvolvedParty<?, ?>) involvedParty);
        });
    }

    @Override
    public Uni<IInvolvedPartyNameType<?, ?>> createNameType(String name, String description, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Creating InvolvedPartyNameType: name={}, description={}", name, description);

        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyNameType xr = new InvolvedPartyNameType();

            return xr.builder()
                    .withName(name)
                    .inActiveRange()
                    .inDateRange()
                    .withEnterprise(enterprise)
                    .getCount()
                    .chain(count -> {
                        if (count > 0) {
                            log.debug("InvolvedPartyNameType already exists: {}", name);
                            return findInvolvedPartyNameType(name, system, identityToken);
                        } else {
                            log.debug("Creating new InvolvedPartyNameType: {}", name);
                            xr.setName(name);
                            xr.setDescription(description);
                            xr.setSystemID(system);
                            xr.setOriginalSourceSystemID(system.getId());
                            xr.setEnterpriseID(enterprise);

                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                            return acService.getActiveFlag(enterprise)
                                    .chain(activeFlag -> {
                                        xr.setActiveFlagID(activeFlag);
                                        return xr.persist();
                                    })
                                    .chain(persisted -> {
                                        persisted.createDefaultSecurity(system, identityToken)
                                            .subscribe().with(
                                                result -> {
                                                    // Security setup completed successfully
                                                },
                                                error -> {
                                                    // Log error but don't fail the main operation
                                                    log.warn("Error in createDefaultSecurity", error);
                                                }
                                            );
                                        return Uni.createFrom().item((IInvolvedPartyNameType<?, ?>) persisted);
                                    });
                        }
                    });
        });
    }

    @Override
    public Uni<IInvolvedPartyIdentificationType<?, ?>> createIdentificationType(ISystems<?, ?> system, String name, String description, UUID... identityToken) {
        log.debug("Creating InvolvedPartyIdentificationType: name={}, description={}", name, description);

        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();

            return xr.builder()
                    .withName(name)
                    .inActiveRange()
                    .inDateRange()
                    .withEnterprise(enterprise)
                    .getCount()
                    .chain(count -> {
                        if (count > 0) {
                            log.debug("InvolvedPartyIdentificationType already exists: {}", name);
                            return findInvolvedPartyIdentificationType(name, system, identityToken);
                        } else {
                            log.debug("Creating new InvolvedPartyIdentificationType: {}", name);
                            xr.setName(name);
                            xr.setDescription(description);
                            xr.setSystemID(system);
                            xr.setOriginalSourceSystemID(system.getId());
                            xr.setEnterpriseID(enterprise);

                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                            return acService.getActiveFlag(enterprise)
                                    .chain(activeFlag -> {
                                        xr.setActiveFlagID(activeFlag);
                                        return xr.persist();
                                    })
                                    .chain(persisted -> {
                                        persisted.createDefaultSecurity(system, identityToken)
                                            .subscribe().with(
                                                result -> {
                                                    // Security setup completed successfully
                                                },
                                                error -> {
                                                    // Log error but don't fail the main operation
                                                    log.warn("Error in createDefaultSecurity", error);
                                                }
                                            );
                                        return Uni.createFrom().item((IInvolvedPartyIdentificationType<?, ?>) persisted);
                                    });
                        }
                    });
        });
    }

    @Override
    public Uni<IInvolvedPartyType<?, ?>> createType(ISystems<?, ?> system, String name, String description, UUID... identityToken) {
        log.debug("Creating InvolvedPartyType: name={}, description={}", name, description);

        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyType xr = new InvolvedPartyType();

            return xr.builder()
                    .withName(name)
                    .inActiveRange()
                    .inDateRange()
                    .withEnterprise(enterprise)
                    .getCount()
                    .chain(count -> {
                        if (count > 0) {
                            log.debug("InvolvedPartyType already exists: {}", name);
                            return findType(name, system, identityToken);
                        } else {
                            log.debug("Creating new InvolvedPartyType: {}", name);
                            xr.setName(name);
                            xr.setDescription(description);
                            xr.setSystemID(system);
                            xr.setOriginalSourceSystemID(system.getId());
                            xr.setEnterpriseID(enterprise);

                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                            return acService.getActiveFlag(enterprise)
                                    .chain(activeFlag -> {
                                        xr.setActiveFlagID(activeFlag);
                                        return xr.persist();
                                    })
                                    .chain(persisted -> {
                                        // Get activity master system
                                        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                            .chain(activityMasterSystem -> {
                                                persisted.createDefaultSecurity(activityMasterSystem, identityToken)
                                                    .subscribe().with(
                                                        result -> {
                                                            // Security setup completed successfully
                                                        },
                                                        error -> {
                                                            // Log error but don't fail the main operation
                                                            log.warn("Error in createDefaultSecurity", error);
                                                        }
                                                    );
                                                return Uni.createFrom().item((IInvolvedPartyType<?, ?>) persisted);
                                            });
                                    });
                        }
                    });
        });
    }

    public Uni<InvolvedPartyOrganicType> createOrganicType(ISystems<?, ?> system, UUID key, String name, String description, UUID... identityToken) {
        log.debug("Creating InvolvedPartyOrganicType: name={}, description={}", name, description);

        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
            xr.setId(key);
            xr.setName(name);
            xr.setDescription(description);
            xr.setSystemID(system);
            xr.setOriginalSourceSystemID(system.getId());
            xr.setEnterpriseID(enterprise);

            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
            return acService.getActiveFlag(enterprise)
                    .chain(activeFlag -> {
                        xr.setActiveFlagID(activeFlag);
                        return xr.persist();
                    })
                    .chain(persisted -> {
                        // Get activity master system
                        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                            .chain(activityMasterSystem -> {
                                persisted.createDefaultSecurity(activityMasterSystem, identityToken)
                                    .subscribe().with(
                                        result -> {
                                            // Security setup completed successfully
                                        },
                                        error -> {
                                            // Log error but don't fail the main operation
                                            log.warn("Error in createDefaultSecurity", error);
                                        }
                                    );
                                return Uni.createFrom().item(persisted);
                            });
                    });
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedPartyIdentificationType<?, ?>> findInvolvedPartyIdentificationType(String idType, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedPartyIdentificationType by name: {}", idType);
        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
            Uni<InvolvedPartyIdentificationType> result = xr.builder()
                    .withName(idType)
                    .inActiveRange()
                    .inDateRange()
                    .get()
                    .onItem().ifNull().failWith(() -> new ActivityMasterException("No Read Access or No Item Found"))
                    .onFailure().invoke(error -> log.error("Failed to find identification type: {}", idType, error));

            return result.map(item -> (IInvolvedPartyIdentificationType<?, ?>) item);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedParty<?, ?>> findByResourceItem(IResourceItem<?, ?> idType, String value, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedParty by ResourceItem: value={}", value);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new InvolvedPartyXResourceItem()
                    .builder()
                    .canRead(system, identityToken)
                    .inActiveRange()
                    .inDateRange()
                    .findLink(null, (ResourceItem) idType, value)
                    .setReturnFirst(true)
                    .get()
                    .onItem().ifNotNull().transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID())
                    .onItem().ifNull().continueWith(() -> null);
        });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> create(ISystems<?, ?> system, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken) {
        return create(system, null, idTypes, isOrganic, identityToken);
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> create(ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken) {
        log.debug("Creating InvolvedParty: key={}, idTypes={}, isOrganic={}", key, idTypes, isOrganic);

        return ReactiveTransactionUtil.withTransaction(session -> {
            final InvolvedParty ip = new InvolvedParty();
            ip.setEnterpriseID(enterprise);

            final UUID finalKey = (key == null) ? UUID.randomUUID() : key;
            ip.setId(finalKey);
            ip.setSystemID(system);
            ip.setOriginalSourceSystemID(system.getId());

            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
            return acService.getActiveFlag(enterprise)
                .chain(activeFlag -> {
                    ip.setActiveFlagID(activeFlag);
                    return ip.persist();
                })
                .chain(persisted -> {
                    // Start createDefaultSecurity in parallel without waiting for it
                    persisted.createDefaultSecurity(system, identityToken)
                        .subscribe().with(
                            result -> {
                                // Security setup completed successfully
                            },
                            error -> {
                                // Log error but don't fail the main operation
                                log.warn("Error in createDefaultSecurity", error);
                            }
                        );

                    // Find identification type
                    return findInvolvedPartyIdentificationType(idTypes.getKey(), system, identityToken)
                        .chain(involvedPartyIdentificationType -> {
                            // Add identification type to involved party
                            return Uni.createFrom().emitter(emitter -> {
                                try {
                                    persisted.addOrUpdateInvolvedPartyIdentificationType(
                                        NoClassification.toString(), 
                                        involvedPartyIdentificationType, 
                                        idTypes.getValue(), 
                                        idTypes.getValue(), 
                                        system, 
                                        identityToken
                                    );

                                    // Setup organic status if needed
                                    if (isOrganic) {
                                        setupInvolvedPartyOrganicStatus(true, persisted, system, identityToken)
                                            .subscribe().with(
                                                result -> {
                                                    // Organic status setup completed successfully
                                                },
                                                error -> {
                                                    // Log error but don't fail the main operation
                                                    log.warn("Error setting up organic status", error);
                                                }
                                            );
                                    } else {
                                        setupInvolvedPartyOrganicStatus(false, persisted, system, identityToken)
                                            .subscribe().with(
                                                result -> {
                                                    // Non-organic status setup completed successfully
                                                },
                                                error -> {
                                                    // Log error but don't fail the main operation
                                                    log.warn("Error setting up non-organic status", error);
                                                }
                                            );
                                    }

                                    emitter.complete((IInvolvedParty<?, ?>) persisted);
                                } catch (Exception e) {
                                    emitter.fail(e);
                                }
                            });
                        });
                });
        });
    }

    private Uni<Void> setupInvolvedPartyOrganicStatus(boolean isOrganic, IInvolvedParty<?, ?> ip, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Setting up InvolvedParty organic status: isOrganic={}, id={}", isOrganic, ip.getId());

        return ReactiveTransactionUtil.withTransaction(session -> {
            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

            if (isOrganic) {
                InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
                ipo.setInvolvedParty((InvolvedParty) ip);
                ipo.setId(ip.getId());
                ipo.setEnterpriseID(enterprise);
                ipo.setSystemID(system);
                ipo.setOriginalSourceSystemID(system.getId());

                return acService.getActiveFlag(enterprise)
                    .chain(activeFlag -> {
                        ipo.setActiveFlagID(activeFlag);
                        return ipo.persist();
                    })
                    .chain(persisted -> {
                        persisted.createDefaultSecurity(system, identityToken)
                            .subscribe().with(
                                result -> {
                                    // Security setup completed successfully
                                },
                                error -> {
                                    // Log error but don't fail the main operation
                                    log.warn("Error in createDefaultSecurity for organic", error);
                                }
                            );
                        return Uni.createFrom().voidItem();
                    });
            } else {
                InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
                ipo.setInvolvedParty((InvolvedParty) ip);
                ipo.setId(ip.getId());
                ipo.setEnterpriseID(enterprise);
                ipo.setSystemID(system);
                ipo.setOriginalSourceSystemID(system.getId());

                return acService.getActiveFlag(enterprise)
                    .chain(activeFlag -> {
                        ipo.setActiveFlagID(activeFlag);
                        return ipo.persist();
                    })
                    .chain(persisted -> {
                        persisted.createDefaultSecurity(system, identityToken)
                            .subscribe().with(
                                result -> {
                                    // Security setup completed successfully
                                },
                                error -> {
                                    // Log error but don't fail the main operation
                                    log.warn("Error in createDefaultSecurity for non-organic", error);
                                }
                            );
                        return Uni.createFrom().voidItem();
                    });
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedPartyType<?, ?>> findType(String nameType, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedPartyType by name: {}", nameType);
        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyType xr = new InvolvedPartyType();
            Uni<InvolvedPartyType> result = xr.builder()
                    .withName(nameType)
                    .inActiveRange()
                    .withEnterprise(enterprise)
                    .inDateRange()
                    .get();

            return result
                .onItem().ifNotNull().transform(item -> (IInvolvedPartyType<?, ?>) item)
                .onItem().ifNull().continueWith(() -> null);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedPartyNameType<?, ?>> findInvolvedPartyNameType(String nameType, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedPartyNameType by name: {}", nameType);
        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyNameType xr = new InvolvedPartyNameType();
            Uni<InvolvedPartyNameType> result = xr.builder()
                    .withName(nameType)
                    .inActiveRange()
                    .inDateRange()
                    .withEnterprise(enterprise)
                    .get();

            return result
                .onItem().ifNotNull().transform(item -> (IInvolvedPartyNameType<?, ?>) item)
                .onItem().ifNull().continueWith(() -> null);
        });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByToken(ISecurityToken<?, ?> token, UUID... identityToken) {
        log.debug("Finding InvolvedParty by token: {}", token.getSecurityToken());
        return ReactiveTransactionUtil.withTransaction(session -> {
            return findInvolvedPartyIdentificationType(IdentificationTypeUUID.toString(), ((SecurityToken) token).getSystemID(), identityToken)
                .chain(id -> {
                    InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                    return idType.builder()
                            .findLink(null, (InvolvedPartyIdentificationType) id, token.getSecurityToken())
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .canRead(((SecurityToken) token).getSystemID(), identityToken)
                            .get()
                            .onItem().ifNotNull().transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID())
                            .onItem().ifNull().continueWith(() -> null);
                });
        });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> find(UUID uuid) {
        log.debug("Finding InvolvedParty by UUID: {}", uuid);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new InvolvedParty().builder()
                    .find(uuid)
                    .get()
                    .onItem().ifNull().failWith(() -> new InvolvedPartyException("The IP does not exist - " + uuid))
                    .map(involvedParty -> (IInvolvedParty<?, ?>) involvedParty);
        });
    }

    @Override
    public Uni<IInvolvedPartyType<?, ?>> findType(UUID uuid) {
        log.debug("Finding InvolvedPartyType by UUID: {}", uuid);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new InvolvedPartyType().builder()
                    .find(uuid)
                    .get()
                    .onItem().ifNull().failWith(() -> new InvolvedPartyException("The IP Type does not exist - " + uuid))
                    .map(involvedPartyType -> (IInvolvedPartyType<?, ?>) involvedPartyType);
        });
    }

    @Override
    public Uni<IInvolvedPartyNameType<?, ?>> findNameType(UUID uuid) {
        log.debug("Finding InvolvedPartyNameType by UUID: {}", uuid);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new InvolvedPartyNameType().builder()
                    .find(uuid)
                    .get()
                    .onItem().ifNull().failWith(() -> new InvolvedPartyException("The IP Name Type does not exist - " + uuid))
                    .map(involvedPartyNameType -> (IInvolvedPartyNameType<?, ?>) involvedPartyNameType);
        });
    }

    @Override
    public Uni<IInvolvedPartyIdentificationType<?, ?>> findIdentificationType(UUID uuid) {
        log.debug("Finding InvolvedPartyIdentificationType by UUID: {}", uuid);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return new InvolvedPartyIdentificationType().builder()
                    .find(uuid)
                    .get()
                    .onItem().ifNull().failWith(() -> new InvolvedPartyException("The IP Identification Type does not exist - " + uuid))
                    .map(involvedPartyIdentificationType -> (IInvolvedPartyIdentificationType<?, ?>) involvedPartyIdentificationType);
        });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByUUID(UUID token, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedParty by UUID token: {}", token);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return findInvolvedPartyIdentificationType(IdentificationTypeUUID.toString(), system, identityToken)
                .chain(id -> {
                    InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                    return idType.builder()
                            .findLink(null, (InvolvedPartyIdentificationType) id, token.toString())
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .canRead(system, identityToken)
                            .get()
                            .onItem().ifNotNull().transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID())
                            .onItem().ifNull().continueWith(() -> null);
                });
        });
    }

    @Override
    public Uni<List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>> findAllByIdentificationType(String identificationType, String value) {
        log.debug("Finding all InvolvedParties by identification type: {}, value: {}", identificationType, value);
        return ReactiveTransactionUtil.withTransaction(session -> {
            InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder();
            builder.inDateRange()
                   .where(InvolvedPartyIdentificationType_.name, Equals, identificationType);

            InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
            if (value != null) {
                ipQb.withValue(value);
            }

            ipQb.inDateRange()
                .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
                .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER);

            return ipQb.getAll()
                .onFailure().invoke(error -> log.error("Error finding involved parties by identification type: {}", error.getMessage(), error))
                .map(list -> (List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>) (List<?>) list);
        });
    }

    @Override
    public Uni<List<IInvolvedParty<?, ?>>> findByRulesClassification(String classification, String value, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedParties by rules classification: {}, value: {}", classification, value);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return classificationService.find(classification, system, identityToken)
                .chain(classification1 -> {
                    return new InvolvedPartyXRules().builder()
                            .withClassification(classification1)
                            .withValue(value)
                            .inActiveRange()
                            .inDateRange()
                            .getAll()
                            .onFailure().invoke(error -> log.error("Error finding involved parties by rules classification: {}", error.getMessage(), error))
                            .map(list -> {
                                List<IInvolvedParty<?, ?>> result = new ArrayList<>();
                                for (InvolvedPartyXRules item : list) {
                                    result.add(item.getInvolvedPartyID());
                                }
                                return result;
                            });
                });
        });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByClassification(String classification, String value, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding InvolvedParty by classification: {}, value: {}", classification, value);
        return ReactiveTransactionUtil.withTransaction(session -> {
            return classificationService.find(classification, system, identityToken)
                .chain(classification1 -> {
                    return new InvolvedPartyXClassification().builder()
                            .withClassification(classification1)
                            .withValue(value)
                            .inActiveRange()
                            .inDateRange()
                            .get()
                            .onFailure().invoke(error -> log.error("Error finding involved party by classification: {}", error.getMessage(), error))
                            .onItem().ifNotNull().transform(item -> (IInvolvedParty<?, ?>) item.getPrimary())
                            .onItem().ifNull().continueWith(() -> null);
                });
        });
    }
}
