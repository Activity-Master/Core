# Architecture Inventory

This directory captures the Stage 1 architecture deliverables for ActivityMaster Core as required by the adoption prompt:

- `c4-context.md`: System context diagram plus textual notes on external actors and dependencies (Java 25, Maven, GuicedEE + Vert.x, PostgreSQL).
- `c4-container.md`: Container-level responsibilities inside ActivityMaster Core, including the FSDM services, Guiced injection layers, and the persistence modules.
- `c4-component-fsdm.md`: Component breakdown for the FSDM bounded context (primary services, binders, and DB modules).
- `sequence-enterprise-activity.md`, `sequence-address-creation.md`, and the newly added `sequence-enterprise-lifecycle.md`: runtime interactions through `ActivityMasterSystemsManager` down to the database modules plus the test harness lifecycle that creates/registers enterprises.
- `erd-fsdm-domain.md`: Initial ERD for the core ActivityMaster domain (Enterprise, Address, Event, Arrangement and their join tables).

Each diagram is authored in Mermaid and references only checked-in code or schema artifacts. See the individual files for cross-links to the Rules Repository reminders (PACT, RULES and GLOSSARY) once those are created.
