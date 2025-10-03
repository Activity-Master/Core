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
                                             return enterpriseService.startNewEnterprise(session,TestEnterprise.name(),"admin","!@adminadmin")
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

        foundConcept.await().atMost(Duration.ofMinutes(2));
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
                                  var id1 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) found1).getId();
                                  var id2 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) createdAgain).getId();
                                  Assertions.assertEquals(id1, id2, "Idempotent create should return the same entity ID (no duplicates)");
                                  log.info("Idempotent create verified for concept='{}' id={}", conceptName, id1);
                                })
                            )
                        );
                  })
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) global).getId(),
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) global).getName());
                  })
                  .chain(ignored -> conceptService.getNoConcept(session, sys)
                      .invoke(noConcept -> {
                        assertNotNull(noConcept, "NoConcept should exist after startup");
                        log.info("NoConcept id={} name={}",
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) noConcept).getId(),
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) noConcept).getName());
                      })
                  )
                  .chain(ignored2 -> conceptService.getSecurityHierarchyConcept(session, sys)
                      .invoke(sec -> {
                        assertNotNull(sec, "SecurityHierarchy concept should exist after startup");
                        log.info("SecurityHierarchyConcept id={} name={}",
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) sec).getId(),
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) sec).getName());
                      })
                  )
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) found).getId(),
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?,?>) found).getName());
                  })
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) noClassy).getId(),
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) noClassy).getName());
                  })
                  .chain(ignored -> classificationService.getIdentityType(session, sys)
                      .invoke(identity -> {
                        assertNotNull(identity, "IdentityType classification should exist after startup");
                        log.info("IdentityType id={} name={}",
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) identity).getId(),
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) identity).getName());
                      })
                  )
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                                  var id1 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) found1).getId();
                                  var id2 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) createdAgain).getId();
                                  Assertions.assertEquals(id1, id2, "Idempotent create should return the same entity ID (no duplicates)");
                                  log.info("Idempotent create verified for classification='{}' id={}", name, id1);
                                })
                            )
                        );
                  })
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                          ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) found).getId(),
                          ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) found).getName());
                    });
              });
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) hType).getId(),
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?,?>) hType).getName());
                  })
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) active).getId(),
                        ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) active).getName());
                  })
                  .chain(ignored -> activeFlagService.getArchivedFlag(session,
                      (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                      .invoke(archived -> {
                        assertNotNull(archived, "Archived flag should exist after startup");
                        log.info("ArchivedFlag id={} name={}",
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) archived).getId(),
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) archived).getName());
                      })
                  )
                  .chain(ignored2 -> activeFlagService.getDeletedFlag(session,
                      (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                      .invoke(deleted -> {
                        assertNotNull(deleted, "Deleted flag should exist after startup");
                        log.info("DeletedFlag id={} name={}",
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) deleted).getId(),
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) deleted).getName());
                      })
                  )
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                        var id1 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) active).getId();
                        var id2 = ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?,?>) found).getId();
                        Assertions.assertEquals(id1, id2, "Active flag found by enum should match getActiveFlag()");
                        log.info("Active flag verified id={}", id1);
                      })
                  )
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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

        uni.await().atMost(Duration.ofMinutes(2));
      }

      @Test
      @Order(4)
      public void testPrototypeGetNotNull()
      {
        IActiveFlagService<?> activeFlagService = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.class);
        var proto = activeFlagService.get();
        assertNotNull(proto, "IActiveFlagService.get() should not return null");
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
                    ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?,?>) sys).getId(),
                    ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?,?>) sys).getName());
              });
        });

        uni.await().atMost(Duration.ofMinutes(2));
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
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?,?>) found).getId(),
                            ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?,?>) found).getName());
                      })
                  )
              );
        });

        uni.await().atMost(Duration.ofMinutes(2));
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

        uni.await().atMost(Duration.ofMinutes(2));
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
                    ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?,?>) sys2).getId(),
                    ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?,?>) sys2).getName());
              });
        });

        uni.await().atMost(Duration.ofMinutes(2));
      }



    }
  }


}