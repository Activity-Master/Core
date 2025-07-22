package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.PostgreSQLTestDBModule;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.client.IGuiceContext;
import com.guicedee.vertxpersistence.PersistService;
import com.guicedee.vertxpersistence.bind.JtaPersistService;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class TestDatabaseConnectivity extends TestDatabaseSetup
{
    private Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup()
    {
        // Initialize the Guice context
        //IGuiceContext.registerModule(new PostgreSQLTestDBModule());
        IGuiceContext.instance();

        log.info("Loading DB Configuration / PersistService from Guice");
        JtaPersistService ps = (JtaPersistService) IGuiceContext.get(Key.get(PersistService.class, Names.named("ActivityMaster-Test")));
        assertNotNull(ps, "PersistService should not be null");
        ps.start();

        // Get the session factory
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");
    }

    @BeforeEach
    public void testMyself()
    {
        //runScript(sessionFactory, "postgres_fsdm.sql");
    }

    @AfterEach
    public void destroyMyself()
    {

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
        var result = sessionFactory.withSession(session -> {
            // Persist the entity
            return session.withTransaction(tx -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                var enterprise = enterpriseService.get();
                enterprise.setName(TestEnterprise.name());
                enterprise.setDescription("Enterprise for Testing");
                return enterprise.persist()
                        .chain(ent -> {
                            return enterpriseService.getEnterprise(TestEnterprise)
                                    .onItemOrFailure()
                                    .transform((ent2,throwable) -> {
                                        if (throwable != null)
                                        {
                                            log.error("Failed to find Enterprise", throwable);
                                            throw new RuntimeException("Failed to find Enterprise", throwable);
                                        }else
                                        {
                                            log.info("Found Enterprise: " + ent2.getName());
                                            log.info("Found Enterprise ID: " + ent2.getId());
                                            return Uni.createFrom()
                                                           .item(ent2);
                                        }
                                    })                                    ;
                        }).chain(ent-> {
                            return enterpriseService.getEnterprise(TestEnterprise);
                        });
            });
        }).await().atMost(Duration.of(50L, ChronoUnit.SECONDS));
        System.out.println("Enterprise Created - " + result.getName() + " / " + result.getId());
    }
}
