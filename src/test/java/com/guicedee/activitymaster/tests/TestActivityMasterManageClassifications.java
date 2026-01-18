package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.LogUtils;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import org.apache.logging.log4j.Level;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterManageClassifications {

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void setup() {
      LogUtils.addConsoleLogger(Level.INFO);

    com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.get()
        .setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    assertNotNull(sessionFactory, "SessionFactory should not be null");

    // Ensure Enterprise and Activity Master system exist for this class context
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .onFailure().recoverWithUni(t -> {
            var ent = enterpriseService.get();
            ent.setName(TestEnterprise.name());
            ent.setDescription("Enterprise for MC tests");
            return enterpriseService.createNewEnterprise(session, ent)
                       .chain(enter-> enterpriseService.startNewEnterprise(session,TestEnterprise.name(),"admin","adminadmin!@"));
          })
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
              .onFailure().recoverWithUni(t -> systemsService.create(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                  ISystemsService.ActivityMasterSystemName, "Activity Master System"))
          )
          .replaceWith(Uni.createFrom().voidItem());
    })).await().atMost(Duration.ofMinutes(2));
  }

  private Uni<Void> ensureClassification(Mutiny.Session session, ISystems<?, ?> sys, String name) {
    IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
    return classificationService.create(session, name, "mc", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
        .replaceWithVoid();
  }

  @Test
  public void testArrangements_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
      IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> arrangementsService.createArrangementType(session, "MC_OrderType_X1", sys)
              .chain(type -> arrangementsService.create(session, "MC_OrderType_X1",
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "ARR-CL-1", sys))
              .chain(arr -> {
                String classyName = "MC_Classy_Arrangements_1";
                String value = "ARR-VAL-CL-1";
                return ensureClassification(session, sys, classyName)
                    .chain(() -> ((IArrangement<?, ?>) arr).addClassification(session, classyName, value, sys)
                        .replaceWith(arr))
                    .chain(a -> ((IArrangement<?, ?>) a).numberOfClassifications(session, classyName, value, sys)
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                        .replaceWith(a))
                    .chain(a -> ((IArrangement<?, ?>) a).findClassification(session, classyName, sys)
                        .invoke(found -> {
                          Assertions.assertNotNull(found);
                          Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                        })
                        .replaceWith(a));
              })
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testEvents_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IEventService<?> eventService = IGuiceContext.get(IEventService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> eventService.createEventType(session, "MC_EventType_X1", sys)
              .replaceWith(eventService.createEvent(session, "MC_EventType_X1", sys))
              .chain(evt -> {
                String classyName = "MC_Classy_Events_1";
                String value = "EVT-VAL-CL-1";
                return ensureClassification(session, sys, classyName)
                    .chain(() -> ((IEvent<?, ?>) evt).addClassification(session, classyName, value, sys)
                        .replaceWith(evt))
                    .chain(e -> ((IEvent<?, ?>) e).numberOfClassifications(session, classyName, value, sys)
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                        .replaceWith(e))
                    .chain(e -> ((IEvent<?, ?>) e).findClassification(session, classyName, sys)
                        .invoke(found -> {
                          Assertions.assertNotNull(found);
                          Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                        })
                        .replaceWith(e));
              })
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testProducts_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IProductService<?> productService = IGuiceContext.get(IProductService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> productService.createProductType(session, "MC_GadgetType_X1", "Gadget Type", sys)
              .chain(pt -> productService.createProduct(session, "MC_GadgetType_X1", "MC_Widget_X1", "Widget Desc", "WID-CL-001", sys))
              .chain(prod -> {
                String classyName = "MC_Classy_Product_1";
                String value = "PROD-VAL-CL-1";
                return ensureClassification(session, sys, classyName)
                    .chain(() -> ((IProduct<?, ?>) prod).addClassification(session, classyName, value, sys)
                        .replaceWith(prod))
                    .chain(p -> ((IProduct<?, ?>) p).numberOfClassifications(session, classyName, value, sys)
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                        .replaceWith(p))
                    .chain(p -> ((IProduct<?, ?>) p).findClassification(session, classyName, sys)
                        .invoke(found -> {
                          Assertions.assertNotNull(found);
                          Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                        })
                        .replaceWith(p));
              })
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testParty_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> {
            String idType = "MC_NationalID";
            String idValue = "MC-PTY-ID-CL";
            return partyService.createIdentificationType(session, sys, idType, "National Identification")
                .chain(() -> partyService.createType(session, sys, "Person", "Person Type"))
                .chain(() -> partyService.create(session, sys, new Pair<>(idType, idValue), true))
                .chain(party -> {
                  String classyName = "MC_Classy_Party_1";
                  String value = "IP-VAL-CL-1";
                  return ensureClassification(session, sys, classyName)
                      .chain(() -> ((IInvolvedParty<?, ?>) party).addClassification(session, classyName, value, sys)
                          .replaceWith(party))
                      .chain(p -> ((IInvolvedParty<?, ?>) p).numberOfClassifications(session, classyName, value, sys)
                          .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                          .replaceWith(p))
                      .chain(p -> ((IInvolvedParty<?, ?>) p).findClassification(session, classyName, sys)
                          .invoke(found -> {
                            Assertions.assertNotNull(found);
                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                          })
                          .replaceWith(p));
                });
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testRules_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> rulesService.createRules(session, "MC_RuleType_X1", "MC_Rule_CL", "desc", sys)
              .chain(rule -> {
                String classyName = "MC_Classy_Rules_1";
                String value = "R-VAL-CL-1";
                return ensureClassification(session, sys, classyName)
                    .chain(() -> ((IRules<?, ?>) rule).addClassification(session, classyName, value, sys)
                        .replaceWith(rule))
                    .chain(r -> ((IRules<?, ?>) r).numberOfClassifications(session, classyName, value, sys)
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                        .replaceWith(r))
                    .chain(r -> ((IRules<?, ?>) r).findClassification(session, classyName, sys)
                        .invoke(found -> {
                          Assertions.assertNotNull(found);
                          Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                        })
                        .replaceWith(r));
              })
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testAddress_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IAddressService<?> addressService = IGuiceContext.get(IAddressService.class);
      IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> {
            // Ensure minimal Address classifications exist (idempotent)
            return classificationService.create(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.Address,
                    (ISystems<?, ?>) sys)
                .chain(() -> classificationService.create(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.ContactAddress,
                    (ISystems<?, ?>) sys,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.Address))
                .chain(() -> classificationService.create(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress,
                    (ISystems<?, ?>) sys,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.ContactAddress))
                .chain(() -> classificationService.create(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddressHost,
                    (ISystems<?, ?>) sys,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress))
                .chain(() -> classificationService.create(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddressDomain,
                    (ISystems<?, ?>) sys,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress))
                .chain(() -> classificationService.create(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddressUser,
                    (ISystems<?, ?>) sys,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress))
                .chain(() -> addressService.addOrFindEmailContact(session, "mc_address_cl@test.local", (ISystems<?, ?>) sys))
                .chain(addr -> {
                  String classyName = "MC_Classy_Address_1";
                  String value = "ADDR-VAL-CL-1";
                  return ensureClassification(session, sys, classyName)
                      .chain(() -> ((IAddress<?, ?>) addr).addClassification(session, classyName, value, sys)
                          .replaceWith(addr))
                      .chain(a -> ((IAddress<?, ?>) a).numberOfClassifications(session, classyName, value, sys)
                          .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                          .replaceWith(a))
                      .chain(a -> ((IAddress<?, ?>) a).findClassification(session, classyName, sys)
                          .invoke(found -> {
                            Assertions.assertNotNull(found);
                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                          })
                          .replaceWith(a));
                });
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testActiveFlag_AddClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);
      IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
              .chain(sys -> activeFlagService.getActiveFlag(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                  .chain(active -> {
                    String classyName = "MC_Classy_ActiveFlag_1";
                    String value = "AF-VAL-CL-1";
                    return classificationService.create(session, classyName, "desc", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
                        .chain(() -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active).addClassification(session, classyName, value, sys)
                            .replaceWith(active))
                        .chain(a -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) a).numberOfClassifications(session, classyName, value, sys)
                            .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                            .replaceWith(a))
                        .chain(a -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) a).findClassification(session, classyName, sys)
                            .invoke(found -> {
                              Assertions.assertNotNull(found);
                              Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                            })
                            .replaceWith(a));
                  })
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }
  
  @Test
  public void testClassification_CreateWithParent_AddsChildLink() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> {
            String parentName = "MC_ClassParent_1";
            String childName = "MC_ClassChild_1";
            return classificationService.create(session, parentName, "descP", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
                .chain(parent -> classificationService.create(session, childName, "descC", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys, 1, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) parent)
                    .replaceWith(parent))
                .chain(parent -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) parent)
                    .findChildren(session, com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), null, sys)
                    .invoke(children -> {
                      Assertions.assertNotNull(children, "Children list should not be null");
                      boolean found = children.stream().anyMatch(rv -> childName.equals(rv.getSecondary().getName()));
                      Assertions.assertTrue(found, "Expected to find child classification linked to parent");
                    })
                    .replaceWith(parent))
                .replaceWithVoid();
          });
    })).await().atMost(java.time.Duration.ofMinutes(2));
  }
}
