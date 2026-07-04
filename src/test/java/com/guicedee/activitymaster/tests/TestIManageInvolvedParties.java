package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestIManageInvolvedParties {

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void setup() {
    ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    assertNotNull(sessionFactory, "SessionFactory should not be null");

    // Provision the enterprise on the stateless pipeline (no bridge). createNewEnterprise creates the record
    // + installs/registers every system via the stateless registerSystem path; startNewEnterprise then seeds
    // the admin + post-startups. Idempotent: create only when absent, always start.
    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
    sessionFactory.openStatelessSession()
        .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
            .onFailure().recoverWithUni(t -> {
              var ent = enterpriseService.get();
              ent.setName(TestEnterprise.name());
              ent.setDescription("Enterprise for IManageInvolvedParties tests");
              return enterpriseService.createNewEnterprise(ss, ent);
            })
            .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "adminadmin!@"))
            .onFailure().recoverWithItem(e -> null)
            .eventually(ss::close))
        .await().atMost(Duration.ofMinutes(2));
  }

  private Uni<ISystems<?, ?>> getActivityMasterSystem(Mutiny.Session session) {
    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
    ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
    return enterpriseService.getEnterprise(session, TestEnterprise.name())
        .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
            .map(sys -> (ISystems<?, ?>) sys));
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

  @Test
  @Order(1)
  public void testAddInvolvedParty_CountHasFind() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      return getActivityMasterSystem(session)
          .chain(sys -> createEvent(session, sys, "EVT_Type_IMP_1")
              .chain(evt -> createParty(session, sys, "IMP_NationalID_1", "IMP-VAL-1")
                  .chain(party -> ((IEvent<?, ?>) evt).addInvolvedParty(session, (IInvolvedParty<?, ?>) party,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                      "IP-REL-1", sys)
                      .replaceWith(evt))
              )
              .chain(evt -> ((IEvent<?, ?>) evt).numberOfInvolvedPartys(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-1", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                  .replaceWith(evt))
              .chain(evt -> ((IEvent<?, ?>) evt).hasInvolvedPartys(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-1", sys)
                  .invoke(Assertions::assertTrue)
                  .replaceWith(evt))
              .chain(evt -> ((IEvent<?, ?>) evt).findInvolvedPartysAll(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-1", sys, true)
                  .invoke(list -> Assertions.assertEquals(1, list.size()))
                  .replaceWith(evt))
              .chain(evt -> ((IEvent<?, ?>) evt).findInvolvedParty(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-1", sys, true, true)
                  .invoke(Assertions::assertNotNull)
                  .replaceWith(Uni.createFrom().voidItem()))
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(2)
  public void testAddOrUpdateInvolvedParty_UpdatesValue() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      return getActivityMasterSystem(session)
          .chain(sys -> createEvent(session, sys, "EVT_Type_IMP_2")
              .chain(evt -> createParty(session, sys, "IMP_NationalID_2", "IMP-VAL-2")
                  .chain(party -> ((IEvent<?, ?>) evt).addOrUpdateInvolvedParty(session,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                      (IInvolvedParty<?, ?>) party,
                      "IP-REL-2", "IP-REL-2", sys)
                      .chain(rel -> ((IEvent<?, ?>) evt).addOrUpdateInvolvedParty(session,
                          com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                          (IInvolvedParty<?, ?>) party,
                          "IP-REL-2", "IP-REL-2-UPDATED", sys))
                      .replaceWith(evt))
              )
              .chain(evt -> ((IEvent<?, ?>) evt).findInvolvedParty(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-2-UPDATED", sys, true, true)
                  .invoke(found -> {
                    Assertions.assertNotNull(found);
                    Assertions.assertEquals("IP-REL-2-UPDATED", ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue());
                  })
                  .replaceWith(evt))
              .chain(evt -> ((IEvent<?, ?>) evt).numberOfInvolvedPartys(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-2-UPDATED", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt)))
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(3)
  public void testAddOrReuseInvolvedParty_NoDuplication() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      return getActivityMasterSystem(session)
          .chain(sys -> createEvent(session, sys, "EVT_Type_IMP_3")
              .chain(evt -> createParty(session, sys, "IMP_NationalID_3", "IMP-VAL-3")
                  .chain(party -> ((IEvent<?, ?>) evt).addOrReuseInvolvedParty(session,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                      (IInvolvedParty<?, ?>) party, "IP-REL-3", sys)
                      .chain(rel -> ((IEvent<?, ?>) evt).addOrReuseInvolvedParty(session,
                          com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                          (IInvolvedParty<?, ?>) party, "IP-REL-3", sys))
                      .replaceWith(evt))
              )
              .chain(evt -> ((IEvent<?, ?>) evt).numberOfInvolvedPartys(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  "IP-REL-3", sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt)))
          );
    })).await().atMost(Duration.ofMinutes(2));
  }
}
