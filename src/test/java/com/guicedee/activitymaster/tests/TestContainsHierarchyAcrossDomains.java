package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestContainsHierarchyAcrossDomains extends TestDatabaseSetup {

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void setup() {
    com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.get()
        .setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    assertNotNull(sessionFactory, "SessionFactory should not be null");

    // Ensure Enterprise and System exist
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .onFailure().recoverWithUni(t -> {
            var ent = enterpriseService.get();
            ent.setName(TestEnterprise.name());
            ent.setDescription("Enterprise for Hierarchy tests");
            return enterpriseService.createNewEnterprise(session, ent)
                       .chain(enter-> enterpriseService.startNewEnterprise(session,TestEnterprise.name(),"admin","adminadmin!@"));
          })
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
              .onFailure().recoverWithUni(t -> systemsService.create(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                  ISystemsService.ActivityMasterSystemName, "Activity Master System")))
          .replaceWith(Uni.createFrom().voidItem());
    })).await().atMost(Duration.ofMinutes(2));
  }

  private Uni<Void> ensureHierarchyClassification(Mutiny.Session session, ISystems<?, ?> sys) {
    return Uni.createFrom()
               .voidItem();

    //NO!
   /* IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
    return classificationService.create(session, DefaultClassifications.HierarchyTypeClassification, (ISystems<?, ?>) sys)
        .replaceWithVoid();*/
  }

  @Test
  @Order(1)
  public void testArrangementHierarchy_AddFindArchive() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);

      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> arrangementsService.createArrangementType(session, "CH_OrderType", sys)
              .chain(type -> arrangementsService.create(session, "CH_OrderType",null, "NoClassification", "ARR-H-01", sys))
              .chain(parent -> arrangementsService.create(session, "CH_OrderType", null,"NoClassification", "ARR-H-02", sys)
                  .map(child -> new Object[]{sys, parent, child}))
          )
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IArrangement<?, ?> parent = (IArrangement<?, ?>) arr[1];
            IArrangement<?, ?> child = (IArrangement<?, ?>) arr[2];
            String hierarchyValue = "is-child-of";

            return ensureHierarchyClassification(session, sys)
                            .chain(() -> ((IArrangement<?, ?>) parent).addChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(p -> ((IArrangement<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(foundParent -> assertEquals(parent, foundParent))
                .chain(v -> ((IArrangement<?, ?>) parent).findChildren(session, (String) null, hierarchyValue, sys))
                .invoke(children -> assertTrue(children.stream().anyMatch(rv -> child.equals(rv.getSecondary()))))
                .chain(v -> ((IArrangement<?, ?>) parent).archiveChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(v -> ((IArrangement<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(afterArchive -> assertNull(afterArchive))
                .replaceWith(Uni.createFrom().voidItem());
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(2)
  public void testEventHierarchy_AddFindArchive() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IEventService<?> eventService = IGuiceContext.get(IEventService.class);

      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> eventService.createEventType(session, "CH_EventType", sys)
              .replaceWith(eventService.createEvent(session, "CH_EventType", sys))
              .chain(parent -> eventService.createEvent(session, "CH_EventType", sys)
                  .map(child -> new Object[]{sys, parent, child}))
          )
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IEvent<?, ?> parent = (IEvent<?, ?>) arr[1];
            IEvent<?, ?> child = (IEvent<?, ?>) arr[2];
            String hierarchyValue = "causes";

            return ensureHierarchyClassification(session, sys)
                            .chain(() -> ((IEvent<?, ?>) parent).addChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(p -> ((IEvent<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(foundParent -> assertEquals(parent, foundParent))
                .chain(v -> ((IEvent<?, ?>) parent).findChildren(session, (String) null, hierarchyValue, sys))
                .invoke(children -> assertTrue(children.stream().anyMatch(rv -> child.equals(rv.getSecondary()))))
                .chain(v -> ((IEvent<?, ?>) parent).archiveChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(v -> ((IEvent<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(afterArchive -> assertNull(afterArchive))
                .replaceWith(Uni.createFrom().voidItem());
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(3)
  public void testRulesHierarchy_AddFindArchive() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);

      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> rulesService.createRules(session, "CH_RuleType", "CH_Rule_A", "desc A", sys)
              .chain(parent -> rulesService.createRules(session, "CH_RuleType", "CH_Rule_B", "desc B", sys)
                  .map(child -> new Object[]{sys, parent, child}))
          )
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IRules<?, ?> parent = (IRules<?, ?>) arr[1];
            IRules<?, ?> child = (IRules<?, ?>) arr[2];
            String hierarchyValue = "depends-on";

            return ensureHierarchyClassification(session, sys)
                            .chain(() -> ((IRules<?, ?>) parent).addChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(p -> ((IRules<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(foundParent -> assertEquals(parent, foundParent))
                .chain(v -> ((IRules<?, ?>) parent).findChildren(session, (String) null, hierarchyValue, sys))
                .invoke(children -> assertTrue(children.stream().anyMatch(rv -> child.equals(rv.getSecondary()))))
                .chain(v -> ((IRules<?, ?>) parent).archiveChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(v -> ((IRules<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(afterArchive -> assertNull(afterArchive))
                .replaceWith(Uni.createFrom().voidItem());
          });
    })).await().atMost(Duration.ofMinutes(2));
  }

  @Test
  @Order(4)
  public void testResourceItemHierarchy_AddFindArchive() {
    sessionFactory.withSession(session -> session.withTransaction(tx -> {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
      IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);

      return enterpriseService.getEnterprise(session, TestEnterprise.name())
          .chain(ent -> systemsService.getActivityMaster(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
          .chain(sys -> resourceItemService.createType(session, "CH_ResType", "desc", sys)
              .chain(t -> resourceItemService.create(session, "CH_ResType", "RES-A", sys))
              .chain(parent -> resourceItemService.create(session, "CH_ResType", "RES-B", sys)
                  .map(child -> new Object[]{sys, parent, child}))
          )
          .chain(arr -> {
            ISystems<?, ?> sys = (ISystems<?, ?>) arr[0];
            IResourceItem<?, ?> parent = (IResourceItem<?, ?>) arr[1];
            IResourceItem<?, ?> child = (IResourceItem<?, ?>) arr[2];
            String hierarchyValue = "contains";

            return ensureHierarchyClassification(session, sys)
                .chain(() -> ((IResourceItem<?, ?>) parent).addChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(p -> ((IResourceItem<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(foundParent -> assertEquals(parent, foundParent))
                .chain(v -> ((IResourceItem<?, ?>) parent).findChildren(session, (String) null, hierarchyValue, sys))
                .invoke(children -> assertTrue(children.stream().anyMatch(rv -> child.equals(rv.getSecondary()))))
                .chain(v -> ((IResourceItem<?, ?>) parent).archiveChild(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable<?, ?, ? extends java.io.Serializable, ?>) child, null, hierarchyValue, sys))
                .chain(v -> ((IResourceItem<?, ?>) child).findParent(session, hierarchyValue, null, sys))
                .invoke(afterArchive -> assertNull(afterArchive))
                .replaceWith(Uni.createFrom().voidItem());
          });
    })).await().atMost(Duration.ofMinutes(2));
  }
}
