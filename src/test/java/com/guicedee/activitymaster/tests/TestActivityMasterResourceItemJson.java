package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.ResourceItemJsonStore;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemDataValue;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for storing JSON resource-item data in MongoDB.
 * <p>
 * Verifies that when a resource item's type is a JSON type ({@link ResourceItemTypes#JsonPacket}):
 * <ol>
 *   <li>the payload is written to MongoDB (and the relational value row is left empty);</li>
 *   <li>{@code ResourceItem.getData} reads the payload back from MongoDB;</li>
 *   <li>the document can be located with native Mongo lookup criteria via
 *       {@link IResourceItemService#findJsonResourceData(JsonObject)};</li>
 *   <li>updating the data replaces the MongoDB document.</li>
 * </ol>
 * The MongoDB instance is provisioned by {@link com.guicedee.activitymaster.MongoTestDBModule} (Testcontainers).
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterResourceItemJson
{
    private Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup()
    {
        // MongoDB-backed JSON storage is opt-in; enable it for this integration test.
        System.setProperty("RESOURCE_ITEM_JSON_STORE", "true");
        com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.get()
                .setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Provision the enterprise on the stateless pipeline (no bridge). createNewEnterprise creates the
        // record + installs/registers every system via the stateless registerSystem path; startNewEnterprise
        // then seeds the admin + post-startups. Idempotent: create only when absent, always start.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(TestEnterprise.name());
                            ent.setDescription("Enterprise for JSON resource item tests");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "adminadmin!@"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(2));
    }

    @Test
    public void testMongoStoreIsEnabled()
    {
        ResourceItemJsonStore jsonStore = IGuiceContext.get(ResourceItemJsonStore.class);
        assertTrue(jsonStore.isEnabled(), "MongoDB-backed JSON store should be enabled when the test Mongo container is running");
        assertTrue(jsonStore.isJsonType(ResourceItemTypes.JsonPacket.name()), "JsonPacket should be recognised as a JSON type");
        assertTrue(jsonStore.isJsonType("MyJsonPayload"), "Type names containing 'json' should be recognised as JSON");
        assertFalse(jsonStore.isJsonType("Documents"), "Non-JSON type names should not be recognised as JSON");
    }

    @Test
    public void testStoreFetchAndLookupJsonResourceData()
    {
        final String json = "{\"title\":\"The Hobbit\",\"author\":\"J. R. R. Tolkien\",\"year\":1937}";

        // TX1: create a JSON resource item.
        UUID resourceId = sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> resourceItemService.createType(session, ResourceItemTypes.JsonPacket, (ISystems<?, ?>) sys)
                            .chain(t -> resourceItemService.create(session, ResourceItemTypes.JsonPacket.name(), "the-hobbit",
                                    json.getBytes(StandardCharsets.UTF_8), (ISystems<?, ?>) sys))
                            .map(ri -> ri.getId()));
        })).await().atMost(Duration.ofMinutes(2));

        assertNotNull(resourceId, "JSON resource item should be created");

        // TX2: the relational value row should be empty (the payload lives in MongoDB).
        byte[] relationalBytes = sessionFactory.withSession(session ->
                session.find(ResourceItemDataValue.class, resourceId)
                        .map(dv -> dv == null ? null : dv.getData())
        ).await().atMost(Duration.ofMinutes(1));
        assertNotNull(relationalBytes, "ResourceItemDataValue row should still exist for the JSON resource item");
        assertEquals(0, relationalBytes.length, "JSON payload should not be stored in the relational column");

        // TX3: getData should read the payload back from MongoDB.
        byte[] fetched = sessionFactory.withSession(session -> {
            ResourceItem ri = new ResourceItem();
            ri.setId(resourceId);
            return ri.getData(session);
        }).await().atMost(Duration.ofMinutes(1));
        assertNotNull(fetched, "getData should return the JSON payload");
        JsonObject fetchedJson = new JsonObject(new String(fetched, StandardCharsets.UTF_8));
        assertEquals("The Hobbit", fetchedJson.getString("title"));
        assertEquals("J. R. R. Tolkien", fetchedJson.getString("author"));
        assertEquals(1937, (int) fetchedJson.getInteger("year"));

        // TX4: locate the document with native Mongo lookup criteria.
        List<JsonObject> hits = sessionFactory.withSession(session -> {
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return resourceItemService.findJsonResourceData(new JsonObject().put("author", "J. R. R. Tolkien"));
        }).await().atMost(Duration.ofMinutes(1));
        assertNotNull(hits, "Lookup should return a list");
        assertTrue(hits.stream().anyMatch(d -> resourceId.toString().equals(d.getString(ResourceItemJsonStore.RESOURCE_ID_FIELD))),
                "Lookup by author should locate the stored JSON resource item");
    }

    @Test
    public void testUpdateJsonResourceData()
    {
        final String original = "{\"status\":\"draft\",\"version\":1}";
        final String updated = "{\"status\":\"published\",\"version\":2}";

        UUID resourceId = sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> resourceItemService.createType(session, ResourceItemTypes.JsonPacket, (ISystems<?, ?>) sys)
                            .chain(t -> resourceItemService.create(session, ResourceItemTypes.JsonPacket.name(), "doc-status",
                                    original.getBytes(StandardCharsets.UTF_8), (ISystems<?, ?>) sys))
                            .map(ri -> ri.getId()));
        })).await().atMost(Duration.ofMinutes(2));

        assertNotNull(resourceId);

        // Update the data — should replace the MongoDB document.
        sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return resourceItemService.updateResourceData(session, updated.getBytes(StandardCharsets.UTF_8), resourceId,
                    ISystemsService.ActivityMasterSystemName);
        })).await().atMost(Duration.ofMinutes(1));

        byte[] fetched = sessionFactory.withSession(session -> {
            ResourceItem ri = new ResourceItem();
            ri.setId(resourceId);
            return ri.getData(session);
        }).await().atMost(Duration.ofMinutes(1));
        assertNotNull(fetched);
        JsonObject fetchedJson = new JsonObject(new String(fetched, StandardCharsets.UTF_8));
        assertEquals("published", fetchedJson.getString("status"), "Updated status should be read back from MongoDB");
        assertEquals(2, (int) fetchedJson.getInteger("version"), "Updated version should be read back from MongoDB");
    }

    @Test
    public void testPartialFieldAndChildUpdates()
    {
        final String json = "{\"status\":\"draft\",\"meta\":{},\"history\":[]}";

        UUID resourceId = sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> resourceItemService.createType(session, ResourceItemTypes.JsonPacket, (ISystems<?, ?>) sys)
                            .chain(t -> resourceItemService.create(session, ResourceItemTypes.JsonPacket.name(), "partial-doc",
                                    json.getBytes(StandardCharsets.UTF_8), (ISystems<?, ?>) sys))
                            .map(ri -> ri.getId()));
        })).await().atMost(Duration.ofMinutes(2));
        assertNotNull(resourceId);

        IResourceItemService<?> service = IGuiceContext.get(IResourceItemService.class);

        // Set a top-level field, a nested child field (dot-notation), and append an array child.
        sessionFactory.withSession(session -> service.updateJsonResourceField(resourceId, "status", "active"))
                .await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> service.updateJsonResourceField(resourceId, "meta.reviewer", "alice"))
                .await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> service.addJsonResourceChild(resourceId, "history",
                new JsonObject().put("action", "created"))).await().atMost(Duration.ofMinutes(1));

        JsonObject afterUpdate = fetchJson(resourceId);
        assertEquals("active", afterUpdate.getString("status"), "Top-level field should be updated");
        assertEquals("alice", afterUpdate.getJsonObject("meta").getString("reviewer"), "Nested child field should be set");
        assertEquals(1, afterUpdate.getJsonArray("history").size(), "Child should be appended to the array");
        assertEquals("created", afterUpdate.getJsonArray("history").getJsonObject(0).getString("action"));

        // Remove the nested field and pull the array child back out.
        sessionFactory.withSession(session -> service.removeJsonResourceField(resourceId, "meta.reviewer"))
                .await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> service.removeJsonResourceChild(resourceId, "history",
                new JsonObject().put("action", "created"))).await().atMost(Duration.ofMinutes(1));

        JsonObject afterRemoval = fetchJson(resourceId);
        assertFalse(afterRemoval.getJsonObject("meta").containsKey("reviewer"), "Nested field should be removed");
        assertTrue(afterRemoval.getJsonArray("history").isEmpty(), "Child should be pulled from the array");
    }

    @Test
    public void testNamedCollectionLookup()
    {
        final String json = "{\"sku\":\"SKU-NAMED-1\",\"qty\":7}";

        UUID resourceId = sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> resourceItemService.createType(session, ResourceItemTypes.JsonPacket, (ISystems<?, ?>) sys)
                            .chain(t -> resourceItemService.create(session, ResourceItemTypes.JsonPacket.name(), "named-doc",
                                    json.getBytes(StandardCharsets.UTF_8), (ISystems<?, ?>) sys))
                            .map(ri -> ri.getId()));
        })).await().atMost(Duration.ofMinutes(2));
        assertNotNull(resourceId);

        ResourceItemJsonStore jsonStore = IGuiceContext.get(ResourceItemJsonStore.class);
        String collection = jsonStore.getDefaultCollection();

        IResourceItemService<?> service = IGuiceContext.get(IResourceItemService.class);
        List<JsonObject> hits = sessionFactory.withSession(session ->
                service.findJsonResourceData(collection, new JsonObject().put("sku", "SKU-NAMED-1"))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(hits);
        assertTrue(hits.stream().anyMatch(d -> resourceId.toString().equals(d.getString(ResourceItemJsonStore.RESOURCE_ID_FIELD))),
                "Named-collection lookup by field should locate the stored document");
    }

    @Test
    public void testFarmJsonResourceFluentApi()
    {
        // Create a "Farm" JSON resource item, then build up its document with the fluent entity API.
        UUID farmId = sessionFactory.withSession(session -> session.withTransaction(tx -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            IResourceItemService<?> resourceItemService = IGuiceContext.get(IResourceItemService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> resourceItemService.createType(session, ResourceItemTypes.JsonPacket, (ISystems<?, ?>) sys)
                            .chain(t -> resourceItemService.create(session, ResourceItemTypes.JsonPacket.name(), "Farm",
                                    "{\"name\":\"Farm\"}".getBytes(StandardCharsets.UTF_8), (ISystems<?, ?>) sys))
                            .map(ri -> ri.getId()));
        })).await().atMost(Duration.ofMinutes(2));
        assertNotNull(farmId);

        ResourceItem farm = new ResourceItem();
        farm.setId(farmId);

        java.util.Map<String, Object> owner = new java.util.HashMap<>();
        owner.put("name", "Alice");
        owner.put("since", 1998);

        java.util.Map<String, Object> more = new java.util.HashMap<>();
        more.put("region", "Free State");
        more.put("hectares", 48.5);

        // String / int / boolean / list / object — all via storeField, plus a multi-field store and a child push.
        sessionFactory.withSession(session -> farm.storeField("acres", 120)).await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> farm.storeField("organic", true)).await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> farm.storeField("animals", List.of("cow", "sheep", "goat"))).await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> farm.storeField("owner", owner)).await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> farm.storeFields(more)).await().atMost(Duration.ofMinutes(1));
        sessionFactory.withSession(session -> farm.addJsonChild("crops", new JsonObject().put("name", "maize"))).await().atMost(Duration.ofMinutes(1));

        JsonObject doc = sessionFactory.withSession(session -> farm.getJson()).await().atMost(Duration.ofMinutes(1));
        assertNotNull(doc, "Farm JSON document should be readable");
        assertEquals("Farm", doc.getString("name"));
        assertEquals(120, (int) doc.getInteger("acres"), "int field");
        assertEquals(Boolean.TRUE, doc.getBoolean("organic"), "boolean field");
        assertEquals(3, doc.getJsonArray("animals").size(), "list field");
        assertEquals("cow", doc.getJsonArray("animals").getString(0));
        assertEquals("Alice", doc.getJsonObject("owner").getString("name"), "object field");
        assertEquals(1998, (int) doc.getJsonObject("owner").getInteger("since"));
        assertEquals("Free State", doc.getString("region"), "multi-field store");
        assertEquals(48.5, doc.getDouble("hectares"), 0.0001, "multi-field store (double)");
        assertEquals("maize", doc.getJsonArray("crops").getJsonObject(0).getString("name"), "child push");

        // Remove a field.
        sessionFactory.withSession(session -> farm.removeField("organic")).await().atMost(Duration.ofMinutes(1));
        JsonObject afterRemoval = sessionFactory.withSession(session -> farm.getJson()).await().atMost(Duration.ofMinutes(1));
        assertFalse(afterRemoval.containsKey("organic"), "removed field should be gone");
    }

    private JsonObject fetchJson(UUID resourceId)
    {
        byte[] fetched = sessionFactory.withSession(session -> {
            ResourceItem ri = new ResourceItem();
            ri.setId(resourceId);
            return ri.getData(session);
        }).await().atMost(Duration.ofMinutes(1));
        assertNotNull(fetched);
        return new JsonObject(new String(fetched, StandardCharsets.UTF_8));
    }
}




