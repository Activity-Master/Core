# 🧩 ActivityMaster Core

[![CI](https://github.com/Activity-Master/Core/actions/workflows/maven-verify.yml/badge.svg)](https://github.com/Activity-Master/Core/actions/workflows/maven-verify.yml)
[![Issues](https://img.shields.io/github/issues/Activity-Master/Core)](https://github.com/Activity-Master/Core/issues)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/Activity-Master/Core/pulls)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![JDK](https://img.shields.io/badge/JDK-25%2B-0A7?logo=java)](https://openjdk.org/projects/jdk/25/)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)

<!-- Tech icons row -->
![Vert.x](https://img.shields.io/badge/Vert.x-5-4B9?logo=eclipsevertdotx&logoColor=white)
![Hibernate Reactive](https://img.shields.io/badge/Hibernate-Reactive-59666C?logo=hibernate)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?logo=postgresql&logoColor=white)
![Guice](https://img.shields.io/badge/Guice-Enabled-2F4F4F)
![GuicedEE](https://img.shields.io/badge/GuicedEE-Core-0A7)

ActivityMaster Core is the open-source implementation of the Functional Service Data Model (FSDM) services—Enterprise, Address, Events, Arrangements, ResourceItem, Classification, and the supporting security/ActiveFlag infrastructure. Built for Java 25 with Maven, Vert.x 5, GuicedEE, Hibernate Reactive 7, and PostgreSQL, the library exposes Guiced client interfaces so downstream applications can manage the canonical warehouse schema without a dedicated desktop client.

## ✨ Features
- Canonical FSDM domain services (Enterprise, Address, Events, Arrangements, ResourceItem, Classification)
- Reactive persistence with Hibernate Reactive 7 + PostgreSQL
- Vert.x 5 integrations for async workflows and messaging patterns
- GuicedEE DI bootstrap with lifecycle and post-startup hooks
- Security token propagation and ActiveFlag row-state enforcement
- Test harness and fixtures aligned to the rules repository

## 📦 Install (Maven)
Add the dependency to your Maven project. Versions are managed by the parent/BOM.

```xml
<dependency>
  <groupId>com.guicedee.activitymaster</groupId>
  <artifactId>activity-master</artifactId>
</dependency>
```

## 🚀 Quick Start
1. Copy `.env.example` to `.env` and adjust values for your environment.
2. Build and run tests locally:

```bash
mvn -B clean verify
```

3. Consume services via the Activity Master Client module in your host app; follow the docs and diagrams listed below.

## ⚙️ Configuration
Environment variables mirror the Rules Repository guidance (`rules/generative/platform/secrets-config/env-variables.md`). Typical keys used by CI and tests:
- DB_URL, DB_USER, DB_PASS
- JWT_TEST_TOKEN
- TEST_DB_CONTAINER_IMAGE
- OAUTH2_ISSUER_URL, JWKS_URI

Keep secrets out of VCS. For local runs, populate `.env`; for CI, set GitHub Action secrets.

## 🧩 JPMS & SPI
- JPMS-friendly; ServiceLoader discovery is used for DI/lifecycle hooks.
- Follow module rules documented under `docs/architecture/*` and RULES.

## Open Source on GitHub
- License: Apache 2.0 (see `rules/LICENSE` and the root `rules/` submodule for the governance materials that apply to this repo).
- Contribution flow: Fork the repo, work within the Java 25 + Maven toolchain, and open a pull request that references the relevant documentation artifact (PACT, RULES, GUIDES, IMPLEMENTATION, GLOSSARY, `docs/architecture/*`, etc.). The PR description should link to the issue it closes, note any security token/ActiveFlag impacts, and cite the diagrams that change.
- Community guidelines: Every change must honor the Documentation-as-Code / Forward-Only policy anchored in `RULES.md`. Stage 1/2 documents (architecture/diagrams, glossary, rules/guides) must be updated before stage 3/4 implementation code, ensuring the PACT ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION ↔ GLOSSARY loop remains closed.
- Lifecycle flow: The test harness and client expectations enforce a `createNewEnterprise` → `loadUpdates` → `startNewEnterprise` order so classifications/types are loaded via `ISystemUpdate`/`@SortedUpdate`, then the admin user is registered through `IPasswordsService`, and finally post-startup actions execute.
- Security / Access: Every service propagates `SecurityToken` metadata (see `docs/architecture/c4-component-fsdm.md`) and respects ActiveFlag row status, so changes in capabilities must describe how value-level access is enforced.

## Key Documents
- `rules/` submodule (`rules/README.md`) is the Rules Repository source; do not modify its contents—only reference it.
- `PACT.md` defines the Human–AI collaboration pact and stage approvals.
- `RULES.md`, `GUIDES.md`, and `GLOSSARY.md` capture project conventions, how-to guidance, and topic-first terminology.
- `IMPLEMENTATION.md` describes the module layout plus runtime/bootstrapping expectations.
- `docs/PROMPT_REFERENCE.md` records selected stacks (Java 25, Maven, Vert.x, GuicedEE, Hibernate Reactive, PostgreSQL, EntityAssist, etc.).
- `docs/architecture/*` contains the Stage 1 artifacts (C4 context/container/component, sequences—including the enterprise lifecycle harness—ERD) with Mermaid sources.
- `docs/implementation-plan.md` summarizes the Stage 3 rollout, environment, CI, and validation roadmap.
- `.env.example` mirrors `rules/generative/platform/secrets-config/env-variables.md` and is the canonical local config template.
- `.github/workflows/maven-verify.yml` demonstrates how CI runs `mvn -B verify` over Java 25 with the required secrets while checking out the `rules/` submodule.

## Environment & CI
- Local development uses `.env.example` variables (DB, OAuth2, observability, testing) and the `PostgreSQLTestDBModule` + Testcontainers from `src/test/java/com/guicedee/activitymaster/tests`.
- CI via `.github/workflows/maven-verify.yml` installs Java 25 with `actions/setup-java@v4`, sets the required secrets (`DB_URL`, `DB_USER`, `DB_PASS`, `JWT_TEST_TOKEN`, `TEST_DB_CONTAINER_IMAGE`, `OAUTH2_ISSUER_URL`, `JWKS_URI`), and executes `mvn -B verify`.
- Jacoco and the Java Micro Harness run within that Maven lifecycle; any new tests must re-use the existing harness to keep coverage aligned.

## 🧰 Troubleshooting & Best Practices
- Verify `.env` is present for local runs and matches the keys above.
- Ensure the `rules/` submodule is initialized if you need to browse referenced docs.
- Keep Stage 1/2 documents updated before changing code in Stage 3/4.

## Documentation & AI Workspace Policy
- AI assistants rely on `.aiassistant/rules/summary.md`, `.github/copilot-instructions.md`, and `.cursor/rules.md` for constraints (Rules sections 4/5, Document Modularity, Forward-Only). Keep those files synchronized with `RULES.md`.
- Update architecture diagrams (`docs/architecture/*`) whenever the service or persistence models change, and reflect the updates in the glossary or guides before touching implementation code.
- Close loops by linking from PACT ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION ↔ GLOSSARY; stage approvals (blanket for this run) must still be recorded in doc headers/notes.

## Next Steps
1. When introducing new services or DB entities, refresh the relevant Mermaid diagrams, glossary entries, and guides before code changes.
2. Add any new environment variables to `.env.example`, document their use in `docs/implementation-plan.md`, and wire them into the GitHub workflow secrets.
3. Keep this README updated with the chosen stacks + OSS guidance so contributors understand the Rules Repository requirements before coding.

## Repository & Links
- GitHub: https://github.com/Activity-Master/Core
- Issues: https://github.com/Activity-Master/Core/issues
- Pull Requests: https://github.com/Activity-Master/Core/pulls
