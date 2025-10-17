package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import jakarta.persistence.NoResultException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestInvolvedPartyService {

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        log.info("Loading DB Configuration / PersistService from Guice (InvolvedPartyService)");
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Ensure Enterprise is installed and Activity Master system exists for this test class context
        sessionFactory.withSession(session ->
                session.withTransaction(tx -> {
                    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                    ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                    // Create or get enterprise
                    return enterpriseService.getEnterprise(session, TestEnterprise.name())
                            .onFailure().recoverWithUni(t -> {
                                var ent = enterpriseService.get();
                                ent.setName(TestEnterprise.name());
                                ent.setDescription("Enterprise Entity for InvolvedPartyService Testing");
                                return enterpriseService.createNewEnterprise(session, ent);
                            })
                            .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                    .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                            ISystemsService.ActivityMasterSystemName, "Activity Master System"))
                            )
                            .replaceWith(Uni.createFrom().voidItem());
                })
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));

        // Start the enterprise (idempotent)
        sessionFactory.withSession(session ->
                session.withTransaction(tx -> {
                    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                    return enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                            .onFailure().recoverWithItem(e -> null)
                            .replaceWith(Uni.createFrom().voidItem());
                })
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    private Uni<ISystems<?, ?>> getActivityMasterSystem(Mutiny.Session session) {
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
        return enterpriseService.getEnterprise(session, TestEnterprise.name())
                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                        .map(sys -> (ISystems<?, ?>) sys));
    }

    @Test
    @Order(1)
    public void testCreateNameType_Idempotent_and_FindThrowsBeforeCreate() {
        // First: attempt to find before creation and expect NoResultException
        try {
            sessionFactory.withTransaction(session -> {
                IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
                return getActivityMasterSystem(session)
                        .chain(sys -> partyService.findInvolvedPartyNameType(session, "QA_DisplayName", (ISystems<?, ?>) sys));
            }).await().atMost(Duration.ofMinutes(2));
            fail("Expected NoResultException before creating name type");
        } catch (Exception e) {
            // Depending on the layer, NoResultException may be wrapped; accept either direct or cause
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            Assertions.assertTrue(cause instanceof NoResultException, "Expected NoResultException before creating name type");
        }

        // Second: create twice (idempotent)
        var uni = sessionFactory.withTransaction(session -> {
            IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
            return getActivityMasterSystem(session)
                    .chain(sys -> partyService.createNameType(session, "QA_DisplayName", "Display Name Type", (ISystems<?, ?>) sys)
                            .chain(created -> partyService.createNameType(session, "QA_DisplayName", "Display Name Type", (ISystems<?, ?>) sys))
                            .replaceWith(Uni.createFrom().voidItem()));
        });
        uni.await().atMost(Duration.ofMinutes(2));
    }

    @Test
    @Order(2)
    public void testCreateIdentificationType_Idempotent() {
        var uni = sessionFactory.withTransaction(session -> {
            IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
            return getActivityMasterSystem(session)
                    .chain(sys -> partyService.createIdentificationType(session, (ISystems<?, ?>) sys,
                                    "QA_NationalID", "National Identification")
                            .chain(created -> partyService.createIdentificationType(session, (ISystems<?, ?>) sys,
                                    "QA_NationalID", "National Identification")) // idempotent
                            .replaceWith(Uni.createFrom().voidItem()));
        });
        uni.await().atMost(Duration.ofMinutes(2));
    }

    @Test
    @Order(3)
    public void testCreateType_Idempotent() {
        var uni = sessionFactory.withTransaction(session -> {
            IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
            return getActivityMasterSystem(session)
                    .chain(sys -> partyService.createType(session, (ISystems<?, ?>) sys,
                                    "QA_Person", "Person Type")
                            .chain(created -> partyService.createType(session, (ISystems<?, ?>) sys,
                                    "QA_Person", "Person Type")) // idempotent
                            .replaceWith(Uni.createFrom().voidItem()));
        });
        uni.await().atMost(Duration.ofMinutes(2));
    }

    @Test
    @Order(4)
    public void testCreateInvolvedParty_OrganicTrue_and_FindAllByIdentificationType() {
        var uni = sessionFactory.withTransaction(session -> {
            IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
            return getActivityMasterSystem(session)
                    .chain(sys -> {
                        String idType = "QA_NationalID2";
                        String idValue = "QA-NAT-123456";
                        return partyService.createIdentificationType(session, (ISystems<?, ?>) sys, idType, "National Identification")
                                .chain(() -> partyService.createType(session, (ISystems<?, ?>) sys, "QA_Person2", "Person Type"))
                                .chain(() -> partyService.createNameType(session, "QA_DisplayName2", "Display Name Type", (ISystems<?, ?>) sys))
                                .chain(() -> partyService.create(session, (ISystems<?, ?>) sys, new Pair<>(idType, idValue), true))
                                .chain(created -> partyService.findAllByIdentificationType(session, idType, idValue)
                                        .invoke(list -> {
                                            assertNotNull(list, "findAllByIdentificationType should not return null");
                                            Assertions.assertFalse(((java.util.List<?>) list).isEmpty(), "findAllByIdentificationType should return at least one party");
                                        })
                                        .replaceWith(Uni.createFrom().voidItem())
                                );
                    });
        });
        uni.await().atMost(Duration.ofMinutes(2));
    }
}
