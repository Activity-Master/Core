# Sequence — Address Creation Flow

Captures the steps when a new address is provisioned through the service layer.

```mermaid
sequenceDiagram
  participant Client as ActivityMaster Client
  participant Systems as ActivityMasterSystemsManager
  participant AddressService as AddressService
  participant Security as SecurityTokenService
  participant DB as ActivityMasterDBModule
  participant Postgres as PostgreSQL

  Client->>Systems: createAddress(payload)
  Systems->>Security: resolveSecurityToken(payload.tokenId)
  Systems->>AddressService: upsertAddress(payload, securityToken)
  AddressService->>DB: upsert via AddressQueryBuilder
  DB->>Postgres: INSERT / UPDATE (Hibernate Reactive)
  Postgres-->>DB: persisted entity
  DB-->>AddressService: saved entity with classifications
  AddressService-->>Systems: addressResult
  Systems-->>Client: confirmation (includes UI-friendly payload)
```

The upstream UI uses predictable payload naming per the host domain model and expects Log4j2 instrumentation for observability. All insert/update steps are sequential on the Mutiny `Session` because Hibernate Reactive 7 requires each database mutation to complete before the next begins; the service layer enforces that by emitting the next reactive stage only after the previous one completes, preserving the session’s sequential pipeline.
