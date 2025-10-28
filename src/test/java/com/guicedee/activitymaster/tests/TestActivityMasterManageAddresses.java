package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterManageAddresses {

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void setup() {
    com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.get()
        .setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    assertNotNull(sessionFactory, "SessionFactory should not be null");

    // Ensure Enterprise and Activity Master system exist
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .onFailure().recoverWithUni(t -> {
            var ent = enterpriseService.get();
            ent.setName(TestEnterprise.name());
            ent.setDescription("Enterprise for IManageAddresses tests");
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

  private Uni<ISystems<?, ?>> getActivityMasterSystem(Mutiny.Session session) {
    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
    ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
    return enterpriseService.getEnterprise(session, TestEnterprise.name())
        .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
            .map(sys -> (ISystems<?, ?>) sys));
  }

  private Uni<Void> ensureAddressClassifications(Mutiny.Session session, ISystems<?, ?> sys) {
    IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
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
        .replaceWithVoid();
  }

  private Uni<IInvolvedParty<?, ?>> createParty(Mutiny.Session session, ISystems<?, ?> sys, String idType, String idValue) {
    IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
    return partyService.createIdentificationType(session, sys, idType, "Test Identification")
        .chain(() -> partyService.createType(session, sys, "QA_ManageParty", "Party Type"))
        .chain(() -> partyService.createNameType(session, "QA_ManageDisplayName", "Display Name", sys))
        .chain(() -> partyService.create(session, sys, new Pair<>(idType, idValue), true))
        .map(p -> (IInvolvedParty<?, ?>) p);
  }

  private Uni<IEvent<?, ?>> createEvent(Mutiny.Session session, ISystems<?, ?> sys, String eventTypeName) {
    IEventService<?> eventService = IGuiceContext.get(IEventService.class);
    return eventService.createEventType(session, eventTypeName, sys)
        .chain(t -> eventService.createEvent(session, eventTypeName, sys))
        .map(e -> (IEvent<?, ?>) e);
  }

  private Uni<IAddress<?, ?>> ensureEmailAddress(Mutiny.Session session, ISystems<?, ?> sys, String email) {
    IAddressService<?> addressService = IGuiceContext.get(IAddressService.class);
    return ensureAddressClassifications(session, sys)
        .chain(() -> addressService.addOrFindEmailContact(session, email, sys))
        .map(a -> (IAddress<?, ?>) a);
  }

  private Uni<Void> ensureRelationshipClassification(Mutiny.Session session, ISystems<?, ?> sys, String classyName) {
    IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
    return classificationService.create(session, classyName, "desc", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
        .replaceWithVoid();
  }

  @Test
  @Order(1)
  public void testParty_AddAddress_Find() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      String relClassy = "AM_AddressRel";
      String email = "addresses_ip@test.local";
      String relValue = "PRIMARY";
      return getActivityMasterSystem(session)
          .chain(sys -> ensureRelationshipClassification(session, sys, relClassy)
              .replaceWith(sys))
          .chain(sys -> ensureEmailAddress(session, sys, email)
              .map(addr -> new Object[]{sys, addr}))
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IAddress<?, ?> addr = (IAddress<?, ?>) arr[1];
            return createParty(session, sys, "ADDR_ID_1", "ADDR-PTY-1")
                .chain(party -> ((IInvolvedParty<?, ?>) party).addAddress(session, addr, relClassy, relValue, sys)
                    .replaceWith(party))
                .chain(party -> ((IInvolvedParty<?, ?>) party).findAddress(session, relClassy, relValue, sys, true, true)
                    .invoke(found -> {
                      assertNotNull(found, "findAddress should return a relationship record");
                      assertEquals(relValue, ((IRelationshipValue<?, ?, ?>) found).getValue(), "Relationship value should match");
                      assertEquals(email, ((IAddress<?, ?>) ((IRelationshipValue<?, ?, ?>) found).getSecondary()).getValue(), "Secondary address value should match email");
                    })
                    .replaceWith(party))
                .replaceWithVoid();
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(2)
  public void testParty_AddOrUpdateAddress_ChangesValue() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      String relClassy = "AM_AddressRel_Update";
      String email = "addresses_ip_update@test.local";
      String searchValue = "PRIMARY";
      String updatedValue = "SECONDARY";
      return getActivityMasterSystem(session)
          .chain(sys -> ensureRelationshipClassification(session, sys, relClassy)
              .replaceWith(sys))
          .chain(sys -> ensureEmailAddress(session, sys, email)
              .map(addr -> new Object[]{sys, addr}))
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IAddress<?, ?> addr = (IAddress<?, ?>) arr[1];
            return createParty(session, sys, "ADDR_ID_2", "ADDR-PTY-2")
                .chain(party -> ((IInvolvedParty<?, ?>) party).addOrUpdateAddress(session, addr, relClassy, searchValue, searchValue, sys)
                    .replaceWith(party))
                .chain(party -> ((IInvolvedParty<?, ?>) party).addOrUpdateAddress(session, addr, relClassy, searchValue, updatedValue, sys)
                    .replaceWith(party))
                .chain(party -> ((IInvolvedParty<?, ?>) party).findAddress(session, relClassy, updatedValue, sys, true, true)
                    .invoke(found -> {
                      assertNotNull(found, "Updated address relation should be retrievable by new value");
                      assertEquals(updatedValue, ((IRelationshipValue<?, ?, ?>) found).getValue());
                    })
                    .replaceWith(party))
                .chain(party -> ((IInvolvedParty<?, ?>) party).findAddresses(session, relClassy, updatedValue, sys, false, false)
                    .invoke(list -> Assertions.assertEquals(1, list.size(), "There should be exactly one active address relation"))
                    .replaceWith(party))
                .replaceWithVoid();
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(3)
  public void testEvent_AddOrReuseAddress_NoDuplication() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      String relClassy = "AM_AddressRel_EVT";
      String email = "addresses_evt@test.local";
      String relValue = "BILLING";
      return getActivityMasterSystem(session)
          .chain(sys -> ensureRelationshipClassification(session, sys, relClassy)
              .replaceWith(sys))
          .chain(sys -> ensureEmailAddress(session, sys, email)
              .map(addr -> new Object[]{sys, addr}))
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IAddress<?, ?> addr = (IAddress<?, ?>) arr[1];
            return createEvent(session, sys, "EVT_Type_ADDR_1")
                .chain(evt -> ((IEvent<?, ?>) evt).addOrReuseAddress(session, addr, relClassy, relValue, relValue, sys)
                    .chain(r -> ((IEvent<?, ?>) evt).addOrReuseAddress(session, addr, relClassy, relValue, relValue, sys))
                    .replaceWith(evt))
                .chain(evt -> ((IEvent<?, ?>) evt).findAddresses(session, relClassy, relValue, sys, false, false)
                    .invoke(list -> Assertions.assertEquals(1, list.size(), "There should be exactly one active event address relation"))
                    .replaceWith(evt))
                .chain(evt -> ((IEvent<?, ?>) evt).findAddress(session, relClassy, relValue, sys, true, true)
                    .invoke(found -> Assertions.assertNotNull(found))
                    .replaceWithVoid());
          });
    })).await().atMost(Duration.ofMinutes(2));
  }
}
