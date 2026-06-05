# Rules & Rule Type REST API — Skills Document

## Overview

The `RulesRestService` and `RulesTypeRestService` provide JAX-RS REST APIs for the ActivityMaster **Rules** domain — reusable business requirements (limits, thresholds, eligibility criteria, prices, permissions) and the structural **rule types** that classify them.

- A **Rule** (`Rules`) is the reusable rule set / business requirement.
- A **RulesType** is the structural/implementation type of a rule (e.g. `Single Rule`, `Statement Rule`, `Range Rule`, `Matrix Rule`).

All endpoints are **reactive** (returning `Uni<T>` via SmallRye Mutiny) and use **JSON**. Create and update apply relationships **fire-and-forget**, echoing the submitted DTO immediately.

---

## Base Paths

```
/{enterprise}/rules
/{enterprise}/rules-type
```

- `{enterprise}` — The enterprise name (path parameter on every request).
- All endpoints additionally require `{requestingSystemName}` — the ActivityMaster system performing the operation (security scope).

---

## DTO Packages

All DTOs live in the **client** module:

| Package | Contents |
|---|---|
| `com.guicedee.activitymaster.fsdm.client.services.rest.rules` | `RulesDTO`, `RulesFindDTO`, `RulesCreateDTO`, `RulesUpdateDTO`, `RulesDataIncludes`, `RulesTypeDTO`, `RulesTypeFindDTO`, `RulesTypeCreateDTO`, `RulesTypeUpdateDTO`, `RulesTypeDataIncludes` |
| `com.guicedee.activitymaster.fsdm.client.services.rest` | `RelationshipUpdateEntry` (shared) |

---

# Rules Endpoints

## 1. Find Rule

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/rules/{requestingSystemName}/find` |
| **Request Body** | `RulesFindDTO` |
| **Response** | `RulesDTO` |

#### Request Body — `RulesFindDTO`

```json
{
  "rulesId": "550e8400-e29b-41d4-a716-446655440000",
  "includes": ["Classifications", "Resources", "Products", "RuleTypes", "Children"]
}
```

#### Available Includes

| Value | Returns |
|---|---|
| `Classifications` | Classification names → stored values (purpose, argument, lifecycle, etc., via `RulesXClassification`) |
| `Resources` | Classification names → resource item UUIDs (via `RulesXResourceItem`) |
| `Products` | Classification names → product UUIDs the rule applies to (via `RulesXProduct`) |
| `RuleTypes` | Rule type names → stored values (via `RulesXRulesType`) |
| `Children` | Child rule names → hierarchy values (via `RulesXRules`) |

#### Example Response — `RulesDTO`

```json
{
  "rulesId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Senior Citizen Eligibility",
  "description": "Eligibility age for senior citizen free checking is age >= 55",
  "classifications": { "RulePurposes": "Eligibility Determination", "RuleArguments": "Length of Service" },
  "ruleTypes": { "Single Rule": "" },
  "products": { "Applies To": "p1r2o3d4-..." },
  "resources": { "Specification": "r1s2t3u4-..." },
  "children": { "Minimum Balance Rule": "Has Precondition" }
}
```

---

## 2. Create Rule

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/rules/{requestingSystemName}/create` |
| **Request Body** | `RulesCreateDTO` |
| **Response** | `RulesDTO` |

#### Request Body — `RulesCreateDTO`

```json
{
  "name": "Senior Citizen Eligibility",
  "description": "Eligibility age for senior citizen free checking is age >= 55",
  "classifications": { "RulePurposes": "Eligibility Determination" },
  "ruleTypes": { "Single Rule": "" },
  "products": { "Applies To": "p1r2o3d4-e5f6-7890-abcd-ef1234567890" },
  "resources": { "Specification": "r1s2t3u4-e5f6-7890-abcd-ef1234567890" },
  "children": { "Minimum Balance Rule": "Has Precondition" }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `name` | `String` | **Yes** | The rule set name. |
| `description` | `String` | No | The human-readable rule statement. |
| `classifications` | `Map<String,String>` | No | Classification name → value pairs. |
| `resources` | `Map<String,String>` | No | Classification name → **resource item UUID**. |
| `products` | `Map<String,String>` | No | Classification name → **product UUID**. |
| `ruleTypes` | `Map<String,String>` | No | Rule type name → stored value. |
| `children` | `Map<String,String>` | No | Existing child rule name → hierarchy value. |

- The first `ruleTypes` key (if any) is passed as the primary structural type to `createRules()`.
- **Relationship persistence is fire-and-forget**: each category persists on its own Vert.x context/session via `SessionUtils.fireAndForget()`. The response is echoed immediately from the DTO.
- **Entity resolution**: resource values resolve via `resourceItemService.findByUUID()`; product values resolve via `productService.find()`.

---

## 3. Update Rule

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/rules/{requestingSystemName}/update` |
| **Request Body** | `RulesUpdateDTO` |
| **Response** | `RulesDTO` |

#### Request Body — `RulesUpdateDTO`

```json
{
  "rulesId": "550e8400-e29b-41d4-a716-446655440000",
  "description": "Updated rule statement",
  "classifications": { "addOrUpdate": { "RuleLifeCycleStatuses": "Active Rule" }, "delete": ["Proposed Rule"] },
  "ruleTypes": { "addOrUpdate": { "Range Rule": "" }, "delete": ["Single Rule"] },
  "products": { "addOrUpdate": { "Applies To": "product-uuid" }, "delete": ["Old Product"] },
  "resources": { "addOrUpdate": { "Specification": "resource-uuid" }, "delete": ["Old Spec"] },
  "children": { "addOrUpdate": { "Increment Rule": "Has Increment Of" }, "delete": ["Old Child"] }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `rulesId` | `UUID` | **Yes** | The rule to update. |
| `description` | `String` | No | New description (applied in place to the managed entity). |
| `classifications` / `ruleTypes` / `products` / `resources` / `children` | `RelationshipUpdateEntry` | No | Per-category `addOrUpdate` map + `delete` list. |

#### Delete semantics

| Category | Add/Update | Delete |
|---|---|---|
| Classifications | `addOrUpdateClassification()` | `removeClassification()` |
| RuleTypes | `addOrUpdateRuleTypes()` | `removeRuleTypes()` |
| Products | `addOrUpdateProduct()` | expire matching `RulesXProduct` rows by classification name |
| Resources | `addOrUpdateResourceItem()` | expire matching `RulesXResourceItem` rows by classification name |
| Children | `addChild()` | `archiveChild()` |

---

# Rule Type Endpoints

## 1. Find Rule Type

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/rules-type/{requestingSystemName}/find` |
| **Request Body** | `RulesTypeFindDTO` |
| **Response** | `RulesTypeDTO` |

#### Available Includes

| Value | Returns |
|---|---|
| `Classifications` | Classification names → values (via `RulesTypeXClassification`) |
| `Resources` | Classification names → resource item UUIDs (via `RulesTypeXResourceItem`) |

---

## 2. Create Rule Type

| Property | Value |
|---|---|
| **Method** | `POST` |
| **Path** | `/{enterprise}/rules-type/{requestingSystemName}/create` |
| **Request Body** | `RulesTypeCreateDTO` |
| **Response** | `RulesTypeDTO` |

```json
{
  "name": "Range Rule",
  "description": "A rule defining a boundary using min, max, increment and preferred value",
  "classifications": { "RuleStructureTypes": "Range Rule" },
  "resources": { "Specification": "resource-item-uuid" }
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `name` | `String` | **Yes** | Rule type name. An existing rule type with the same name is reused. |
| `description` | `String` | No | Defaults to the name when null. |
| `classifications` | `Map<String,String>` | No | Classification name → value. |
| `resources` | `Map<String,String>` | No | Classification name → resource item UUID. |

---

## 3. Update Rule Type

| Property | Value |
|---|---|
| **Method** | `PUT` |
| **Path** | `/{enterprise}/rules-type/{requestingSystemName}/update` |
| **Request Body** | `RulesTypeUpdateDTO` |
| **Response** | `RulesTypeDTO` |

```json
{
  "rulesTypeId": "550e8400-e29b-41d4-a716-446655440000",
  "description": "Updated rule type description",
  "classifications": { "addOrUpdate": { "RuleStructureTypes": "Matrix Rule" }, "delete": ["Range Rule"] },
  "resources": { "addOrUpdate": { "Specification": "resource-uuid" }, "delete": ["Old Spec"] }
}
```

---

## Data Model

| Entity | Cross-ref Table(s) | Notes |
|---|---|---|
| `Rules` | `RulesXClassification`, `RulesXResourceItem`, `RulesXProduct`, `RulesXRulesType`, `RulesXRules` | The rule itself; applies to products, links rule types, composes from child rules. |
| `RulesType` | `RulesTypeXClassification`, `RulesTypeXResourceItem` | The structural classification of a rule. |

> `Rules` also carries `RulesXArrangement` and `RulesXInvolvedParty` relationships. These are **not** exposed through this API yet — arrangement/party rule assignment is managed from the arrangement/party side or deferred to a future iteration.

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
| `RulesFindDTO` | `POST rules/.../find` | Find a rule by ID with selectable includes |
| `RulesCreateDTO` | `POST rules/.../create` | Create a rule + optional relationships |
| `RulesUpdateDTO` | `PUT rules/.../update` | Update description + relationships |
| `RulesDTO` | Response | Standard rule response |
| `RulesTypeFindDTO` | `POST rules-type/.../find` | Find a rule type by ID with selectable includes |
| `RulesTypeCreateDTO` | `POST rules-type/.../create` | Create/reuse a rule type + optional relationships |
| `RulesTypeUpdateDTO` | `PUT rules-type/.../update` | Update description + classifications/resources |
| `RulesTypeDTO` | Response | Standard rule type response |
| `RelationshipUpdateEntry` | Within update DTOs | Add/update map + delete list for one relationship category |

---

## Testing

Two JUnit 5 test classes (in `core/src/test/java/com/guicedee/activitymaster/tests`) cover the REST surface and the OpenAPI document:

| Test | Coverage |
|---|---|
| `TestActivityMasterRestServices` | Exercises `RulesRestService`, `RulesTypeRestService`, `ClassificationRestService` and `ClassificationDataConceptRestService` the same way the Vert.x router does — the Guice-managed resource is obtained from `IGuiceContext` and its `find`/`create`/`update` methods are invoked directly with the enterprise + requesting-system path parameters and a request DTO. Asserts the synchronous response contract: the generated id and the immediately echoed DTO state. Also asserts the not-found and unknown-concept (400) failure paths. Relationship persistence is intentionally fire-and-forget, so assertions focus on the echo rather than racing the async writes. |
| `TestActivityMasterOpenApi` | Validates the merged OpenAPI 3.1 model resolved via `IGuiceContext.get(OpenAPI.class)`: the global `@OpenAPIDefinition` info/version survives, the full canonical tag catalogue is merged, every domain (including the new `rules`, `rules-type`, `classification` and `classification-data-concept` resources) contributes scanned `find`/`create`/`update` paths, the `{enterprise}`/`{requestingSystemName}` scoping templates are preserved, and each operation carries its expected resource tag. |

Both classes bootstrap the GuicedEE context via `IGuiceContext.instance()` and resolve the `ActivityMaster-Test` `Mutiny.SessionFactory`; `TestActivityMasterRestServices` additionally ensures the test enterprise and Activity Master system exist before invoking the resources.
