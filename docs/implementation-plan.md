# Implementation Plan

## Goals
1. Wire stage 1/2 docs to actual code and ensure compliance with the Rules Repository (GuicedEE + Vert.x + Hibernate Reactive + PostgreSQL).
2. Standardize environment + CI (including `.env.example` and GitHub Actions) before adding new source-level changes.
3. Keep changes forward-only and clearly traceable through PACT ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION ↔ GLOSSARY.

## Module & File Tree Snapshot
- `src/main/java/com/guicedee/activitymaster/fsdm`: service layer (see `IMPLEMENTATION.md`).
- `src/main/java/com/guicedee/activitymaster/fsdm/db`: persistence + query builders (CRTP strategy). 
- `src/main/java/com/guicedee/activitymaster/fsdm/implementations`: Guice binders per service.
- `src/main/resources/META-INF`: PostgreSQL/Hibernate scripts.
- `src/test/java/com/guicedee/activitymaster/tests`: Java Micro Harness/Testcontainers suite.

## Build & Annotation Processor Wiring
- Maven handles Lombok and Hibernate annotation processing via existing `pom.xml`; no changes required unless a new module adds processors.
- Ensure any new module references only Java 25 APIs and uses the Maven compiler plugin settings already present (see `rules/generative/language/java/build-tooling.md`).

## CI & Environment Plan
- Add a GitHub Actions workflow that runs `mvn verify`, leverages Jacoco, and respects secrets via `rules/generative/platform/ci-cd/providers/github-actions.md` (see Stage 4 checklist for the template). 
- Use `.env.example` as the canonical local dev config; align real secrets with the env doc `rules/generative/platform/secrets-config/env-variables.md`.

## Rollout & Validation
- Validate diagrams (docs/architecture) when new services or DB tables are added by updating the Mermaid files.
- Tests must always exercise `TestActivityMaster*` and rely on Testcontainers to provision PostgreSQL (see `IMPLEMENTATION.md`).
- All new code should run through the Java Micro Harness plus Jacoco coverage; add targeted assertions when classification/security flows change.

## Risks & Mitigations
- **Risk:** Changing persistence layers may break the canonical schema; mitigate by cross-referencing `src/main/resources/META-INF` SQL scripts and the ERD (`docs/architecture/erd-fsdm-domain.md`).
- **Risk:** Missing env variables in GitHub Actions; mitigate by referencing `.env.example` and updating workflow secrets documentation.
- **Risk:** Inconsistent logging; mitigate by enforcing `@Log4j2` usage (RULES + GUIDES cover this) and verifying `logging.properties` coverage.

## Next Steps
1. Hook up GitHub Actions workflow (stage 4 code change) referencing shared GuicedEE workflow example.
2. Document required secrets in the workflow and README (`README.md` update). 
3. Add AI workspace policy files for `.aiassistant/rules/`, `.github/copilot-instructions.md`, `.cursor/rules.md`, and `ROO_WORKSPACE_POLICY.md`.
4. Ensure README links to PACT/RULES/GUIDES/IMPLEMENTATION/GLOSSARY and mentions the `rules/` submodule.
