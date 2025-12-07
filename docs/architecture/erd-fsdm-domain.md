# ERD — Core FSDM Domain

The core relational domain reuses the following tables, reflecting the entities under `src/main/java/com/guicedee/activitymaster/fsdm/db/entities`:

```mermaid
erDiagram
  ENTERPRISE {
    UUID enterpriseid PK
    string name
  }
  ADDRESS {
    UUID addressid PK
    UUID enterpriseid FK
    string line1
  }
  EVENT {
    UUID eventid PK
    UUID enterpriseid FK
    UUID addressid FK
    string eventtype
  }
  ARRANGEMENT {
    UUID arrangementid PK
    UUID enterpriseid FK
    string arrangementtype
  }
  ADDRESSXCLASSIFICATION {
    UUID addressid FK
    UUID classificationid FK
  }
  SECURITYTOKEN {
    UUID securitytokenid PK
    string tokenname
  }

  ENTERPRISE ||--o{ ADDRESS : owns
  ENTERPRISE ||--o{ EVENT : records
  ENTERPRISE ||--o{ ARRANGEMENT : governs
  ADDRESS ||--o{ EVENT : hosts
  ARRANGEMENT ||--o{ EVENT : references
  ADDRESS ||--o{ ADDRESSXCLASSIFICATION : classified
  SECURITYTOKEN ||--o{ EVENT : secures
```

The ERD intentionally keeps the join tables (e.g., `AddressXClassification`, `ArrangementXRules`) abstracted as classification relationships managed via the `ClassificationService` and `QueryBuilder` helpers. PostgreSQL schema scripts under `src/main/resources/META-INF` enforce these foreign keys and indexes.
