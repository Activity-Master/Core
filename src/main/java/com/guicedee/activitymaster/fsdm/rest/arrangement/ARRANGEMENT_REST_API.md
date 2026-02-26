# Arrangement REST API — Skills Document

## Overview

The `ArrangementRestService` provides a JAX-RS REST API for managing **Arrangements** within the ActivityMaster FSDM (Financial Services Data Model). Arrangements are the central entity and support typed relationships to classifications, arrangement types, involved parties, resource items, rules, products, rule types, events, and child arrangements.

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON** for request/response bodies.

---

## Base Path

```
/{enterprise}/arrangement
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation.

---

## Authentication & Session Management

- Sessions and transactions are managed internally via `SessionUtils.withActivityMaster()`.
- The `create` endpoint is an exception — `ArrangementsService.create()` manages its own session internally. The REST endpoint chains subsequent relationship operations in separate fire-and-forget sessions.
- **Never** wrap `arrangementsService.create()` inside `withActivityMaster()` — it will cause nested session/transaction errors.

---

## DTO Packages

All DTOs used by this API live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.arrangements` | `ArrangementDTO`, `ArrangementFindDTO`, `ArrangementCreateDTO`, `ArrangementUpdateDTO`, `ArrangementPivotRequest`, `ArrangementPivotResponse`, `ArrangementDataIncludes` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry`, `PivotEntry`, `EntityRef` (shared across all REST APIs) |

---

## Endpoints

### 1. Find Arrangement

Retrieves an arrangement by ID with selectable relationship includes.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/arrangement/{requestingSystemName}/find` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ArrangementFindDTO` |
| **Response** | `ArrangementDTO` |

#### Request Body — `ArrangementFindDTO`

```json
{
  "arrangementId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Types", "Classifications", "Parties"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `arrangementId` | `UUID` | **Yes** | The arrangement to retrieve. |
| `includes` | `List<ArrangementDataIncludes>` | No | Which relationship types to include. If omitted/empty, only `arrangementId` is returned. |

#### Available Includes

| Value | Returns |
|---|---|
| `Types` | Arrangement type names → stored values |
| `Classifications` | Classification names → stored values |
| `Parties` | Involved party UUIDs → stored values |
| `Resources` | Resource item UUIDs → stored values |
| `Events` | Event UUIDs → stored values |
| `Rules` | Rule UUIDs → stored values |
| `Products` | Product UUIDs → stored values |
| `RuleTypes` | Rule type UUIDs → stored values |
| `Arrangements` | Child arrangement UUIDs → stored values |

#### Example Response — `ArrangementDTO`

```json
{
  "arrangementId": "550e8400-e29b-41d4-a716-446655440000",
  "types": {
    "PackingStaffTimesheet": "grader-shift-001"
  },
  "classifications": {
    "StaffName": "John Smith",
    "StationNumber": "5",
    "StartDate": "2025-06-15T08:00:00"
  },
  "parties": {
    "a1b2c3d4-...": "supervisor"
  }
}
```

Only populated relationship maps are included in the response (nulls/empties are omitted via `@JsonInclude(NON_EMPTY)`).

---

### 2. Find Pivoted (Optimized Native Query)

Retrieves specific classifications and arrangement types by **name** using an optimized single native SQL query with UNION ALL. Returns rich entity references (id + name) with timestamps.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/arrangement/{requestingSystemName}/pivot` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ArrangementPivotRequest` |
| **Response** | `ArrangementPivotResponse` |

#### Request Body — `ArrangementPivotRequest`

```json
{
  "arrangementId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": ["Status", "Priority", "StartDate"],
  "types": ["PackingStaffTimesheet"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `arrangementId` | `UUID` | **Yes** | The arrangement to query. |
| `classifications` | `List<String>` | No | Classification names to include. |
| `types` | `List<String>` | No | Arrangement type names to include. |

At least one of `classifications` or `types` should be provided; otherwise an empty response is returned.

#### Example Response — `ArrangementPivotResponse`

```json
{
  "arrangementId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": [
    {
      "entity": { "id": "cls-uuid-1", "name": "Status" },
      "value": "Active",
      "timestamp": "2025-06-15 08:00:00"
    },
    {
      "entity": { "id": "cls-uuid-2", "name": "Priority" },
      "value": "High",
      "timestamp": "2025-06-15 08:00:00"
    }
  ],
  "types": [
    {
      "entity": { "id": "type-uuid-1", "name": "PackingStaffTimesheet" },
      "value": "grader-shift-001",
      "timestamp": "2025-06-15 08:00:00"
    }
  ]
}
```

---

### 3. Create Arrangement

Creates a new arrangement with an arrangement type, and optionally adds classifications, parties, resources, rules, products, and child arrangement links in the same request.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/arrangement/{requestingSystemName}/create` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ArrangementCreateDTO` |
| **Response** | `ArrangementDTO` |

#### Request Body — `ArrangementCreateDTO`

```json
{
  "type": "PackingStaffTimesheet",
  "classification": "Grader",
  "typeValue": "grader-shift-001",
  "key": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": {
    "StaffName": "John Smith",
    "StationNumber": "5",
    "StartDate": "2025-06-15T08:00:00",
    "EndDate": "2025-06-15T16:00:00"
  },
  "parties": {
    "EmployeeClassification": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  },
  "resources": {
    "EquipmentClassification": "r1s2t3u4-e5f6-7890-abcd-ef1234567890"
  },
  "rules": {
    "ShiftRuleClassification": "max-8-hours"
  },
  "products": {
    "ProductClassification": "grade-a-apples"
  },
  "childArrangements": {
    "SubTaskClassification": "a1b2c3d4-e5f6-7890-abcd-ef1234567890_71"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `type` | `String` | **Yes** | Arrangement type name (e.g. `"PackingStaffTimesheet"`). |
| `classification` | `String` | **Yes** | Classification name for the arrangement type relationship (e.g. `"Grader"`). |
| `typeValue` | `String` | **Yes** | The value to store on the arrangement type relationship. |
| `key` | `UUID` | **Yes** | The unique key for the arrangement. |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs to add/update. |
| `parties` | `Map<String, String>` | No | Classification name → **involved party UUID**. The UUID is resolved to an entity before linking. |
| `resources` | `Map<String, String>` | No | Classification name → **resource item UUID**. The UUID is resolved to an entity before linking. |
| `rules` | `Map<String, String>` | No | Classification name → store value for rule relationships. |
| `products` | `Map<String, String>` | No | Classification name → store value for product relationships. |
| `childArrangements` | `Map<String, String>` | No | Classification name → child arrangement UUID (optionally suffixed with `_hierarchyValue`, e.g. `"uuid_71"`). |

#### Session & Response Handling

- `arrangementsService.create()` manages its own session — the REST endpoint does **not** wrap it in `withActivityMaster()`.
- **Relationship persistence is fire-and-forget**: Each relationship type (classifications, parties, resources, etc.) is persisted asynchronously on its **own Vert.x context** via `SessionUtils.fireAndForget()`. Each fire-and-forget Uni opens its own session and transaction, fully isolated from the others.
- **Response is returned immediately** from the DTO input (echoed back) — no DB round-trip needed.

#### Entity Resolution

- **Parties**: The value is an **involved party UUID**. It is resolved via `involvedPartyService.find(session, partyId)` before being passed to `addOrUpdateInvolvedParty()`.
- **Resources**: The value is a **resource item UUID**. It is resolved via `resourceItemService.findByUUID(session, riId)` before being passed to `addOrUpdateResourceItem()`.
- **Child Arrangements**: The value is a **child arrangement UUID**, optionally suffixed with `_hierarchyValue` (e.g. `"2c7c36bf-ec6d-4b70-a664-47366d3aec0d_71"`). The first 36 characters are parsed as the UUID, and any suffix after `_` at position 36 is extracted as the hierarchy value passed to `addChild()`. The child arrangement is resolved via `arrangementsService.find(session, childId)`.
- Rules and products pass the map value as a store value string (no entity resolution needed).

---

### 4. Update Arrangement

Updates an existing arrangement's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship type.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/arrangement/{requestingSystemName}/update` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ArrangementUpdateDTO` |
| **Response** | `ArrangementDTO` |

#### Request Body — `ArrangementUpdateDTO`

```json
{
  "arrangementId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": {
    "addOrUpdate": {
      "Status": "Active",
      "Priority": "High"
    },
    "delete": ["Deprecated"]
  },
  "types": {
    "addOrUpdate": {
      "Invoice": "INV-2025-001"
    }
  },
  "parties": {
    "addOrUpdate": {
      "EmployeeClassification": "party-uuid"
    },
    "delete": ["old-classification"]
  },
  "resources": {
    "addOrUpdate": {
      "EquipmentClassification": "resource-item-uuid"
    }
  },
  "childArrangements": {
    "addOrUpdate": {
      "SubTaskClassification": "child-uuid_71"
    },
    "delete": ["old-child-classification"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `arrangementId` | `UUID` | **Yes** | The arrangement to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Arrangement type operations. |
| `parties` | `RelationshipUpdateEntry` | No | Involved party operations. Key = classification name, Value = **involved party UUID**. |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations. Key = classification name, Value = **resource item UUID**. |
| `rules` | `RelationshipUpdateEntry` | No | Rule operations. |
| `products` | `RelationshipUpdateEntry` | No | Product operations. |
| `ruleTypes` | `RelationshipUpdateEntry` | No | Rule type operations. |
| `childArrangements` | `RelationshipUpdateEntry` | No | Child arrangement operations. Key = classification name, Value = child UUID (optionally suffixed with `_hierarchyValue`). |

#### `RelationshipUpdateEntry` Structure

```json
{
  "addOrUpdate": { "Name": "Value", "Name2": "Value2" },
  "delete": ["NameToExpire1", "NameToExpire2"]
}
```

| Field | Type | Description |
|---|---|---|
| `addOrUpdate` | `Map<String, String>` | Key/value pairs to upsert. Key is the **classification name** or **type name**. Value depends on the relationship type (UUID for parties/resources/children, store value for others). |
| `delete` | `List<String>` | Names to expire (soft-delete). For classifications and rule types, uses dedicated remove methods. For other relationship types, queries the relationship table and expires matching rows. |

#### Session & Response Handling (Update)

- The arrangement is validated (exists) in an initial session.
- **All relationship updates are fire-and-forget**: Each relationship type is persisted asynchronously on its own Vert.x context via `SessionUtils.fireAndForget()`.
- **Response is returned immediately** from the DTO input — no DB round-trip.

---

## Data Model Concepts

### Arrangement

The central entity. Has a UUID `arrangementId` and is linked to an enterprise and system.

### Relationship Types

An arrangement can have multiple relationship types, each managed through a dedicated cross-reference table:

| Relationship | Cross-ref Table | Key Type | Add/Update Method | Remove/Expire |
|---|---|---|---|---|
| Classifications | `ArrangementXClassification` | Classification name | `addOrUpdateClassification()` | `removeClassification()` |
| Arrangement Types | `ArrangementXArrangementType` | Type name | `addOrUpdateArrangementType()` | Expire via query |
| Involved Parties | `ArrangementXInvolvedParty` | Classification name | `addOrUpdateInvolvedParty()` | Expire via query |
| Resource Items | `ArrangementXResourceItem` | Classification name | `addOrUpdateResourceItem()` | Expire via query |
| Rules | `ArrangementXRules` | Classification name | `addOrUpdateRules()` | Expire via query |
| Products | `ArrangementXProduct` | Classification name | `addOrUpdateProduct()` | Expire via query |
| Rule Types | `ArrangementXRulesType` | Rule type name | `addOrUpdateRuleTypes()` | `removeRuleTypes()` |
| Child Arrangements | `ArrangementXArrangement` | Classification name | `addChild()` | Expire via query |
| Events | `EventXArrangement` | Event UUID | (read-only in this API) | — |

### SCD (Slowly Changing Dimension) Pattern

All relationship records follow the SCD pattern:
- `effectiveFromDate` / `effectiveToDate` — date range validity.
- `activeFlagID` — active/deleted flag.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- **Expire** = set `effectiveToDate` to now (record stops appearing in range queries).
- **Delete** = set `activeFlagID` to the deleted flag (record stops appearing in active queries).

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

### Creating an Arrangement with Full Details

```json
POST /{enterprise}/arrangement/{system}/create
{
  "type": "PackingStaffTimesheet",
  "classification": "Grader",
  "typeValue": "grader-shift-001",
  "key": "unique-key-uuid",
  "classifications": {
    "StaffName": "John Smith",
    "StationNumber": "5",
    "StartDate": "2025-06-15T08:00:00"
  },
  "parties": {
    "Employee": "party-uuid"
  },
  "resources": {
    "Equipment": "resource-item-uuid"
  }
}
```

### Updating Multiple Relationship Types

```json
PUT /{enterprise}/arrangement/{system}/update
{
  "arrangementId": "...",
  "classifications": {
    "addOrUpdate": { "Status": "Completed", "EndDate": "2025-06-15T16:00:00" },
    "delete": ["InProgress"]
  },
  "types": {
    "addOrUpdate": { "ClosedTimesheet": "final" }
  }
}
```

### Reading Specific Fields Efficiently

Use the **pivot** endpoint when you only need specific named classifications/types — it executes a single native SQL query instead of loading all relationships:

```json
POST /{enterprise}/arrangement/{system}/pivot
{
  "arrangementId": "...",
  "classifications": ["Status", "StaffName"],
  "types": ["PackingStaffTimesheet"]
}
```

### Reading All Relationships

Use the **find** endpoint with all includes:

```json
POST /{enterprise}/arrangement/{system}/find
{
  "arrangementId": "...",
  "includes": ["Types", "Classifications", "Parties", "Resources", "Rules", "Products", "RuleTypes", "Arrangements", "Events"]
}
```

---

## Error Handling

- All endpoints return reactive `Uni<T>` — errors propagate as `Uni.failure()`.
- Errors are logged with `@Log4j2` at the endpoint level.
- Invalid `arrangementId` or missing arrangements will result in failure propagation from the service layer.
- All fields marked `@NotNull` are validated — missing required fields result in a 400 Bad Request.

---

## JSON Serialization

- All DTOs use `@JsonInclude(NON_EMPTY)` — null and empty maps/lists are omitted from responses.
- All DTOs use `@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)` — serialization is field-based, not getter-based.
- The project uses a centralized `DefaultObjectMapper` (from `ObjectBinderKeys.DefaultObjectMapper`) for consistent serialization configuration.

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `ArrangementFindDTO` | `POST .../find` | Find by ID with selectable includes |
| `ArrangementPivotRequest` | `POST .../pivot` | Optimized native query for named fields |
| `ArrangementCreateDTO` | `POST .../create` | Create with optional relationships |
| `ArrangementUpdateDTO` | `PUT .../update` | Update relationships (add/update/delete) |
| `ArrangementDTO` | Response | Standard arrangement response |
| `ArrangementPivotResponse` | Response | Pivot response with entity refs + timestamps |
| `RelationshipUpdateEntry` | Within `ArrangementUpdateDTO` | Add/update map + delete list for one relationship type |
| `PivotEntry` | Within `ArrangementPivotResponse` | Single pivoted result with entity ref |
| `EntityRef` | Within `PivotEntry` | Entity id + name reference |

