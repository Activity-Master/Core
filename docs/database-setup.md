# Creating an ActivityMaster database

The schema is the ordered scripts in `core/src/main/resources/db/`. They are the definition, and
`com.guicedee.activitymaster.fsdm.db.FsdmSchema` is the single place that says what they are and in
what order they run.

```java
FsdmSchema.forEachScript((script, sql) -> execute(sql));
```

`FsdmSchema` has no database dependency — the caller decides how to run each script, over JDBC,
through `psql`, or into a migration tool. Scripts are read as bytes off the classpath, so it makes
no difference whether core is an exploded directory or a jar. They ship inside `activity-master`,
so any module that depends on core already has them.

## Order

| Script | Contents |
|--------|----------|
| `00.1.setup.sql` | extensions, operators, session setup |
| `01`–`16` | one schema area each: enterprise, activeflag, systems, classification, address, arrangement, product, resourceitem, party, rules, securitytoken, event, geography, time, transactions |
| `17.foreign-key-indexes.sql` | join-key indexes across the warehouse |
| `20.1.setup.sql` | views and helper functions |

Adding a script means adding the file **and** registering it in `FsdmSchema.ORDERED`. Nothing scans
the directory, because resource enumeration is not reliable once the module is packaged.

## What this replaced

Every module used to build its test database from `postgres_fsdm.sql` plus
`postgres_structure.sql`. That pair was a dump taken from a live database, so it captured whatever
Hibernate had generated at the time rather than anything reviewed, and each module kept its own
copy, so the copies drifted. Those two files are no longer used to create anything.

Switching over surfaced three genuine defects in the ordered scripts, now fixed:

* `09.resourceitem.sql` and `14.geography.sql` had ten malformed UUID defaults
  (`00000000-0000-0000-0000-0000-000000000000`, one group too many), which Postgres rejects.
* `12.securitytoken.sql` created an index on `security.securityhierarchy`, a table the ordered
  scripts do not create. The hierarchy tables were deliberately dropped from the schema, so the
  orphaned index was removed.
* The ordered scripts indexed the warehouse metadata columns compositely with `warehousefromdate`,
  which serves enterprise, system, activeflag and classification lookups as leading columns, but
  left the **join targets** unindexed — `eventxinvolvedparty(involvedpartyid)`,
  `eventxevent(parenteventid)`, every `*securitytoken` table's `securitytokenid` and owning-row id,
  and so on. The old dump had them; without them a read that starts from a party or a parent event
  degrades to a sequential scan of the whole link table. `17.foreign-key-indexes.sql` restores
  them: 255 indexes across 116 tables, identifier columns only.
  `originalsourcesystemid` is deliberately excluded — nothing filters on it, and indexing it across
  173 tables would cost writes and buy no reads.

## Index measurements

`17.foreign-key-indexes.sql` restores join-key parity with the old dump. `18.query-indexes.sql`
holds indexes that were added because a measured plan asked for them. Both were checked against a
synthetic tenant on postgres:17 — 200k notifications, 300k events, 500k party links, 700k
classification links, 50k security tokens, 5k classifications.

| Change | Before | After | Verdict |
|--------|--------|-------|---------|
| `securitytoken(securitytoken)` | 4.0 ms | 0.07 ms | **Added.** Runs on every authenticated request in five modules; was a sequential scan of the token table |
| `classification(classificationname)` | 0.58 ms | 0.07 ms | **Added.** Resolved 6–10 times per operation |
| `eventtype(eventtypename)` | 0.061 ms | 0.059 ms | **Added.** No gain at 502 rows, but it is the same pattern and grows with the taxonomy |
| `eventxinvolvedparty(involvedpartyid, classificationid)` | 4.1 ms | 4.1 ms | **Rejected.** Used by the planner, no measurable gain, on a clean inbox or a heavily-linked party |
| `CREATE STATISTICS` on that pair | 24 ms | 24 ms | **Rejected.** No plan change |

The `(column, warehousefromdate)` composites the per-table scripts create are effectively
single-column indexes for these workloads: nothing filters on `warehousefromdate`, so only the
leading column does any work.

### The SCD lookup shape

Every SCD table is read the same way: equality on `enterpriseid`, equality on a natural key, the
temporal window, and a join to `dbo.activeflag`. Measured against 50k classifications (10%
current), 40k rows at 200 versions per key, and 4k concepts (5% current).

**Use a STABLE timestamp, never `clock_timestamp()`.** `clock_timestamp()` is VOLATILE, so
PostgreSQL cannot use it in an index condition — both temporal predicates collapse into a per-row
filter and every historical version of the key is fetched and discarded. `statement_timestamp()`
and `now()` are STABLE and become index conditions:

| Predicate | Plan |
|---|---|
| `effectivetodate > clock_timestamp()` | `Filter:` — `Rows Removed by Filter: 9` |
| `effectivetodate > statement_timestamp()` | `Index Cond:` — nothing removed |

At 200 versions per key that is 37.5 us versus 18.8 us, and the gap widens with history depth.
It is also a correctness point: `clock_timestamp()` advances *during* a statement, so the two
halves of a temporal window can be evaluated against different instants. The production services
already use `statement_timestamp()`, and EntityAssist binds a Java-side value; only three test
fixtures use `clock_timestamp()`.

**Index the name column.** This is the large, unconditional win.

| | Before | After |
|---|---|---|
| `classificationdataconcept` by name | 283 us | 25 us |

`18.query-indexes.sql` now covers the remaining lookup tables that had no index on their name
column: classification data concept, resource item type, arrangement type, product type, rules
type, the three involved-party type tables, and active flag.

**Add `(enterpriseid, <name>, effectivetodate)` composites.** With only the name indexed,
PostgreSQL fetches every historical version of a key and discards all but the current one. The
third column skips them in the index instead.

| `involvedpartyidentificationtype` by name, 41 versions per key | |
|---|---|
| no name index | 54.6 us |
| name index only | 28.7 us |
| + `(enterpriseid, name, effectivetodate)` | **15.2 us** |

These only pay off once the temporal predicate uses a STABLE function, and only where a key
accumulates versions. Taxonomy is written by find-or-insert today, so most keys have one version
right now — but names are expected to change over time, and the composite costs nothing while they
have not.

The plain single-column name indexes are kept alongside. Every name lookup in the codebase is
enterprise-scoped, so they are strictly redundant and could be dropped; they are retained because
these tables are small and rarely written, and the cost of being wrong about an un-scoped lookup is
a sequential scan.

**`allowaccess` is `INTEGER`.** Compare it with `= 1`. Casting it (`allowaccess::text IN
('1','true','t')`) makes it a non-sargable expression, discards the column statistics and adds a
per-row conversion — and the `'true'`/`'t'` branches are dead, since an integer only ever renders
as digits.

### `FOR SHARE` on a read path is expensive

Some guard queries end with `FOR SHARE OF t,s,a,b`. Measured with pgbench against the
identification-type guard, 16 clients:

| Variant | Latency | TPS |
|---|---|---|
| no `FOR SHARE` | 0.121 ms | **132,061** |
| `FOR SHARE OF t` | 2.93 ms | 5,461 |
| `FOR SHARE OF t,s,a,b` | 5.43 ms | 2,946 |

Roughly **45x less throughput**, and it holds across concurrency levels — at 1 client it is
13,745 tps against 286, at 32 clients 190,918 against 5,616.

The mechanism is that a row lock is a write. WAL generated per 1,000 executions:

| | WAL |
|---|---|
| plain read | **0 bytes** |
| with `FOR SHARE` | **204 kB** |

So the query stops being a read: it dirties pages, writes WAL and pays a commit flush. That also
explains why trimming the lock list barely helps — repeated runs put `FOR SHARE OF t` and
`FOR SHARE OF t,s,a,b` within noise of each other (2927/2864, 2938/2955, 2357/2911 tps). Once the
first row lock has made it a writing transaction, the extra rows are marginal.

Locking `dbo.activeflag` and `dbo.systems` is still worth removing on its own merits: every caller
in the application locks the *same* handful of rows, which is a global contention point rather than
a per-row one. But the real decision is whether the guard needs `FOR SHARE` at all. It is only
warranted if the caller must stop the taxonomy being superseded between the check and a subsequent
write in the same transaction; for a plain read-and-validate it should be dropped.

### Hierarchy-walk queries

A four-level classification walk (party award -> definition -> source -> domain -> root, 13
relations) measured against 2,109 classifications, 2,105 hierarchy links and 20,200 awards, of
which 200 belong to the party being queried.

**Missing temporal predicates are the landmine.** That query filters on ids only — no
`effectivefromdate`/`effectivetodate`, no activeflag check, on any of its twelve relations. Today
that is invisible because nothing has been superseded. After a single round of supersession, so
two versions per link:

| | Rows returned | Execution |
|---|---|---|
| as written | **1,600** (1,400 of them expired) | 47.2 ms |
| with `live()` predicates | 200 | 9.9 ms |

Each hierarchy level multiplies, so it is N^3 in versions per link: two versions gives 8x, ten
would give 1,000x. It is a correctness bug first — the extra rows are stale duplicates — and the
cost happens to follow.

**Three joins are dead.** `ta`, `ts`, `td` join `classification` on `ha/hs/hd.classificationid`,
which is already pinned to a bound parameter, and are then referenced in neither the projection nor
the `WHERE`. Removing all three returns byte-identical results and drops the query from 13
relations to 10.

**Bind the visible activeflag ids instead of an `EXISTS` per alias.** Nine correlated `EXISTS`
subqueries against `dbo.activeflag` cost more to plan than they save. Core already resolves this
set once (`getVisibleRangeAndUpIds`); passing it as an id list is both faster and simpler.

| Variant | Planning | Execution | TPS (4 clients) | Correct |
|---|---|---|---|---|
| as written | 35.6 ms | 47.2 ms | 77 | no — 1,600 rows |
| + `live()`, dead joins removed | 50.1 ms | 9.9 ms | 434 | yes |
| + bound activeflag id list | 33.0 ms | 8.9 ms | **495** | yes |

**6.4x throughput, and it stops returning stale rows.**

A composite on `classificationxclassification (childclassificationid, classificationid)` was also
tried: 9.6 ms to 8.75 ms, within noise. Not added.

Note that planning outweighs execution here (33 ms against 9 ms) because 10-13 relations sits at or
above the default `join_collapse_limit` of 8. Prepared statements amortise that per connection, so
it is a cold cost rather than a per-request one — but it is another reason to drop the dead joins.
The taxonomy above the award is small and static; resolving it once in the application rather than
walking it per request would remove the problem entirely.

### Relationship `value` columns

A relationship `value` is a short discriminator and is now `varchar(150)`. The per-table scripts
had declared it `text` — a script-separation bug — and 73 columns across 15 scripts were corrected.
`address.address.value` is left as `text`: it is a base-entity column, not a relationship.

The type mattered because ten scripts index `(value)`, and
`resource.resourceitemxclassification` carries four indexes containing it. While the column was
`text`, storing anything large in it failed on insert:

```
ERROR:  index row requires 20496 bytes, maximum size is 8191
```

A hash index does not help, because three of the four are composite btrees that include `value`.
Note the failure only appears with incompressible content — `repeat('x',65536)` fits because TOAST
compresses it, which is why the existing suites passed: Translations' fixture is
`"漢😀é".repeat(4000)`, which compresses away.

**Large content is a resource item plus its data**, never a link value. The payload table is
`resource.resourceitemdatavalue`, keyed by the resource item id, `EXTENDED` storage with `lz4`.
It carries no warehouse or security columns, so writing it directly preserves a module's
"no independent grants" guarantee.

Three modules were storing bodies as link values and were corrected:

| Module | Was | Now |
|---|---|---|
| Notification Master | body + payload as two `ResourceItemXClassification` values | one JSON document in the item's data |
| Translations Master | text as a `TranslationText` classification value | text in the item's data; search projects and filters it with `convert_from(...,'UTF8')` |
| Conversation Master | text as a `ConversationBody` classification value | text in the item's data |

Each also asserted at install time that the column was `text`; those assertions are gone, since
they codified the bug and fail against the corrected schema. Short fields still ride as link
values and are capped to fit 150 — notification subject went 512→150 and delivery detail 1024→150.

### The bigger win is not an index

Read queries join `classification.classification` and `event.eventtype` **by name** to turn a role
into an id. That is the main cost in the multi-predicate `WHERE` clauses, and it is a query-shape
problem rather than a missing index:

| Notification list, heavily-linked actor | Planning | Execution |
|---|---|---|
| Joining taxonomy by name (current) | 37 ms | 130 ms |
| Taxonomy ids bound as parameters | 3.2 ms | 23 ms |

The name joins dominate **planning**, which for a hot endpoint is paid on every new prepared
statement, and they also deny the planner a usable row estimate, so it seq-scans the type links
instead of driving from the actor's own rows. `NotificationService` already resolves and caches
these ids once per operation, so binding them instead of joining by name is a contained change.
Not done yet.

Note also that a clean inbox is fast regardless — 4.1 ms for a 51-row page against 200k
notifications. The 130 ms case is a party that appears on many links in other roles.

## Test bootstraps

Each module's `PostgreSQLTestDBModule` starts a container and applies the scripts through
`FsdmSchema`. The loop is about eight lines and identical everywhere; the part worth sharing — the
list and the script bodies — is shared.

Suites verified green on the ordered scripts:

| Module | Tests |
|--------|-------|
| notifications | 20 |
| translations | 9 |
| user-sessions | 9 |
| marketplace-master | 3 |
| conversations | 2 |

## Known-failing suites

These four fail for reasons unrelated to the schema. They were **not** baselined against the old
setup, so it is not established whether the conversion contributed; the failure modes below are not
schema errors, and no script failed to apply in any of them.

| Module | Failure | Looks like |
|--------|---------|-----------|
| `core` | `FindException: Module com.sun.jna.platform not found, required by com.azure.identity` — boot layer never starts, 0 tests run | Pre-existing: the same boot-layer failure appears in a surefire dump from 2026-09-22, and it reproduces with the schema change reverted |
| `images` | `Unable to boot Guice Injector` → `IllegalArgumentException: Invalid ActivityMaster pool configuration` | Connection-pool configuration, not schema |
| `profiles` | 3 errors, all `current transaction is aborted (25P02)` on party classification reads | A prior statement in the same transaction failed; the root error is masked and needs a run with the first failure surfaced |
| `geography` | 5 errors: `NoResultException`, and `getEntityManagerStateless()` returning null | Entity manager wiring, not schema |
| `cerial` | 34 errors across 51 tests, mostly `TimedComPortSender*` | Serial-port hardware tests |

## Still on the old path

`FSDMMigrateGUI` still creates databases from `META-INF/postgres_fsdm.sql` and
`META-INF/postgres_structure.sql`. **`META-INF/postgres_fsdm.sql` has already been deleted**, so
its "Create Database" path is broken today. It should be moved to `FsdmSchema`; until then a
database built through that tool will not match one built from the ordered scripts.

The legacy dumps have been deleted from `core/src/test/resources`. Unused copies may still remain
in the `cerial`, `geography`, `images`, `profiles` and `user-sessions` test resources; nothing
loads them any more.
