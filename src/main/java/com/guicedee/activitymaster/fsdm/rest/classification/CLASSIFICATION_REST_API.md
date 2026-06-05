# Classification & Data Concept REST API — Skills Document

## Overview

The `ClassificationRestService` and `ClassificationDataConceptRestService` provide JAX-RS REST APIs for managing the ActivityMaster **shared vocabulary** layer:

- A **ClassificationDataConcept** is the reusable bucket/scheme (the question) — e.g. `IndustryClassifications`, `ArrangementRoles`.
- A **Classification** is a value inside a concept (the answer) — e.g. `Manufacturing`, `Is Managed By`.

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON** for request/response bodies. Create and update apply relationships **fire-and-forget**, echoing the submitted DTO immediately.

> Concept names must resolve to a known `EnterpriseClassificationDataConcepts` value (matched by enum name or its classification value). Classification values are free-form names anchored to a concept (defaulting to `NoClassification`).

---

## Base Paths

```
/{enterprise}/classification
/{enterprise}/classification-data-concept
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation (security scope).

---

## DTO Packages

All DTOs used by these APIs live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.classifications` | `ClassificationDTO`, `ClassificationFindDTO`, `ClassificationCreateDTO`, `ClassificationUpdateDTO`, `ClassificationDataIncludes`, `ClassificationDataConceptDTO`, `ClassificationDataConceptFindDTO`, `ClassificationDataConceptCreateDTO`, `ClassificationDataConceptUpdateDTO`, `ClassificationDataConceptDataIncludes` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry` (shared across all REST APIs) |

---

# Classification Endpoints

## 1. Find Classification

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/classification/{requestingSystemName}/find` |
| **Request Body** | `ClassificationFindDTO` |
| **Response** | `ClassificationDTO` |

#### Request Body — `ClassificationFindDTO`

```json
{
  "classificationId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Children"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `classificationId` | `UUID` | **Yes** | The classification to retrieve. |
| `includes` | `List<ClassificationDataIncludes>` | No | Which relationships to hydrate. If omitted/empty, only core fields are returned. |

#### Available Includes

| Value | Returns |
|---|---|
| `Children` | Child classification names → hierarchy values (via `ClassificationXClassification`) |

#### Example Response — `ClassificationDTO`

```json
{
  "classificationId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Manufacturing",
  "description": "Manufacturing industry",
  "sequenceNumber": 1,
  "concept": "IndustryClassifications",
  "children": {
    "Light Industrial": ""
  }
}
```

---

## 2. Create Classification

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/classification/{requestingSystemName}/create` |
| **Request Body** | `ClassificationCreateDTO` |
| **Response** | `ClassificationDTO` |

#### Request Body — `ClassificationCreateDTO`

```json
{
  "name": "Manufacturing",
  "description": "Manufacturing industry",
  "concept": "IndustryClassifications",
  "sequenceNumber": 1,
  "parentName": "Industries",
  "children": {
    "Light Industrial": ""
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `name` | `String` | **Yes** | The classification value name. |
| `description` | `String` | No | Human-readable explanation. |
| `concept` | `String` | No | Owning concept name (known `EnterpriseClassificationDataConcepts`). When blank/unknown, defaults to `NoClassification`. |
| `sequenceNumber` | `Integer` | No | Sort order within the concept (defaults to 1). |
| `parentName` | `String` | No | Parent classification name to attach this value beneath. |
| `children` | `Map<String, String>` | No | Existing child classification name → hierarchy value, attached after creation. |

#### Session & Response Handling

- `classificationService.create()` runs **inside** the request's `withActivityMaster()` session, resolving the concept (or `NoClassification`) and optional parent.
- **Child hierarchy persistence is fire-and-forget** via `SessionUtils.fireAndForget()` — each runs on its own Vert.x context/session.
- **Response is returned immediately** from the DTO input — no extra DB round-trip.

---

## 3. Update Classification

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/classification/{requestingSystemName}/update` |
| **Request Body** | `ClassificationUpdateDTO` |
| **Response** | `ClassificationDTO` |

#### Request Body — `ClassificationUpdateDTO`

```json
{
  "classificationId": "550e8400-e29b-41d4-a716-446655440000",
  "description": "Updated description",
  "sequenceNumber": 2,
  "children": {
    "addOrUpdate": { "Heavy Industrial": "" },
    "delete": ["Light Industrial"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `classificationId` | `UUID` | **Yes** | The classification to update. |
| `description` | `String` | No | New description (applied in place to the managed entity). |
| `sequenceNumber` | `Integer` | No | New sort order (applied in place). |
| `children` | `RelationshipUpdateEntry` | No | Child hierarchy operations. `addOrUpdate` → `addChild()`, `delete` → `archiveChild()`. |

- Core fields (`description`, `sequenceNumber`) are mutated on the managed entity and flushed by the surrounding transaction.
- **Child operations are fire-and-forget**; the response echoes the intended `addOrUpdate` state.

---

# Classification Data Concept Endpoints

## 1. Find Data Concept

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/classification-data-concept/{requestingSystemName}/find` |
| **Request Body** | `ClassificationDataConceptFindDTO` |
| **Response** | `ClassificationDataConceptDTO` |

#### Request Body — `ClassificationDataConceptFindDTO`

```json
{
  "conceptId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Classifications", "Resources"]
}
```

#### Available Includes

| Value | Returns |
|---|---|
| `Classifications` | Classification value names → descriptions belonging to this concept |
| `Resources` | Value classification names → resource item UUIDs (via `ClassificationDataConceptXResourceItem`) |

#### Example Response — `ClassificationDataConceptDTO`

```json
{
  "conceptId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "IndustryClassifications",
  "description": "Business industry categories reusable across domains",
  "classifications": {
    "Manufacturing": "Manufacturing industry",
    "Agricultural": "Agricultural industry"
  },
  "resources": {
    "Documentation Item": "r1s2t3u4-..."
  }
}
```

---

## 2. Create Data Concept

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/classification-data-concept/{requestingSystemName}/create` |
| **Request Body** | `ClassificationDataConceptCreateDTO` |
| **Response** | `ClassificationDataConceptDTO` |

#### Request Body — `ClassificationDataConceptCreateDTO`

```json
{
  "name": "IndustryClassifications",
  "description": "Business industry categories reusable across domains",
  "resources": {
    "Documentation Item": "r1s2t3u4-e5f6-7890-abcd-ef1234567890"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `name` | `String` | **Yes** | Concept name — must resolve to a known `EnterpriseClassificationDataConcepts` value (`400 Bad Request` otherwise). |
| `description` | `String` | No | Friendly explanation of the bucket. |
| `resources` | `Map<String, String>` | No | Value classification name → **resource item UUID**, attached after creation. |

- `dataConceptService.createDataConcept()` runs inside the request session. Resource items are resolved via `resourceItemService.findByUUID()` and linked through `addOrUpdateResourceItem()` fire-and-forget.

---

## 3. Update Data Concept

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/classification-data-concept/{requestingSystemName}/update` |
| **Request Body** | `ClassificationDataConceptUpdateDTO` |
| **Response** | `ClassificationDataConceptDTO` |

#### Request Body — `ClassificationDataConceptUpdateDTO`

```json
{
  "conceptId": "550e8400-e29b-41d4-a716-446655440000",
  "description": "Updated concept description",
  "resources": {
    "addOrUpdate": { "Documentation Item": "resource-item-uuid" },
    "delete": ["OldDoc"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `conceptId` | `UUID` | **Yes** | The concept to update. |
| `description` | `String` | No | New description (applied in place). |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations. `addOrUpdate` → `addOrUpdateResourceItem()`, `delete` → expire matching `ClassificationDataConceptXResourceItem` rows by value-classification name. |

---

## Data Model

| Entity | Cross-ref Table(s) | Notes |
|---|---|---|
| `ClassificationDataConcept` | `ClassificationDataConceptXClassification`, `ClassificationDataConceptXResourceItem` | The bucket/scheme; owns its classification values and supporting resources. |
| `Classification` | `ClassificationXClassification` (hierarchy), `ClassificationXResourceItem` | A value anchored to a concept; participates in parent/child hierarchy. |

### SCD Pattern

All relationship records follow the Slowly Changing Dimension pattern:
- **Expire** = set `effectiveToDate` to now.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- All lazy-loaded entities are fetched via `session.fetch()` (Hibernate Reactive requirement).

---

## Async Relationship Persistence

Create and update operations use **fire-and-forget** async persistence for relationships:

1. Each relationship category is persisted in a separate `SessionUtils.fireAndForget()` call.
2. Each `fireAndForget()` runs on its own Vert.x context to ensure session isolation.
3. Each async operation opens its own session and transaction via `SessionUtils.withActivityMaster()`.
4. Failures are logged at ERROR level but never propagated to the caller.
5. The HTTP response is returned **immediately** — relationship data is echoed from the submitted DTO.

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `ClassificationFindDTO` | `POST classification/.../find` | Find a value by ID with selectable includes |
| `ClassificationCreateDTO` | `POST classification/.../create` | Create a value under a concept (+ optional parent/children) |
| `ClassificationUpdateDTO` | `PUT classification/.../update` | Update core fields + child hierarchy |
| `ClassificationDTO` | Response | Standard classification response |
| `ClassificationDataConceptFindDTO` | `POST classification-data-concept/.../find` | Find a concept by ID with selectable includes |
| `ClassificationDataConceptCreateDTO` | `POST classification-data-concept/.../create` | Create a concept (known name) + optional resources |
| `ClassificationDataConceptUpdateDTO` | `PUT classification-data-concept/.../update` | Update description + resource links |
| `ClassificationDataConceptDTO` | Response | Standard concept response |
| `RelationshipUpdateEntry` | Within update DTOs | Add/update map + delete list for one relationship category |

