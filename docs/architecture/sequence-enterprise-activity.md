# Sequence — Enterprise Activity Retrieval

This sequence covers a typical query issued by a Guice-integrated client (e.g., ActivityMaster Desktop) to list enterprise-related arrangements and their events.

```mermaid
sequenceDiagram
  participant Client as ActivityMaster Client
  participant Systems as ActivityMasterSystemsManager
  participant Enterprise as EnterpriseService
  participant DB as ActivityMasterDBModule
  participant Postgres as PostgreSQL

  Client->>Systems: requestEnterpriseActivities(enterpriseId)
  Systems->>Enterprise: enterpriseActivitySummary(enterpriseId)
  Enterprise->>DB: reactiveQuery(builders for Arrangements + Event join)
  DB->>Postgres: async SQL execution (Hibernate Reactive 7)
  Postgres-->>DB: row stream
  DB-->>Enterprise: result set mapped via EntityAssist helpers
  Enterprise-->>Systems: aggregated DTO with classification tokens
  Systems-->>Client: response stream (Vert.x/Mutiny)
```

The flow relies on `EntityAssist` utilities (see `rules/generative/data/entityassist/README.md`) to hydrate DTOs and ensures classification/security tokens travel alongside each row.
