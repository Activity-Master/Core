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

### 2. Create Resource Item

Creates a new resource item with a type and data value, optionally adding classifications, resource item types, and child links in the same request.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}` |
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

#### Behavior

1. Creates the resource item with the given `type` and `dataValue` (and optional binary `data`).
2. If any optional relationship maps are provided, adds them via `addOrUpdateClassification`, `addOrUpdateResourceItemTypes`, and `addChild`.
3. Returns the full `ResourceItemDTO` with Types, Classifications, and any Children that were provided.

#### Important: Session Handling

The `create` endpoint does **not** wrap `resourceItemService.create()` in `withActivityMaster()` because the service method manages its own session. Subsequent relationship operations and response fetching each open their own fresh sessions.

---

### 3. Update Resource Item

Updates an existing resource item's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship type.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/resource-item/{requestingSystemName}` |
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

| Field | Type | Description |
|---|---|---|
| `addOrUpdate` | `Map<String, String>` | Key/value pairs to upsert. Key is the **name** (classification name, type name). |
| `delete` | `List<String>` | Names to expire (soft-delete). For classifications and resource item types, uses dedicated remove methods. For children, queries the hierarchy table and expires matching rows. |

---

### 4. Update Resource Item Data

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

#### Example Response

```json
{
  "resourceItemId": "550e8400-e29b-41d4-a716-446655440000"
}
```

The response confirms the update with the resource item ID. No relationship data is returned.

---

## Data Model

### Resource Item

A storable resource entity with a UUID, linked to an enterprise and system. Can contain binary data via `ResourceItemData`.

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

## Common Patterns

### Creating a Resource Item with Classifications

```json
POST /{enterprise}/resource-item/{system}
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

### Updating Classifications and Types

```json
PUT /{enterprise}/resource-item/{system}
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

---

## JSON Serialization

- All DTOs use `@JsonInclude(NON_EMPTY)` — null and empty maps/lists are omitted from responses.
- All DTOs use `@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)` — field-based serialization.
- Binary `data` fields are Base64-encoded in JSON.

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `ResourceItemFindDTO` | `POST .../find` | Find by ID with selectable includes |
| `ResourceItemCreateDTO` | `POST .../{system}` | Create with optional relationships |
| `ResourceItemUpdateDTO` | `PUT .../{system}` | Update relationships (add/update/delete) |
| `ResourceItemUpdateDataDTO` | `PATCH .../data` | Update binary data payload |
| `ResourceItemDTO` | Response | Standard resource item response |
| `RelationshipUpdateEntry` | Within `ResourceItemUpdateDTO` | Add/update map + delete list for one relationship type |

