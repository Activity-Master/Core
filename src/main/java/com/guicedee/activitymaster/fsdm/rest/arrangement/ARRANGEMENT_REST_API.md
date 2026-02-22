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
- The `create` endpoint is an exception — `ArrangementsService.create()` manages its own session internally. The REST endpoint chains subsequent relationship operations and response fetching in separate sessions.
- **Never** wrap `arrangementsService.create()` inside `withActivityMaster()` — it will cause nested session/transaction errors.

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
| `Parties` | Involved party IDs → stored values |
| `Resources` | Resource item IDs → stored values |
| `Events` | Event IDs → stored values |
| `Rules` | Rule IDs → stored values |
| `Products` | Product IDs → stored values |
| `RuleTypes` | Rule type IDs → stored values |
| `Arrangements` | Child arrangement IDs → stored values |

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
| **Path** | `/{enterprise}/arrangement/{requestingSystemName}` |
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
  "classifications": {
    "StaffName": "John Smith",
    "StationNumber": "5",
    "StartDate": "2025-06-15T08:00:00",
    "EndDate": "2025-06-15T16:00:00"
  },
  "parties": {
    "EmployeeClassification": "staff-member-value"
  },
  "resources": {
    "EquipmentClassification": "forklift-42"
  },
  "rules": {
    "ShiftRuleClassification": "max-8-hours"
  },
  "products": {
    "ProductClassification": "grade-a-apples"
  },
  "childArrangements": {
    "a1b2c3d4-e5f6-7890-abcd-ef1234567890": "sub-task"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `type` | `String` | **Yes** | Arrangement type name (e.g. `"PackingStaffTimesheet"`). |
| `classification` | `String` | **Yes** | Classification name for the arrangement type relationship (e.g. `"Grader"`). |
| `typeValue` | `String` | **Yes** | The value to store on the arrangement type relationship. |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs to add/update. |
| `parties` | `Map<String, String>` | No | Classification name → store value for involved party relationships. |
| `resources` | `Map<String, String>` | No | Classification name → store value for resource item relationships. |
| `rules` | `Map<String, String>` | No | Classification name → store value for rule relationships. |
| `products` | `Map<String, String>` | No | Classification name → store value for product relationships. |
| `childArrangements` | `Map<String, String>` | No | Child arrangement UUID (as string) → relationship value. |

#### Behavior

1. Creates the arrangement and its initial arrangement type (with `type`, `classification`, `typeValue`).
2. If any optional relationship maps are provided, adds them via `addOrUpdateClassification`, `addOrUpdateInvolvedParty`, etc.
3. Returns the full `ArrangementDTO` with Types, Classifications, and any other relationships that were provided.

#### Important: Session Handling

The `create` endpoint does **not** wrap `arrangementsService.create()` in `withActivityMaster()` because the service method manages its own session. Subsequent relationship operations and response fetching each open their own fresh sessions.

---

### 4. Update Arrangement

Updates an existing arrangement's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship type.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/arrangement/{requestingSystemName}` |
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
      "Priority": "High",
      "StartDate": "2025-06-15T08:00:00"
    },
    "delete": ["Deprecated", "OldStatus"]
  },
  "types": {
    "addOrUpdate": {
      "Invoice": "INV-2025-001"
    }
  },
  "parties": {
    "addOrUpdate": {
      "EmployeeClassification": "new-staff-member"
    },
    "delete": ["old-party-id"]
  },
  "ruleTypes": {
    "addOrUpdate": {
      "ValidationRule": "Enabled"
    },
    "delete": ["OldRule"]
  },
  "childArrangements": {
    "addOrUpdate": {
      "child-uuid-string": "sub-task"
    },
    "delete": ["old-child-uuid-string"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `arrangementId` | `UUID` | **Yes** | The arrangement to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Arrangement type operations. |
| `parties` | `RelationshipUpdateEntry` | No | Involved party operations. |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations. |
| `rules` | `RelationshipUpdateEntry` | No | Rule operations. |
| `products` | `RelationshipUpdateEntry` | No | Product operations. |
| `ruleTypes` | `RelationshipUpdateEntry` | No | Rule type operations. |
| `childArrangements` | `RelationshipUpdateEntry` | No | Child arrangement operations. |

#### `RelationshipUpdateEntry` Structure

```json
{
  "addOrUpdate": { "Name": "Value", "Name2": "Value2" },
  "delete": ["NameToExpire1", "NameToExpire2"]
}
```

| Field | Type | Description |
|---|---|---|
| `addOrUpdate` | `Map<String, String>` | Key/value pairs to upsert. Key is the **name** (classification name, type name, product name, rule name, etc.). Value is the relationship value to store. |
| `delete` | `List<String>` | Names to expire (soft-delete). For classifications and rule types, uses dedicated remove methods. For other relationship types, queries the relationship table and expires matching rows. |

#### Key Rules

- **All string keys are names, not IDs** — classification names, type names, product names, rule names, rule type names.
- **Exception**: Child arrangement keys and involved party/resource/rule delete keys are UUID strings.
- **Delete = expire**: Soft-delete by setting `effectiveToDate` to now. The record remains in the database but falls out of active/date range queries.
- **Add/Update is idempotent**: `addOrUpdate` uses upsert semantics — if a relationship with that name already exists and is active, its value is updated; otherwise a new one is created.

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
| Child Arrangements | `ArrangementXArrangement` | Child UUID | `addChild()` | Expire via query |
| Events | `EventXArrangement` | Event UUID | (read-only in this API) | — |

### SCD (Slowly Changing Dimension) Pattern

All relationship records follow the SCD pattern:
- `effectiveFromDate` / `effectiveToDate` — date range validity.
- `activeFlagID` — active/deleted flag.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- **Expire** = set `effectiveToDate` to now (record stops appearing in range queries).
- **Delete** = set `activeFlagID` to the deleted flag (record stops appearing in active queries).

---

## Common Patterns

### Creating an Arrangement with Full Details (replacing chained service calls)

**Before** (tedious, error-prone):
```java
arrangementsService.create(session, "PackingStaffTimesheet", timesheetId, "Grader", typeValue, system, token)
    .chain(arr -> arr.addClassification(session, "StaffName", name, system, token))
    .chain(() -> arr.addClassification(session, "StationNumber", station, system, token))
    .chain(() -> arr.addClassification(session, "StartDate", start, system, token))
    // ... many more chains
```

**After** (single REST call):
```json
POST /{enterprise}/arrangement/{system}
{
  "type": "PackingStaffTimesheet",
  "classification": "Grader",
  "typeValue": "grader-shift-001",
  "classifications": {
    "StaffName": "John Smith",
    "StationNumber": "5",
    "StartDate": "2025-06-15T08:00:00"
  }
}
```

### Updating Multiple Relationship Types Atomically

```json
PUT /{enterprise}/arrangement/{system}
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
| `ArrangementCreateDTO` | `POST .../{system}` | Create with optional relationships |
| `ArrangementUpdateDTO` | `PUT .../{system}` | Update relationships (add/update/delete) |
| `ArrangementDTO` | Response | Standard arrangement response |
| `ArrangementPivotResponse` | Response | Pivot response with entity refs + timestamps |
| `RelationshipUpdateEntry` | Within `ArrangementUpdateDTO` | Add/update map + delete list for one relationship type |
| `PivotEntry` | Within `ArrangementPivotResponse` | Single pivoted result with entity ref |
| `EntityRef` | Within `PivotEntry` | Entity id + name reference |

