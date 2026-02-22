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
| `Parties` | Classification names → involved party IDs |
| `Resources` | Classification names → resource item IDs |
| `Products` | Classification names → product IDs |
| `Rules` | Classification names → rule IDs |
| `Arrangements` | Classification names → arrangement IDs |
| `Children` | Child event IDs → stored values |

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

Creates a new event with an event type, and optionally adds classifications, additional event types, parties, resources, products, rules, arrangements, and child event links in the same request.

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
  "type": "PackingSession",
  "classifications": {
    "Status": "InProgress",
    "StartTime": "2026-02-22T08:00:00"
  },
  "types": {
    "ShiftEvent": "morning"
  },
  "parties": {
    "Operator": "staff-value"
  },
  "resources": {
    "Station": "station-value"
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
    "child-event-uuid": "sub-event"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `type` | `String` | **Yes** | Event type name (e.g. `"PackingSession"`, `"Login"`). |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs. |
| `types` | `Map<String, String>` | No | Additional event type name → value pairs. |
| `parties` | `Map<String, String>` | No | Classification name → store value for involved party relationships. |
| `resources` | `Map<String, String>` | No | Classification name → store value for resource item relationships. |
| `products` | `Map<String, String>` | No | Classification name → store value for product relationships. |
| `rules` | `Map<String, String>` | No | Classification name → store value for rule relationships. |
| `arrangements` | `Map<String, String>` | No | Classification name → store value for arrangement relationships. |
| `children` | `Map<String, String>` | No | Child event UUID (as string) → relationship value. |

#### Session Handling

The `create` endpoint does **not** wrap `eventService.createEvent()` in `withActivityMaster()` because the service method manages its own session. Subsequent relationship operations and response fetching each open their own fresh sessions.

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
      "Supervisor": "supervisor-value"
    }
  },
  "children": {
    "delete": ["old-child-uuid"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `eventId` | `UUID` | **Yes** | The event to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Event type operations. |
| `parties` | `RelationshipUpdateEntry` | No | Involved party operations (key = classification name). |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations (key = classification name). |
| `products` | `RelationshipUpdateEntry` | No | Product operations (key = classification name). |
| `rules` | `RelationshipUpdateEntry` | No | Rule operations (key = classification name). |
| `arrangements` | `RelationshipUpdateEntry` | No | Arrangement operations (key = classification name). |
| `children` | `RelationshipUpdateEntry` | No | Child event operations (key = child event UUID string). |

#### `RelationshipUpdateEntry` Structure

```json
{
  "addOrUpdate": { "Name": "Value" },
  "delete": ["NameToExpire"]
}
```

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
| Children | `EventXEvent` | Child UUID | `addChild()` | Expire via query |

### SCD Pattern

All relationship records follow the Slowly Changing Dimension pattern:
- **Expire** = set `effectiveToDate` to now.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- All lazy-loaded entities are fetched via `session.fetch()` (Hibernate Reactive requirement).

---

## Response Key Conventions

For relationships that link through a classification (parties, resources, products, rules, arrangements):
- **Key** = classification name (the descriptive classification on the cross-reference row)
- **Value** = the related entity's UUID

For direct relationships (types, children, classifications):
- **Key** = entity name or UUID
- **Value** = the stored relationship value

---

## Common Patterns

### Creating an Event with Full Details

```json
POST /{enterprise}/event/{system}/create
{
  "type": "PackingSession",
  "classifications": {
    "Status": "InProgress",
    "Operator": "John Smith"
  },
  "resources": {
    "Station": "station-42"
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
| `EventCreateDTO` | `POST .../create` | Create with optional relationships |
| `EventUpdateDTO` | `PUT .../update` | Update relationships (add/update/delete) |
| `EventDTO` | Response | Standard event response |
| `RelationshipUpdateEntry` | Within `EventUpdateDTO` | Add/update map + delete list for one relationship type |

