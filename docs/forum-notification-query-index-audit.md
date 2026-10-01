# Forum and Notification query indexes

`25.forum-notification-query-indexes.sql` adds five shared FSDM indexes after
`24.domain-query-indexes.sql`. It is registered in `FsdmSchema.ORDERED` and shipped
in the core JAR. Earlier migration contents are unchanged because installed
scripts have recorded checksums. Apply pending updates through the tracked runner
described in [database setup](database-setup.md).

The audit traces native SQL in `ForumService` and `NotificationService`, including
membership checks, subscriber lists, posts, notification windows, list filters,
counts, read-all selection, latest state and delivery history. The additions
address reverse traversal and classification reads that otherwise examine
retained history.

## Added paths

| Table | Leading keys | Queries served |
|---|---|---|
| `arrangement.arrangementxinvolvedparty` | involved party, enterprise, system, effective-to, arrangement | Actor's current forum subscriptions |
| `event.eventxinvolvedparty` | involved party, enterprise, system, effective-to, event | Current notification recipient window |
| `event.eventxarrangement` | arrangement, enterprise, system, effective-to, event | Current posts belonging to a forum |
| `arrangement.arrangementxclassification` | enterprise, system, arrangement, effective-to, classification | Current forum context and title |
| `event.eventxclassification` | enterprise, system, event, effective-to, classification | Notification headers, latest-state fields and delivery fields |

The indexes include effective-from and active-flag columns for the remaining
visibility checks. Relationship indexes also include classification; field
indexes include value. They are non-unique B-trees with no tenant constants or
clock-dependent partial predicates. They do not alter query semantics, locks,
count cardinality, effective dates or permissions.

## Existing paths retained

The current Arrangement/party pair index from script 23 already serves exact
membership changes, and its shared contents index from 24 serves subscriber
enumeration. Current Event parent/child and Event/party pair/contents indexes
from 24 serve state and delivery traversal. Event/resource contents indexes
serve body links; ResourceItem and ResourceItemDataValue payload reads use
primary keys. These joins receive no additional duplicate indexes.

Credential, actor, active-flag, taxonomy and system/party grant queries were also
reviewed against the existing primary keys, credential/name indexes from 18 and
grant owner/token indexes. No additional indexes were added for those paths.
This audit measures the domain read shapes below; it does not benchmark the
complete shared security-token or provider-behaviour resolution chain.

## PostgreSQL evidence

`src/test/scripts/forum_notification_query_index_audit.py` compiles the current
Java SQL helpers in isolation and extracts the direct post, subscriber, title,
notification find and delivery SQL. List, count and read-all statements combine
those production helpers with equivalent service projections and filters.
Paging is represented by SQL limits at offset zero. No application database is
accessed; the PostgreSQL 17 container publishes no ports and is removed on exit.

The fixture contains 6,000 notifications, 1,200 forums and 12,000 posts across
multiple enterprise/system scopes, with 20 expired versions plus one current
version of each exercised historical relationship or field. Two thirds of the
notifications have three state Events; the remainder exercise unread selection.
Each notification has two delivery Events. The selected actor has 300 current
notifications in the tested scope, below the service's scan/count ceilings.
Fixture seeding bypasses mutation triggers; storage tests separately exercise
the real application writes and authority checks.

All preceding indexes remain installed in the baseline. PostgreSQL 17.11
`EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON)` reports use five-run medians, in
milliseconds:

| Query | Before execution | After execution | Before planning | After planning |
|---|---:|---:|---:|---:|
| Forum list | 11.000 | 0.757 | 66.712 | 28.149 |
| Forum membership, shared lock | 0.187 | 0.188 | 8.025 | 6.859 |
| Forum moderator, update lock | 0.178 | 0.187 | 6.676 | 7.442 |
| Forum subscribers | 0.204 | 0.076 | 7.146 | 4.164 |
| Forum title | 0.077 | 0.031 | 0.412 | 0.251 |
| Forum posts | 17.672 | 0.376 | 78.305 | 64.363 |
| Notification recipient window | 14.088 | 2.399 | 20.603 | 11.620 |
| Notification default list | 35.995 | 5.466 | 85.492 | 38.584 |
| Notification unread list | 17.960 | 9.263 | 40.917 | 37.234 |
| Notification category/unread list | 18.206 | 10.861 | 40.093 | 37.185 |
| Notification find | 6.969 | 2.982 | 39.108 | 38.420 |
| Notification counts | 17.963 | 12.254 | 37.984 | 38.086 |
| Notification read-all selection | 19.285 | 12.445 | 38.307 | 37.331 |
| Notification deliveries | 0.212 | 0.139 | 1.654 | 1.702 |

Every new index was selected by at least one plan. All 14 sorted result
fingerprints remain unchanged. Reapplying the migration creates no extra indexes;
all indexes are valid and ready. Each definition also passes installation on a
partitioned parent, with partitions created both before and after installation.

The tracked migration audit passes fresh installation, existing-schema upgrades,
replay, checksum/sequence enforcement, rollback/retry and concurrent installation
with all 27 registered scripts. The service test run passes 24 tests: 3 forum
storage, 12 notification storage, 6 notification contract and 3 mail isolation.
Core packaging includes script 25 and the updated schema registry.

These are synthetic read-query measurements, not production or live-host latency.
Point membership checks were already fast and are effectively unchanged. Planning
still contributes substantial time on the complex joins. Data distribution,
history volume, partition pruning, the maximum paging window and prepared plans
can change index choice. Additional indexes also require storage and write
maintenance.

Reproduce from `ActivityMaster/core`:

```powershell
python -B -X utf8 src/test/scripts/forum_notification_query_index_audit.py
python -B -X utf8 src/test/scripts/fsdm_schema_update_audit.py
```

The JSON report at `target/forum-notification-query-index-audit.json` contains
the SQL, source hashes, result fingerprints and full before/after plans.
[The CSV report](forum-notification-query-index-audit.csv) records the compact
measurements. The earlier [shared domain audit](domain-query-index-audit.md)
retains its original migration boundary and measurements.
