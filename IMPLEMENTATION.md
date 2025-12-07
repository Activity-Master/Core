# ActivityMaster Core Implementation Map

## Module Layout
- `src/main/java/com/guicedee/activitymaster/fsdm`: Contains the service classes (e.g., `EnterpriseService`, `EventsService`, `PasswordsService`) behind the FSDM domain.
- `src/main/java/com/guicedee/activitymaster/fsdm/db`: Houses the entity definitions, query builders, and the `ActivityMasterDBModule`/`ActivityMasterDestinationDBModule` persistence wiring.
- `src/main/java/com/guicedee/activitymaster/fsdm/implementations`: Guice binder implementations for each service.
- `src/main/java/com/guicedee/activitymaster/fsdm/services` and `systems`: Runtime helpers, including `ActivityMasterSystemsManager`, the legacy migration utilities, and the system-specific orchestrators under `systems/` (e.g., `AddressSystem`, `EnterpriseSystem`).
- `src/main/resources/META-INF`: Database schema scripts, SQL helpers, and `persistence.xml` definitions required by Hibernate Reactive + PostgreSQL.

## Bootstrapping & Runtime Flow
- The `ActivityMasterSystemsManager` boots GuicedEE (per `GUIDES.md`) and wires each binder, making the service APIs available to the client via `IActivityMasterSystem`.
- Services rely on CRTP query builders to keep the fluent API consistent with the `rules/generative/backend/fluent-api/GLOSSARY.md` expectations.
- Every persistence call flows through `ActivityMasterDBModule`, `ActivityMasterDestinationDBModule`, and the reactive PostgreSQL connector; refer to `docs/architecture/sequence-enterprise-activity.md` and `docs/architecture/sequence-address-creation.md` for concrete call stacks.

## Validation & CI Expectations
- Tests (`src/test/java/com/guicedee/activitymaster/tests`) are built with Testcontainers + `PostgreSQLTestDBModule`; running `mvn test` replicates the flows from the stage 1 diagrams.
- Jacoco coverage reports and Java Micro Harness executions happen within the Maven lifecycle; update `IMPLEMENTATION.md` when new test patterns are introduced.

## Traceability
- Rules (`RULES.md`) and Guides (`GUIDES.md`) point to this implementation map; cross-link to `docs/architecture/README.md` for diagrams and `docs/PROMPT_REFERENCE.md` for stack selections.
- The next step is to align environment variables (`.env.example`), CI workflows, and workspace policies (Copilot/Cursor/AI Assistant) with these modules before touching stage 4 code.
