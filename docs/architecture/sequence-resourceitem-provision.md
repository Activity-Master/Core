# Sequence — ResourceItem Provisioning Flow

Documents how resource items (e.g., personnel or equipment) are created or updated.

```mermaid
sequenceDiagram
  participant Client as Client Interfaces
  participant Systems as ActivityMasterSystemsManager
  participant Resource as ResourceItemService
  participant Security as SecurityTokenService
  participant DB as ActivityMasterDBModule
  participant Postgres as PostgreSQL

  Client->>Systems: provisionResourceItem(payload)
  Systems->>Security: resolveSecurityToken(payload.tokenId)
  Systems->>Resource: upsertResourceItem(payload, securityToken)
  Resource->>DB: upsert via ResourceItemQueryBuilder
  DB->>Postgres: async INSERT / UPDATE
  Postgres-->>DB: persisted entity
  DB-->>Resource: resourceRecord
  Resource-->>Systems: resourceAck
  Systems-->>Client: confirmation (includes classification tokens, ActiveFlag)
```

Every ResourceItem change respects value-level security tokens, and the ActiveFlag metadata tracks whether the row is active, archived, or otherwise filtered during downstream queries.
