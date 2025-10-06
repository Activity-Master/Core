package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.entityassist.enumerations.ActiveFlag;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
public class TestActivityMaster extends TestDatabaseSetup
{

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void setup()
  {
    // Initialize the Guice context
    //IGuiceContext.registerModule(new PostgreSQLTestDBModule());
    ActivityMasterConfiguration.get()
        .setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();

    log.info("Loading DB Configuration / PersistService from Guice");
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    assertNotNull(sessionFactory, "SessionFactory should not be null");
  }


  @AfterAll
  public void afterAll()
  {
    // JtaPersistService ps = (JtaPersistService) IGuiceContext.get(Key.get(PersistService.class, Names.named("ActivityMaster-Test")));
    //ps.stop();
  }


  @Test
  public void testPostgreSQLConnects()
  {
    var result =
        sessionFactory.withSession(session -> {
              // Persist the entity
              return session.withTransaction(tx -> {
                log.info("Session: " + session);
                return Uni.createFrom()
                           .voidItem();
              });
            })
            .await()
            .atMost(Duration.of(50L, ChronoUnit.SECONDS))
        ;
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  public class TestEnterprise
  {

    @Test
    @Order(1)
    public void testEnterpriseInstallation()
    {
      sessionFactory.withSession(session -> {
            // Persist the entity
            return session.withTransaction(tx -> {

                  IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                  var enterprise = enterpriseService.get();
                  enterprise.setName(TestEnterprise.name());
                  enterprise.setDescription("Enterprise Entity for Testing");

                  return enterpriseService.createNewEnterprise(session, enterprise);
                })
                       .chain(result -> {
                         System.out.println("Enterprise Created - " + result.getName() + " / " + result.getId());
                         return Uni.createFrom()
                                    .item(result);
                       });
          })
          .await()
          .atMost(Duration.of(2, ChronoUnit.MINUTES))
      ;

    }


    @Test
    @Order(2)
    public void testLoadTime()
    {
      ITimeSystem timeService = IGuiceContext.get(ITimeSystem.class);
      timeService.loadTimeRange(2025, 2025)
          .await()
          .atMost(Duration.ofMinutes(1))
      ;

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class EnterpriseSetup
    {
      @Test
      @Order(1)
      public void testEnterpriseUpdates()
      {
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        var updates = sessionFactory.withTransaction(session -> {
          return enterpriseService.getEnterprise(session, TestEnterprise.name())
                     .chain(enterprise -> {
                       return enterpriseService.loadUpdates(session, enterprise)
                                  .onFailure()
                                  .invoke(a -> log.fatal("Cannot load updates", a));
                     });
        });
        updates.onFailure()
            .invoke(a -> log.fatal("Cannot load updates", a));
        updates.onItem()
            .invoke(a -> log.info("loaded updates"));
        updates.await()
            .atMost(Duration.ofMinutes(1))
        ;
      }

      @Test
      @Order(2)
      public void testStartNewEnterprise()
      {
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        var updates = sessionFactory.withTransaction(session -> {
          return enterpriseService.getEnterprise(session, TestEnterprise.name())
                     .chain(enterprise -> {
                       return enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                                  .onFailure()
                                  .invoke(a -> log.fatal("Cannot load updates", a));
                     });
        });
        updates.onFailure()
            .invoke(a -> log.fatal("Cannot load updates", a));
        updates.onItem()
            .invoke(a -> log.info("loaded updates"));
        updates.await()
            .atMost(Duration.ofMinutes(1))
        ;
      }


      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class ClassificationDataConcept
      {
        @Test
        @Order(1)
        public void testClassificationDataConceptFindAfterStartup()
        {
          // This test intentionally runs AFTER EnterpriseSetup.testStartNewEnterprise() to rely on startup-created defaults
          var foundConcept = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationDataConceptService<?> conceptService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {

                         // After startup, the Global concept should exist; verify via interface-only API
                         return conceptService.find(session,
                                 com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName,
                                 sys)
                                    .invoke(foundObj -> {
                                      com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?> found =
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) foundObj;
                                      assertNotNull(found, "Found classification data concept should not be null");
                                      log.info("Found concept id={} name={}", found.getId(), found.getName());
                                    });
                       });
          });

          foundConcept.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateFindIdempotentByString()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationDataConceptService<?> conceptService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.class);
            com.guicedee.activitymaster.fsdm.ClassificationsDataConceptService concreteService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.ClassificationsDataConceptService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           // Choose a concept enum; creation is idempotent and startup may already have created it.
                                           var conceptEnum = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem;
                                           String conceptName = conceptEnum.classificationValue();

                                           // First create (idempotent if exists)
                                           return conceptService.createDataConcept(session, conceptEnum, "Test ensure idempotent create", sys)
                                                      // Then find by STRING to ensure it is there
                                                      .chain(created -> concreteService.find(session, conceptName, sys)
                                                                            .invoke(found -> {
                                                                              assertNotNull(found, "After create, find-by-string should return the concept");
                                                                              log.info("Verified find-by-string for concept='{}'", conceptName);
                                                                            })
                                                                            // Create again to test idempotency
                                                                            .chain(found1 -> conceptService.createDataConcept(session, conceptEnum, "Second create should not duplicate", sys)
                                                                                                 .invoke(createdAgain -> {
                                                                                                   assertNotNull(createdAgain, "Second create should return an entity");
                                                                                                   var id1 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) found1).getId();
                                                                                                   var id2 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) createdAgain).getId();
                                                                                                   Assertions.assertEquals(id1, id2, "Idempotent create should return the same entity ID (no duplicates)");
                                                                                                   log.info("Idempotent create verified for concept='{}' id={}", conceptName, id1);
                                                                                                 })
                                                                            )
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testGetGlobalNoAndSecurityConceptsViaInterface()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationDataConceptService<?> conceptService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> conceptService.getGlobalConcept(session, sys)
                                         .invoke(global -> {
                                           assertNotNull(global, "Global Classification Data Concept should exist after startup");
                                           log.info("GlobalConcept id={} name={}",
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) global).getId(),
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) global).getName());
                                         })
                                         .chain(ignored -> conceptService.getNoConcept(session, sys)
                                                               .invoke(noConcept -> {
                                                                 assertNotNull(noConcept, "NoConcept should exist after startup");
                                                                 log.info("NoConcept id={} name={}",
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) noConcept).getId(),
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) noConcept).getName());
                                                               })
                                         )
                                         .chain(ignored2 -> conceptService.getSecurityHierarchyConcept(session, sys)
                                                                .invoke(sec -> {
                                                                  assertNotNull(sec, "SecurityHierarchy concept should exist after startup");
                                                                  log.info("SecurityHierarchyConcept id={} name={}",
                                                                      ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) sec).getId(),
                                                                      ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) sec).getName());
                                                                })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testFindByEnumViaInterface()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationDataConceptService<?> conceptService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> conceptService.find(session,
                               com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName,
                               sys)
                                         .invoke(found -> {
                                           assertNotNull(found, "find by enum should return a concept");
                                           log.info("find(enum) returned id={} name={}",
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) found).getId(),
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>) found).getName());
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(5)
        public void testPrototypeGetNotNull()
        {
          IClassificationDataConceptService<?> conceptService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.class);
          var prototype = conceptService.get();
          assertNotNull(prototype, "IClassificationDataConceptService.get() should not return null");
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Classification
      {
        @Test
        @Order(1)
        public void testClassificationDefaultsAfterStartup()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> classificationService.getNoClassification(session, sys)
                                         .invoke(noClassy -> {
                                           assertNotNull(noClassy, "NoClassification should exist after startup");
                                           log.info("NoClassification id={} name={}",
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) noClassy).getId(),
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) noClassy).getName());
                                         })
                                         .chain(ignored -> classificationService.getIdentityType(session, sys)
                                                               .invoke(identity -> {
                                                                 assertNotNull(identity, "IdentityType classification should exist after startup");
                                                                 log.info("IdentityType id={} name={}",
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) identity).getId(),
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) identity).getName());
                                                               })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateFindIdempotentClassification()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String name = "TestClassification_X";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, name, "Test classification create", concept, sys)
                                                      .chain(created -> classificationService.find(session, name, sys)
                                                                            .invoke(found -> {
                                                                              assertNotNull(found, "After create, find-by-name should return the classification");
                                                                              log.info("Verified find-by-name for classification='{}'", name);
                                                                            })
                                                                            .chain(found1 -> classificationService.create(session, name, "Test classification create again", concept, sys)
                                                                                                 .invoke(createdAgain -> {
                                                                                                   var id1 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) found1).getId();
                                                                                                   var id2 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) createdAgain).getId();
                                                                                                   Assertions.assertEquals(id1, id2, "Idempotent create should return the same entity ID (no duplicates)");
                                                                                                   log.info("Idempotent create verified for classification='{}' id={}", name, id1);
                                                                                                 })
                                                                            )
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testFindByNameAndConceptViaInterface()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String name = "TestClassification_X"; // created in Order(2)
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;
                         return classificationService.find(session, name, concept, sys)
                                    .invoke(found -> {
                                      assertNotNull(found, "find(name, concept) should return the classification");
                                      log.info("find(name, concept) returned id={} name={}",
                                          ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) found).getId(),
                                          ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) found).getName());
                                    });
                       });
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testGetHierarchyTypeViaInterface()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> classificationService.getHierarchyType(session, sys)
                                         .invoke(hType -> {
                                           assertNotNull(hType, "HierarchyType classification should exist after startup");
                                           log.info("HierarchyType id={} name={}",
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) hType).getId(),
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>) hType).getName());
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(5)
        public void testAddClassificationToSystemCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String name = "JoinTest_Classy_1";
                                           String value = "A-Value-1";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, name, "join test classification", concept, sys)
                                                      .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .addClassification(session, name, value, sys)
                                                      )
                                                      .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                        .numberOfClassifications(session, name, value, sys)
                                                                        .invoke(count -> {
                                                                          Assertions.assertEquals(1L, count, "Exactly one SystemXClassification record should exist after addClassification");
                                                                          log.info("SystemXClassification count for '{}' with value '{}' is {}", name, value, count);
                                                                        })
                                                      )
                                                      .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .findClassification(session, name, sys)
                                                                            .invoke(found -> {
                                                                              assertNotNull(found, "findClassification should return a relationship record");
                                                                              String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                              Assertions.assertEquals(value, got, "Join value should match the inserted value");
                                                                              log.info("findClassification returned value='{}' for '{}'", got, name);
                                                                            })
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(6)
        public void testHasAndNumberOfClassificationsHelpers()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String name = "JoinTest_Classy_1"; // from Order(5)
                         String value = "A-Value-1";
                         var manageable = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys;
                         return manageable.hasClassifications(session, name, value, sys)
                                    .invoke(has -> Assertions.assertTrue(Boolean.TRUE.equals(has), "hasClassifications should be true for existing join"))
                                    .chain(ignored -> manageable.numberOfClassifications(session, name, value, sys)
                                                          .invoke(count -> Assertions.assertEquals(1L, count, "numberOfClassifications should be 1 for existing join"))
                                    );
                       });
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(7)
        public void testAddOrUpdateClassificationUpdatesValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String name = "JoinTest_Classy_1"; // from earlier tests
                         String value2 = "A-Value-2";
                         var manageable = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys;

                         // Update existing classification value
                         return manageable.addOrUpdateClassification(session, name, value2, sys)
                                    .chain(updated -> manageable.findClassification(session, name, sys)
                                                          .invoke(found -> {
                                                            assertNotNull(found, "Updated classification relationship should be retrievable");
                                                            String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                            Assertions.assertEquals(value2, got, "Updated relationship value should match");
                                                            log.info("Updated SystemXClassification value to '{}' for '{}'", got, name);
                                                          })
                                    )
                                    .chain(ignored -> manageable.numberOfClassifications(session, name, value2, sys)
                                                          .invoke(count -> Assertions.assertEquals(1L, count, "There should still be a single active join after update"))
                                    );
                       });
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(8)
        public void testAddClassificationToEnterpriseCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String name = "JoinTest_Classy_Enterprise_1";
                                           String value = "E-Value-1";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, name, "join test enterprise classification", concept, sys)
                                                      .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                                            .addClassification(session, name, value, sys)
                                                      )
                                                      .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                                        .numberOfClassifications(session, name, value, sys)
                                                                        .invoke(count -> {
                                                                          Assertions.assertEquals(1L, count, "Exactly one EnterpriseXClassification record should exist after addClassification");
                                                                          log.info("EnterpriseXClassification count for '{}' with value '{}' is {}", name, value, count);
                                                                        })
                                                      )
                                                      .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                                            .findClassification(session, name, sys)
                                                                            .invoke(found -> {
                                                                              assertNotNull(found, "findClassification on Enterprise should return a relationship record");
                                                                              String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                              Assertions.assertEquals(value, got, "Enterprise join value should match the inserted value");
                                                                              log.info("Enterprise findClassification returned value='{}' for '{}'", got, name);
                                                                            })
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(9)
        public void testAddOrUpdateClassificationOnEnterpriseUpdatesValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String name = "JoinTest_Classy_Enterprise_1"; // from previous test
                                           String value2 = "E-Value-2";
                                           var manageable = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent;

                                           return manageable.addOrUpdateClassification(session, name, value2, sys)
                                                      .chain(updated -> manageable.findClassification(session, name, sys)
                                                                            .invoke(found -> {
                                                                              assertNotNull(found, "Updated enterprise classification relationship should be retrievable");
                                                                              String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                              Assertions.assertEquals(value2, got, "Updated enterprise relationship value should match");
                                                                              log.info("Updated EnterpriseXClassification value to '{}' for '{}'", got, name);
                                                                            })
                                                      )
                                                      .chain(ignored -> manageable.numberOfClassifications(session, name, value2, sys)
                                                                            .invoke(count -> Assertions.assertEquals(1L, count, "There should still be a single active enterprise join after update"))
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class ActiveFlag
      {
        @Test
        @Order(1)
        public void testActiveFlagDefaultsAfterStartup()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> activeFlagService.getActiveFlag(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .invoke(active -> {
                                           assertNotNull(active, "Active flag should exist after startup");
                                           log.info("ActiveFlag id={} name={}",
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active).getId(),
                                               ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active).getName());
                                         })
                                         .chain(ignored -> activeFlagService.getArchivedFlag(session,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                               .invoke(archived -> {
                                                                 assertNotNull(archived, "Archived flag should exist after startup");
                                                                 log.info("ArchivedFlag id={} name={}",
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) archived).getId(),
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) archived).getName());
                                                               })
                                         )
                                         .chain(ignored2 -> activeFlagService.getDeletedFlag(session,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                                .invoke(deleted -> {
                                                                  assertNotNull(deleted, "Deleted flag should exist after startup");
                                                                  log.info("DeletedFlag id={} name={}",
                                                                      ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) deleted).getId(),
                                                                      ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) deleted).getName());
                                                                })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testFindFlagByNameEnum()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> activeFlagService.getActiveFlag(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(active -> activeFlagService.findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Active,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                              .invoke(found -> {
                                                                assertNotNull(found, "findFlagByName(Active) should return a result");
                                                                var id1 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active).getId();
                                                                var id2 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) found).getId();
                                                                Assertions.assertEquals(id1, id2, "Active flag found by enum should match getActiveFlag()");
                                                                log.info("Active flag verified id={}", id1);
                                                              })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testRangesNonEmpty()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> activeFlagService.findActiveRange(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .invoke(list -> {
                                           Assertions.assertFalse(list == null || list.isEmpty(), "findActiveRange should return at least one flag (Active)");
                                           log.info("findActiveRange size={}", list.size());
                                         })
                                         .chain(ignored -> activeFlagService.getVisibleRange(session,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                               .invoke(list -> {
                                                                 Assertions.assertFalse(list == null || list.isEmpty(), "getVisibleRange should return at least one flag (Active)");
                                                                 log.info("getVisibleRange size={}", list.size());
                                                               })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testPrototypeGetNotNull()
        {
          IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);
          var proto = activeFlagService.get();
          assertNotNull(proto, "IActiveFlagService.get() should not return null");
        }

        @Test
        @Order(5)
        public void testAddClassificationToActiveFlagCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);
            IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> activeFlagService.getActiveFlag(session,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                           .chain(active -> {
                                                             String name = "JoinTest_Classy_Active_1";
                                                             String value = "AF-Value-1";
                                                             var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                                             return classificationService.create(session, name, "join test activeflag classification", concept, sys)
                                                                        .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active)
                                                                                              .addClassification(session, name, value, sys)
                                                                        )
                                                                        .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active)
                                                                                          .numberOfClassifications(session, name, value, sys)
                                                                                          .invoke(count -> {
                                                                                            Assertions.assertEquals(1L, count, "Exactly one ActiveFlagXClassification record should exist after addClassification");
                                                                                            log.info("ActiveFlagXClassification count for '{}' with value '{}' is {}", name, value, count);
                                                                                          })
                                                                        )
                                                                        .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active)
                                                                                              .findClassification(session, name, sys)
                                                                                              .invoke(found -> {
                                                                                                assertNotNull(found, "findClassification on ActiveFlag should return a relationship record");
                                                                                                String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                                                Assertions.assertEquals(value, got, "ActiveFlag join value should match the inserted value");
                                                                                                log.info("ActiveFlag findClassification returned value='{}' for '{}'", got, name);
                                                                                              })
                                                                        );
                                                           })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(6)
        public void testAddOrUpdateClassificationOnActiveFlagUpdatesValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> activeFlagService.getActiveFlag(session,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                                           .chain(active -> {
                                                             String name = "JoinTest_Classy_Active_1"; // from previous test
                                                             String value2 = "AF-Value-2";
                                                             var manageable = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?>) active;

                                                             return manageable.addOrUpdateClassification(session, name, value2, sys)
                                                                        .chain(updated -> manageable.findClassification(session, name, sys)
                                                                                              .invoke(found -> {
                                                                                                assertNotNull(found, "Updated activeflag classification relationship should be retrievable");
                                                                                                String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                                                Assertions.assertEquals(value2, got, "Updated ActiveFlag relationship value should match");
                                                                                                log.info("Updated ActiveFlagXClassification value to '{}' for '{}'", got, name);
                                                                                              })
                                                                        )
                                                                        .chain(ignored -> manageable.numberOfClassifications(session, name, value2, sys)
                                                                                              .invoke(count -> Assertions.assertEquals(1L, count, "There should still be a single active ActiveFlag join after update"))
                                                                        );
                                                           })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }


      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Address
      {
        @Test
        @Order(1)
        public void testAddOrFindEmailContactCreatesAddressAndValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            com.guicedee.activitymaster.fsdm.client.services.IAddressService<?> addressService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IAddressService.class);
            com.guicedee.activitymaster.fsdm.client.services.IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys ->
                                  // Ensure required email address classifications exist (idempotent creates)
                                  classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.Address,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                      .chain(() -> classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.ContactAddress,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.Address))
                                      .chain(() -> classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.InternetAddress,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.Address))
                                      .chain(() -> classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.ContactAddress))
                                      .chain(() -> classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddressHost,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress))
                                      .chain(() -> classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddressDomain,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress))
                                      .chain(() -> classificationService.create(session,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddressUser,
                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                          com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications.EmailAddress))
                                      .chain(() -> {
                                        String email = "qa_address+am@test.local";
                                        return addressService.addOrFindEmailContact(session, email, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                   .invoke(addr -> {
                                                     assertNotNull(addr, "addOrFindEmailContact should return an address");
                                                     String got = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) addr).getValue();
                                                     Assertions.assertEquals(email, got, "Address value should equal the email supplied");
                                                     log.info("Address created/found id={} value={}",
                                                         ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) addr).getId(),
                                                         got);
                                                   });
                                      })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testAddClassificationToAddressCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);
            com.guicedee.activitymaster.fsdm.client.services.IAddressService<?> addressService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IAddressService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String classyName = "JoinTest_Classy_Address_1";
                                           String relValue = "ADDR-VAL-1";
                                           String email = "qa_address+join1@test.local";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, classyName, "address join classification", concept, sys)
                                                      .chain(createdClassy -> addressService.addOrFindEmailContact(session, email, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                                      .chain(address -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                            .addClassification(session, classyName, relValue, sys))
                                                      .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) rel.getPrimary())
                                                                        .numberOfClassifications(session, classyName, relValue, sys)
                                                                        .invoke(count -> Assertions.assertEquals(1L, count, "Exactly one AddressXClassification record should exist after addClassification"))
                                                      )
                                                      .chain(ignored -> addressService.addOrFindEmailContact(session, email, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .chain(address -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                                  .findClassification(session, classyName, sys)
                                                                                                  .invoke(found -> {
                                                                                                    assertNotNull(found, "findClassification on Address should return a relationship record");
                                                                                                    String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                                                    Assertions.assertEquals(relValue, got, "Address join value should match the inserted value");
                                                                                                  })
                                                                            )
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testAddOrUpdateClassificationOnAddressUpdatesValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            com.guicedee.activitymaster.fsdm.client.services.IAddressService<?> addressService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IAddressService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String classyName = "JoinTest_Classy_Address_1"; // from previous test
                                           String relValue2 = "ADDR-VAL-2";
                                           String email = "qa_address+join1@test.local";

                                           return addressService.addOrFindEmailContact(session, email, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .chain(address -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                            .addOrUpdateClassification(session, classyName, relValue2, sys)
                                                                            .chain(updated -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                                  .findClassification(session, classyName, sys)
                                                                                                  .invoke(found -> {
                                                                                                    assertNotNull(found, "Updated address classification relationship should be retrievable");
                                                                                                    String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                                                    Assertions.assertEquals(relValue2, got, "Updated Address relationship value should match");
                                                                                                  })
                                                                            )
                                                                            .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                                  .numberOfClassifications(session, classyName, relValue2, sys)
                                                                                                  .invoke(count -> Assertions.assertEquals(1L, count, "There should still be a single active Address join after update"))
                                                                            )
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testRemoveClassificationFromAddress()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);
            com.guicedee.activitymaster.fsdm.client.services.IAddressService<?> addressService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IAddressService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String classyName = "JoinTest_Classy_Address_Remove_1";
                                           String relValue = "ADDR-REM-1";
                                           String email = "qa_address+rem@test.local";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, classyName, "address remove classification", concept, sys)
                                                      .chain(created -> addressService.addOrFindEmailContact(session, email, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                                      .chain(address -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                            .addClassification(session, classyName, relValue, sys)
                                                                            .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                              .removeClassification(session, classyName, relValue, sys)
                                                                                              .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                                                    .numberOfClassifications(session, classyName, relValue, sys)
                                                                                                                    .invoke(count -> Assertions.assertEquals(0L, count, "After removeClassification, there should be 0 active joins for the given value"))
                                                                                              )
                                                                            )
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(5)
        public void testArchiveClassificationOnAddress()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);
            com.guicedee.activitymaster.fsdm.client.services.IAddressService<?> addressService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IAddressService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String classyName = "JoinTest_Classy_Address_Archive_1";
                                           String relValue = "ADDR-ARC-1";
                                           String email = "qa_address+arc@test.local";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, classyName, "address archive classification", concept, sys)
                                                      .chain(created -> addressService.addOrFindEmailContact(session, email, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                                      .chain(address -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                            .addClassification(session, classyName, relValue, sys)
                                                                            .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                              .archiveClassification(session, classyName, relValue, sys)
                                                                                              .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress<?, ?>) address)
                                                                                                                    .numberOfClassifications(session, classyName, relValue, sys)
                                                                                                                    .invoke(count -> Assertions.assertEquals(0L, count, "After archiveClassification, there should be 0 active joins for the given value"))
                                                                                              )
                                                                            )
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }


      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Party
      {
        @Test
        @Order(1)
        public void testCreateAndFindNameType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var partyService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> partyService.createNameType(session, "DisplayName", "Display Name Type",
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                         .chain(created -> partyService.findInvolvedPartyNameType(session, "DisplayName",
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(found -> assertNotNull(found, "findInvolvedPartyNameType should return created name type"))
                                         )
                       );
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateAndFindIdentificationType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var partyService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> partyService.createIdentificationType(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                               "NationalID", "National Identification")
                                         .chain(created -> partyService.findInvolvedPartyIdentificationType(session, "NationalID",
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(found -> assertNotNull(found, "findInvolvedPartyIdentificationType should return created id type"))
                                         )
                       );
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testCreateAndFindPartyType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var partyService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> partyService.createType(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                               "Person", "Person Type")
                                         .chain(created -> partyService.findType(session, "Person",
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(found -> assertNotNull(found, "findType should return created party type"))
                                         )
                       );
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testCreateInvolvedPartyAndFindByIdentificationType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var partyService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String idType = "NationalID";
                         String idValue = "NAT-123456";
                         // Ensure types exist (idempotent)
                         return partyService.createIdentificationType(session,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                 idType, "National Identification")
                                    .chain(() -> partyService.createType(session,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                        "Person", "Person Type"))
                                    .chain(() -> partyService.createNameType(session, "DisplayName", "Display Name Type",
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(() -> partyService.create(session,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                        new com.guicedee.guicedinjection.pairing.Pair<>(idType, idValue), true))
                                    .chain(created -> partyService.findAllByIdentificationType(session, idType, idValue)
                                                          .invoke(list -> {
                                                            java.util.List<?> l = (java.util.List<?>) list;
                                                            Assertions.assertFalse(l == null || l.isEmpty(), "findAllByIdentificationType should return at least one party");
                                                          })
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(5)
        public void testPrototypeGetNotNull()
        {
          var partyService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.class);
          var proto = partyService.get();
          assertNotNull(proto, "IInvolvedPartyService.get() should not return null");
        }

        @Test
        @Order(6)
        public void testAddClassificationToInvolvedPartyCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);
            var partyService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String classyName = "JoinTest_Classy_Party_1";
                         String relValue = "PTY-VAL-1";
                         String idType = "NationalID";
                         String idValue = "NAT-654321";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "party join classification", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> partyService.createIdentificationType(session,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                        idType, "National Identification"))
                                    .chain(() -> partyService.createType(session,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                        "Person", "Person Type"))
                                    .chain(() -> partyService.create(session,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys,
                                        new com.guicedee.guicedinjection.pairing.Pair<>(idType, idValue), true))
                                    .chain(party -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty<?, ?>) party)
                                                        .addClassification(session, classyName, relValue,
                                                            (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                        .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty<?, ?>) party)
                                                                          .numberOfClassifications(session, classyName, relValue,
                                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                          .invoke(count -> Assertions.assertEquals(1L, count, "Exactly one InvolvedPartyXClassification record should exist after addClassification"))
                                                        )
                                                        .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty<?, ?>) party)
                                                                              .findClassification(session, classyName,
                                                                                  (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                              .invoke(found -> {
                                                                                assertNotNull(found, "findClassification on InvolvedParty should return a relationship record");
                                                                                String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                                Assertions.assertEquals(relValue, got, "InvolvedParty join value should match the inserted value");
                                                                              })
                                                        )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }


      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Arrangements
      {
        @Test
        @Order(1)
        public void testCreateAndFindArrangementType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var arrangementsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IArrangementsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "OrderType_X1";
                         return arrangementsService.createArrangementType(session, typeName,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> arrangementsService.findArrangementType(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(found -> assertNotNull(found, "findArrangementType should return created arrangement type"));
                       });
          });
          uni.await()
              .atMost(java.time.Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateArrangementAndFindByClassification()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var arrangementsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IArrangementsService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "OrderType_X1"; // from previous test
                         String classificationName = "Arr_Classy_1";
                         String value = "ARR-VAL-1";

                         // Ensure the classification exists
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;
                         return classificationService.create(session, classificationName, "Arrangement test classification", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> arrangementsService.create(session, typeName,
                                        com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(), value,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement<?, ?>) created)
                                                          .addClassification(session, classificationName, value,
                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .replaceWith(created))
                                    .chain(ignored -> arrangementsService.findArrangementsByClassification(session, classificationName, value,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(list -> {
                                      java.util.List<?> l = (java.util.List<?>) list;
                                      org.junit.jupiter.api.Assertions.assertFalse(l == null || l.isEmpty(), "findArrangementsByClassification should return at least one arrangement");
                                    });
                       });
          });
          uni.await()
              .atMost(java.time.Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testPrototypeGetNotNull()
        {
          var arrangementsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IArrangementsService.class);
          var proto = arrangementsService.get();
          assertNotNull(proto, "IArrangementsService.get() should not return null");
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Products
      {
        @Test
        @Order(1)
        public void testCreateAndFindProductType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "GadgetType_X1";
                         return productService.createProductType(session, typeName, "Gadget Type",
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> productService.findProductTypeForProduct(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(found -> assertNotNull(found, "findProductTypeForProduct should return created product type"));
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateProductAndFindByName()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "GadgetType_X1"; // from previous test
                         String prodName = "Widget_X1";
                         String code = "WID-001";
                         return productService.createProduct(session, typeName, prodName, "Widget Desc", code,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> productService.findProduct(session, prodName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(found -> assertNotNull(found, "findProduct should return created product"));
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testAddProductTypeToProductCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String prodName = "Widget_X1"; // created earlier
                         String extraType = "AccessoryType_A1";
                         String value = "VAL-1";

                         return productService.createProductType(session, extraType, "Accessory Type",
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(createdType -> productService.findProduct(session, prodName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .addOrReuseProductTypes(session, extraType,
                                                           com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                           null,
                                                           value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                                         .numberOfProductTypes(session,
                                                                             com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                                             extraType,
                                                                             (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                         .invoke(count -> Assertions.assertEquals(1L, count, "Exactly one ProductXProductType record should exist after addOrReuseProductTypes"))
                                                       )
                                                       .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                                             .findProductType(session,
                                                                                 com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                                                 extraType,
                                                                                 value,
                                                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                             .invoke(found -> {
                                                                               assertNotNull(found, "findProductType on Product should return a relationship record");
                                                                               String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                               Assertions.assertEquals(value, got, "Product type join value should match the inserted value");
                                                                             })
                                                       )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testPrototypeGetNotNull()
        {
          var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);
          var proto = productService.get();
          assertNotNull(proto, "IProductService.get() should not return null");
          var typeProto = productService.getType();
          assertNotNull(typeProto, "IProductService.getType() should not return null");
        }

        @Test
        @Order(5)
        public void testAddClassificationToProductCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String prodName = "Widget_X1"; // created earlier in Order(2)
                         String classyName = "JoinTest_Classy_Product_1";
                         String value = "PROD-VAL-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "product classification", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> productService.findProduct(session, prodName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .addClassification(session, classyName, value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .replaceWith(prod))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .numberOfClassifications(session, classyName, value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(1L, count, "Exactly one ProductXClassification should exist after addClassification"))
                                                       .replaceWith(prod))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .findClassification(session, classyName,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .invoke(found -> {
                                                         org.junit.jupiter.api.Assertions.assertNotNull(found, "findClassification on Product should return a relationship record");
                                                         String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                         org.junit.jupiter.api.Assertions.assertEquals(value, got, "Product classification join value should match the inserted value");
                                                       })
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(6)
        public void testAddOrUpdateClassificationOnProductUpdatesValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> productService.findProduct(session, "Widget_X1",
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                         .chain(prod -> {
                                           String classyName = "JoinTest_Classy_Product_1"; // from previous test
                                           String value2 = "PROD-VAL-2";
                                           var manageable = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod;
                                           return manageable.addOrUpdateClassification(session, classyName, value2,
                                                   (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .chain(updated -> manageable.findClassification(session, classyName,
                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .invoke(found -> {
                                                                              org.junit.jupiter.api.Assertions.assertNotNull(found, "Updated product classification relationship should be retrievable");
                                                                              String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                              org.junit.jupiter.api.Assertions.assertEquals(value2, got, "Updated Product relationship value should match");
                                                                            })
                                                      )
                                                      .chain(ignored -> manageable.numberOfClassifications(session, classyName, value2,
                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(1L, count, "There should still be a single active Product join after update"))
                                                      );
                                         })
                       );
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(7)
        public void testRemoveClassificationFromProduct()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String prodName = "Widget_X1";
                         String classyName = "JoinTest_Classy_Product_Remove_1";
                         String value = "PROD-REM-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "product classification remove", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> productService.findProduct(session, prodName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .addClassification(session, classyName, value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .replaceWith(prod))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .removeClassification(session, classyName, value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(ignored -> productService.findProduct(session, prodName,
                                            (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                                             .numberOfClassifications(session, classyName, value,
                                                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                             .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(0L, count, "After removeClassification, there should be 0 active Product joins for the given value"))
                                                          )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(8)
        public void testArchiveClassificationOnProduct()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var productService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IProductService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String prodName = "Widget_X1";
                         String classyName = "JoinTest_Classy_Product_Archive_1";
                         String value = "PROD-ARC-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "product classification archive", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> productService.findProduct(session, prodName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .addClassification(session, classyName, value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .replaceWith(prod))
                                    .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                       .archiveClassification(session, classyName, value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(ignored -> productService.findProduct(session, prodName,
                                            (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .chain(prod -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct<?, ?>) prod)
                                                                             .numberOfClassifications(session, classyName, value,
                                                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                             .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(0L, count, "After archiveClassification, there should be 0 active Product joins for the given value"))
                                                          )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Rules
      {
        @Test
        @Order(1)
        public void testCreateAndFindRulesType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var rulesService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IRulesService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "RuleType_X1";
                         return rulesService.createRulesType(session, typeName,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> rulesService.findRulesTypes(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(found -> assertNotNull(found, "findRulesTypes should return created rules type"));
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateRulesAndFindByName()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var rulesService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IRulesService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String typeName = "RuleType_X1"; // from previous test
                                           String ruleName = "Rule_X1";
                                           String desc = "First rule";
                                           return rulesService.createRules(session, typeName, ruleName, desc,
                                                   (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .chain(created -> rulesService.findRules(session, ruleName,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                                                      .invoke(found -> assertNotNull(found, "findRules by name should return created rule"));
                                         })
                       );
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testAddRuleTypeToRulesCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var rulesService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IRulesService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "RuleType_Join_1";
                         String ruleName = "Rule_Join_1";
                         String desc = "Join rule";
                         String value = "RUL-VAL-1";

                         return rulesService.createRulesType(session, typeName,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(createdType -> rulesService.createRules(session, typeName, ruleName, desc,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(rule -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules<?, ?>) rule)
                                                       .addOrReuseRuleTypes(session, typeName,
                                                           com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                           null,
                                                           value,
                                                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                       .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules<?, ?>) rule)
                                                                         .numberOfRuleTypes(session,
                                                                             com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                                             typeName,
                                                                             (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                         .invoke(count -> Assertions.assertEquals(1L, count, "Exactly one RulesXRulesType record should exist after addOrReuseRuleTypes"))
                                                       )
                                                       .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules<?, ?>) rule)
                                                                             .findRulesTypes(session,
                                                                                 com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                                                 typeName,
                                                                                 value,
                                                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                             .invoke(found -> {
                                                                               assertNotNull(found, "findRulesTypes on Rules should return a relationship record");
                                                                               String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                               Assertions.assertEquals(value, got, "Rule type join value should match the inserted value");
                                                                             })
                                                       )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Security
      {
        @Test
        @Order(1)
        public void testDefaultSecurityGroupsAndFoldersExist()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var securityService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> securityService.getEveryoneGroup(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                         .invoke(tok -> assertNotNull(tok, "Everyone group should exist after startup"))
                                         .chain(ignored -> securityService.getEverywhereGroup(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(tok -> assertNotNull(tok, "Everywhere group should exist after startup")))
                                         .chain(ignored -> securityService.getAdministratorsFolder(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(tok -> assertNotNull(tok, "Administrators folder should exist after startup")))
                                         .chain(ignored -> securityService.getSystemsFolder(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(tok -> assertNotNull(tok, "Systems folder should exist after startup")))
                                         .chain(ignored -> securityService.getPluginsFolder(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(tok -> assertNotNull(tok, "Plugins folder should exist after startup")))
                                         .chain(ignored -> securityService.getApplicationsFolder(session, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                               .invoke(tok -> assertNotNull(tok, "Applications folder should exist after startup")))
                       );
          });
          uni.await()
              .atMost(java.time.Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateSecurityTokenAndFindByUUID()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var securityService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String classification = com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications.System.toString();
                         String name = "SecToken_Test_A";
                         String desc = "Test Security Token A";
                         return securityService.create(session, classification, name, desc, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> {
                                      String tokStr = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?>) created).getSecurityToken();
                                      java.util.UUID tokUuid = java.util.UUID.fromString(tokStr);
                                      return securityService.getSecurityToken(session, tokUuid, (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                 .invoke(found -> assertNotNull(found, "getSecurityToken by UUID should return created token"));
                                    });
                       });
          });
          uni.await()
              .atMost(java.time.Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testGrantAccessBetweenTokens()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var securityService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String classification = com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications.System.toString();
                         return securityService.create(session, classification, "SecToken_From", "From token", (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(fromTok -> securityService.create(session, classification, "SecToken_To", "To token", (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .chain(toTok -> securityService.grantAccessToToken(session,
                                                                  (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?>) fromTok,
                                                                  (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?>) toTok,
                                                                  true, true, false, true,
                                                                  (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                              .replaceWith(toTok)
                                                          )
                                    )
                                    .invoke(granted -> assertNotNull(granted, "Granting access between tokens should complete without error"));
                       });
          });
          uni.await()
              .atMost(java.time.Duration.ofMinutes(2));
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Systems
      {
        @Test
        @Order(1)
        public void testGetActivityMasterByEnterprise()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .invoke(sys -> {
                         assertNotNull(sys, "Activity Master system should exist after startup");
                         log.info("ActivityMaster system id={} name={}",
                             ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys).getId(),
                             ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys).getName());
                       });
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testDoesSystemExistAndFind()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.doesSystemExist(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                               com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName)
                                         .invoke(exists -> Assertions.assertTrue(Boolean.TRUE.equals(exists), "ActivityMaster system should exist"))
                                         .chain(ignored -> systemsService.findSystem(session,
                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                                                 com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName)
                                                               .invoke(found -> {
                                                                 assertNotNull(found, "findSystem should return Activity Master system");
                                                                 log.info("findSystem(Activity Master) id={} name={}",
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) found).getId(),
                                                                     ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) found).getName());
                                                               })
                                         )
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testGetSecurityIdentityToken()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> systemsService.getSecurityIdentityToken(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                       .invoke(token -> {
                         assertNotNull(token, "Security identity token should not be null for Activity Master system");
                         log.info("SecurityIdentityToken={}", token);
                       });
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testGetActivityMasterBySystemOverload()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                       .invoke(sys2 -> {
                         assertNotNull(sys2, "getActivityMaster by system overload should return a system");
                         log.info("ActivityMaster (overload) id={} name={}",
                             ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys2).getId(),
                             ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys2).getName());
                       });
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(5)
        public void testRemoveClassificationFromSystem()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String name = "JoinTest_Classy_Systems_Remove_1";
                                           String value = "SYS-REM-1";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, name, "systems remove classification", concept, sys)
                                                      .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .addClassification(session, name, value, sys))
                                                      .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                        .removeClassification(session, name, value, sys))
                                                      .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .numberOfClassifications(session, name, value, sys)
                                                                            .invoke(count -> {
                                                                              Assertions.assertEquals(0L, count, "After removeClassification, there should be 0 active joins for the given value");
                                                                              log.info("After remove, SystemXClassification active count for '{}' with value '{}' is {}", name, value, count);
                                                                            })
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(6)
        public void testArchiveClassificationOnSystem()
        {
          var uni = sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            IClassificationService<?> classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                                         .chain(sys -> {
                                           String name = "JoinTest_Classy_Systems_Archive_1";
                                           String value = "SYS-ARC-1";
                                           var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                           return classificationService.create(session, name, "systems archive classification", concept, sys)
                                                      .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .addClassification(session, name, value, sys))
                                                      .chain(rel -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                        .archiveClassification(session, name, value, sys))
                                                      .chain(ignored -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .numberOfClassifications(session, name, value, sys)
                                                                            .invoke(count -> {
                                                                              Assertions.assertEquals(0L, count, "After archiveClassification, there should be 0 active joins for the given value");
                                                                              log.info("After archive, SystemXClassification active count for '{}' with value '{}' is {}", name, value, count);
                                                                            })
                                                      );
                                         })
                       );
          });

          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class ResourceItems
      {
        @Test
        @Order(1)
        public void testCreateAndFindResourceItemType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var resourceService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IResourceItemService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "FileType_X1";
                         return resourceService.createType(session, typeName, typeName,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> resourceService.findResourceItemType(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(found -> assertNotNull(found, "findResourceItemType should return created resource item type"));
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateResourceItemAndFindByType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var resourceService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IResourceItemService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "FileType_X1"; // from previous test
                         String value = "demo_file_value_1";
                         return resourceService.create(session, typeName, value,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .invoke(created -> assertNotNull(created, "create(resource item) should return a resource item"))
                                    .chain(created -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) created)
                                                          .numberOfResourceItemTypes(session,
                                                              com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification.name(),
                                                              typeName,
                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .invoke(count -> Assertions.assertEquals(1L, count, "New resource item should have exactly one type assigned"))
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testAddClassificationToResourceItemCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var resourceService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IResourceItemService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "FileType_X1";
                         String resourceValue = "demo_file_value_2";
                         String classyName = "JoinTest_Classy_ResourceItem_1";
                         String relValue = "RI-VAL-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "resource item classification", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> resourceService.create(session, typeName, resourceValue,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(res -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) res)
                                                      .addClassification(session, classyName, relValue,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .replaceWith(res))
                                    .chain(res -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) res)
                                                      .numberOfClassifications(session, classyName, relValue,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .invoke(count -> Assertions.assertEquals(1L, count, "Exactly one ResourceItemXClassification should exist after addClassification"))
                                                      .replaceWith(res))
                                    .chain(res -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) res)
                                                      .findClassification(session, classyName,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .invoke(found -> {
                                                        assertNotNull(found, "findClassification on ResourceItem should return a relationship record");
                                                        String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                        Assertions.assertEquals(relValue, got, "ResourceItem join value should match the inserted value");
                                                      })
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testUpdateDataValueAndGetDataRow()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var resourceService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IResourceItemService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "FileType_X1";
                         String value = "demo_file_value_update_1";
                         String value2 = "demo_file_value_update_2";
                         return resourceService.create(session, typeName, value,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(res -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) res)
                                                      .getDataRow(session)
                                                      .invoke(row -> assertNotNull(row, "getDataRow should return a non-null data row"))
                                                      .replaceWith(res))
                                    .chain(res -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) res)
                                                      .updateDataTypeValue(session, value2)
                                                      .invoke(updated -> assertNotNull(updated, "updateDataTypeValue should return the resource item"))
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }
      }

      @Nested
      @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
      class Events
      {
        @Test
        @Order(1)
        public void testCreateAndFindEventType()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var eventService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEventService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "EventType_X1";
                         return eventService.createEventType(session, typeName,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(created -> eventService.findEventType(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .invoke(found -> assertNotNull(found, "findEventType should return created event type"));
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testCreateEventAndAddClassificationCreatesJoin()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var eventService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEventService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "EventType_X1"; // from previous test
                         String classyName = "JoinTest_Classy_Event_1";
                         String value = "EVT-VAL-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "event classification", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> eventService.createEvent(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .addClassification(session, classyName, value,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .replaceWith(evt))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .numberOfClassifications(session, classyName, value,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(1L, count, "Exactly one EventXClassification should exist after addClassification"))
                                                      .replaceWith(evt))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .findClassification(session, classyName,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .invoke(found -> {
                                                        org.junit.jupiter.api.Assertions.assertNotNull(found, "findClassification on Event should return a relationship record");
                                                        String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                        org.junit.jupiter.api.Assertions.assertEquals(value, got, "Event classification join value should match the inserted value");
                                                      })
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testAddOrUpdateClassificationOnEventUpdatesValue()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var eventService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEventService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> eventService.createEvent(session, "EventType_X1",
                               (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                         .chain(evt -> {
                                           String classyName = "JoinTest_Classy_Event_1"; // from previous test
                                           String value2 = "EVT-VAL-2";
                                           var manageable = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt;
                                           return manageable.addOrUpdateClassification(session, classyName, value2,
                                                   (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .chain(updated -> manageable.findClassification(session, classyName,
                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .invoke(found -> {
                                                                              org.junit.jupiter.api.Assertions.assertNotNull(found, "Updated event classification relationship should be retrievable");
                                                                              String got = ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) found).getValue();
                                                                              org.junit.jupiter.api.Assertions.assertEquals(value2, got, "Updated Event relationship value should match");
                                                                            })
                                                      )
                                                      .chain(ignored -> manageable.numberOfClassifications(session, classyName, value2,
                                                              (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                            .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(1L, count, "There should still be a single active Event join after update"))
                                                      );
                                         })
                       );
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(4)
        public void testRemoveClassificationFromEvent()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var eventService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEventService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "EventType_X1";
                         String classyName = "JoinTest_Classy_Event_Remove_1";
                         String value = "EVT-REM-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "event classification remove", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> eventService.createEvent(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .addClassification(session, classyName, value,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .replaceWith(evt))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .removeClassification(session, classyName, value,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(ignored -> eventService.createEvent(session, typeName,
                                            (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .chain(evt2 -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt2)
                                                                             .numberOfClassifications(session, classyName, value,
                                                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                             .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(0L, count, "After removeClassification, there should be 0 active Event joins for the given value"))
                                                          )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(5)
        public void testArchiveClassificationOnEvent()
        {
          var uni = sessionFactory.withTransaction(session -> {
            var enterpriseService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.class);
            var systemsService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);
            var eventService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEventService.class);
            var classificationService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);

            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                       .chain(ent -> systemsService.getActivityMaster(session,
                           (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent))
                       .chain(sys -> {
                         String typeName = "EventType_X1";
                         String classyName = "JoinTest_Classy_Event_Archive_1";
                         String value = "EVT-ARC-1";
                         var concept = com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                         return classificationService.create(session, classyName, "event classification archive", concept,
                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                    .chain(() -> eventService.createEvent(session, typeName,
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .addClassification(session, classyName, value,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                      .replaceWith(evt))
                                    .chain(evt -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt)
                                                      .archiveClassification(session, classyName, value,
                                                          (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys))
                                    .chain(ignored -> eventService.createEvent(session, typeName,
                                            (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                          .chain(evt2 -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent<?, ?>) evt2)
                                                                             .numberOfClassifications(session, classyName, value,
                                                                                 (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                                             .invoke(count -> org.junit.jupiter.api.Assertions.assertEquals(0L, count, "After archiveClassification, there should be 0 active Event joins for the given value"))
                                                          )
                                    );
                       });
          });
          uni.await()
              .atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(6)
        public void testPrototypeGetNotNull()
        {
          var svc = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IEventService.class);
          var uni = svc.get();
          var proto = uni.await()
                          .atMost(Duration.ofMinutes(1));
          assertNotNull(proto, "IEventService.get() should not return null");
        }
      }

    }
  }

}
