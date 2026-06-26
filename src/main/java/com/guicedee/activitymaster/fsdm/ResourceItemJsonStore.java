package com.guicedee.activitymaster.fsdm;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemTypes;
import com.guicedee.client.Environment;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.UpdateOptions;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * MongoDB document store for resource-item data whose {@code ResourceItemType} is a JSON type.
 * <p>
 * When a resource item is created (or its data updated) and its type is a JSON type (by default
 * {@link ResourceItemTypes#JsonPacket}, or any type whose name contains {@code "json"}), the payload is
 * persisted as a document in MongoDB instead of the relational {@code resource.resourceitemdatavalue} column.
 * The relational {@code ResourceItemData}/{@code ResourceItemDataValue} rows are still written (with an empty
 * payload) so the FSDM security/SCD structure stays intact — only the bytes live in MongoDB.
 *
 * <h2>Collections — addressable by name</h2>
 * A document is stored keyed by its resource item id ({@code _id}) inside a <em>collection</em>. By default
 * every JSON resource item lands in the single default collection ({@code resourceitemdata_json}). Specific
 * resource-item types can be routed to their own named collections with {@code RESOURCE_ITEM_JSON_COLLECTIONS}
 * (e.g. {@code InvoiceJson=invoices,ContractJson=contracts}); use {@link #collectionForType(String)} to resolve
 * a type's collection and {@link #listCollections()} to discover the collections that exist. Id-keyed reads
 * transparently search every {@link #knownCollections() known collection}, so {@code ResourceItem.getData}
 * keeps working regardless of routing.
 *
 * <h2>Documents — addressable by id or name</h2>
 * Read a document by resource id ({@link #fetch(UUID)} / {@link #getById(String, String)}) or by a name field
 * ({@link #getByName(String, String)}), and run arbitrary Mongo criteria with {@link #find(JsonObject)} /
 * {@link #find(String, JsonObject)} / {@link #findByField(String, Object)}.
 *
 * <h2>Partial updates — fields and children</h2>
 * Rather than rewriting the whole document, individual fields and nested children can be mutated with MongoDB
 * update operators (dot-notation supports nesting and array indices):
 * <ul>
 *   <li>{@link #setField(UUID, String, Object)} / {@link #setFields(UUID, JsonObject)} — {@code $set}</li>
 *   <li>{@link #unsetField(UUID, String)} — {@code $unset}</li>
 *   <li>{@link #pushChild(UUID, String, Object)} — append a child to an array ({@code $push})</li>
 *   <li>{@link #pullChild(UUID, String, Object)} — remove matching children from an array ({@code $pull})</li>
 * </ul>
 *
 * <h2>Opt-in</h2>
 * MongoDB-backed JSON storage is <strong>opt-in</strong> and disabled by default. It is only activated when
 * {@code RESOURCE_ITEM_JSON_STORE=true} is set <em>and</em> a Vert.x
 * {@link com.guicedee.persistence.implementations.mongodb.MongoModule MongoModule}
 * (see {@code ActivityMasterMongoModule}) binds a shared {@code io.vertx.ext.mongo.MongoClient}. When the
 * opt-in flag is not set the store never resolves a client (so deployments without MongoDB are unaffected and
 * never trigger an unsatisfied-binding lookup) and callers transparently fall back to relational storage.
 */
@Log4j2
@Singleton
public class ResourceItemJsonStore
{
    /** Internal Mongo document key (also the {@code _id}). */
    public static final String RESOURCE_ID_FIELD = "_resourceItemId";
    /** Field used to wrap a non-object JSON payload (array / scalar / non-JSON bytes). */
    static final String RAW_PAYLOAD_FIELD = "_payload";
    /** Default field used by {@link #getByName(String, String)} when none is supplied. */
    public static final String DEFAULT_NAME_FIELD = "name";

    /**
     * Collection JSON resource-item payloads are stored in when their type is not explicitly routed.
     * Overridable with {@code RESOURCE_ITEM_JSON_COLLECTION} (default {@code resourceitemdata_json}).
     */
    private final String collectionName =
            Environment.getProperty("RESOURCE_ITEM_JSON_COLLECTION", "resourceitemdata_json");

    /**
     * Optional per-type collection routing, parsed from {@code RESOURCE_ITEM_JSON_COLLECTIONS}
     * (e.g. {@code InvoiceJson=invoices,ContractJson=contracts}). Keys are matched case-insensitively.
     */
    private final Map<String, String> typeCollections = parseTypeCollections();

    /**
     * The set of resource-item type names treated as JSON. Overridable with a comma-separated
     * {@code RESOURCE_ITEM_JSON_TYPES} (default {@code JsonPacket}). Any type whose name contains
     * {@code "json"} (case-insensitive) is also treated as JSON.
     */
    private final Set<String> jsonTypeNames = parseJsonTypeNames();

    /**
     * Master opt-in switch for routing JSON resource-item payloads to MongoDB. Resolved once from
     * {@code RESOURCE_ITEM_JSON_STORE} (default {@code false}). When {@code false} the store stays disabled and
     * never asks Guice for a {@code MongoClient}, so relational storage is used everywhere.
     */
    private final boolean optedIn = isOptedIn();

    private volatile io.vertx.ext.mongo.MongoClient mongoClient;
    private volatile boolean resolved = false;

    /**
     * Whether MongoDB-backed JSON storage has been explicitly opted into via {@code RESOURCE_ITEM_JSON_STORE}.
     *
     * @return {@code true} when {@code RESOURCE_ITEM_JSON_STORE=true}
     */
    private static boolean isOptedIn()
    {
        String enabled = Environment.getProperty("RESOURCE_ITEM_JSON_STORE", "false");
        return enabled != null && "true".equalsIgnoreCase(enabled.trim());
    }

    private static Set<String> parseJsonTypeNames()
    {
        Set<String> names = new LinkedHashSet<>();
        names.add(ResourceItemTypes.JsonPacket.name());
        String configured = Environment.getProperty("RESOURCE_ITEM_JSON_TYPES", "");
        if (configured != null && !configured.isBlank())
        {
            Arrays.stream(configured.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(names::add);
        }
        return Collections.unmodifiableSet(names);
    }

    private static Map<String, String> parseTypeCollections()
    {
        Map<String, String> map = new LinkedHashMap<>();
        String configured = Environment.getProperty("RESOURCE_ITEM_JSON_COLLECTIONS", "");
        if (configured != null && !configured.isBlank())
        {
            for (String entry : configured.split(","))
            {
                String[] kv = entry.split("=", 2);
                if (kv.length == 2 && !kv[0].isBlank() && !kv[1].isBlank())
                {
                    map.put(kv[0].trim().toLowerCase(), kv[1].trim());
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Availability + type detection
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Whether a MongoDB client is available (resolved lazily, and only when opted in). When {@code false} the
     * store is a no-op and callers fall back to relational storage.
     *
     * @return {@code true} when JSON storage is opted in and a {@code MongoClient} is bound
     */
    public boolean isEnabled()
    {
        if (!optedIn)
        {
            return false;
        }
        if (!resolved)
        {
            resolveClient();
        }
        return mongoClient != null;
    }

    private synchronized void resolveClient()
    {
        if (resolved)
        {
            return;
        }
        if (!optedIn)
        {
            // Not opted in — never ask Guice for a MongoClient so an unsatisfied binding is never surfaced.
            mongoClient = null;
            resolved = true;
            return;
        }
        try
        {
            // JIT lookup by fully-qualified name (no import / no reflection); resolves the bound client when present.
            mongoClient = IGuiceContext.get(io.vertx.ext.mongo.MongoClient.class);
            if (mongoClient != null)
            {
                log.info("🍃 ResourceItemJsonStore active — JSON resource items stored in MongoDB (default collection '{}', {} routed type(s))",
                        collectionName, typeCollections.size());
            }
        }
        catch (Throwable t)
        {
            log.debug("MongoClient not bound — JSON resource items will use relational storage ({})", t.getMessage());
            mongoClient = null;
        }
        resolved = true;
    }

    /**
     * Returns {@code true} when the given resource-item type name denotes a JSON payload.
     *
     * @param resourceItemTypeName the resource item type name (e.g. {@code JsonPacket})
     * @return {@code true} when the type should be stored as a JSON document in MongoDB
     */
    public boolean isJsonType(String resourceItemTypeName)
    {
        if (resourceItemTypeName == null)
        {
            return false;
        }
        String type = resourceItemTypeName.trim();
        if (type.isEmpty())
        {
            return false;
        }
        for (String json : jsonTypeNames)
        {
            if (json.equalsIgnoreCase(type))
            {
                return true;
            }
        }
        return type.toLowerCase().contains("json");
    }

    /**
     * Whether the JSON payload should be routed to MongoDB for the given resource type and data.
     *
     * @param resourceItemTypeName the resource item type name
     * @param data                 the payload bytes
     * @return {@code true} when MongoDB is enabled, the type is JSON and there is data to store
     */
    public boolean shouldStoreJson(String resourceItemTypeName, byte[] data)
    {
        return isEnabled() && isJsonType(resourceItemTypeName) && data != null && data.length > 0;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Collections — addressable by name
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Resolves the collection a JSON payload of the given resource-item type is stored in.
     *
     * @param resourceItemType the resource item type name (may be {@code null})
     * @return the configured collection for the type, or the default collection
     */
    public String collectionForType(String resourceItemType)
    {
        if (resourceItemType != null)
        {
            String mapped = typeCollections.get(resourceItemType.trim().toLowerCase());
            if (mapped != null)
            {
                return mapped;
            }
        }
        return collectionName;
    }

    /** @return the default collection used when a type is not explicitly routed. */
    public String getDefaultCollection()
    {
        return collectionName;
    }

    /** @return every collection this store may write JSON resource items to (default + routed types). */
    public Set<String> knownCollections()
    {
        Set<String> collections = new LinkedHashSet<>();
        collections.add(collectionName);
        collections.addAll(typeCollections.values());
        return collections;
    }

    /**
     * Lists the collections that currently exist in the MongoDB database (discovery by name).
     *
     * @return a Uni emitting the collection names, or an empty list when disabled
     */
    public Uni<List<String>> listCollections()
    {
        if (!isEnabled())
        {
            return Uni.createFrom().item(Collections.emptyList());
        }
        return toUni(mongoClient.getCollections());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Store (whole document)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Upserts the JSON payload for a resource item in the default collection. No-op when disabled.
     *
     * @param resourceItemId the resource item id (Mongo {@code _id})
     * @param data           the JSON payload bytes
     * @return a Uni completing when the document has been written
     */
    public Uni<Void> store(UUID resourceItemId, byte[] data)
    {
        return store(null, resourceItemId, data);
    }

    /**
     * Upserts the JSON payload for a resource item in the collection its type routes to.
     *
     * @param resourceItemType the resource item type (selects the collection; {@code null} = default)
     * @param resourceItemId   the resource item id (Mongo {@code _id})
     * @param data             the JSON payload bytes
     * @return a Uni completing when the document has been written
     */
    public Uni<Void> store(String resourceItemType, UUID resourceItemId, byte[] data)
    {
        if (!isEnabled() || resourceItemId == null)
        {
            return Uni.createFrom().voidItem();
        }
        String collection = collectionForType(resourceItemType);
        JsonObject document = toDocument(resourceItemId, data == null ? new byte[0] : data);
        return toUni(mongoClient.save(collection, document))
                .invoke(() -> log.debug("Stored JSON resource item {} in MongoDB collection '{}'", resourceItemId, collection))
                .replaceWithVoid();
    }

    /**
     * Upserts the JSON payload for a resource item, resolving the collection it already lives in (or the one
     * its type routes to). Used by the data-update path where only the id is known.
     *
     * @param session        the reactive session (used to resolve the resource type when needed)
     * @param resourceItemId the resource item id
     * @param data           the JSON payload bytes
     * @return a Uni completing when the document has been written
     */
    public Uni<Void> storeForResource(Mutiny.Session session, UUID resourceItemId, byte[] data)
    {
        if (!isEnabled() || resourceItemId == null)
        {
            return Uni.createFrom().voidItem();
        }
        return resolveCollection(session, resourceItemId)
                .chain(collection -> {
                    JsonObject document = toDocument(resourceItemId, data == null ? new byte[0] : data);
                    return toUni(mongoClient.save(collection, document)).replaceWithVoid();
                });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Read (by id or name)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fetches the JSON payload for a resource item, searching every {@link #knownCollections() known
     * collection}. Emits {@code null} when disabled or absent.
     *
     * @param resourceItemId the resource item id
     * @return a Uni emitting the payload bytes, or {@code null} when there is no MongoDB document
     */
    public Uni<byte[]> fetch(UUID resourceItemId)
    {
        if (!isEnabled() || resourceItemId == null)
        {
            return Uni.createFrom().nullItem();
        }
        return fetchDocument(resourceItemId).map(document -> document == null ? null : extractPayload(document));
    }

    /**
     * Fetches the JSON payload for a resource item as a {@link JsonObject}. Emits {@code null} when disabled or
     * absent.
     *
     * @param resourceItemId the resource item id
     * @return a Uni emitting the payload as a JSON object, or {@code null}
     */
    public Uni<JsonObject> fetchJson(UUID resourceItemId)
    {
        return fetch(resourceItemId).map(bytes -> {
            if (bytes == null || bytes.length == 0)
            {
                return null;
            }
            String json = new String(bytes, StandardCharsets.UTF_8);
            try
            {
                return new JsonObject(json);
            }
            catch (Exception notAnObject)
            {
                return new JsonObject().put(RAW_PAYLOAD_FIELD, json);
            }
        });
    }

    /**
     * Reads a document by its {@code _id} from a named collection.
     *
     * @param collection the collection name
     * @param id         the document id
     * @return a Uni emitting the document (without the Mongo {@code _id}), or {@code null}
     */
    public Uni<JsonObject> getById(String collection, String id)
    {
        if (!isEnabled() || collection == null || id == null)
        {
            return Uni.createFrom().nullItem();
        }
        JsonObject query = new JsonObject().put("_id", id);
        return toUni(mongoClient.findOne(collection, query, null)).map(this::strip);
    }

    /**
     * Reads the first document whose {@link #DEFAULT_NAME_FIELD name} field equals the given value.
     *
     * @param collection the collection name
     * @param name       the name to match
     * @return a Uni emitting the matching document, or {@code null}
     */
    public Uni<JsonObject> getByName(String collection, String name)
    {
        return getByField(collection, DEFAULT_NAME_FIELD, name);
    }

    /**
     * Reads the first document whose {@code field} equals the given value.
     *
     * @param collection the collection name
     * @param field      the field name
     * @param value      the value to match
     * @return a Uni emitting the matching document, or {@code null}
     */
    public Uni<JsonObject> getByField(String collection, String field, Object value)
    {
        if (!isEnabled() || collection == null || field == null)
        {
            return Uni.createFrom().nullItem();
        }
        JsonObject query = new JsonObject().put(field, value);
        return toUni(mongoClient.findOne(collection, query, null)).map(this::strip);
    }

    /**
     * Whether a JSON document exists in any known collection for the resource item.
     *
     * @param resourceItemId the resource item id
     * @return a Uni emitting {@code true} when a document is present
     */
    public Uni<Boolean> existsInMongo(UUID resourceItemId)
    {
        if (!isEnabled() || resourceItemId == null)
        {
            return Uni.createFrom().item(false);
        }
        return fetchDocument(resourceItemId).map(document -> document != null);
    }

    /**
     * Whether the resource item's payload should be read from / written to MongoDB. {@code true} when a JSON
     * document already exists for it, or when one of its resource-item types is a JSON type.
     *
     * @param session        the reactive session
     * @param resourceItemId the resource item id
     * @return a Uni emitting {@code true} when MongoDB is the system of record for this item's payload
     */
    public Uni<Boolean> isJsonResource(Mutiny.Session session, UUID resourceItemId)
    {
        if (!isEnabled() || resourceItemId == null)
        {
            return Uni.createFrom().item(false);
        }
        return existsInMongo(resourceItemId)
                .chain(exists -> exists
                        ? Uni.createFrom().item(true)
                        : typeIsJson(session, resourceItemId).map(types -> !types.isEmpty()));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Lookup criteria
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Runs a MongoDB lookup over the default collection. The Mongo {@code _id} is stripped from the results;
     * {@link #RESOURCE_ID_FIELD} is retained so callers can map a hit back to its resource item.
     *
     * @param query the MongoDB query criteria (a {@code null}/empty query matches all documents)
     * @return a Uni emitting the matching documents
     */
    public Uni<List<JsonObject>> find(JsonObject query)
    {
        return find(collectionName, query);
    }

    /**
     * Runs a MongoDB lookup over a named collection.
     *
     * @param collection the collection name
     * @param query      the MongoDB query criteria (a {@code null}/empty query matches all documents)
     * @return a Uni emitting the matching documents
     */
    public Uni<List<JsonObject>> find(String collection, JsonObject query)
    {
        if (!isEnabled() || collection == null)
        {
            return Uni.createFrom().item(Collections.emptyList());
        }
        JsonObject criteria = query == null ? new JsonObject() : query;
        return toUni(mongoClient.find(collection, criteria))
                .map(documents -> {
                    if (documents == null)
                    {
                        return Collections.<JsonObject>emptyList();
                    }
                    documents.forEach(document -> document.remove("_id"));
                    return documents;
                });
    }

    /**
     * Convenience lookup for a single field equality criterion against the default collection.
     *
     * @param field the JSON field path
     * @param value the value to match
     * @return a Uni emitting the matching documents
     */
    public Uni<List<JsonObject>> findByField(String field, Object value)
    {
        return find(new JsonObject().put(field, value));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Partial updates — fields
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Sets a single field on the resource item's document ({@code $set}); the field path may use dot-notation
     * to reach nested children (e.g. {@code "address.city"} or {@code "items.0.qty"}). The value may be a
     * string, number, boolean, {@code null}, a {@code List}/array (stored as a JSON array), a {@code Map} or
     * POJO (stored as a JSON object), or a Vert.x {@link JsonObject}/{@link JsonArray}. The document is created
     * if it does not yet exist (upsert).
     *
     * @param resourceItemId the resource item id
     * @param fieldPath      the (possibly nested) field path
     * @param value          the value to set (any JSON-compatible type — see above)
     * @return a Uni completing when the field has been updated
     */
    public Uni<Void> setField(UUID resourceItemId, String fieldPath, Object value)
    {
        return updateById(resourceItemId, new JsonObject().put("$set", new JsonObject().put(fieldPath, normalizeValue(value))));
    }

    /**
     * Sets multiple fields on the resource item's document in one operation ({@code $set}).
     *
     * @param resourceItemId the resource item id
     * @param fields         the field path → value object (dot-notation supported; values normalized as in
     *                       {@link #setField(UUID, String, Object)})
     * @return a Uni completing when the fields have been updated
     */
    public Uni<Void> setFields(UUID resourceItemId, JsonObject fields)
    {
        if (fields == null || fields.isEmpty())
        {
            return Uni.createFrom().voidItem();
        }
        JsonObject set = new JsonObject();
        for (String key : fields.fieldNames())
        {
            set.put(key, normalizeValue(fields.getValue(key)));
        }
        return updateById(resourceItemId, new JsonObject().put("$set", set));
    }

    /**
     * Sets multiple fields on the resource item's document from a plain {@code Map} ({@code $set}). Values may
     * be strings, numbers, booleans, lists, maps, arrays, POJOs or Vert.x JSON types.
     *
     * @param resourceItemId the resource item id
     * @param fields         the field path → value map (dot-notation supported)
     * @return a Uni completing when the fields have been updated
     */
    public Uni<Void> setFields(UUID resourceItemId, Map<String, ?> fields)
    {
        if (fields == null || fields.isEmpty())
        {
            return Uni.createFrom().voidItem();
        }
        JsonObject set = new JsonObject();
        for (Map.Entry<String, ?> entry : fields.entrySet())
        {
            set.put(entry.getKey(), normalizeValue(entry.getValue()));
        }
        return updateById(resourceItemId, new JsonObject().put("$set", set));
    }

    /**
     * Removes a field (or nested child) from the resource item's document ({@code $unset}).
     *
     * @param resourceItemId the resource item id
     * @param fieldPath      the (possibly nested) field path to remove
     * @return a Uni completing when the field has been removed
     */
    public Uni<Void> unsetField(UUID resourceItemId, String fieldPath)
    {
        return updateById(resourceItemId, new JsonObject().put("$unset", new JsonObject().put(fieldPath, "")));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Partial updates — children (arrays)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Appends a child to an array field on the resource item's document ({@code $push}). The child may be a
     * string, number, boolean, list, map, POJO or Vert.x JSON type. The document/array is created if missing.
     *
     * @param resourceItemId the resource item id
     * @param arrayPath      the (possibly nested) array field path
     * @param child          the child value
     * @return a Uni completing when the child has been appended
     */
    public Uni<Void> pushChild(UUID resourceItemId, String arrayPath, Object child)
    {
        return updateById(resourceItemId, new JsonObject().put("$push", new JsonObject().put(arrayPath, normalizeValue(child))));
    }

    /**
     * Removes every child matching the criterion from an array field on the resource item's document
     * ({@code $pull}).
     *
     * @param resourceItemId the resource item id
     * @param arrayPath      the (possibly nested) array field path
     * @param match          the value/criterion identifying the children to remove
     * @return a Uni completing when matching children have been removed
     */
    public Uni<Void> pullChild(UUID resourceItemId, String arrayPath, Object match)
    {
        return updateById(resourceItemId, new JsonObject().put("$pull", new JsonObject().put(arrayPath, normalizeValue(match))));
    }

    /**
     * Applies a raw MongoDB update document (e.g. one mixing {@code $set}/{@code $push}) to the resource item's
     * document, in whichever known collection holds it (or the default/type collection, creating it if missing).
     *
     * @param resourceItemId the resource item id
     * @param update         the MongoDB update document
     * @return a Uni completing when the update has been applied
     */
    public Uni<Void> updateById(UUID resourceItemId, JsonObject update)
    {
        if (!isEnabled() || resourceItemId == null || update == null || update.isEmpty())
        {
            return Uni.createFrom().voidItem();
        }
        return resolveTargetCollection(resourceItemId)
                .chain(collection -> {
                    JsonObject query = new JsonObject().put("_id", resourceItemId.toString());
                    JsonObject finalUpdate = update.copy();
                    // Ensure documents created by an upsert still carry the resource-id marker.
                    JsonObject setOnInsert = finalUpdate.getJsonObject("$setOnInsert", new JsonObject());
                    setOnInsert.put(RESOURCE_ID_FIELD, resourceItemId.toString());
                    finalUpdate.put("$setOnInsert", setOnInsert);
                    UpdateOptions options = new UpdateOptions().setUpsert(true);
                    return toUni(mongoClient.updateCollectionWithOptions(collection, query, finalUpdate, options)).replaceWithVoid();
                });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Delete
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Removes the JSON document for a resource item from every known collection. No-op when disabled.
     *
     * @param resourceItemId the resource item id
     * @return a Uni completing when the document(s) have been removed
     */
    public Uni<Void> delete(UUID resourceItemId)
    {
        if (!isEnabled() || resourceItemId == null)
        {
            return Uni.createFrom().voidItem();
        }
        JsonObject query = new JsonObject().put("_id", resourceItemId.toString());
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (String collection : knownCollections())
        {
            chain = chain.chain(() -> toUni(mongoClient.removeDocuments(collection, query)).replaceWithVoid());
        }
        return chain;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Internals
    // ─────────────────────────────────────────────────────────────────────────

    private Uni<JsonObject> fetchDocument(UUID resourceItemId)
    {
        return fetchDocument(new ArrayList<>(knownCollections()), resourceItemId.toString(), 0);
    }

    private Uni<JsonObject> fetchDocument(List<String> collections, String id, int index)
    {
        if (index >= collections.size())
        {
            return Uni.createFrom().nullItem();
        }
        JsonObject query = new JsonObject().put("_id", id);
        return toUni(mongoClient.findOne(collections.get(index), query, null))
                .chain(document -> document != null
                        ? Uni.createFrom().item(document)
                        : fetchDocument(collections, id, index + 1));
    }

    /**
     * Resolves the collection to read/write a resource's document for partial updates: the collection it
     * currently lives in, else the default collection (so an upsert creates it there).
     */
    private Uni<String> resolveTargetCollection(UUID resourceItemId)
    {
        Set<String> known = knownCollections();
        if (known.size() == 1)
        {
            return Uni.createFrom().item(collectionName);
        }
        return findCollectionContaining(new ArrayList<>(known), resourceItemId.toString(), 0)
                .map(existing -> existing != null ? existing : collectionName);
    }

    /**
     * Normalises an arbitrary Java value into something the MongoDB/Vert.x JSON codec stores cleanly:
     * {@code List}/array → {@link JsonArray}, {@code Map}/POJO → {@link JsonObject}, enums → their name,
     * strings/numbers/booleans/{@code null} and Vert.x JSON types pass through unchanged.
     */
    @SuppressWarnings("unchecked")
    private Object normalizeValue(Object value)
    {
        if (value == null || value instanceof JsonObject || value instanceof JsonArray
                || value instanceof String || value instanceof Number || value instanceof Boolean)
        {
            return value;
        }
        if (value instanceof Enum<?> enumValue)
        {
            return enumValue.name();
        }
        if (value instanceof CharSequence sequence)
        {
            return sequence.toString();
        }
        if (value instanceof Map<?, ?> map)
        {
            JsonObject object = new JsonObject();
            for (Map.Entry<?, ?> entry : map.entrySet())
            {
                object.put(String.valueOf(entry.getKey()), normalizeValue(entry.getValue()));
            }
            return object;
        }
        if (value instanceof Iterable<?> iterable)
        {
            JsonArray array = new JsonArray();
            for (Object element : iterable)
            {
                array.add(normalizeValue(element));
            }
            return array;
        }
        if (value.getClass().isArray())
        {
            JsonArray array = new JsonArray();
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++)
            {
                array.add(normalizeValue(java.lang.reflect.Array.get(value, i)));
            }
            return array;
        }
        // Fall back to mapping a POJO to a JSON object via the Vert.x Jackson codec.
        try
        {
            return JsonObject.mapFrom(value);
        }
        catch (Exception notMappable)
        {
            return String.valueOf(value);
        }
    }

    private Uni<String> findCollectionContaining(List<String> collections, String id, int index)
    {
        if (index >= collections.size())
        {
            return Uni.createFrom().nullItem();
        }
        JsonObject query = new JsonObject().put("_id", id);
        String collection = collections.get(index);
        return toUni(mongoClient.count(collection, query))
                .chain(count -> count != null && count > 0
                        ? Uni.createFrom().item(collection)
                        : findCollectionContaining(collections, id, index + 1));
    }

    /** Resolves the collection to write a resource's document to: existing location, else its type's collection. */
    private Uni<String> resolveCollection(Mutiny.Session session, UUID resourceItemId)
    {
        Set<String> known = knownCollections();
        if (known.size() == 1)
        {
            return Uni.createFrom().item(collectionName);
        }
        return findCollectionContaining(new ArrayList<>(known), resourceItemId.toString(), 0)
                .chain(existing -> existing != null
                        ? Uni.createFrom().item(existing)
                        : typeIsJson(session, resourceItemId).map(types -> {
                            for (String type : types)
                            {
                                return collectionForType(type);
                            }
                            return collectionName;
                        }));
    }

    private Uni<List<String>> typeIsJson(Mutiny.Session session, UUID resourceItemId)
    {
        if (session == null)
        {
            return Uni.createFrom().item(Collections.emptyList());
        }
        return session.createQuery(
                        "select t.name from ResourceItemXResourceItemType x " +
                                "join x.resourceItemTypeID t where x.resourceItemID.id = :id", String.class)
                .setParameter("id", resourceItemId)
                .getResultList()
                .map(names -> names == null ? Collections.<String>emptyList() : names.stream().filter(this::isJsonType).toList())
                .onFailure().recoverWithItem(Collections.emptyList());
    }

    private JsonObject toDocument(UUID resourceItemId, byte[] data)
    {
        String json = new String(data, StandardCharsets.UTF_8).trim();
        JsonObject document;
        if (json.startsWith("{"))
        {
            try
            {
                document = new JsonObject(json);
            }
            catch (Exception decodeFailure)
            {
                document = new JsonObject().put(RAW_PAYLOAD_FIELD, json);
            }
        }
        else
        {
            document = new JsonObject().put(RAW_PAYLOAD_FIELD, json);
        }
        document.put("_id", resourceItemId.toString());
        document.put(RESOURCE_ID_FIELD, resourceItemId.toString());
        return document;
    }

    private byte[] extractPayload(JsonObject document)
    {
        document.remove("_id");
        document.remove(RESOURCE_ID_FIELD);
        if (document.size() == 1 && document.containsKey(RAW_PAYLOAD_FIELD))
        {
            String raw = document.getString(RAW_PAYLOAD_FIELD);
            return (raw == null ? "" : raw).getBytes(StandardCharsets.UTF_8);
        }
        return document.encode().getBytes(StandardCharsets.UTF_8);
    }

    /** Strips the internal Mongo {@code _id} from a document read for callers (keeps {@link #RESOURCE_ID_FIELD}). */
    private JsonObject strip(JsonObject document)
    {
        if (document == null)
        {
            return null;
        }
        document.remove("_id");
        return document;
    }

    private static <T> Uni<T> toUni(io.vertx.core.Future<T> future)
    {
        return Uni.createFrom().completionStage(future.toCompletionStage());
    }
}

