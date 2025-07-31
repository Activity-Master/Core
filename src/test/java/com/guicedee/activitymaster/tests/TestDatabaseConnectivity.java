package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ITimeService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.vertxpersistence.PersistService;
import com.guicedee.vertxpersistence.bind.JtaPersistService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log4j2
public class TestDatabaseConnectivity extends TestDatabaseSetup
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
    JtaPersistService ps = (JtaPersistService) IGuiceContext.get(Key.get(PersistService.class, Names.named("ActivityMaster-Test")));
    ps.stop();
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
          .await().atMost(Duration.ofMinutes(1));

    }

    @Test
    @Order(3)
    public void testUpdates()
    {
      IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
      var updates = Multi.createFrom()
                        .items(
                            sessionFactory.withTransaction(session -> {
                              return enterpriseService.getEnterprise(session, TestEnterprise.name())
                                         .chain(enterprise -> {
                                           return enterpriseService.loadUpdates(session, enterprise)
                                                      .onFailure()
                                                      .invoke(a -> log.fatal("Cannot load updates", a));
                                         });
                            }));
      updates.onFailure().invoke(a -> log.fatal("Cannot load updates", a));
      updates.onItem()
          .invoke(a -> log.info("loaded updates"));
      updates.toUni().await().atMost(Duration.ofMinutes(1));


    }
  }


}