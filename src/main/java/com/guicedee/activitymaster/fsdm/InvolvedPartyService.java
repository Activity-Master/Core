package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
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
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;

@Log4j2
public class InvolvedPartyService implements IInvolvedPartyService<InvolvedPartyService>
{

  @Inject
  private IClassificationService<?> classificationService;

  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Vertx vertx;

  @Override
  public IInvolvedParty<?, ?> get()
  {
    log.debug("Getting new InvolvedParty instance");
    return new InvolvedParty();
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByID(Mutiny.Session session, UUID id)
  {
    log.debug("Finding InvolvedParty by ID: {}", id);
    return new InvolvedParty().builder(session)
               .find(id)
               .get()
               .onItem()
               .ifNull()
               .failWith(() -> new InvolvedPartyException("The IP does not exist - " + id))
               .map(involvedParty -> (IInvolvedParty<?, ?>) involvedParty);
  }

  @Override
  public Uni<IInvolvedPartyNameType<?, ?>> createNameType(Mutiny.Session session, String name, String description, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyNameType: name={}, description={}", name, description);
    InvolvedPartyNameType xr = new InvolvedPartyNameType();
    var enterprise = system.getEnterprise();
    return xr.builder(session)
               .withName(name)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               .getCount()
               .chain(count -> {
                 if (count > 0)
                 {
                   log.debug("InvolvedPartyNameType already exists: {}", name);
                   return findInvolvedPartyNameType(session, name, system, identityToken);
                 }
                 else
                 {
                   log.debug("Creating new InvolvedPartyNameType: {}", name);
                   xr.setName(name);
                   xr.setDescription(description);
                   xr.setSystemID(system);
                   xr.setOriginalSourceSystemID(system.getId());
                   xr.setEnterpriseID(enterprise);

                   IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                   return acService.getActiveFlag(session, enterprise)
                              .chain(activeFlag -> {
                                xr.setActiveFlagID(activeFlag);
                                return session.persist(xr)
                                           .replaceWith(Uni.createFrom()
                                                            .item(xr));
                              })
                              .chain(persisted -> {
                                persisted.createDefaultSecurity(system, identityToken)
                                    .subscribe()
                                    .with(
                                        result -> {
                                          // Security setup completed successfully
                                        },
                                        error -> {
                                          // Log error but don't fail the main operation
                                          log.warn("Error in createDefaultSecurity", error);
                                        }
                                    )
                                ;
                                return Uni.createFrom()
                                           .item((IInvolvedPartyNameType<?, ?>) persisted);
                              });
                 }
               });

  }

  @Override
  public Uni<IInvolvedPartyIdentificationType<?, ?>> createIdentificationType(Mutiny.Session session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyIdentificationType: name={}, description={}", name, description);
    InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
    var enterprise = system.getEnterprise();
    return xr.builder(session)
               .withName(name)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               .getCount()
               .chain(count -> {
                 if (count > 0)
                 {
                   log.debug("InvolvedPartyIdentificationType already exists: {}", name);
                   return findInvolvedPartyIdentificationType(session, name, system, identityToken);
                 }
                 else
                 {
                   log.debug("Creating new InvolvedPartyIdentificationType: {}", name);
                   xr.setName(name);
                   xr.setDescription(description);
                   xr.setSystemID(system);
                   xr.setOriginalSourceSystemID(system.getId());
                   xr.setEnterpriseID(enterprise);

                   IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                   return acService.getActiveFlag(session, enterprise)
                              .chain(activeFlag -> {
                                xr.setActiveFlagID(activeFlag);
                                return session.persist(xr)
                                           .replaceWith(Uni.createFrom()
                                                            .item(xr));
                              })
                              .chain(persisted -> {
                                persisted.createDefaultSecurity(system, identityToken)
                                    .subscribe()
                                    .with(
                                        result -> {
                                          // Security setup completed successfully
                                        },
                                        error -> {
                                          // Log error but don't fail the main operation
                                          log.warn("Error in createDefaultSecurity", error);
                                        }
                                    )
                                ;
                                return Uni.createFrom()
                                           .item((IInvolvedPartyIdentificationType<?, ?>) persisted);
                              });
                 }
               });
  }

  @Override
  public Uni<IInvolvedPartyType<?, ?>> createType(Mutiny.Session session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyType: name={}, description={}", name, description);

    InvolvedPartyType xr = new InvolvedPartyType();
    var enterprise = system.getEnterprise();
    return xr.builder(session)
               .withName(name)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               .getCount()
               .chain(count -> {
                 if (count > 0)
                 {
                   log.debug("InvolvedPartyType already exists: {}", name);
                   return findType(session, name, system, identityToken);
                 }
                 else
                 {
                   log.debug("Creating new InvolvedPartyType: {}", name);
                   xr.setName(name);
                   xr.setDescription(description);
                   xr.setSystemID(system);
                   xr.setOriginalSourceSystemID(system.getId());
                   xr.setEnterpriseID(enterprise);

                   IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                   return acService.getActiveFlag(session, enterprise)
                              .chain(activeFlag -> {
                                xr.setActiveFlagID(activeFlag);
                                return session.persist(xr)
                                           .replaceWith(Uni.createFrom()
                                                            .item(xr));
                              })
                              .chain(persisted -> {
                                // Get activity master system
                                return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                                           .chain(activityMasterSystem -> {
                                             persisted.createDefaultSecurity(activityMasterSystem, identityToken)
                                                 .subscribe()
                                                 .with(
                                                     result -> {
                                                       // Security setup completed successfully
                                                     },
                                                     error -> {
                                                       // Log error but don't fail the main operation
                                                       log.warn("Error in createDefaultSecurity", error);
                                                     }
                                                 )
                                             ;
                                             return Uni.createFrom()
                                                        .item((IInvolvedPartyType<?, ?>) persisted);
                                           });
                              });
                 }
               });
  }

  public Uni<InvolvedPartyOrganicType> createOrganicType(Mutiny.Session session, ISystems<?, ?> system, UUID key, String name, String description, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyOrganicType: name={}, description={}", name, description);
    var enterprise = system.getEnterprise();
    InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
    xr.setId(key);
    xr.setName(name);
    xr.setDescription(description);
    xr.setSystemID(system);
    xr.setOriginalSourceSystemID(system.getId());
    xr.setEnterpriseID(enterprise);

    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
    return acService.getActiveFlag(session, enterprise)
               .chain(activeFlag -> {
                 xr.setActiveFlagID(activeFlag);
                 return session.persist(xr)
                            .replaceWith(Uni.createFrom()
                                             .item(xr));
               })
               .chain(persisted -> {
                 // Get activity master system
                 return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                            .chain(activityMasterSystem -> {
                              persisted.createDefaultSecurity(activityMasterSystem, identityToken)
                                  .subscribe()
                                  .with(
                                      result -> {
                                        // Security setup completed successfully
                                      },
                                      error -> {
                                        // Log error but don't fail the main operation
                                        log.warn("Error in createDefaultSecurity", error);
                                      }
                                  )
                              ;
                              return Uni.createFrom()
                                         .item(persisted);
                            });
               });

  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedPartyIdentificationType<?, ?>> findInvolvedPartyIdentificationType(Mutiny.Session session, String idType, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedPartyIdentificationType by name: {}", idType);
    InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
    return (Uni) xr.builder(session)
                     .withName(idType)
                     .inActiveRange()
                     .inDateRange()
                     .get();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedParty<?, ?>> findByResourceItem(Mutiny.Session session, IResourceItem<?, ?> idType, String value, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by ResourceItem: value={}", value);
    return new InvolvedPartyXResourceItem()
               .builder(session)
               .canRead(system, identityToken)
               .inActiveRange()
               .inDateRange()
               .findLink(null, (ResourceItem) idType, value)
               .setReturnFirst(true)
               .get()
               .onItem()
               .transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID());

  }

  @Override
  public Uni<IInvolvedParty<?, ?>> create(Mutiny.Session session, ISystems<?, ?> system, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
  {
    return create(session, system, null, idTypes, isOrganic, identityToken);
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> create(Mutiny.Session session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
  {
    log.debug("Creating InvolvedParty: key={}, idTypes={}, isOrganic={}", key, idTypes, isOrganic);
    final InvolvedParty ip = new InvolvedParty();
    var enterprise = system.getEnterprise();
    ip.setEnterpriseID(enterprise);

    final UUID finalKey = (key == null) ? UUID.randomUUID() : key;
    ip.setId(finalKey);
    ip.setSystemID(system);
    ip.setOriginalSourceSystemID(system.getId());

    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
    return acService.getActiveFlag(session, enterprise)
               .chain(activeFlag -> {
                 ip.setActiveFlagID(activeFlag);
                 return session.persist(ip)
                            .replaceWith(Uni.createFrom()
                                             .item(ip));
               })
               .chain(persisted -> {
                 // Start createDefaultSecurity in parallel without waiting for it
                 persisted.createDefaultSecurity(system, identityToken)
                     .subscribe()
                     .with(
                         result -> {
                           // Security setup completed successfully
                         },
                         error -> {
                           // Log error but don't fail the main operation
                           log.warn("Error in createDefaultSecurity", error);
                         }
                     )
                 ;

                 // Find identification type
                 return findInvolvedPartyIdentificationType(session, idTypes.getKey(), system, identityToken)
                            .chain(involvedPartyIdentificationType -> {
                              // Add identification type to involved party
                              return Uni.createFrom()
                                         .emitter(emitter -> {
                                           try
                                           {
                                             persisted.addOrUpdateInvolvedPartyIdentificationType(
                                                 session, NoClassification.toString(),
                                                 involvedPartyIdentificationType,
                                                 idTypes.getValue(),
                                                 idTypes.getValue(),
                                                 system,
                                                 identityToken
                                             );

                                             // Setup organic status if needed
                                             if (isOrganic)
                                             {
                                               setupInvolvedPartyOrganicStatus(session, true, persisted, system, identityToken)
                                                   .subscribe()
                                                   .with(
                                                       result -> {
                                                         // Organic status setup completed successfully
                                                       },
                                                       error -> {
                                                         // Log error but don't fail the main operation
                                                         log.warn("Error setting up organic status", error);
                                                       }
                                                   )
                                               ;
                                             }
                                             else
                                             {
                                               setupInvolvedPartyOrganicStatus(session, false, persisted, system, identityToken)
                                                   .subscribe()
                                                   .with(
                                                       result -> {
                                                         // Non-organic status setup completed successfully
                                                       },
                                                       error -> {
                                                         // Log error but don't fail the main operation
                                                         log.warn("Error setting up non-organic status", error);
                                                       }
                                                   )
                                               ;
                                             }

                                             emitter.complete((IInvolvedParty<?, ?>) persisted);
                                           }
                                           catch (Exception e)
                                           {
                                             emitter.fail(e);
                                           }
                                         });
                            });
               });
  }

  private Uni<Void> setupInvolvedPartyOrganicStatus(Mutiny.Session session, boolean isOrganic, IInvolvedParty<?, ?> ip, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Setting up InvolvedParty organic status: isOrganic={}, id={}", isOrganic, ip.getId());
    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
    var enterprise = system.getEnterprise();
    if (isOrganic)
    {
      InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
      ipo.setInvolvedParty((InvolvedParty) ip);
      ipo.setId(ip.getId());
      ipo.setEnterpriseID(enterprise);
      ipo.setSystemID(system);
      ipo.setOriginalSourceSystemID(system.getId());

      return acService.getActiveFlag(session, enterprise)
                 .chain(activeFlag -> {
                   ipo.setActiveFlagID(activeFlag);
                   return session.persist(ipo)
                              .replaceWith(Uni.createFrom()
                                               .item(ipo));
                 })
                 .chain(persisted -> {
                   persisted.createDefaultSecurity(system, identityToken)
                       .subscribe()
                       .with(
                           result -> {
                             // Security setup completed successfully
                           },
                           error -> {
                             // Log error but don't fail the main operation
                             log.warn("Error in createDefaultSecurity for organic", error);
                           }
                       )
                   ;
                   return Uni.createFrom()
                              .voidItem();
                 });
    }
    else
    {
      InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
      ipo.setInvolvedParty((InvolvedParty) ip);
      ipo.setId(ip.getId());
      ipo.setEnterpriseID(enterprise);
      ipo.setSystemID(system);
      ipo.setOriginalSourceSystemID(system.getId());

      return acService.getActiveFlag(session, enterprise)
                 .chain(activeFlag -> {
                   ipo.setActiveFlagID(activeFlag);
                   return session.persist(ipo)
                              .replaceWith(Uni.createFrom()
                                               .item(ipo));
                 })
                 .chain(persisted -> {
                   persisted.createDefaultSecurity(system, identityToken)
                       .subscribe()
                       .with(
                           result -> {
                             // Security setup completed successfully
                           },
                           error -> {
                             // Log error but don't fail the main operation
                             log.warn("Error in createDefaultSecurity for non-organic", error);
                           }
                       )
                   ;
                   return Uni.createFrom()
                              .voidItem();
                 });
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.Session session, String nameType, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedPartyType by name: {}", nameType);
    InvolvedPartyType xr = new InvolvedPartyType();
    var enterprise = system.getEnterprise();
    Uni<InvolvedPartyType> result = xr.builder(session)
                                        .withName(nameType)
                                        .inActiveRange()
                                        .withEnterprise(enterprise)
                                        .inDateRange()
                                        .get()
        ;

    return result
               .onItem()
               .transform(item -> (IInvolvedPartyType<?, ?>) item);

  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedPartyNameType<?, ?>> findInvolvedPartyNameType(Mutiny.Session session, String nameType, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedPartyNameType by name: {}", nameType);
    var enterprise = system.getEnterprise();
    InvolvedPartyNameType xr = new InvolvedPartyNameType();
    return (Uni) xr.builder(session)
                     .withName(nameType)
                     .inActiveRange()
                     .inDateRange()
                     .withEnterprise(enterprise)
                     .get()
        ;

  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByToken(Mutiny.Session session, ISecurityToken<?, ?> token, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by token: {}", token.getSecurityToken());

    return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), ((SecurityToken) token).getSystemID(), identityToken)
               .chain(id -> {
                 InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                 return idType.builder(session)
                            .findLink(null, (InvolvedPartyIdentificationType) id, token.getSecurityToken())
                            .inActiveRange()
                            .inDateRange()
                            //.withEnterprise(enterprise)
                            .canRead(((SecurityToken) token).getSystemID(), identityToken)
                            .get()
                            .onItem()
                            .transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID());
               });
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> find(Mutiny.Session session, UUID uuid)
  {
    log.debug("Finding InvolvedParty by UUID: {}", uuid);
    return (Uni) new InvolvedParty().builder(session)
                     .find(uuid)
                     .get();

  }

  @Override
  public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.Session session, UUID uuid)
  {
    log.debug("Finding InvolvedPartyType by UUID: {}", uuid);
    return (Uni) new InvolvedPartyType().builder(session)
                     .find(uuid)
                     .get();

  }

  @Override
  public Uni<IInvolvedPartyNameType<?, ?>> findNameType(Mutiny.Session session, UUID uuid)
  {
    log.debug("Finding InvolvedPartyNameType by UUID: {}", uuid);

    return (Uni) new InvolvedPartyNameType().builder(session)
                     .find(uuid)
                     .get();
  }

  @Override
  public Uni<IInvolvedPartyIdentificationType<?, ?>> findIdentificationType(Mutiny.Session session, UUID uuid)
  {
    log.debug("Finding InvolvedPartyIdentificationType by UUID: {}", uuid);
    return (Uni) new InvolvedPartyIdentificationType().builder(session)
                     .find(uuid)
                     .get();

  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByUUID(Mutiny.Session session, UUID token, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by UUID token: {}", token);
    var enterprise = system.getEnterprise();
    return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), system, identityToken)
               .chain(id -> {
                 InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                 return idType.builder(session)
                            .findLink(null, (InvolvedPartyIdentificationType) id, token.toString())
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .canRead(system, identityToken)
                            .get()
                            .onItem()
                            .ifNotNull()
                            .transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID())
                            .onItem()
                            .ifNull()
                            .continueWith(() -> null);
               });

  }

  @Override
  public Uni<List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>> findAllByIdentificationType(Mutiny.Session session, String identificationType, String value)
  {
    log.debug("Finding all InvolvedParties by identification type: {}, value: {}", identificationType, value);
    InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder(session);
    builder.inDateRange()
        .where(InvolvedPartyIdentificationType_.name, Equals, identificationType);

    InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder(session);
    if (value != null)
    {
      ipQb.withValue(value);
    }

    ipQb.inDateRange()
        .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
        .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER)
    ;

    return ipQb.getAll()
               .onFailure()
               .invoke(error -> log.error("Error finding involved parties by identification type: {}", error.getMessage(), error))
               .map(list -> (List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>) (List<?>) list);

  }

  @Override
  public Uni<List<IInvolvedParty<?, ?>>> findByRulesClassification(Mutiny.Session session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParties by rules classification: {}, value: {}", classification, value);
    return classificationService.find(session, classification, system, identityToken)
               .chain(classification1 -> {
                 return new InvolvedPartyXRules().builder(session)
                            .withClassification(classification1)
                            .withValue(value)
                            .inActiveRange()
                            .inDateRange()
                            .getAll()
                            .onFailure()
                            .invoke(error -> log.error("Error finding involved parties by rules classification: {}", error.getMessage(), error))
                            .map(list -> {
                              List<IInvolvedParty<?, ?>> result = new ArrayList<>();
                              for (InvolvedPartyXRules item : list)
                              {
                                result.add(item.getInvolvedPartyID());
                              }
                              return result;
                            });
               });
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByClassification(Mutiny.Session session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by classification: {}, value: {}", classification, value);
    return classificationService.find(session, classification, system, identityToken)
               .chain(classification1 -> {
                 return new InvolvedPartyXClassification().builder(session)
                            .withClassification(classification1)
                            .withValue(value)
                            .inActiveRange()
                            .inDateRange()
                            .get()
                            .onFailure()
                            .invoke(error -> log.error("Error finding involved party by classification: {}", error.getMessage(), error))
                            .onItem()
                            .ifNotNull()
                            .transform(item -> (IInvolvedParty<?, ?>) item.getPrimary())
                            .onItem()
                            .ifNull()
                            .continueWith(() -> null);
               });

  }
}
