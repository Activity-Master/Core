# Involved Party REST API — Skills Document

## Overview

The `PartyRestService` provides a JAX-RS REST API for managing **Involved Parties** within the ActivityMaster FSDM. Involved parties represent people, organisations, or entities (organic or non-organic) and support typed relationships to classifications, involved party types, name types, identification types, resource items, products, rules, addresses, and child parties (hierarchy).

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON** for request/response bodies.

---

## Base Path

```
/{enterprise}/party
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation.

---

## DTO Packages

All DTOs used by this API live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.parties` | `PartyDTO`, `PartyFindDTO`, `PartyCreateDTO`, `PartyUpdateDTO`, `PartySearchByClassificationDTO`, `PartySearchByIdentificationDTO`, `PartyDataIncludes` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry` (shared across all REST APIs) |

---

## Endpoints

### 1. Find Party

Retrieves an involved party by ID with selectable relationship includes.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/party/{requestingSystemName}/find` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `PartyFindDTO` |
| **Response** | `PartyDTO` |

#### Request Body — `PartyFindDTO`

```json
{
  "partyId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Types", "Classifications", "NameTypes", "IdentificationTypes"]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `partyId` | `UUID` | **Yes** | The involved party to retrieve. |
| `includes` | `List<PartyDataIncludes>` | No | Which relationships to include. If omitted/empty, only `partyId` is returned. |

#### Available Includes

| Value | Returns |
|---|---|
| `Types` | Involved party type classification names → stored values |
| `Classifications` | Classification names → stored values |
| `NameTypes` | Name type classification names → stored values |
| `IdentificationTypes` | Identification type classification names → stored values |
| `Resources` | Resource item classification names → resource item IDs |
| `Products` | Product classification names → product IDs |
| `Rules` | Rule classification names → rule IDs |
| `Addresses` | Address classification names → address IDs |
| `Children` | Child involved party IDs → stored values |

#### Example Response — `PartyDTO`

```json
{
  "partyId": "550e8400-e29b-41d4-a716-446655440000",
  "types": {
    "NoClassification": "Customer"
  },
  "classifications": {
    "Status": "Active",
    "Tier": "Gold"
  },
  "nameTypes": {
    "NoClassification": "John Smith"
  },
  "identificationTypes": {
    "NoClassification": "user@example.com"
  },
  "resources": {
    "ProfilePhoto": "r1s2t3u4-..."
  },
  "addresses": {
    "Home": "a1b2c3d4-..."
  }
}
```

---

### 2. Search Parties by Classification

Searches for involved parties that have a specific classification name and value.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/party/{requestingSystemName}/search/classification` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `PartySearchByClassificationDTO` |
| **Response** | `List<PartyDTO>` |

#### Request Body — `PartySearchByClassificationDTO`

```json
{
  "classificationName": "Status",
  "classificationValue": "Active",
  "includes": ["Types", "Classifications", "IdentificationTypes"],
  "maxResults": 20
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `classificationName` | `String` | **Yes** | The classification name to match. |
| `classificationValue` | `String` | **Yes** | The classification value to match. |
| `includes` | `List<PartyDataIncludes>` | No | Which relationships to include in each result. If omitted/empty, only `partyId` is returned. |
| `maxResults` | `Integer` | No | Maximum number of results to return. If null or ≤ 0, all matching results are returned. |

#### Example Response

```json
[
  {
    "partyId": "550e8400-e29b-41d4-a716-446655440000",
    "types": { "NoClassification": "Customer" },
    "classifications": { "Status": "Active", "Tier": "Gold" },
    "identificationTypes": { "NoClassification": "user@example.com" }
  },
  {
    "partyId": "660f9511-f3ac-52e5-b827-557766551111",
    "types": { "NoClassification": "Customer" },
    "classifications": { "Status": "Active", "Tier": "Silver" },
    "identificationTypes": { "NoClassification": "other@example.com" }
  }
]
```

---

### 3. Search Parties by Identification Type

Searches for involved parties by identification type name and optional value.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/party/{requestingSystemName}/search/identification` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `PartySearchByIdentificationDTO` |
| **Response** | `List<PartyDTO>` |

#### Request Body — `PartySearchByIdentificationDTO`

```json
{
  "identificationType": "Email",
  "identificationValue": "user@example.com",
  "includes": ["Types", "Classifications", "NameTypes"],
  "maxResults": 5
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `identificationType` | `String` | **Yes** | The identification type name to match (e.g. `"Email"`, `"PhoneNumber"`, `"IdentificationTypeUUID"`). |
| `identificationValue` | `String` | No | Optional identification value to match. If null, all parties with the identification type are returned. |
| `includes` | `List<PartyDataIncludes>` | No | Which relationships to include in each result. If omitted/empty, only `partyId` is returned. |
| `maxResults` | `Integer` | No | Maximum number of results to return. If null or ≤ 0, all matching results are returned. |

#### Example: Find All Parties with Email

```json
{
  "identificationType": "Email",
  "includes": ["IdentificationTypes"],
  "maxResults": 100
}
```

#### Example: Find Party by Exact Email

```json
{
  "identificationType": "Email",
  "identificationValue": "user@example.com",
  "includes": ["Types", "Classifications", "NameTypes", "IdentificationTypes"]
}
```

#### Example Response

```json
[
  {
    "partyId": "550e8400-e29b-41d4-a716-446655440000",
    "types": { "NoClassification": "Customer" },
    "classifications": { "Status": "Active" },
    "nameTypes": { "NoClassification": "John Smith" },
    "identificationTypes": { "NoClassification": "user@example.com" }
  }
]
```

---

### 4. Create Party

Creates a new involved party with an identification type and value, optionally adding classifications, types, name types, resource items, products, rules, and child party links in the same request.

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/party/{requestingSystemName}/create` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `PartyCreateDTO` |
| **Response** | `PartyDTO` |

#### Request Body — `PartyCreateDTO`

```json
{
  "identificationType": "Email",
  "identificationValue": "user@example.com",
  "organic": true,
  "classifications": {
    "Status": "Active",
    "Tier": "Gold"
  },
  "types": {
    "Customer": "retail"
  },
  "nameTypes": {
    "FullName": "John Smith"
  },
  "resources": {
    "ProfilePhoto": "photo-resource-value"
  },
  "products": {
    "Subscription": "premium"
  },
  "rules": {
    "AccessLevel": "standard"
  },
  "children": {
    "child-party-uuid": "dependent"
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `identificationType` | `String` | **Yes** | Identification type name (e.g. `"Email"`, `"PhoneNumber"`). |
| `identificationValue` | `String` | **Yes** | Identification type value (e.g. `"user@example.com"`, `"+1234567890"`). |
| `organic` | `boolean` | No | Whether this party is organic (person) or non-organic (organisation). Defaults to `true`. |
| `classifications` | `Map<String, String>` | No | Classification name → value pairs. |
| `types` | `Map<String, String>` | No | Involved party type classification name → store value pairs. |
| `nameTypes` | `Map<String, String>` | No | Name type classification name → store value pairs. |
| `resources` | `Map<String, String>` | No | Classification name → store value for resource item relationships. |
| `products` | `Map<String, String>` | No | Classification name → store value for product relationships. |
| `rules` | `Map<String, String>` | No | Classification name → store value for rule relationships. |
| `children` | `Map<String, String>` | No | Child party UUID (as string) → relationship value. |

#### Session Handling

The `create` endpoint does **not** wrap `involvedPartyService.create()` in `withActivityMaster()` because the service method manages its own session. Subsequent relationship operations and response fetching each open their own fresh sessions.

---

### 5. Update Party

Updates an existing involved party's relationships. Supports **add/update** (upsert) and **delete** (expire/soft-delete) for each relationship type.

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/party/{requestingSystemName}/update` |
| **Consumes** | `application/json` |
| **Produces** | `application/json` |
| **Request Body** | `PartyUpdateDTO` |
| **Response** | `PartyDTO` |

#### Request Body — `PartyUpdateDTO`

```json
{
  "partyId": "550e8400-e29b-41d4-a716-446655440000",
  "classifications": {
    "addOrUpdate": {
      "Status": "Inactive",
      "DeactivatedDate": "2026-02-22"
    },
    "delete": ["Tier"]
  },
  "types": {
    "addOrUpdate": {
      "FormerCustomer": "churned"
    },
    "delete": ["Customer"]
  },
  "nameTypes": {
    "addOrUpdate": {
      "LegalName": "Jonathan Smith"
    }
  },
  "identificationTypes": {
    "addOrUpdate": {
      "PhoneNumber": "+1234567890"
    }
  },
  "resources": {
    "addOrUpdate": {
      "ProfilePhoto": "new-photo-value"
    }
  },
  "products": {
    "delete": ["Subscription"]
  },
  "rules": {
    "addOrUpdate": {
      "AccessLevel": "restricted"
    }
  },
  "children": {
    "delete": ["old-child-uuid"]
  }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `partyId` | `UUID` | **Yes** | The involved party to update. |
| `classifications` | `RelationshipUpdateEntry` | No | Classification operations. |
| `types` | `RelationshipUpdateEntry` | No | Involved party type operations (key = classification name). |
| `nameTypes` | `RelationshipUpdateEntry` | No | Name type operations (key = classification name). |
| `identificationTypes` | `RelationshipUpdateEntry` | No | Identification type operations (key = classification name). |
| `resources` | `RelationshipUpdateEntry` | No | Resource item operations (key = classification name). |
| `products` | `RelationshipUpdateEntry` | No | Product operations (key = classification name). |
| `rules` | `RelationshipUpdateEntry` | No | Rule operations (key = classification name). |
| `children` | `RelationshipUpdateEntry` | No | Child party operations (key = child party UUID string). |

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
| `delete` | `List<String>` | Names to expire (soft-delete). For classifications, uses `removeClassification()`. For types/name types/identification types/resources/products/rules, queries the cross-ref table and expires matching rows. For children, matches by child party UUID. |

---

## Data Model

### Involved Party

A person, organisation, or entity with a UUID, linked to an enterprise and system. Can be **organic** (person) or **non-organic** (organisation/system).

### Relationships

| Relationship | Cross-ref Table | Key Type | Add/Update Method | Remove/Expire |
|---|---|---|---|---|
| Classifications | `InvolvedPartyXClassification` | Classification name | `addOrUpdateClassification()` | `removeClassification()` |
| Involved Party Types | `InvolvedPartyXInvolvedPartyType` | Classification name | `addOrUpdateInvolvedPartyType()` | Expire via query |
| Name Types | `InvolvedPartyXInvolvedPartyNameType` | Classification name | `addOrUpdateInvolvedPartyNameType()` | Expire via query |
| Identification Types | `InvolvedPartyXInvolvedPartyIdentificationType` | Classification name | `addOrUpdateInvolvedPartyIdentificationType()` | Expire via query |
| Resource Items | `InvolvedPartyXResourceItem` | Classification name | `addOrUpdateResourceItem()` | Expire via query |
| Products | `InvolvedPartyXProduct` | Classification name | `addOrUpdateProduct()` | Expire via query |
| Rules | `InvolvedPartyXRules` | Classification name | `addOrUpdateRules()` | Expire via query |
| Addresses | `InvolvedPartyXAddress` | Classification name | `addOrUpdateAddress()` | Expire via query |
| Children (hierarchy) | `InvolvedPartyXInvolvedParty` | Child party UUID | `addChild()` | Expire via query |

### SCD Pattern

All relationship records follow the Slowly Changing Dimension pattern:
- **Expire** = set `effectiveToDate` to now.
- Queries use `.inActiveRange()` and `.inDateRange()` to filter to current, active records.
- All lazy-loaded entities are fetched via `session.fetch()` (Hibernate Reactive requirement).

---

## Response Key Conventions

For relationships that link through a classification (types, name types, identification types, resources, products, rules, addresses):
- **Key** = classification name (the descriptive classification on the cross-reference row)
- **Value** = the related entity's name or UUID

For direct relationships (children):
- **Key** = child party UUID
- **Value** = the stored relationship value

For classifications:
- **Key** = classification name
- **Value** = stored value

---

## Common Patterns

### Creating a Customer Party

```json
POST /{enterprise}/party/{system}/create
{
  "identificationType": "Email",
  "identificationValue": "customer@example.com",
  "organic": true,
  "classifications": {
    "Status": "Active",
    "RegisteredDate": "2026-02-22"
  },
  "types": {
    "Customer": "retail"
  },
  "nameTypes": {
    "FullName": "Jane Doe"
  }
}
```

### Creating an Organisation (Non-Organic)

```json
POST /{enterprise}/party/{system}/create
{
  "identificationType": "BusinessNumber",
  "identificationValue": "ABN-12345678",
  "organic": false,
  "classifications": {
    "CompanyName": "Acme Corp",
    "Industry": "Technology"
  },
  "types": {
    "Vendor": "supplier"
  }
}
```

### Searching for Active Customers

```json
POST /{enterprise}/party/{system}/search/classification
{
  "classificationName": "Status",
  "classificationValue": "Active",
  "includes": ["Types", "Classifications", "IdentificationTypes"],
  "maxResults": 50
}
```

### Finding a Party by Email

```json
POST /{enterprise}/party/{system}/search/identification
{
  "identificationType": "Email",
  "identificationValue": "customer@example.com",
  "includes": ["Types", "Classifications", "NameTypes", "IdentificationTypes"]
}
```

### Updating Party Details

```json
PUT /{enterprise}/party/{system}/update
{
  "partyId": "...",
  "classifications": {
    "addOrUpdate": { "Status": "Inactive", "DeactivatedDate": "2026-02-22" },
    "delete": ["Tier"]
  },
  "nameTypes": {
    "addOrUpdate": { "LegalName": "Jane M. Doe" }
  }
}
```

### Reading All Relationships

```json
POST /{enterprise}/party/{system}/find
{
  "partyId": "...",
  "includes": ["Types", "Classifications", "NameTypes", "IdentificationTypes", "Resources", "Products", "Rules", "Addresses", "Children"]
}
```

---

## JSON Serialization

- All DTOs use `@JsonInclude(NON_EMPTY)` — null and empty maps/lists are omitted from responses.
- All DTOs use `@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)` — field-based serialization.

---

## RestClient Quick Reference

The `RestClients` class in the client module provides convenience methods for calling all party endpoints:

| Method | Endpoint | Description |
|---|---|---|
| `createParty(systemName, dto)` | `POST .../create` | Create a new involved party |
| `updateParty(systemName, dto)` | `PUT .../update` | Update an involved party's relationships |
| `findParty(systemName, dto)` | `POST .../find` | Find a party by ID with includes |
| `searchPartiesByClassification(systemName, dto)` | `POST .../search/classification` | Search parties by classification |
| `searchPartiesByIdentification(systemName, dto)` | `POST .../search/identification` | Search parties by identification type |

---

## DTO Quick Reference

| DTO | Used By | Purpose |
|---|---|---|
| `PartyFindDTO` | `POST .../find` | Find by ID with selectable includes |
| `PartySearchByClassificationDTO` | `POST .../search/classification` | Search by classification name + value with optional includes and max results |
| `PartySearchByIdentificationDTO` | `POST .../search/identification` | Search by identification type name + optional value with optional includes and max results |
| `PartyCreateDTO` | `POST .../create` | Create with identification type + optional relationships |
| `PartyUpdateDTO` | `PUT .../update` | Update relationships (add/update/delete) |
| `PartyDTO` | Response | Standard involved party response |
| `RelationshipUpdateEntry` | Within `PartyUpdateDTO` | Add/update map + delete list for one relationship type |
| `PartyDataIncludes` | Within find/search DTOs | Enum of includeable relationship types |

