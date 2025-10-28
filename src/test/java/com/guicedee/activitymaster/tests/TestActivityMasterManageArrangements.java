package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterManageArrangements {

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
                ent.setDescription("Enterprise for MA tests");
                return enterpriseService.createNewEnterprise(session, ent)
                       .chain(enter-> enterpriseService.startNewEnterprise(session,TestEnterprise.name(),"admin","adminadmin!@"));
              })
              .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                  .onFailure().recoverWithUni(t -> systemsService.create(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                      ISystemsService.ActivityMasterSystemName, "Activity Master System"))
              )
              .replaceWith(Uni.createFrom().voidItem());
        })
    ).await().atMost(Duration.ofMinutes(2));
  }

  private Uni<IArrangement<?, ?>> createArrangement(Mutiny.Session session, ISystems<?, ?> sys, String typeName, String value) {
    IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
    return arrangementsService.createArrangementType(session, typeName, sys)
        .chain(t -> arrangementsService.create(session, typeName,
            com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), value, sys))
        .map(a -> (IArrangement<?, ?>) a);
  }

  @Test
  public void testEvents_AddArrangement() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IEventService<?> eventService = IGuiceContext.get(IEventService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> eventService.createEventType(session, "MA_EventType_X1", sys)
              .replaceWith(eventService.createEvent(session, "MA_EventType_X1", sys))
              .chain(evt -> createArrangement(session, (ISystems<?, ?>) sys, "MA_OrderType_X1", "ARR-VAL-1")
                  .chain(arr -> ((IEvent<?, ?>) evt).addArrangement(session, (IArrangement<?, ?>) arr,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "EVT-ARR-1", (ISystems<?, ?>) sys)
                      .replaceWith(evt)))
              .chain(evt -> ((IEvent<?, ?>) evt).numberOfArrangements(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "EVT-ARR-1", (ISystems<?, ?>) sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                  .replaceWith(evt))
              .chain(evt -> ((IEvent<?, ?>) evt).hasArrangements(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "EVT-ARR-1", (ISystems<?, ?>) sys)
                  .invoke(Assertions::assertTrue)
                  .replaceWith(evt))
              .chain(evt -> ((IEvent<?, ?>) evt).findArrangementsAll(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "EVT-ARR-1", (ISystems<?, ?>) sys, true)
                  .invoke(list -> Assertions.assertEquals(1, list.size()))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testRules_AddArrangement() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> rulesService.createRules(session, "MA_RuleType_X1", "MA_Rule_Arr", "desc", (ISystems<?, ?>) sys)
              .chain(rule -> createArrangement(session, (ISystems<?, ?>) sys, "MA_OrderType_X2", "ARR-VAL-2")
                  .chain(arr -> ((IRules<?, ?>) rule).addArrangement(session, (IArrangement<?, ?>) arr,
                      com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "RUL-ARR-1", (ISystems<?, ?>) sys)
                      .replaceWith(rule)))
              .chain(rule -> ((IRules<?, ?>) rule).numberOfArrangements(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "RUL-ARR-1", (ISystems<?, ?>) sys)
                  .invoke(cnt -> Assertions.assertEquals(1L, cnt))
                  .replaceWith(rule))
              .chain(rule -> ((IRules<?, ?>) rule).hasArrangements(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "RUL-ARR-1", (ISystems<?, ?>) sys)
                  .invoke(Assertions::assertTrue)
                  .replaceWith(rule))
              .chain(rule -> ((IRules<?, ?>) rule).findArrangement(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), "RUL-ARR-1", (ISystems<?, ?>) sys, true, true)
                  .invoke(found -> Assertions.assertNotNull(found))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testPassThrough_FindArrangementsByClassification() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> rulesService.createRules(session, "MA_RuleType_PT", "MA_Rule_PT", "desc", (ISystems<?, ?>) sys)
              .chain(rule -> createArrangement(session, (ISystems<?, ?>) sys, "MA_OrderType_PT1", "ARR-PT-1")
                  .chain(arr -> ((IRules<?, ?>) rule).findArrangementsByClassification(session,
                          com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                          "ARR-PT-1",
                          (ISystems<?, ?>) sys)
                      .invoke(list -> Assertions.assertNotNull(list))
                  )
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  public void testPassThrough_FindArrangementsByClassificationComparators() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> rulesService.createRules(session, "MA_RuleType_PT2", "MA_Rule_PT2", "desc", (ISystems<?, ?>) sys)
              .chain(rule -> createArrangement(session, (ISystems<?, ?>) sys, "MA_OrderType_PT2", "10")
                  .chain(a1 -> createArrangement(session, (ISystems<?, ?>) sys, "MA_OrderType_PT2", "20")
                      .chain(a2 -> createArrangement(session, (ISystems<?, ?>) sys, "MA_OrderType_PT2", "30")
                          .replaceWith(rule)))
              )
              .chain(rule -> ((IRules<?, ?>) rule).findArrangementsByClassificationGT(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  null,
                  "15",
                  (ISystems<?, ?>) sys)
                  .invoke(list -> Assertions.assertNotNull(list))
                  .replaceWith(rule))
              .chain(rule -> ((IRules<?, ?>) rule).findArrangementsByClassificationGTE(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  null,
                  "20",
                  (ISystems<?, ?>) sys)
                  .invoke(list -> Assertions.assertNotNull(list))
                  .replaceWith(rule))
              .chain(rule -> ((IRules<?, ?>) rule).findArrangementsByClassificationLT(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  null,
                  "20",
                  (ISystems<?, ?>) sys)
                  .invoke(list -> Assertions.assertNotNull(list))
                  .replaceWith(rule))
              .chain(rule -> ((IRules<?, ?>) rule).findArrangementsByClassificationLTE(session,
                  com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                  null,
                  "20",
                  (ISystems<?, ?>) sys)
                  .invoke(list -> Assertions.assertNotNull(list))
              )
          );
    })).await().atMost(Duration.ofMinutes(2));
  }
}
