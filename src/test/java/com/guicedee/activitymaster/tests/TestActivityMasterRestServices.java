package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.rest.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.rest.rules.*;
import com.guicedee.activitymaster.fsdm.rest.classification.ClassificationDataConceptRestService;
import com.guicedee.activitymaster.fsdm.rest.classification.ClassificationRestService;
import com.guicedee.activitymaster.fsdm.rest.rules.RulesRestService;
import com.guicedee.activitymaster.fsdm.rest.rules.RulesTypeRestService;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.LogUtils;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.BadRequestException;
import org.apache.logging.log4j.Level;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the FSDM REST services that were added for the Rules and Classification
 * domains — {@link RulesRestService}, {@link RulesTypeRestService}, {@link ClassificationRestService}
 * and {@link ClassificationDataConceptRestService}.
 *
 * <p>These exercise the resource classes the same way the Vert.x router does: the Guice-managed
 * resource is obtained from {@link IGuiceContext}, then its {@code find} / {@code create} / {@code update}
 * methods are invoked directly with the path parameters (enterprise + requesting system) and the
 * request DTO. The resources self-manage their reactive session via {@code SessionUtils.withActivityMaster},
 * so no surrounding transaction is needed. Assertions focus on the synchronous response contract — the
 * generated id and the immediately echoed DTO state — because relationship persistence is intentionally
 * fire-and-forget.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterRestServices
{
    private static final String SYSTEM = ISystemsService.ActivityMasterSystemName;

    private Mutiny.SessionFactory sessionFactory;

    private String enterprise()
    {
        return TestEnterprise.name();
    }

    @BeforeAll
    public void setup()
    {
        LogUtils.addConsoleLogger(Level.INFO);
        ActivityMasterConfiguration.get()
                .setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();

        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Ensure the enterprise and Activity Master system exist for this class context.
        sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .onFailure().recoverWithUni(t -> {
                        var ent = enterpriseService.get();
                        ent.setName(TestEnterprise.name());
                        ent.setDescription("Enterprise for REST service tests");
                        return enterpriseService.createNewEnterprise(session, ent)
                                .chain(enter -> enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "adminadmin!@"));
                    })
                    .chain(ent -> systemsService.getActivityMaster(session,
                                    (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent)
                            .onFailure().recoverWithUni(t -> systemsService.create(session,
                                    (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?>) ent,
                                    ISystemsService.ActivityMasterSystemName, "Activity Master System")))
                    .replaceWith(Uni.createFrom().voidItem());
        })).await().atMost(Duration.ofMinutes(2));
    }

    private static <T> T await(Uni<T> uni)
    {
        return uni.await().atMost(Duration.ofMinutes(2));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Classification
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    public void classificationCreateFindUpdate()
    {
        ClassificationRestService rest = IGuiceContext.get(ClassificationRestService.class);
        assertNotNull(rest, "ClassificationRestService should be injectable");

        ClassificationCreateDTO create = new ClassificationCreateDTO();
        create.name = "REST_Classy_1";
        create.description = "rest classification";
        create.concept = EnterpriseClassificationDataConcepts.NoClassificationDataConceptName.name();
        create.sequenceNumber = 5;

        ClassificationDTO created = await(rest.create(enterprise(), SYSTEM, create));
        assertNotNull(created, "create should echo a DTO");
        assertNotNull(created.classificationId, "create should return a generated classification id");
        assertEquals("REST_Classy_1", created.name, "create should echo the submitted name");

        UUID id = created.classificationId;

        ClassificationFindDTO find = new ClassificationFindDTO();
        find.classificationId = id;
        find.includes = List.of(ClassificationDataIncludes.Children);

        ClassificationDTO found = await(rest.find(enterprise(), SYSTEM, find));
        assertNotNull(found, "find should return the classification");
        assertEquals(id, found.classificationId, "find should return the same id");
        assertEquals("REST_Classy_1", found.name, "find should return the persisted name");

        ClassificationUpdateDTO update = new ClassificationUpdateDTO();
        update.classificationId = id;
        update.description = "rest classification updated";
        update.sequenceNumber = 9;

        ClassificationDTO updated = await(rest.update(enterprise(), SYSTEM, update));
        assertNotNull(updated, "update should echo a DTO");
        assertEquals(id, updated.classificationId, "update should echo the id");
        assertEquals("rest classification updated", updated.description, "update should echo the new description");
    }

    @Test
    @Order(2)
    public void classificationFindUnknownIdFails()
    {
        ClassificationRestService rest = IGuiceContext.get(ClassificationRestService.class);
        ClassificationFindDTO find = new ClassificationFindDTO();
        find.classificationId = UUID.randomUUID();
        find.includes = List.of();

        assertThrows(RuntimeException.class,
                () -> await(rest.find(enterprise(), SYSTEM, find)),
                "Finding a non-existent classification id should fail");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Classification Data Concept
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(3)
    public void dataConceptCreateAndFind()
    {
        ClassificationDataConceptRestService rest = IGuiceContext.get(ClassificationDataConceptRestService.class);
        assertNotNull(rest, "ClassificationDataConceptRestService should be injectable");

        ClassificationDataConceptCreateDTO create = new ClassificationDataConceptCreateDTO();
        create.name = EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem.name();
        create.description = "rest data concept";

        ClassificationDataConceptDTO created = await(rest.create(enterprise(), SYSTEM, create));
        assertNotNull(created, "create should echo a DTO");
        assertNotNull(created.conceptId, "create should return a concept id");

        ClassificationDataConceptFindDTO find = new ClassificationDataConceptFindDTO();
        find.conceptId = created.conceptId;
        find.includes = List.of();

        ClassificationDataConceptDTO found = await(rest.find(enterprise(), SYSTEM, find));
        assertNotNull(found, "find should return the concept");
        assertEquals(created.conceptId, found.conceptId, "find should return the same concept id");
    }

    @Test
    @Order(4)
    public void dataConceptCreateUnknownNameFails()
    {
        ClassificationDataConceptRestService rest = IGuiceContext.get(ClassificationDataConceptRestService.class);

        ClassificationDataConceptCreateDTO create = new ClassificationDataConceptCreateDTO();
        create.name = "Definitely_Not_A_Known_Concept_ZZZ";
        create.description = "should be rejected";

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> await(rest.create(enterprise(), SYSTEM, create)),
                "Creating a concept with an unknown name should be rejected with 400");
        assertTrue(ex.getMessage() == null || ex.getMessage().contains("Unknown"),
                "The rejection should reference the unknown concept name");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Rule Types
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(5)
    public void ruleTypeCreateFindUpdate()
    {
        RulesTypeRestService rest = IGuiceContext.get(RulesTypeRestService.class);
        assertNotNull(rest, "RulesTypeRestService should be injectable");

        RulesTypeCreateDTO create = new RulesTypeCreateDTO();
        create.name = "REST_RuleType_1";
        create.description = "rest rule type";

        RulesTypeDTO created = await(rest.create(enterprise(), SYSTEM, create));
        assertNotNull(created, "create should echo a DTO");
        assertNotNull(created.rulesTypeId, "create should return a generated rule type id");
        assertEquals("REST_RuleType_1", created.name, "create should echo the submitted name");

        UUID id = created.rulesTypeId;

        RulesTypeFindDTO find = new RulesTypeFindDTO();
        find.rulesTypeId = id;
        find.includes = List.of(RulesTypeDataIncludes.Classifications);

        RulesTypeDTO found = await(rest.find(enterprise(), SYSTEM, find));
        assertNotNull(found, "find should return the rule type");
        assertEquals(id, found.rulesTypeId, "find should return the same id");
        assertEquals("REST_RuleType_1", found.name, "find should return the persisted name");

        RulesTypeUpdateDTO update = new RulesTypeUpdateDTO();
        update.rulesTypeId = id;
        update.description = "rest rule type updated";

        RulesTypeDTO updated = await(rest.update(enterprise(), SYSTEM, update));
        assertNotNull(updated, "update should echo a DTO");
        assertEquals(id, updated.rulesTypeId, "update should echo the id");
        assertEquals("rest rule type updated", updated.description, "update should echo the new description");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Rules
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(6)
    public void ruleCreateFindUpdate()
    {
        // Ensure the rule type referenced by the rule exists (reuse-by-name is idempotent).
        RulesTypeRestService ruleTypeRest = IGuiceContext.get(RulesTypeRestService.class);
        RulesTypeCreateDTO typeCreate = new RulesTypeCreateDTO();
        typeCreate.name = "REST_RuleType_ForRule";
        typeCreate.description = "rule type for rule test";
        await(ruleTypeRest.create(enterprise(), SYSTEM, typeCreate));

        RulesRestService rest = IGuiceContext.get(RulesRestService.class);
        assertNotNull(rest, "RulesRestService should be injectable");

        RulesCreateDTO create = new RulesCreateDTO();
        create.name = "REST_Rule_1";
        create.description = "rest rule";
        create.ruleTypes = Map.of("REST_RuleType_ForRule", "RULE-VAL-1");

        RulesDTO created = await(rest.create(enterprise(), SYSTEM, create));
        assertNotNull(created, "create should echo a DTO");
        assertNotNull(created.rulesId, "create should return a generated rule id");
        assertEquals("REST_Rule_1", created.name, "create should echo the submitted name");
        assertNotNull(created.ruleTypes, "create should echo the submitted rule types");
        assertEquals("RULE-VAL-1", created.ruleTypes.get("REST_RuleType_ForRule"),
                "create should echo the submitted rule type value");

        UUID id = created.rulesId;

        RulesFindDTO find = new RulesFindDTO();
        find.rulesId = id;
        find.includes = List.of();

        RulesDTO found = await(rest.find(enterprise(), SYSTEM, find));
        assertNotNull(found, "find should return the rule");
        assertEquals(id, found.rulesId, "find should return the same id");
        assertEquals("REST_Rule_1", found.name, "find should return the persisted name");

        RulesUpdateDTO update = new RulesUpdateDTO();
        update.rulesId = id;
        update.description = "rest rule updated";

        RulesDTO updated = await(rest.update(enterprise(), SYSTEM, update));
        assertNotNull(updated, "update should echo a DTO");
        assertEquals(id, updated.rulesId, "update should echo the id");
        assertEquals("rest rule updated", updated.description, "update should echo the new description");
    }

    @Test
    @Order(7)
    public void ruleFindUnknownIdFails()
    {
        RulesRestService rest = IGuiceContext.get(RulesRestService.class);
        RulesFindDTO find = new RulesFindDTO();
        find.rulesId = UUID.randomUUID();
        find.includes = List.of();

        assertThrows(RuntimeException.class,
                () -> await(rest.find(enterprise(), SYSTEM, find)),
                "Finding a non-existent rule id should fail");
    }
}

