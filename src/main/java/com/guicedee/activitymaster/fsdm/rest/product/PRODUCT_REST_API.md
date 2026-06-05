# Product REST API — Skills Document

## Overview

The `ProductRestService` provides a JAX-RS REST API for managing **Products** within the ActivityMaster FSDM. Products represent goods or services in the catalogue and support typed relationships to **product types**, **classifications**, and **resource items**.

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON** for request/response bodies.

> Unlike Events or Parties, the Product domain exposes only three relationship categories — **Types**, **Classifications**, and **Resources**. The `IProduct` capability surface (`IManageProductTypes`, `IManageClassifications`, `IManageResourceItems`) does not include hierarchy, party, rule, or arrangement management, so those categories are intentionally absent from this API.

---

## Base Path

```
/{enterprise}/product
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation.

---

## DTO Packages

All DTOs used by this API live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.products` | `ProductDTO`, `ProductFindDTO`, `ProductCreateDTO`, `ProductUpdateDTO`, `ProductDataIncludes` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry` (shared across all REST APIs) |

---

## Endpoints

### 1. Find Product

Retrieves a product by ID with selectable relationship includes. The product's core attributes (`name`, `description`, `code`) are always populated from the loaded entity.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/product/{requestingSystemName}/find` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ProductFindDTO` |
| **Response** | `ProductDTO` |

#### Request Body — `ProductFindDTO`

```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Types", "Classifications", "Resources"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `productId` | `UUID` | **Yes** | The product to retrieve. |
| `includes` | `List<ProductDataIncludes>` | No | Which relationships to include. If omitted/empty, only the core attributes are returned. |

#### Available Includes

| Value | Returns |
|---|---|
| `Types` | Product type names → stored values |
| `Classifications` | Classification names → stored values |
| `Resources` | Classification names → resource item UUIDs |

#### Example Response — `ProductDTO`

```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Premium Widget",
  "description": "Top-tier widget for industrial use",
  "code": "WID-001",
  "types": {
    "Hardware": "catalogue"
  },
  "classifications": {
    "Status": "Active",
    "Tier": "Premium"
  },
  "resources": {
    "Datasheet": "r1s2t3u4-..."
  }
}
```

---

### 2. Create Product

Creates a new product. The first entry in `types` is used as the primary product type for the underlying `createProduct()` call (which also seeds the product code, name and description). All `types` entries (including the first) are then persisted with their values asynchronously alongside any classifications and resources.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/product/{requestingSystemName}/create` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ProductCreateDTO` |
| **Response** | `ProductDTO` |

#### Request Body — `ProductCreateDTO`

```json
{
  "name": "Premium Widget",
  "description": "Top-tier widget for industrial use",
  "code": "WID-001",
  "types": {
    "Hardware": "catalogue"
  },
  "classifications": {
    "Status": "Active",
    "Tier": "Premium"
  },
  "resources": {
    "Datasheet": "r1s2t3u4-e5f6-7890-abcd-ef1234567890"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `name` | `String` | **Yes** | The product name. |
| `description` | `String` | No | The product description. |
| `code` | `String` | No | The short catalogue code (max 10 chars). |
| `types` | `Map<String, String>` | **Yes** | Product type name → relationship value pairs. **At least one entry is required.** The first entry is used as the primary product type during creation; all entries are persisted with their values. |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs. |
| `resources` | `Map<String, String>` | No | Classification name → **resource item UUID**. The UUID is resolved to an entity before linking. |

#### Session & Response Handling

- `productService.createProduct()` runs **inside** the request's `withActivityMaster()` session (it requires a caller-supplied `Mutiny.Session`). It seeds the primary product type link automatically (`NoClassification`, empty value).
- **Relationship persistence is fire-and-forget**: Each relationship category (types, classifications, resources) is persisted asynchronously on its **own Vert.x context** via `SessionUtils.fireAndForget()`. Each fire-and-forget Uni opens its own session and transaction, fully isolated from the others.
- **Response is returned immediately** from the DTO input (echoed back) — no extra DB round-trip needed. The response includes all submitted relationship data even though the async persistence may still be in progress.

#### Entity Resolution

- **Resources**: The value is a **resource item UUID**. It is resolved via `resourceItemService.findByUUID(session, riId)` before being passed to `addOrUpdateResourceItem()`.
- **Types** and **Classifications** pass the map value as a store value string (the type/classification name is resolved internally by name).

---

### 3. Update Product

Updates an existing product's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship category.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/product/{requestingSystemName}/update` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `ProductUpdateDTO` |
| **Response** | `ProductDTO` |

#### Request Body — `ProductUpdateDTO`

```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": {
    "addOrUpdate": {
      "Status": "Discontinued"
    },
    "delete": ["Active"]
  },
  "types": {
    "addOrUpdate": {
      "Refurbished": "catalogue"
    },
    "delete": ["Hardware"]
  },
  "resources": {
    "addOrUpdate": {
      "Datasheet": "resource-item-uuid"
    },
    "delete": ["OldDatasheet"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `productId` | `UUID` | **Yes** | The product to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Product type operations. |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations. Key = classification name, Value = **resource item UUID**. |

#### `RelationshipUpdateEntry` Structure

```json
{
  "addOrUpdate": { "Name": "Value" },
  "delete": ["NameToExpire"]
}
```

#### Session & Response Handling (Update)

- The product is validated (exists) in an initial session.
- **All relationship updates are fire-and-forget**: Each relationship category is persisted asynchronously on its own Vert.x context via `SessionUtils.fireAndForget()`.
- **Response is returned immediately** from the DTO input — no DB round-trip.
- **Delete semantics**: classifications use `removeClassification()`, types use `removeProductTypes()`, and resources are expired by querying the active `ProductXResourceItem` rows and matching the classification name.

---

## Data Model

### Product

A catalogue good or service entity with a UUID, name, description and code, linked to an enterprise and system.

### Relationships

| Relationship | Cross-ref Table | Key Type | Add/Update Method | Remove/Expire |
|---|---|---|---|---|
| Product Types | `ProductXProductType` | Type name | `addOrUpdateProductTypes()` | `removeProductTypes()` |
| Classifications | `ProductXClassification` | Classification name | `addOrUpdateClassification()` | `removeClassification()` |
| Resource Items | `ProductXResourceItem` | Classification name | `addOrUpdateResourceItem()` | Expire via query |

> The entity also carries reverse-mapped collections for arrangements, involved parties, events, and child products (`ProductXProduct`), but these are owned and managed from the other side of the relationship and are **not** exposed through this API.

### SCD Pattern

All relationship records follow the Slowly Changing Dimension pattern:
- **Expire** = set `effectiveToDate` to now.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- All lazy-loaded entities are fetched via `session.fetch()` (Hibernate Reactive requirement).

---

## Response Key Conventions

For all relationship maps:
- **Key** = classification name or product type name (the descriptive name on the cross-reference row)
- **Value** = the related entity's UUID (for resources) or the stored relationship value (for types, classifications)

---

## Async Relationship Persistence

All create and update operations use **fire-and-forget** async persistence for relationships:

1. Each relationship category (types, classifications, resources) is persisted in a **separate** `SessionUtils.fireAndForget()` call.
2. Each `fireAndForget()` runs on its **own Vert.x context** (`vertx.getOrCreateContext().runOnContext(...)`) to ensure session isolation.
3. Each async operation opens its own session and transaction via `SessionUtils.withActivityMaster()`.
4. Failures are logged at ERROR level but never propagated to the caller.
5. The HTTP response is returned **immediately** — relationship data is echoed from the submitted DTO.

This pattern avoids Hibernate Reactive session thread-affinity errors (HR000069) and "Illegal pop()" errors that occur when multiple `withTransaction` calls share the same Vert.x context.

---

## Common Patterns

### Creating a Product with Full Details

```json
POST /{enterprise}/product/{system}/create
{
  "name": "Premium Widget",
  "description": "Top-tier widget for industrial use",
  "code": "WID-001",
  "types": {
    "Hardware": "catalogue"
  },
  "classifications": {
    "Status": "Active",
    "Tier": "Premium"
  },
  "resources": {
    "Datasheet": "resource-item-uuid"
  }
}
```

### Discontinuing a Product

```json
PUT /{enterprise}/product/{system}/update
{
  "productId": "...",
  "classifications": {
    "addOrUpdate": { "Status": "Discontinued" },
    "delete": ["Active"]
  }
}
```

### Reading All Relationships

```json
POST /{enterprise}/product/{system}/find
{
  "productId": "...",
  "includes": ["Types", "Classifications", "Resources"]
}
```

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `ProductFindDTO` | `POST .../find` | Find by ID with selectable includes |
| `ProductCreateDTO` | `POST .../create` | Create with mandatory name + types + optional relationships |
| `ProductUpdateDTO` | `PUT .../update` | Update relationships (add/update/delete) |
| `ProductDTO` | Response | Standard product response |
| `RelationshipUpdateEntry` | Within `ProductUpdateDTO` | Add/update map + delete list for one relationship category |

