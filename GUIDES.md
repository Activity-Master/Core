# ActivityMaster Core Guides

## 1. Service Wiring Guide
- Always bind FSDM services through the binder classes (e.g., `EventsBinder`, `PasswordsServiceBinder`) so clients can inject `IActivityMasterSystem` instances; refer to `rules/generative/backend/guicedee/client/README.md` for the GuicedEE client contract.
- Use `ActivityMasterSystemsManager` as the entry point (see `docs/architecture/sequence-enterprise-activity.md`) and keep Vert.x/Mutiny contexts in the service boundary.
- Register new services inside `ActivityMasterSystemsManager` following the pattern in `com.guicedee.activitymaster.fsdm.services` and annotate with `@Log4j2` for consistent logging.

-## 2. Persistence Guide
- Surface data access through `ActivityMasterDBModule` / `ActivityMasterDestinationDBModule` builders so Hibernate Reactive 7 connection pools are reused (`rules/generative/backend/hibernate/README.md`).
- Builders must follow CRTP fluent APIs: return `(J)this` and use `@SuppressWarnings("unchecked")` as in the existing `QueryBuilder*` implementations (reinforced by `rules/generative/data/entityassist/README.md`).
- Reuse `EntityAssist` helpers for mapping to DTOs/records and ensure classification joins are resolved via the provided `X` tables (see `src/main/java/com/guicedee/activitymaster/fsdm/db/entities/*`).
- Because Mutiny's `Session` forbids parallel insert/update/delete operations, compose persistence calls sequentially by wiring each mutation into the reactive continuation (e.g., `onItem().call(...)`); emit the next builder invocation only after the prior reactive stage completes so the session remains sequential without ever calling `.await()`.

## 3. Security & Classification Guide
- All CRUD interactions must accept or derive a `SecurityToken` via `SecurityTokenService` before touching classification tables (`rules/generative/platform/security-auth/README.md`).
- Join tables such as `AddressXClassification` and `ArrangementXRules` must never be mutated without coordination through the classification helpers mentioned in the `rules/generative/backend/guicedee/README.md`.
- **Classification/type creation policy:** Classification, type, and taxonomy records (e.g., `Classification`, `ClassificationDataConcept`, `RulesType`, `ResourceItemType`) are only loaded through system update pipelines that implement `ISystemUpdate` and `@SortedUpdate` (see `src/main/java/com/guicedee/activitymaster/fsdm/injections/updates`). Systems plug into the ActivityMaster bootstrapping by registering their updates inside `ActivityMasterSystemsManager` / `ActivityMasterPostStartup`; they must never create these rows ad hoc. The only exceptions are `ProductType` and `EventType`, which have dedicated APIs allowing runtime provisioning with the same classification guardrails.

## 4. Logging & Observability Guide
- Use Log4j2 (`logging.properties`) plus Lombok `@Log4j2`; avoid other Lombok logging annotations (`rules/generative/platform/observability/README.md`).
- Instrument sequences with traceable events so the diagrams in `docs/architecture/sequence-*.md` can be validated during runtime debugging.

## 5. Testing Guide
- Follow the repository’s test harness (see `src/test/java/com/guicedee/activitymaster/tests`). Tests rely on Testcontainers + `PostgreSQLTestDBModule`; closing loops to the Stage 1 diagrams ensures coverage for both sequence flows.
- Jacoco coverage and the Java Micro Harness run in Maven; annotate new tests to reuse the core `TestActivityMaster*` scaffolding.

## 6. Glossary & Implementation References
- Terms such as CRTP, `ActivityMasterSystemsManager`, and `SecurityToken` are defined in `GLOSSARY.md`. Refer to it when naming new concepts.
- Implementation details must cross-link back to this doc, the rules listed in `RULES.md`, and the diagram set in `docs/architecture/README.md`.

## 7. Lifecycle Test Guidance
- The lifecycle test (`src/test/java/com/guicedee/activitymaster/tests/TestActivityMasterLifecycle.java`) illustrates enterprise creation, admin token registration, and system startup in a single flow (`docs/architecture/sequence-enterprise-lifecycle.md`). Use it as the reference implementation when adding new entrypoints or bootstrapping sequences; align new systems with `ISystemUpdate`/`@SortedUpdate` pipelines and keep the Mutiny `Session` sequential by chaining the reactive results already shown in the sequence docs.
- The observed test harness runs `createNewEnterprise(session, enterprise)` first to establish the base systems, then executes `loadUpdates(session, enterprise)` (via `ISystemUpdate/@SortedUpdate` loaders) before calling `startNewEnterprise(session, name, admin, password)` to register the systems, create an admin user, and invoke `performPostStartup`. This order ensures all classifications and types are loaded, updates commit, and security/ActiveFlag metadata is stable before the admin user or runtime services become active.
