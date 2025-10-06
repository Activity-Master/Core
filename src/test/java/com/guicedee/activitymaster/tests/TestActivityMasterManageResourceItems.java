package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterManageResourceItems {

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void setup() {
    // Ensure Guice is initialized and SessionFactory is available
    com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.get()
        .setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    assertNotNull(sessionFactory, "SessionFactory should not be null");

    // Ensure Enterprise and Activity Master system exist
    sessionFactory.withSession(session ->
        session.withTransaction(tx -> {
          IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
          ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
          return enterpriseService.getEnterprise(session, TestEnterprise.name())
              .onFailure().recoverWithUni(t -> {
                var ent = enterpriseService.get();
                ent.setName(TestEnterprise.name());
                ent.setDescription("Enterprise for MRI tests");
                return enterpriseService.createNewEnterprise(session, ent);
              })
              .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                  .onFailure().recoverWithUni(t -> systemsService.create(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                      ISystemsService.ActivityMasterSystemName, "Activity Master System"))
              )
              .replaceWith(Uni.createFrom().voidItem());
        })
    ).await().atMost(Duration.ofMinutes(2));
  }

  private Uni<IResourceItem<?, ?>> createResource(Mutiny.Session session, ISystems<?, ?> sys, String typeName, String value) {
    IResourceItemService<?> resourceService = IGuiceContext.get(IResourceItemService.class);
    return resourceService.createType(session, typeName, typeName, sys)
        .chain(t -> resourceService.create(session, typeName, value, sys))
        .map(ri -> (IResourceItem<?, ?>) ri);
  }

  @Test
  public void testClassification_AddResourceItem() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> {
            String classyName = "MRI_Classy_Classification_MRI";
            return classificationService.create(session, classyName, "MRI", com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, (ISystems<?, ?>) sys)
                .chain(classy -> createResource(session, (ISystems<?, ?>) sys, "FileType_MRI", "res-classy-mri")
                    .chain(res -> ((IClassification<?, ?>) classy).addResourceItem(session,
                        com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "C-VAL-1", (ISystems<?, ?>) sys)
                        .invoke(rel -> Assertions.assertEquals("C-VAL-1", ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) rel).getValue()))
                    )
                );
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testArrangements_AddResourceItem() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> arrangementsService.createArrangementType(session, "OrderType_X1", sys)
              .replaceWith(arrangementsService.create(session, "OrderType_X1",
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "ARR-VAL-1", sys))
              .chain(arr -> createResource(session, sys, "FileType_MRI", "res-arr-mri")
                  .chain(res -> ((IArrangement<?, ?>) arr).addResourceItem(session,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "A-VAL-1", sys)
                      .replaceWith(arr)))
              .chain(arr -> ((IArrangement<?, ?>) arr).numberOfResourceItems(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "A-VAL-1", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testEvents_AddResourceItem() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IEventService<?> eventService = IGuiceContext.get(IEventService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> eventService.createEventType(session, "EventType_X1", sys)
              .replaceWith(eventService.createEvent(session, "EventType_X1", sys))
              .chain(evt -> createResource(session, sys, "FileType_MRI", "res-evt-mri")
                  .chain(res -> ((IEvent<?, ?>) evt).addResourceItem(session,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "E-VAL-1", sys)
                      .replaceWith(evt)))
              .chain(evt -> ((IEvent<?, ?>) evt).numberOfResourceItems(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "E-VAL-1", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testProducts_AddResourceItem() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IProductService<?> productService = IGuiceContext.get(IProductService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> productService.createProductType(session, "GadgetType_X1", "Gadget Type", sys)
              .chain(pt -> productService.createProduct(session, "GadgetType_X1", "Widget_X1", "Widget Desc", "WID-001", sys))
              .chain(prod -> createResource(session, sys, "FileType_MRI", "res-prod-mri")
                  .chain(res -> ((IProduct<?, ?>) prod).addResourceItem(session,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "P-VAL-1", sys)
                      .replaceWith(prod)))
              .chain(prod -> ((IProduct<?, ?>) prod).numberOfResourceItems(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "P-VAL-1", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testRules_AddResourceItem() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> rulesService.createRules(session, "RuleType_X1", "MRI_Rule_RI", "desc", sys)
              .chain(rule -> createResource(session, sys, "FileType_MRI", "res-rule-mri")
                  .chain(res -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules<?, ?>) rule).addResourceItem(session,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "R-VAL-1", sys)
                      .replaceWith(rule)))
              .chain(rule -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules<?, ?>) rule).numberOfResourceItems(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "R-VAL-1", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testParty_AddResourceItem() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> {
            String idType = "NationalID";
            String idValue = "MRI-PTY-ID";
            return partyService.createIdentificationType(session, sys, idType, "National Identification")
                .chain(() -> partyService.createType(session, sys, "Person", "Person Type"))
                .chain(() -> partyService.create(session, sys, new com.guicedee.guicedinjection.pairing.Pair<>(idType, idValue), true))
                .chain(party -> createResource(session, sys, "FileType_MRI", "res-pty-mri")
                    .chain(res -> ((IInvolvedParty<?, ?>) party).addResourceItem(session,
                        com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "IP-VAL-1", sys)
                        .replaceWith(party)))
                .chain(party -> ((IInvolvedParty<?, ?>) party).numberOfResourceItems(session,
                    com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "IP-VAL-1", sys)
                    .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                );
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testAddress_AddResourceItem() {
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
                .chain(() -> addressService.addOrFindEmailContact(session, "mri_address_ri@test.local", (ISystems<?, ?>) sys))
                .chain(addr -> createResource(session, sys, "FileType_MRI", "res-addr-mri")
                    .chain(res -> ((IAddress<?, ?>) addr).addResourceItem(session,
                        com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), res, "A-VAL-1", sys)
                        .invoke(rel -> Assertions.assertEquals("A-VAL-1", ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) rel).getValue()))
                        .replaceWith(addr))
                );
          });
    })).await().atMost(Duration.ofMinutes(2));
  }
}
