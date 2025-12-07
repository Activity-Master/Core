# Sequence — Classification Update Flow

Shows how classification concepts are refreshed within the domain.

```mermaid
sequenceDiagram
  participant Client as Client Interfaces
  participant Systems as ActivityMasterSystemsManager
  participant Classification as ClassificationService
  participant Concept as ClassificationDataConceptService
  participant Security as SecurityTokenService
  participant DB as ActivityMasterDBModule
  participant Postgres as PostgreSQL

  Client->>Systems: refreshClassification(definition)
  Systems->>Security: resolveSecurityToken(definition.tokenId)
  Systems->>Classification: updateClassification(definition, securityToken)
  Classification->>Concept: enrichWithDataConcepts(definition)
  Concept->>DB: persist classification + concept rows via QueryBuilder
  DB->>Postgres: async UPSERT
  Postgres-->>DB: persisted tuples
  DB-->>Classification: classificationRecord
  Classification-->>Systems: classificationSummary
  Systems-->>Client: success (classification visibility updated)
```

The flow ensures classification data concepts and related `X` join tables are updated atomically and that clients receive the adjusted security/access state via the ActiveFlag status. Classification and type records (including `Classification`, `ClassificationDataConcept`, `RulesType`, `ResourceItemType`, etc.) are only ever created through the `ISystemUpdate`/`@SortedUpdate` mechanism driven by each system’s update pipeline; free-form creation in client code or external services is forbidden except for `ProductType` and `EventType`, which remain safe to upsert at runtime when business logic requires new options.
