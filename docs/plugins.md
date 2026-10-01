# ActivityMaster plugins

`PluginService` registers extensions and manages delegated system access using the
existing secured FSDM. It creates no plugin tables. The consuming host owns
authentication, management endpoints and UI.

A plugin implements the independent `IMasterPlugin` SPI, normally through
`MasterDefaultPlugin`. It must never implement `IMasterSystem` or inherit
`MasterDefaultSystem`, and cannot register as Activity Master System. Enterprise
bootstrap discovers and provisions plugins separately from Systems. Publish the
provider through `IMasterPlugin` in both JPMS and classpath service declarations.

A plugin has a durable technical `Systems` registration, a **Plugin** security identity under the
**Plugins** folder, and a typed catalogue Arrangement. Ordinary Masters retain
**System** identities under **System**. Public registration cannot change an existing
Master into a plugin. Registration is administrator-only and requires a name,
title, description and version. Titles, names and versions accept up to 150 UTF-16
code units; descriptions accept up to 250. Optional icons and ordered screenshots
reference existing live, readable ResourceItems (up to 20 screenshots). The
ResourceItem service controls access to the media itself.

`PluginInstall` provisions the core-owned catalogue and Event vocabulary through
the ordered enterprise update mechanism. Existing enterprises must run their
normal authorized updates before using this service. Provisioning taxonomy grants
no party installation, system declaration or user consent.

## Authorization and lifecycle

Each invocation requires all of the following:

1. A live, authenticated organic user and that user's ActivityMaster credential
   (the `User` or canonical account `Identity` classification).
2. A current installation on an organic or nonorganic involved party that the
   user can currently read. Installation/removal require write access to that party.
3. A declaration that the plugin uses the target registered System or Plugin (up to 100).
4. The calling user's explicit consent to that system for the current installation
   and declaration. Organisation installation never consents for its members.
5. No administrator denial for that plugin/system at enterprise or party level.
6. The target Master's normal current row permissions, membership and behavior grants.

Registering a plugin grants none of these independently. `setSystemAccess` requires
current Administrators hierarchy membership; enterprise denial overrides party
allowance and user consent. `remove` revokes installation. `consent(..., false)`
withdraws that user's consent. Removing/reinstalling a plugin or withdrawing and
reintroducing a system declaration requires fresh consent. Version/metadata
updates that retain a declaration do not themselves invalidate consent.

State changes archive the previous Event and create a current Event. Checks hold
a shared lock on the plugin catalogue Arrangement in the caller's transaction;
management changes take an exclusive lock on that same row. Revocation waits for
already authorized transactions, then blocks later checks. Callers must keep
transactions short and run domain work in that same transaction.

## Host and Master integration

The host constructs `PluginModels.Identity(partyId, enterpriseId, userCredential)`
from verified authentication and binds `Invocation(pluginId, installationPartyId)`
to the extension making the call. Request-body or GraphQL arguments never establish
this authority. Bind the credential to the user server-side and verify the user's
relationship to an organisation installation. Preserve invocation context across
every downstream identity provider; clearing it would bypass delegated checks.

All `PluginService` operations require a caller-owned `Mutiny.StatelessSession`
transaction. Registration, installation, consent and administrator policies are
separate host-authorized management operations. `find` returns catalogue metadata;
it does not authorize invocation or media payload retrieval.

Masters can use `PluginService.execute(session, targetSystem, identity, invocation,
operation, work)`: it checks delegation, passes the original user identity to
`work`, and records an initiation audit after successful writes. A null operation
denotes a read. The callback must still enforce normal row and domain permissions.
Alternatively, call `check` before domain work and `audit` after successful writes
in the same transaction. Do not substitute a plugin credential or a Master
credential for the user, or expose privileged raw FSDM APIs as a plugin endpoint.
Applicable-token expansion excludes Plugin identities and stops at Plugin ancestors.

Forum Master and Notification Master enforce delegation when their host identity
contains an invocation. `ForumApi` audits successful mutations. Low-level forum
callers must compose `audit` (or `execute`) with their domain operation. Other
Masters must integrate this boundary before exposing plugin calls; registration
alone does not install enforcement in every Master.

## Built-in plugins

Mail, Wallet, Payments, Documents, Conversations, Marketplace and Marketplace
Products implement `IMasterPlugin` through `MasterDefaultPlugin`. They retain
durable registration IDs for FSDM relationships and provenance; those IDs do not
grant System authority.

| Plugin | Declared dependencies |
|---|---|
| Mail Master | Activity Master System |
| Wallet Master | Activity Master System |
| Payment Master | Activity Master System, Wallet Master |
| Document Master | Activity Master System |
| Conversation Master | Activity Master System |
| Marketplace Master | Activity Master System |
| Marketplace Products | Activity Master System, Marketplace Master |

`BuiltInPluginsInstall` runs at update 1020, after plugin taxonomy (1010) and before
domain taxonomy. It retains existing registration IDs and domain records, archives
legacy System credentials and their hierarchy links, and registers Plugin identities
under Plugins. The live core bootstrap credential is required for this forward
conversion and taxonomy provisioning. Re-running it does not grant installations
or consent. No new database schema migration is needed.
`PluginArchitectureInstall` (1030) separately discovers the independent plugin
SPI for enterprises that have already recorded 1020. Module updates
`ConversationPluginInstall` (1174), `DocumentPluginInstall` (1184) and
`MarketplacePluginInstall` (1187) provision their respective registrations and
catalogues when added later. The Marketplace update provisions both plugins;
its domain taxonomy follows at 1188. Each built-in's domain update also provisions
its registration and catalogue. Distinct forward update classes run even when
the original domain update has already been recorded. Choose unique sort orders
for new updates and keep previously applied receipts.
Repeated provisioning preserves live catalogue icons and screenshots.

The host must install each built-in plugin on the authorized party and obtain each
user's consent to its declared dependencies. Wallet and Payment also retain their
existing provider behavior grants and row security checks. Payment operations
require both Payment and Wallet admission; callbacks resolve the initiating user's
current identity and repeat those checks before settlement. Administrators may
disable a dependency for the enterprise or installation party.

Mail ingestion resolves `MailIdentityProvider` or accepts a verified `MailIdentity`
from a trusted host/job. It denies by default. The identity determines the mailbox;
email headers and the compatibility mailbox label never establish ownership.
Installation, consent and policy checks, private body/attachment storage, links and
audit run in one transaction. SMTP and IMAP transport engines remain host services;
jobs that persist user mail must supply the current authorized user identity.

Documents, Conversations and Marketplace use identities with
`(partyId, enterpriseId, context, identityToken, installationPartyId)`; the
four-argument constructors select the user's party as the installation party.
The host verifies membership before selecting an organisation installation.
Personal and Social context owners are the user party; Work is enterprise-owned.
Each service checks built-in admission on every entry, including document
downloads/version history, conversation message reads and marketplace browsing.
Current bucket membership, conversation participation, provider behavior grants
and row permissions remain additional requirements. Membership alone cannot
replace plugin admission.

Marketplace products are loaded through the separate `MarketplaceProductsApi.load`
or `POST /{enterprise}/marketplace-products`. The host binds a verified
`MarketplaceIdentityProvider`; producer installation and consent to both Core and
Marketplace are required, together with Marketplace's own admission, approved
seller status, sell behavior and row permissions. Existing draft entry points
delegate through this producer. The producer calls Marketplace with the same
user and records `marketplace.products.load` in the invocation audit. Marketplace
remains the writing capability. Buyers need Marketplace admission and their normal
row/behavior grants; removing the producer blocks new loads without removing
existing authorized listings. Checkout creates a pending-payment order; the host
must separately perform authorized Wallet/Payment settlement.

## Provenance

Domain records use the writing Master's ID as `OriginalSourceSystemID`, including
when a plugin initiated the call. The plugin is recorded separately through the
`Plugin Invocation` Event's catalogue relationship, actor, installation party,
target system and operation. This preserves the distinction between writer and
initiator. Generic relationship replacement helpers archive and insert a new
version when the writing system changes, even when the value is unchanged; the
new version links to its predecessor through `OriginalSourceSystemUniqueID`.

Plugin catalogue, declarations, installation, consent, policy and invocation
records use existing Arrangement, Event, Classification, InvolvedParty and
ResourceItem entities with current effective dates and FSDM row grants. The
catalogue never grants a plugin direct access to another user's data.
