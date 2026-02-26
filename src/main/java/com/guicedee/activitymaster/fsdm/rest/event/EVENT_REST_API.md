# Event REST API — Skills Document

## Overview

The `EventRestService` provides a JAX-RS REST API for managing **Events** within the ActivityMaster FSDM. Events represent occurrences or actions (logins, packing sessions, transactions) and support typed relationships to classifications, event types, involved parties, resource items, products, rules, arrangements, and child events (hierarchy).

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON** for request/response bodies.

---

## Base Path

```
/{enterprise}/event
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation.

---

## DTO Packages

All DTOs used by this API live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.events` | `EventDTO`, `EventFindDTO`, `EventCreateDTO`, `EventUpdateDTO`, `EventDataIncludes` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry` (shared across all REST APIs) |

---

## Endpoints

### 1. Find Event

Retrieves an event by ID with selectable relationship includes.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/event/{requestingSystemName}/find` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `EventFindDTO` |
| **Response** | `EventDTO` |

#### Request Body — `EventFindDTO`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Types", "Classifications", "Parties", "Resources"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `eventId` | `UUID` | **Yes** | The event to retrieve. |
| `includes` | `List<EventDataIncludes>` | No | Which relationships to include. If omitted/empty, only `eventId` is returned. |

#### Available Includes

| Value | Returns |
|---|---|
| `Types` | Event type names → stored values |
| `Classifications` | Classification names → stored values |
| `Parties` | Classification names → involved party UUIDs |
| `Resources` | Classification names → resource item UUIDs |
| `Products` | Classification names → product UUIDs |
| `Rules` | Classification names → rule UUIDs |
| `Arrangements` | Classification names → arrangement UUIDs |
| `Children` | Classification names → child event UUIDs |

#### Example Response — `EventDTO`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "types": {
    "PackingSession": "session-001"
  },
  "classifications": {
    "Status": "InProgress",
    "StartTime": "2026-02-22T08:00:00"
  },
  "parties": {
    "Operator": "a1b2c3d4-..."
  },
  "resources": {
    "Station": "r1s2t3u4-..."
  }
}
```

---

### 2. Create Event

Creates a new event from the `types` map. The first entry in `types` is used as the primary event type for the underlying `createEvent()` call. All entries (including the first) are then persisted with their values asynchronously.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/event/{requestingSystemName}/create` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `EventCreateDTO` |
| **Response** | `EventDTO` |

#### Request Body — `EventCreateDTO`

```json
{
  "types": {
    "PackingSession": "session-001",
    "ShiftEvent": "morning"
  },
  "classifications": {
    "Status": "InProgress",
    "StartTime": "2026-02-22T08:00:00"
  },
  "parties": {
    "Operator": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  },
  "resources": {
    "Station": "r1s2t3u4-e5f6-7890-abcd-ef1234567890"
  },
  "products": {
    "ProductGrade": "grade-a"
  },
  "rules": {
    "ShiftRule": "max-8-hours"
  },
  "arrangements": {
    "Timesheet": "timesheet-value"
  },
  "children": {
    "SubEventClassification": "child-event-uuid"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `types` | `Map<String, String>` | **Yes** | Event type name → relationship value pairs. **At least one entry is required.** The first entry is used as the primary event type during creation; all entries are persisted with their values. |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs. |
| `parties` | `Map<String, String>` | No | Classification name → **involved party UUID**. The UUID is resolved to an entity before linking. |
| `resources` | `Map<String, String>` | No | Classification name → **resource item UUID**. The UUID is resolved to an entity before linking. |
| `products` | `Map<String, String>` | No | Classification name → store value for product relationships. |
| `rules` | `Map<String, String>` | No | Classification name → store value for rule relationships. |
| `arrangements` | `Map<String, String>` | No | Classification name → store value for arrangement relationships. |
| `children` | `Map<String, String>` | No | Classification name → child event UUID string. |

#### Session & Response Handling

- `eventService.createEvent()` manages its own session — the REST endpoint does **not** wrap it in `withActivityMaster()`.
- **Relationship persistence is fire-and-forget**: Each relationship type (types, classifications, parties, resources, etc.) is persisted asynchronously on its **own Vert.x context** via `SessionUtils.fireAndForget()`. Each fire-and-forget Uni opens its own session and transaction, fully isolated from the others.
- **Response is returned immediately** from the DTO input (echoed back) — no DB round-trip needed. The response includes all submitted relationship data even though the async persistence may still be in progress.

#### Entity Resolution

- **Parties**: The value is an **involved party UUID**. It is resolved via `involvedPartyService.find(session, partyId)` before being passed to `addOrUpdateInvolvedParty()`.
- **Resources**: The value is a **resource item UUID**. It is resolved via `resourceItemService.findByUUID(session, riId)` before being passed to `addOrUpdateResourceItem()`.
- **Children**: The key is a **classification name** and the value is a **child event UUID**. The child event is resolved via `eventService.find(session, childId)` before calling `addChild()`.
- Products, rules, and arrangements pass the map value as a store value string (no entity resolution needed).

---

### 3. Update Event

Updates an existing event's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship type.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/event/{requestingSystemName}/update` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `EventUpdateDTO` |
| **Response** | `EventDTO` |

#### Request Body — `EventUpdateDTO`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": {
    "addOrUpdate": {
      "Status": "Completed",
      "EndTime": "2026-02-22T16:00:00"
    },
    "delete": ["InProgress"]
  },
  "types": {
    "addOrUpdate": {
      "ClosedSession": "final"
    },
    "delete": ["OpenSession"]
  },
  "parties": {
    "addOrUpdate": {
      "Supervisor": "supervisor-party-uuid"
    }
  },
  "resources": {
    "addOrUpdate": {
      "Station": "resource-item-uuid"
    }
  },
  "children": {
    "addOrUpdate": {
      "SubEventClassification": "child-event-uuid"
    },
    "delete": ["old-child-classification"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `eventId` | `UUID` | **Yes** | The event to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Event type operations. |
| `parties` | `RelationshipUpdateEntry` | No | Involved party operations. Key = classification name, Value = **involved party UUID**. |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations. Key = classification name, Value = **resource item UUID**. |
| `products` | `RelationshipUpdateEntry` | No | Product operations (key = classification name). |
| `rules` | `RelationshipUpdateEntry` | No | Rule operations (key = classification name). |
| `arrangements` | `RelationshipUpdateEntry` | No | Arrangement operations (key = classification name). |
| `children` | `RelationshipUpdateEntry` | No | Child event operations. Key = classification name, Value = child event UUID. |

#### `RelationshipUpdateEntry` Structure

```json
{
  "addOrUpdate": { "Name": "Value" },
  "delete": ["NameToExpire"]
}
```

#### Session & Response Handling (Update)

- The event is validated (exists) in an initial session.
- **All relationship updates are fire-and-forget**: Each relationship type is persisted asynchronously on its own Vert.x context via `SessionUtils.fireAndForget()`.
- **Response is returned immediately** from the DTO input — no DB round-trip.

---

## Data Model

### Event

An occurrence or action entity with a UUID, linked to an enterprise and system.

### Relationships

| Relationship | Cross-ref Table | Key Type | Add/Update Method | Remove/Expire |
|---|---|---|---|---|
| Classifications | `EventXClassification` | Classification name | `addOrUpdateClassification()` | `removeClassification()` |
| Event Types | `EventXEventType` | Type name | `addOrUpdateEventTypes()` | `removeEventTypes()` |
| Involved Parties | `EventXInvolvedParty` | Classification name | `addOrUpdateInvolvedParty()` | Expire via query |
| Resource Items | `EventXResourceItem` | Classification name | `addOrUpdateResourceItem()` | Expire via query |
| Products | `EventXProduct` | Classification name | `addOrUpdateProduct()` | Expire via query |
| Rules | `EventXRules` | Classification name | `addOrUpdateRules()` | Expire via query |
| Arrangements | `EventXArrangement` | Classification name | `addOrUpdateArrangement()` | Expire via query |
| Children | `EventXEvent` | Classification name | `addChild()` | Expire via query |

### SCD Pattern

All relationship records follow the Slowly Changing Dimension pattern:
- **Expire** = set `effectiveToDate` to now.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- All lazy-loaded entities are fetched via `session.fetch()` (Hibernate Reactive requirement).

---

## Response Key Conventions

For all relationship maps:
- **Key** = classification name or entity name (the descriptive name on the cross-reference row)
- **Value** = the related entity's UUID (for parties, resources, products, rules, arrangements, children) or the stored relationship value (for types, classifications)

---

## Async Relationship Persistence

All create and update operations use **fire-and-forget** async persistence for relationships:

1. Each relationship type (types, classifications, parties, etc.) is persisted in a **separate** `SessionUtils.fireAndForget()` call.
2. Each `fireAndForget()` runs on its **own Vert.x context** (`vertx.getOrCreateContext().runOnContext(...)`) to ensure session isolation.
3. Each async operation opens its own session and transaction via `SessionUtils.withActivityMaster()`.
4. Failures are logged at ERROR level but never propagated to the caller.
5. The HTTP response is returned **immediately** — relationship data is echoed from the submitted DTO.

This pattern avoids Hibernate Reactive session thread-affinity errors (HR000069) and "Illegal pop()" errors that occur when multiple `withTransaction` calls share the same Vert.x context.

---

## Common Patterns

### Creating an Event with Full Details

```json
POST /{enterprise}/event/{system}/create
{
  "types": {
    "PackingSession": "session-001"
  },
  "classifications": {
    "Status": "InProgress",
    "Operator": "John Smith"
  },
  "resources": {
    "Station": "resource-item-uuid"
  }
}
```

### Updating and Closing an Event

```json
PUT /{enterprise}/event/{system}/update
{
  "eventId": "...",
  "classifications": {
    "addOrUpdate": { "Status": "Completed", "EndTime": "2026-02-22T16:00:00" },
    "delete": ["InProgress"]
  }
}
```

### Reading All Relationships

```json
POST /{enterprise}/event/{system}/find
{
  "eventId": "...",
  "includes": ["Types", "Classifications", "Parties", "Resources", "Products", "Rules", "Arrangements", "Children"]
}
```

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `EventFindDTO` | `POST .../find` | Find by ID with selectable includes |
| `EventCreateDTO` | `POST .../create` | Create with mandatory types + optional relationships |
| `EventUpdateDTO` | `PUT .../update` | Update relationships (add/update/delete) |
| `EventDTO` | Response | Standard event response |
| `RelationshipUpdateEntry` | Within `EventUpdateDTO` | Add/update map + delete list for one relationship type |

