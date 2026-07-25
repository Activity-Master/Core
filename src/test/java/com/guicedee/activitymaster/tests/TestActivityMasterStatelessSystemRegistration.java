package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Replicates the reported defect: when an enterprise is created and started <strong>purely
 * statelessly</strong> (no bridge to a managed {@link Mutiny.Session}), some {@link IMasterSystem}s —
 * notably the {@code TimeSystem} — never get a row in the {@code Systems} table, even though the process
 * completes without error.
 *
 * <p>The enterprise is provisioned once through the genuinely-stateless, no-bridge
 * {@link IEnterpriseService#startNewEnterprise(Mutiny.StatelessSession, String, String, String)} entry
 * point (every phase on its own stateless transaction). Afterwards <em>every</em> system reported by
 * {@link ActivityMasterConfiguration#getAllSystems()} must be registered — asserted via the stateless
 * {@link ISystemsService#doesSystemExist(Mutiny.StatelessSession, IEnterprise, String, UUID...)} lookup.</p>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterStatelessSystemRegistration
{
    /** A fresh enterprise never touched by the stateful path — forces the real stateless registration. */
    private static final String ENTERPRISE = "SlSysRegCo";

    private Mutiny.SessionFactory sessionFactory;
    private UUID enterpriseId;

    @BeforeAll
    public void setup()
    {
        com.guicedee.client.utils.LogUtils.addConsoleLogger(org.apache.logging.log4j.Level.INFO);
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(ENTERPRISE);
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        // Genuine, purely-stateless enterprise lifecycle — NO bridge to a managed session:
        // createNewEnterprise (creates the enterprise record + installs/registers every system) FIRST,
        // then startNewEnterprise (admin user + post-startups). Both on the same stateless session.
        var ent = es.get();
        ent.setName(ENTERPRISE);
        ent.setDescription("Stateless system-registration test enterprise");

        IEnterprise<?, ?> created = sessionFactory.openStatelessSession()
                .chain(ss -> es.createNewEnterprise(ss, ent)
                        .chain(e -> es.startNewEnterprise(ss, ENTERPRISE, "admin", "!@adminadmin"))
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(5));

        assertNotNull(created, "Purely-stateless create+start enterprise must complete and return the enterprise");
        enterpriseId = created.getId();
        assertNotNull(enterpriseId, "Stateless-created enterprise must have an id");
    }

    @Test
    @Order(1)
    @DisplayName("Every master system registers a Systems row after a purely-stateless enterprise start")
    public void allSystems_registerStatelessly()
    {
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        List<IMasterSystem<?>> systems = new ArrayList<>(ActivityMasterConfiguration.get().getAllSystems());
        assertFalse(systems.isEmpty(), "There must be master systems to install");

        // Key by CLASS name (not system name) so a missing/duplicate system name can never hide a gap.
        Map<String, Boolean> registration = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, ENTERPRISE)
                        .chain(ent -> {
                            Uni<Map<String, Boolean>> chain = Uni.createFrom().item(new LinkedHashMap<>());
                            for (IMasterSystem<?> system : systems)
                            {
                                final String className = system.getClass().getSimpleName();
                                final String name = system.getSystemName();
                                chain = chain.chain(acc -> ss.doesSystemExist(session, (IEnterprise<?, ?>) ent, name)
                                        .map(exists -> {
                                            acc.put(className + " [" + name + "]", exists);
                                            return acc;
                                        }));
                            }
                            return chain;
                        })
        ).await().atMost(Duration.ofMinutes(2));

        List<String> missing = registration.entrySet().stream()
                .filter(e -> !Boolean.TRUE.equals(e.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        log.info("Stateless system registration result: {}", registration);
        assertEquals(systems.size(), registration.size(),
                "Every discovered system must have a distinct registration entry (no system hidden by a shared name)");
        assertTrue(missing.isEmpty(),
                "All master systems must be registered after a purely-stateless enterprise start, but these were missing: "
                        + missing);
    }
}



