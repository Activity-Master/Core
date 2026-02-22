# Sequence — ResourceItem Provisioning Flow

Documents how resource items (e.g., personnel or equipment) are created, updated, or retrieved via the REST API.

```mermaid
sequenceDiagram
  participant Client as Client / REST Consumer
  participant REST as ResourceItemRestService
  participant Systems as SessionUtils / ActivityMasterSystemsManager
  participant Resource as ResourceItemService
  participant Security as SecurityTokenService
  participant DB as ActivityMasterDBModule
  participant Postgres as PostgreSQL

  Note over Client,Postgres: Create / Update Flow
  Client->>REST: POST .../create or PUT .../update (JSON body)
  REST->>Systems: withActivityMaster(enterprise, system)
  Systems->>Security: resolveSecurityToken(identityToken)
  Systems->>Resource: create() / findByUUID() + addOrUpdate…()
  Resource->>DB: upsert via ResourceItemQueryBuilder
  DB->>Postgres: async INSERT / UPDATE
  Postgres-->>DB: persisted entity
  DB-->>Resource: resourceRecord
  Resource-->>REST: ResourceItemDTO
  REST-->>Client: 200 OK (JSON)

  Note over Client,Postgres: Get Data Flow
  Client->>REST: GET .../data/{resourceItemId}
  REST->>Systems: withActivityMaster(enterprise, system)
  Systems->>Security: resolveSecurityToken(identityToken)
  Systems->>Resource: findByUUID(resourceItemId)
  Resource->>DB: find ResourceItemDataValue
  DB->>Postgres: async SELECT
  Postgres-->>DB: compressed data row
  DB-->>Resource: byte[] (decompressed)
  Resource-->>REST: byte[]
  REST-->>Client: 200 OK (application/octet-stream)
```

Every ResourceItem change respects value-level security tokens, and the ActiveFlag metadata tracks whether the row is active, archived, or otherwise filtered during downstream queries.

See `RESOURCE_ITEM_REST_API.md` in `src/main/java/com/guicedee/activitymaster/fsdm/rest/resourceitem/` for the full endpoint reference.

