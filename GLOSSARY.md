# ActivityMaster Core Glossary

## Glossary Precedence Policy
Host terms follow a strict topic-first hierarchy. When a topic has its own glossary (e.g., `rules/generative/backend/guicedee/GLOSSARY.md`), that document owns terms within its domain; the host glossary acts as an index for cross-topic navigation and includes only the fewest duplicate definitions needed for traceability. Always cite the topic glossary first, and refer back to this file for glue concepts such as CRTP enforcement or ActivityMaster-specific services.

## Topic Glossary Index
- **Java 25:** `rules/generative/language/java/GLOSSARY.md`
- **GuicedEE & Vert.x:** `rules/generative/backend/guicedee/GLOSSARY.md`, `rules/generative/backend/guicedee/vertx/GLOSSARY.md`
- **GuicedEE Persistence:** `rules/generative/backend/guicedee/persistence/GLOSSARY.md`
- **EntityAssist & Activity Master:** `rules/generative/data/entityassist/GLOSSARY.md`, `rules/generative/data/activity-master/GLOSSARY.md`
- **Hibernate Reactive:** `rules/generative/backend/hibernate/GLOSSARY.md`
- **Lombok:** `rules/generative/backend/lombok/GLOSSARY.md`
- **Logging & Observability:** `rules/generative/platform/observability/README.md` (no dedicated glossary) plus `rules/generative/backend/guicedee/GLOSSARY.md` for instrumentation terms.
- **JSPECIFY:** `rules/generative/backend/jspecify/GLOSSARY.md`
- **CI/Testing:** `rules/generative/platform/testing/GLOSSARY.md`

## Host Terms (Minimal Duplication)
- **ActivityMasterSystemsManager:** The orchestrator that wires Guice binders, services, and persistence modules for the FSDM domain (`docs/architecture/sequence-enterprise-activity.md`). See `rules/generative/backend/guicedee/GLOSSARY.md` for GuicedEE bootstrap vocabulary.
- **CRTP:** The fluent API strategy required by the GuicedEE + query builder stack; builders return `(J)this` and use `@SuppressWarnings("unchecked")` so the chain can be safely reused (`rules/generative/backend/fluent-api/GLOSSARY.md`).
- **EntityAssist Helpers:** The set of utilities that hydrate DTOs from reactive row streams; available across the `activity-master` data rules (`rules/generative/data/entityassist/GLOSSARY.md`).
- **Security Token & Classification:** Domain tokens that gate access to join tables like `AddressXClassification`; definitions live in `rules/generative/backend/guicedee/GLOSSARY.md` and `rules/generative/platform/security-auth/README.md`, but this glossary anchors their usage in ActivityMaster-specific flows.

## Forward References
- This glossary links to `RULES.md`, `GUIDES.md`, `IMPLEMENTATION.md`, and `docs/architecture/README.md`, ensuring every new term is rooted in an observable artifact.
