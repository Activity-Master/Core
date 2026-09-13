# Party creation and security failure atomicity

Canonical account provisioning must not report success unless the party and its
organic/non-organic record have completed security installation on the caller's
transaction. Scope-restricted creation must fail when canonical security folders
are missing or a grant write fails. The caller owns rollback; services do not
open nested transactions or convert these failures into successful null/zero items.

```mermaid
sequenceDiagram
    participant Caller as Provisioning transaction
    participant Party as InvolvedPartyService
    participant Security as WarehouseCoreTable
    Caller->>Party: createScopeRestricted(session, reserved UUID, scope)
    Party->>Security: Create party grants on same session
    Security-->>Party: Completed grants or failure
    Party->>Security: Create organic/non-organic grants
    Security-->>Party: Completed grants or failure
    alt all required work succeeds
        Party-->>Caller: Party
        Caller->>Caller: Commit party and onboarding checkpoint
    else security unavailable
        Party-->>Caller: Original failure
        Caller->>Caller: Roll back; keep previous checkpoint
    end
```

The stateless restricted matrix writes Administrators CRUD, Systems/Applications/
Plugins create-update-read, and an optional scope read grant. Resolve all four
required folders before writing; missing folders must not produce a partial
successful matrix. Sequence asynchronous writes using `chain`, not `invoke`:
constructing a lazy Uni inside a synchronous callback does not execute its write.
Errors stop later grants and propagate to the transaction owner. A null scope
still means the four infrastructure grants; it does not enable public reads.

The live scope-restricted path has the same failure semantics. Bootstrap
tolerance remains on the separate live public/default installation path when
security enforcement is explicitly disabled. Ordinary live default creation
delegates to restricted security when enforcement is enabled; callers needing a
fixed restricted policy should select `createScopeRestricted` explicitly.

This change preserves the caller-assigned party UUID, identification linkage,
organic/non-organic choice and existing session ownership. It does not create
administrator memberships, passwords or browser sessions.

Live grant lookup outages and unexpected null results also propagate: only
`NoResultException` permits a missing grant to be inserted. Returning a null
canonical folder fails instead of attempting to create a grant without a token.

Validation on 12 September 2026: six `PartySecurityFailureTest` tests passed.
They execute the actual service/security pipelines with controlled persistence
and folder failures, asserting subscription/order/permissions and failure identity
for managed/stateless party and subtype creation. Tests isolate GuicedEE bootstrap.
Run `mvnw.cmd -o install -Dtest=PartySecurityFailureTest` from this module (using
the workspace Maven 4 wrapper); the artifact is installed locally, not published.
Two `AccountScopeSecurityIntegrationTest` tests additionally passed at 15:49 SAST
on 12 September 2026 against the full FSDM schema and real Guice/canonical services.
Bootstrap failures propagate to the test. They verify exactly five grants on the
party and organic subtype, the scope's UserGroup data concept and real hierarchy
access (owner scope read-only; sibling, Registered parent, guest and unknown denied;
system read/write). Run with `-Dtest=AccountScopeSecurityIntegrationTest,PartySecurityFailureTest`
for eight tests. The test modules own disposable PostgreSQL/MongoDB containers.
Evidence: `target/ne1-account-scope-integration.log`.

NE1 separately verifies its adapter and trusted context using actual Hibernate
Reactive/PostgreSQL transactions and controlled domain writes. The full-domain
fixture uses ActivityMaster's own system, not NE1's future provisioner registration.
These tests do not yet cover identification-link visibility or account identity
membership after registration.
