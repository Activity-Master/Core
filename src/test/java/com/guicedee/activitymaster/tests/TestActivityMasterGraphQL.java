package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.IRulesService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end database integration tests for the FSDM GraphQL layer.
 *
 * <p>This populates one (or more) records in every one of the seven FSDM domains and then
 * exercises the real {@code graphql.GraphQL} pipeline built by {@code GraphQLModule} — including
 * the {@code VertxFutureAdapter} instrumentation — driving each query through
 * {@code FsdmGraphQLSchemaProvider}, {@code QueryBuilderSCD.applyQuerySpec(...)},
 * {@code SessionUtils.withActivityMaster(...)} and the reactive Hibernate session against the
 * Testcontainers PostgreSQL instance.</p>
 *
 * <p>Coverage:</p>
 * <ul>
 *     <li>each domain query returns scalar-flattened rows under the enterprise/system scope</li>
 *     <li>pagination ({@code max}) and ordering ({@code orderBy})</li>
 *     <li>value-based dynamic filtering (Equals on a String column)</li>
 *     <li>value-less dynamic filtering (Null / NotNull)</li>
 *     <li>security scoping rejects an unknown enterprise</li>
 * </ul>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterGraphQL extends TestDatabaseSetup
{
    private static final String ENTERPRISE = TestEnterprise.name();
    private static final String SYSTEM = ISystemsService.ActivityMasterSystemName;
    private static final String NO_CLASSIFICATION = DefaultClassifications.NoClassification.name();

    protected Mutiny.SessionFactory sessionFactory;
    protected GraphQL graphQL;

    @BeforeAll
    public void setup()
    {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(ENTERPRISE);
        IGuiceContext.instance();

        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        graphQL = IGuiceContext.get(GraphQL.class);
        assertNotNull(graphQL, "GraphQL instance should be assembled from the schema providers");

        ensureEnterpriseAndSystem();
        populateAllDomains();
    }

    /** Ensures the enterprise and the Activity Master system exist (idempotent) — stateless pipeline, no bridge. */
    private void ensureEnterpriseAndSystem()
    {
        // createNewEnterprise creates the record + installs/registers every system via the stateless
        // registerSystem path; startNewEnterprise then seeds the admin + post-startups.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, ENTERPRISE)
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(ENTERPRISE);
                            ent.setDescription("Enterprise for GraphQL tests");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, ENTERPRISE, "admin", "adminadmin!@"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(2));
    }

    /** Creates at least one record in each of the seven FSDM domains (plus a second arrangement). */
    private void populateAllDomains()
    {
        IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
        IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
        IEventService<?> eventService = IGuiceContext.get(IEventService.class);
        IProductService<?> productService = IGuiceContext.get(IProductService.class);
        IResourceItemService<?> resourceService = IGuiceContext.get(IResourceItemService.class);
        IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
        IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);

        // ---- Phase 1: taxonomy / types (each committed independently) -------------------------
        inTx((s, sys) -> partyService.createIdentificationType(s, sys, "GQL_NationalID", "National Identification")
                .chain(() -> partyService.createType(s, sys, "Person", "Person Type")));
        inTx((s, sys) -> arrangementsService.createArrangementType(s, "GQL_OrderType", sys));
        inTx((s, sys) -> eventService.createEventType(s, "GQL_EventType", sys));
        inTx((s, sys) -> productService.createProductType(s, "GQL_GadgetType", "Gadget Type", sys));
        inTx((s, sys) -> resourceService.createType(s, "GQL_FileType", "GQL_FileType", sys));
        inTx((s, sys) -> classificationService.create(s, "GQL_Classy_Filter", "filter target",
                EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys));

        // PostgreSQL's current_timestamp is the *transaction start* time and the type look-ups below
        // filter on "effectiveFromDate <= current_timestamp". Pause so the freshly committed taxonomy
        // rows' effective-from timestamps fall safely in the past relative to the instance-creating
        // transactions, avoiding a millisecond-boundary NoResult race.
        sleepQuietly(2000);

        // ---- Phase 2: instances (each committed independently) --------------------------------
        inTx((s, sys) -> partyService.create(s, sys, new Pair<>("GQL_NationalID", "GQL-PTY-1"), true));
        inTx((s, sys) -> arrangementsService.create(s, "GQL_OrderType", null, NO_CLASSIFICATION, "GQL-ARR-1", sys)
                .chain(a -> arrangementsService.create(s, "GQL_OrderType", null, NO_CLASSIFICATION, "GQL-ARR-2", sys)));
        inTx((s, sys) -> eventService.createEvent(s, "GQL_EventType", sys));
        inTx((s, sys) -> productService.createProduct(s, "GQL_GadgetType", "GQL_Widget", "Widget Desc", "GQL-WID-1", sys));
        inTx((s, sys) -> resourceService.create(s, "GQL_FileType", "GQL-res-1", sys));
        inTx((s, sys) -> rulesService.createRules(s, "GQL_RuleType", "GQL_Rule_1", "desc", sys));
    }

    private static void sleepQuietly(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs the given work inside a fresh committed transaction, resolving the enterprise and the
     * Activity Master system first. Each domain is created in its own transaction so the session
     * stays clean between domains, while type and instance creation remain chained within it.
     */
    private void inTx(BiFunction<Mutiny.Session, ISystems<?, ?>, Uni<?>> work)
    {
        sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            return enterpriseService.getEnterprise(session, ENTERPRISE)
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> work.apply(session, (ISystems<?, ?>) sys).replaceWithVoid());
        })).await().atMost(Duration.ofMinutes(2));
    }

    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    private Map<String, Object> baseQuery()
    {
        Map<String, Object> q = new HashMap<>();
        q.put("enterprise", ENTERPRISE);
        q.put("system", SYSTEM);
        return q;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> runDomainQuery(String field, Map<String, Object> queryInput)
    {
        String document = "query Run($q: QueryInput!) { " + field + "(query: $q) }";
        ExecutionInput input = ExecutionInput.newExecutionInput()
                .query(document)
                .variables(Map.of("q", queryInput))
                .build();

        ExecutionResult result;
        try
        {
            result = graphQL.executeAsync(input).get(2, TimeUnit.MINUTES);
        }
        catch (Exception e)
        {
            throw new RuntimeException("GraphQL execution failed for '" + field + "'", e);
        }
        assertTrue(result.getErrors().isEmpty(), () -> "GraphQL errors for '" + field + "': " + result.getErrors());

        Map<String, Object> data = result.getData();
        assertNotNull(data, "GraphQL data should not be null for '" + field + "'");
        return (List<Map<String, Object>>) data.get(field);
    }

    private void assertScalarRows(String field, List<Map<String, Object>> rows, int minSize)
    {
        assertNotNull(rows, field + " should return a list");
        assertTrue(rows.size() >= minSize, field + " should return at least " + minSize + " row(s), was " + rows.size());
        Map<String, Object> first = rows.get(0);
        assertFalse(first.isEmpty(), field + " rows should be serialised with scalar columns");
        assertTrue(first.containsKey("effectiveFromDate"),
                field + " rows should expose the SCD scalar column 'effectiveFromDate', keys=" + first.keySet());
    }

    // ---------------------------------------------------------------------------------------------
    // Per-domain mapping tests
    // ---------------------------------------------------------------------------------------------

    @Test
    @Order(1)
    public void involvedPartiesQueryReturnsRows()
    {
        assertScalarRows("involvedParties", runDomainQuery("involvedParties", baseQuery()), 1);
    }

    @Test
    @Order(2)
    public void arrangementsQueryReturnsRows()
    {
        assertScalarRows("arrangements", runDomainQuery("arrangements", baseQuery()), 2);
    }

    @Test
    @Order(3)
    public void eventsQueryReturnsRows()
    {
        assertScalarRows("events", runDomainQuery("events", baseQuery()), 1);
    }

    @Test
    @Order(4)
    public void productsQueryReturnsRows()
    {
        assertScalarRows("products", runDomainQuery("products", baseQuery()), 1);
    }

    @Test
    @Order(5)
    public void resourceItemsQueryReturnsRows()
    {
        assertScalarRows("resourceItems", runDomainQuery("resourceItems", baseQuery()), 1);
    }

    @Test
    @Order(6)
    public void classificationsQueryReturnsRows()
    {
        assertScalarRows("classifications", runDomainQuery("classifications", baseQuery()), 1);
    }

    @Test
    @Order(7)
    public void rulesQueryReturnsRows()
    {
        assertScalarRows("rules", runDomainQuery("rules", baseQuery()), 1);
    }

    // ---------------------------------------------------------------------------------------------
    // Pagination / ordering / filtering / security
    // ---------------------------------------------------------------------------------------------

    @Test
    @Order(10)
    public void paginationLimitsResults()
    {
        Map<String, Object> q = baseQuery();
        q.put("max", 1);
        List<Map<String, Object>> rows = runDomainQuery("arrangements", q);
        assertEquals(1, rows.size(), "max=1 should return exactly one arrangement");
    }

    @Test
    @Order(11)
    public void orderingExecutesAndReturnsRows()
    {
        Map<String, Object> q = baseQuery();
        q.put("orderBy", "effectiveFromDate");
        q.put("descending", true);
        List<Map<String, Object>> rows = runDomainQuery("arrangements", q);
        assertTrue(rows.size() >= 2, "ordered arrangements should still return all rows");
    }

    @Test
    @Order(12)
    public void filterByStringColumnEquals()
    {
        Map<String, Object> q = baseQuery();
        q.put("filters", List.of(Map.of(
                "path", "name",
                "operand", "Equals",
                "value", "GQL_Classy_Filter")));
        List<Map<String, Object>> rows = runDomainQuery("classifications", q);
        assertFalse(rows.isEmpty(), "Equals filter should match the known classification");
        for (Map<String, Object> row : rows)
        {
            assertEquals("GQL_Classy_Filter", row.get("name"),
                    "Every filtered row must match the requested name");
        }
    }

    @Test
    @Order(13)
    public void filterNotNullReturnsRows()
    {
        Map<String, Object> q = baseQuery();
        q.put("filters", List.of(Map.of(
                "path", "effectiveFromDate",
                "operand", "NotNull")));
        List<Map<String, Object>> rows = runDomainQuery("arrangements", q);
        assertTrue(rows.size() >= 2, "NotNull on effectiveFromDate should match all arrangements");
    }

    @Test
    @Order(14)
    public void filterNullReturnsNoRows()
    {
        Map<String, Object> q = baseQuery();
        q.put("filters", List.of(Map.of(
                "path", "effectiveFromDate",
                "operand", "Null")));
        List<Map<String, Object>> rows = runDomainQuery("arrangements", q);
        assertTrue(rows.isEmpty(), "Null on the always-populated effectiveFromDate should match nothing");
    }

    @Test
    @Order(20)
    public void unknownEnterpriseIsRejected()
    {
        Map<String, Object> q = baseQuery();
        q.put("enterprise", "GQL_Nonexistent_Enterprise");

        String document = "query Run($q: QueryInput!) { arrangements(query: $q) }";
        ExecutionInput input = ExecutionInput.newExecutionInput()
                .query(document)
                .variables(Map.of("q", q))
                .build();

        ExecutionResult result;
        try
        {
            result = graphQL.executeAsync(input).get(2, TimeUnit.MINUTES);
        }
        catch (Exception e)
        {
            // A propagated failure is an acceptable rejection of an unknown enterprise scope.
            return;
        }
        assertFalse(result.getErrors().isEmpty(),
                "Querying an unknown enterprise must surface a GraphQL error rather than data");
    }
}






