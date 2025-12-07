# ActivityMaster Core Rules

## Scope
ActivityMaster Core is the Java 25 Maven service library that delivers the FSDM (Functional Service Data Model) domain over GuicedEE + Vert.x 5 on top of Hibernate Reactive 7 and PostgreSQL. All changes must respect the existing domain models (`src/main/java/com/guicedee/activitymaster/fsdm`) and the canonical schema scripts under `src/main/resources/META-INF`.

## Chosen Stacks & References
- **Language & Build:** Java 25 LTS / Maven (see `rules/generative/language/java/java-25.rules.md` and `rules/generative/language/java/build-tooling.md`).
- **Backend Reactive:** Vert.x 5 + GuicedEE Core/Client plus Hibernate Reactive 7 persistence (see `rules/generative/backend/vertx/README.md`, `rules/generative/backend/guicedee/README.md`, `rules/generative/backend/guicedee/vertx/README.md`, `rules/generative/backend/guicedee/client/README.md`, `rules/generative/backend/guicedee/persistence/README.md`, and `rules/generative/backend/hibernate/README.md`).
- **Databases & Data Access:** PostgreSQL schema + EntityAssist helpers (`rules/generative/data/activity-master/README.md` and `rules/generative/data/entityassist/README.md`).
- **Architecture & Process:** Specification-Driven Design, Documentation-as-Code, DDD, and TDD (see `rules/generative/architecture/README.md`, `rules/generative/architecture/tdd/README.md`, `rules/generative/platform/observability/README.md`, `rules/generative/platform/security-auth/README.md`).
- **CI/CD:** GitHub Actions template references in `rules/generative/platform/ci-cd/README.md` and `rules/generative/platform/ci-cd/providers/github-actions.md` (see Stage 3 plan for implementation).

## Structural & Code Conventions
- **CRTP Fluent API:** GuicedEE + Lombok stack enforces CRTP style for all query builders and fluent setters (return `(J)this` with `@SuppressWarnings("unchecked")` per `rules/generative/data/entityassist/README.md`). Do not introduce Lombok `@Builder` on these components.
- **Logging:** Use Log4j2 with Lombok `@Log4j2` (avoid other Lombok logging annotations) and configure through `logging.properties` plus `rules/generative/platform/observability/README.md` guidance.
- **Security & Classification:** All service endpoints must thread `SecurityTokenService` results and classification metadata (per `rules/generative/backend/guicedee/README.md` and `rules/generative/platform/security-auth/README.md`).
- **JSpecify & Typing:** Follow the `rules/generative/language/java/java-25.rules.md` expectations for nullness annotations and prefer `Optional` fields only when required.
- **Testing Strategy:** Align with the existing Jacoco + Java Micro Harness coverage found in this repo’s Maven configuration and the canonical `src/test/java` suite; CI guidance can be traced from `rules/generative/platform/ci-cd/README.md`.

## Glossary & Guides
- Cross-link to `GLOSSARY.md` for naming (CRTP, WaInput, classification, etc.) and obey the Glossary Precedence Policy documented there.
- Guides in `GUIDES.md` must illustrate how to apply these rules when wiring new FSDM services, persistence flows, or binder registrations.

## Documentation Flow
Close loops between `PACT.md`, `RULES.md`, `GUIDES.md`, `IMPLEMENTATION.md`, and `GLOSSARY.md` as soon as each artifact is drafted. References to `docs/architecture/*.md` and `docs/PROMPT_REFERENCE.md` ground the C4/sequence/ERD diagrams.
