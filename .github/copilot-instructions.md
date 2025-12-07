# Copilot Workspace Instructions
- Anchor on `RULES.md` sections 4/5 plus the Document Modularity and Forward-Only policies; do not create docs/code without following the stage-gated workflow.
- Stage gates: produce Stage 1 (architecture), Stage 2 (Guides), Stage 3 (Implementation plan), Stage 4 (code) deliverables in order; log when a stage auto-advances under blanket approval.
- When the user is asked to approve a stage, respond with the options block and wait for their reply unless blanket approval is active. The options block structure is:
  Options:
  - 1) Approve Stage N → proceed to Stage N+1
  - 2) Request changes to Stage N (specify what to adjust)
  - 3) Skip approval for this stage and proceed (recorded as optional approval)
  - 4) Pause here (do not proceed)
- Treat `rules/` as a read-only submodule; link to its resources (e.g., `rules/generative/backend/guicedee/README.md`).
