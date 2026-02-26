# Resource Item REST API — Skills Document

## Overview

The `ResourceItemRestService` provides a JAX-RS REST API for managing **Resource Items** within the ActivityMaster FSDM. Resource items represent any storable resource (documents, images, files, data records) and support typed relationships to classifications, resource item types, and child resource items (hierarchy).

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON** for request/response bodies.

---

## Base Path

```
/{enterprise}/resource-item
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation.

---

## DTO Packages

All DTOs used by this API live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.resourceitems` | `ResourceItemDTO`, `ResourceItemFindDTO`, `ResourceItemCreateDTO`, `ResourceItemUpdateDTO`, `ResourceItemUpdateDataDTO`, `ResourceItemSearchDTO`, `ResourceItemDataIncludes`, `SortDirection`, `SearchSortField` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry` (shared across all REST APIs) |

---

## Endpoints

### 1. Find Resource Item

Retrieves a resource item by ID with selectable relationship includes.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}/find` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ResourceItemFindDTO` |
| **Response** | `ResourceItemDTO` |

#### Request Body — `ResourceItemFindDTO`

```json
{
  "resourceItemId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Types", "Classifications", "Children"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `resourceItemId` | `UUID` | **Yes** | The resource item to retrieve. |
| `includes` | `List<ResourceItemDataIncludes>` | No | Which relationship types to include. If omitted/empty, only `resourceItemId` is returned. |

#### Available Includes

| Value | Returns |
|---|---|
| `Types` | Resource item type names → stored values |
| `Classifications` | Classification names → stored values |
| `Children` | Child resource item UUIDs → stored values |

#### Example Response — `ResourceItemDTO`

```json
{
  "resourceItemId": "550e8400-e29b-41d4-a716-446655440000",
  "types": {
    "ProfilePhoto": "avatar-001"
  },
  "classifications": {
    "MimeType": "image/jpeg",
    "FileName": "profile.jpg",
    "FileSize": "245760"
  },
  "children": {
    "a1b2c3d4-...": "thumbnail"
  }
}
```

---

### 2. Search Resource Items

Searches for resource items by resource item type and classification, with optional sorting and result limiting.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}/search` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ResourceItemSearchDTO` |
| **Response** | `List<ResourceItemDTO>` |

#### Request Body — `ResourceItemSearchDTO`

```json
{
  "resourceItemType": "ProfilePhoto",
  "classificationName": "MimeType",
  "classificationValue": "image/jpeg",
  "includes": ["Types", "Classifications"],
  "maxResults": 10,
  "sortDirection": "DESC",
  "sortField": "WAREHOUSE_CREATED_TIMESTAMP"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `resourceItemType` | `String` | **Yes** | The resource item type name to filter by. |
| `classificationName` | `String` | **Yes** | The classification name to match. |
| `classificationValue` | `String` | No | Optional classification value to match. If null, all items with the classification are returned. |
| `includes` | `List<ResourceItemDataIncludes>` | No | Which relationship types to include in each result. |
| `maxResults` | `Integer` | No | Maximum number of results to return. |
| `sortDirection` | `SortDirection` | No | `ASC` (oldest first) or `DESC` (newest first). |
| `sortField` | `SearchSortField` | No | Which field to sort by. Defaults to `WAREHOUSE_CREATED_TIMESTAMP`. |

#### Sort Fields

| Value | Description |
|---|---|
| `EFFECTIVE_FROM_DATE` | Sort by the effective-from date of the cross-reference record. |
| `WAREHOUSE_CREATED_TIMESTAMP` | Sort by the warehouse-created timestamp. |

#### Example: Get Latest Record

```json
{
  "resourceItemType": "ProfilePhoto",
  "classificationName": "UserId",
  "classificationValue": "user-42",
  "maxResults": 1,
  "sortDirection": "DESC"
}
```

#### Example Response

```json
[
  {
    "resourceItemId": "550e8400-e29b-41d4-a716-446655440000",
    "types": {
      "ProfilePhoto": "avatar-001"
    },
    "classifications": {
      "MimeType": "image/jpeg",
      "UserId": "user-42"
    }
  }
]
```

---

### 3. Create Resource Item

Creates a new resource item with a type and data value, optionally adding classifications, resource item types, and child links in the same request.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}/create` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ResourceItemCreateDTO` |
| **Response** | `ResourceItemDTO` |

#### Request Body — `ResourceItemCreateDTO`

```json
{
  "type": "ProfilePhoto",
  "dataValue": "image/jpeg",
  "data": "base64-encoded-bytes...",
  "classifications": {
    "FileName": "profile.jpg",
    "FileSize": "245760",
    "MimeType": "image/jpeg"
  },
  "types": {
    "ImageType": "avatar"
  },
  "children": {
    "child-uuid-string": "thumbnail"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `type` | `String` | **Yes** | Resource item type name (e.g. `"ProfilePhoto"`, `"Document"`). |
| `dataValue` | `String` | **Yes** | The data type or value description for the resource item. |
| `data` | `byte[]` | No | Optional binary data (Base64-encoded in JSON). |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs to add. |
| `types` | `Map<String, String>` | No | Resource item type name → relationship value pairs to add. |
| `children` | `Map<String, String>` | No | Child resource item UUID (as string) → relationship value. |

#### Session & Response Handling

- `resourceItemService.create()` manages its own session — the REST endpoint does **not** wrap it in `withActivityMaster()`.
- **Relationship persistence is fire-and-forget**: Each relationship type (classifications, types, children) is persisted asynchronously on its **own Vert.x context** via `SessionUtils.fireAndForget()`. Each fire-and-forget Uni opens its own session and transaction, fully isolated from the others.
- **Response is returned immediately** from the DTO input (echoed back) — no DB round-trip needed.

#### Entity Resolution

- **Children**: The key is a **child resource item UUID**. The child is resolved via `resourceItemService.findByUUID(session, childId)` before calling `addChild()`.
- Types use `addOrUpdateResourceItemTypes()` which resolves type names internally (no entity resolution needed in the REST service).

---

### 4. Update Resource Item

Updates an existing resource item's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship type.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}/update` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ResourceItemUpdateDTO` |
| **Response** | `ResourceItemDTO` |

#### Request Body — `ResourceItemUpdateDTO`

```json
{
  "resourceItemId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": {
    "addOrUpdate": {
      "FileName": "profile-updated.jpg",
      "FileSize": "512000"
    },
    "delete": ["OldTag"]
  },
  "types": {
    "addOrUpdate": {
      "ImageType": "banner"
    },
    "delete": ["OldType"]
  },
  "children": {
    "addOrUpdate": {
      "child-uuid-string": "high-res"
    },
    "delete": ["old-child-uuid-string"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `resourceItemId` | `UUID` | **Yes** | The resource item to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Resource item type operations. |
| `children` | `RelationshipUpdateEntry` | No | Child resource item operations. |

#### `RelationshipUpdateEntry` Structure

```json
{
  "addOrUpdate": { "Name": "Value" },
  "delete": ["NameToExpire"]
}
```

#### Session & Response Handling (Update)

- The resource item is validated (exists) in an initial session.
- **All relationship updates are fire-and-forget**: Each relationship type is persisted asynchronously on its own Vert.x context via `SessionUtils.fireAndForget()`.
- **Response is returned immediately** from the DTO input — no DB round-trip.

---

### 5. Update Resource Item Data

Replaces the binary data payload of an existing resource item. Only the data is updated — no metadata, classifications, or relationships are affected.

| Property | Value |
|---|---|
| **Method** | `PATCH` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}/data` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ResourceItemUpdateDataDTO` |
| **Response** | `ResourceItemDTO` |

#### Request Body — `ResourceItemUpdateDataDTO`

```json
{
  "resourceItemId": "550e8400-e29b-41d4-a716-446655440000",
  "data": "base64-encoded-bytes..."
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `resourceItemId` | `UUID` | **Yes** | The resource item whose data to update. |
| `data` | `byte[]` | **Yes** | The new binary data (Base64-encoded in JSON). |

---

### 6. Get Resource Item Data

Retrieves the binary data payload of a resource item. The data is returned as a raw octet stream (not JSON-wrapped).

| Property | Value |
|---|---|
| **Method** | `GET` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}/data/{resourceItemId}` |
| **Produces** | `application/octet-stream` |
| **Response** | `byte[]` |

#### Path Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `enterprise` | `String` | **Yes** | The enterprise name. |
| `requestingSystemName` | `String` | **Yes** | The ActivityMaster system performing the operation. |
| `resourceItemId` | `UUID` | **Yes** | The resource item whose binary data to retrieve. |

#### Behavior

1. Resolves the enterprise, system, and identity token via `SessionUtils.withActivityMaster()`.
2. Looks up the resource item by UUID using `resourceItemService.findByUUID()`.
3. Retrieves the binary data via `ResourceItem.getData()`, which reads from the `ResourceItemDataValue` entity and decompresses (un-gzips) the stored bytes.
4. Returns the raw `byte[]` as `application/octet-stream`.

---

## Data Model

### Resource Item

A storable resource entity with a UUID, linked to an enterprise and system. Can contain binary data via `ResourceItemData` / `ResourceItemDataValue`.

### Relationships

| Relationship | Cross-ref Table | Key Type | Add/Update Method | Remove/Expire |
|---|---|---|---|---|
| Classifications | `ResourceItemXClassification` | Classification name | `addOrUpdateClassification()` | `removeClassification()` |
| Resource Item Types | `ResourceItemXResourceItemType` | Type name | `addOrUpdateResourceItemTypes()` | `removeResourceItemTypes()` |
| Children (hierarchy) | `ResourceItemXResourceItem` | Child UUID | `addChild()` | Expire via query |

### SCD Pattern

All relationship records follow the Slowly Changing Dimension pattern:
- **Expire** = set `effectiveToDate` to now.
- **Delete** = set `activeFlagID` to the deleted flag.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.

---

## Async Relationship Persistence

All create and update operations use **fire-and-forget** async persistence for relationships:

1. Each relationship type (types, classifications, children) is persisted in a **separate** `SessionUtils.fireAndForget()` call.
2. Each `fireAndForget()` runs on its **own Vert.x context** (`vertx.getOrCreateContext().runOnContext(...)`) to ensure session isolation.
3. Each async operation opens its own session and transaction via `SessionUtils.withActivityMaster()`.
4. Failures are logged at ERROR level but never propagated to the caller.
5. The HTTP response is returned **immediately** — relationship data is echoed from the submitted DTO.

This pattern avoids Hibernate Reactive session thread-affinity errors (HR000069) and "Illegal pop()" errors that occur when multiple `withTransaction` calls share the same Vert.x context.

---

## Common Patterns

### Creating a Resource Item with Classifications

```json
POST /{enterprise}/resource-item/{system}/create
{
  "type": "Document",
  "dataValue": "application/pdf",
  "classifications": {
    "FileName": "invoice.pdf",
    "FileSize": "1048576",
    "Category": "Finance"
  }
}
```

### Searching for Resource Items

```json
POST /{enterprise}/resource-item/{system}/search
{
  "resourceItemType": "Document",
  "classificationName": "Category",
  "classificationValue": "Finance",
  "includes": ["Types", "Classifications"],
  "maxResults": 20,
  "sortDirection": "DESC"
}
```

### Updating Classifications and Types

```json
PUT /{enterprise}/resource-item/{system}/update
{
  "resourceItemId": "...",
  "classifications": {
    "addOrUpdate": { "Category": "Archive", "Status": "Processed" },
    "delete": ["Pending"]
  },
  "types": {
    "addOrUpdate": { "ArchivedDocument": "archived" }
  }
}
```

### Reading All Relationships

```json
POST /{enterprise}/resource-item/{system}/find
{
  "resourceItemId": "...",
  "includes": ["Types", "Classifications", "Children"]
}
```

### Downloading Binary Data

```
GET /{enterprise}/resource-item/{system}/data/{resourceItemId}
```

The response is `application/octet-stream` — save or process the raw bytes directly.

---

## JSON Serialization

- All DTOs use `@JsonInclude(NON_EMPTY)` — null and empty maps/lists are omitted from responses.
- All DTOs use `@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)` — field-based serialization.
- Binary `data` fields are Base64-encoded in JSON (for request bodies and `ResourceItemUpdateDataDTO`).
- The **Get Data** endpoint (`GET .../data/{resourceItemId}`) returns raw `application/octet-stream`, not JSON.

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `ResourceItemFindDTO` | `POST .../find` | Find by ID with selectable includes |
| `ResourceItemSearchDTO` | `POST .../search` | Search by type + classification with sorting/limiting |
| `ResourceItemCreateDTO` | `POST .../create` | Create with optional relationships |
| `ResourceItemUpdateDTO` | `PUT .../update` | Update relationships (add/update/delete) |
| `ResourceItemUpdateDataDTO` | `PATCH .../data` | Update binary data payload |
| `ResourceItemDTO` | Response | Standard resource item response |
| `RelationshipUpdateEntry` | Within `ResourceItemUpdateDTO` | Add/update map + delete list for one relationship type |
| `SortDirection` | Within `ResourceItemSearchDTO` | `ASC` / `DESC` enum for search ordering |
| `SearchSortField` | Within `ResourceItemSearchDTO` | `EFFECTIVE_FROM_DATE` / `WAREHOUSE_CREATED_TIMESTAMP` enum |
