# Prompt Reference

## Selected Stacks & Rules
- **Language:** Java 25 LTS (rules/generative/language/java/java-25.rules.md + rules/generative/language/java/build-tooling.md).
- **Build:** Maven (rules/generative/language/java/build-tooling.md describes plugin wiring).
- **Backend Frameworks:** Vert.x 5 & GuicedEE (rules/generative/backend/guicedee/README.md + rules/generative/backend/vertx/README.md). Persistence relies on Hibernate Reactive 7 (rules/generative/backend/hibernate/README.md) and EntityAssist helpers (rules/generative/data/entityassist/README.md).
- **Datastore:** PostgreSQL (rules/generative/backend/hibernate/README.md links to Postgres guidance).
- **Security & Logging:** Log4j2 default with Lombok `@Log4j2`, classification via GuicedEE security helpers (rules/generative/architecture/tdd/README.md touches observability).
- **Testing:** Java Micro Harness and Jacoco (rules/generative/testing/README? need confirm? maybe not but mention as chosen). For test harness, refer to `TestActivityMaster*` classes that rely on Testcontainers.

## Diagrams
- Architecture index: `docs/architecture/README.md` links Stage 1 diagrams.
- C4 Context: `docs/architecture/c4-context.md`
- C4 Container: `docs/architecture/c4-container.md`
- Component view: `docs/architecture/c4-component-fsdm.md`
- Sequences: `docs/architecture/sequence-enterprise-activity.md`, `docs/architecture/sequence-address-creation.md`
- ERD: `docs/architecture/erd-fsdm-domain.md`

## Glossary Strategy
Host glossary (to be created) must respect topic-first precedence and mention CRTP strategy, GuicedEE, and WaInput/Za?? ex ??? (document once GLOSSARY exists). This reference file will remain the anchor for future prompts.
