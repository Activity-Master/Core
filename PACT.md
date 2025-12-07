---
version: 2.0
date: 2025-10-30
title: ActivityMaster Core Human–AI Collaboration Pact
project: ActivityMaster / Core
authors: [ActivityMaster Team, Codex]
---

# 🤝 ActivityMaster Core Pact

## 1. Purpose
This pact codifies how AI and humans co-create the ActivityMaster Core codebase: documentation first, stage-gated, and rooted in the Rules Repository that governs GuicedEE + Vert.x + Hibernate Reactive + PostgreSQL stacks. Our supply chain (PACT → RULES → GUIDES → IMPLEMENTATION → GLOSSARY) ensures every artifact is traceable, forward-only, and aligned to the observed repo.

## 2. Principles
- **Continuity:** Each stage references the previous artifacts (PACT ↔ RULES ↔ GUIDES ↔ IMPLEMENTATION ↔ GLOSSARY). Any assumption is flagged, not guessed.
- **Documentation First:** Per the adoption prompt, stage 1/2 deliver docs before any code changes (with blanket approval noted at runtime).
- **Fidelity:** Descriptions, diagrams, and plans mirror actual Java 25 + Maven modules inside `src/` and `META-INF`.
- **Forward-Only:** Legacy docs are retired or linked; we do not reintroduce deprecated structures.

## 3. Stages & Artifacts
| Stage | Artifact | Location |
| --- | --- | --- |
| 1 | Architecture & Foundations | `docs/architecture/*.md`, `docs/PROMPT_REFERENCE.md` |
| 2 | Guides & Design Validation | `PACT.md`, `RULES.md`, `GUIDES.md`, `GLOSSARY.md`, `IMPLEMENTATION.md` |
| 3 | Implementation Plan | `IMPLEMENTATION.md`, `docs/PROMPT_REFERENCE.md`, `docs/architecture/*.md` |
| 4 | Implementation & Scaffolding | Code + environment updates + CI + README + `.aiassistant/rules/`, `.github/copilot-instructions.md`, `.cursor/rules.md`, `ROO_WORKSPACE_POLICY.md` |

## 4. Traceability
Every artifact links forward/back to: `PACT.md`, `RULES.md`, `GUIDES.md`, `IMPLEMENTATION.md`, `GLOSSARY.md`, `docs/PROMPT_REFERENCE.md`, and `docs/architecture/README.md`.

## 5. Communication
- Blanket approvals granted for this run; stage gates proceed automatically but remain documented.
- Document any uncertain area explicitly; escalate via open questions in stage summaries.

## 6. Next Steps
1. Reference the Rules Repository submodule at `rules/` without modifying its internals.
2. Ensure RULES, GUIDES, IMPLEMENTATION, and GLOSSARY all cite the relevant `rules/` entries (e.g., GuicedEE, Postgres, EntityAssist, logging, Angular terminology for WaButton/WaInput etc.).
3. After each stage, summarize outputs with diagrams and document cross-links.
