# UWE FSDM query and index audit

The shared-domain extension is now registered in
`24.domain-query-indexes.sql`; see [the domain index audit](domain-query-index-audit.md).
It reuses the indexes below and adds equivalent patterns on other FSDM tables.
Script 21 remains unchanged for installations that have recorded its checksum.

Reviewed on 2026-09-30 against `C:/Java/UWE/pom.xml` (the active reactor) and
ActivityMaster core's current ordered schema. The new, additive definitions are in
`src/main/resources/db/21.uwe-query-indexes.sql`, registered after `20.1.setup.sql`
in `FsdmSchema`. Existing local changes, including structured party addresses and
enterprise encryption, are preserved.

## Query coverage

The review followed the active modules' direct SQL, shared query-builder pivots,
ActivityMaster service calls, and REST DTOs through to their underlying tables.
Disabled `v4Dashboards` SQL Server queries and application-owned `uwe.*` reporting
entities do not belong in the shared FSDM schema.

| UWE workload / producer | Actual access pattern | Coverage / decision |
|---|---|---|
| `TimesheetStatisticsLoader.SQL` | Session and station classification/value `EXISTS`, then expand event fields and aggregate measured time | Add `(classificationid,value,eventid)` and `(eventid,classificationid) INCLUDE(value)` on `event.eventxclassification` |
| `RegenerationReplayService.PMU_SQL`, `assertEventsExist` | Audit timestamp window, event type links, classification pivot, session `HAVING` | Existing `idx_ev_wh_created`; new covering event classification index and taxonomy description indexes. Late session filtering remains expensive |
| `SessionLineService.COUNT_MEASUREMENT_EVENTS_SQL` | Event type description plus timestamp window, distinct event ids | Existing timestamp, event-id and event-type-id indexes; add type description lookup. A full-window count can still legitimately scan many rows |
| `StaffTimesheetService.loadTimeSheets` | Session -> line -> grader -> timesheet arrangement tree with type descriptions | Existing parent/child and arrangement/type FK indexes; add arrangement type description lookup. No speculative composite on every hierarchy level |
| `StaffService.loadStaffIds` | Farm's child parties, staff classification description, username identification description | Existing parent-party and party/type FK indexes; add the missing taxonomy description lookups |
| `BarcodeService` -> `ResourceItemService.findByResourceItemType` | Enterprise, Barcode/BarcodeBatch type, optional short discriminator, effective window, active flag | Add `(enterpriseid,resourceitemtypeid,value,effectivetodate)` on `resource.resourceitemxresourceitemtype`; type-only enumeration uses the leading pair |
| `GraderService`, `ServerService` -> `ResourceItemService.findByClassification` | Classification/value -> resource item -> type | Existing `rix_cls_val_effdesc_idx`, `ric_class_value_eff_idx` and type FK indexes already cover this; do not add another duplicate |
| `FarmService`, `PackingShedService`, `StaffService`, `PackingSessionService` -> party/arrangement REST find | UUID identity followed by requested relationship includes | Existing primary keys and owner FK indexes cover includes; no global index on every included field |
| `ServerCraftService`, `BarcodeService`, `StaffService`, `PackingShedService`, `StationService`, instructions/materials/grader loaders -> `IQueryBuilderClassifications.getClassificationsValuePivot` | Owner UUID(s), classification names, visible flags and effective windows, then conditional aggregation | Existing owner and name indexes; resource classifications already have an owner/classification/time covering index. The filtered field set is an aggregate, not a JSON property index |
| Shared party identification / UUID-token lookups | Enterprise + identification type + plaintext/legacy equality OR authenticated encrypted-header equality | Promote both optional `am_party_identification_lookup_*` definitions into the ordered schema. Names and expression match the existing encryption SQL and `EncryptedValuePredicate` |
| `getIEnterprise`, `getISystem`, taxonomy and security resolution | Small enterprise catalogue; system name; enterprise/name/current taxonomy; token value | Existing system-name, token and taxonomy-name indexes. No additional enterprise-name index justified for the small catalogue |
| Resource state / image reads and writes | Resource item UUID; `ResourceItemDataValue` payload primary key | Already covered by primary key; do not index compressed payload bytes |
| Farm/staff/session metadata Mongo reads | `_id`; partitioned session document `_state` / `_parent` | Mongo `_id` and the existing `SessionPartitionMongoStore` compound index. PostgreSQL scripts cannot index Mongo documents |
| Session/staff/farm listeners in other active server modules | Delegate to the services above rather than introduce independent FSDM query shapes | Covered through the producer/service paths above |

## Isolated PostgreSQL measurements

`src/test/scripts/uwe_query_index_audit.py` creates its own PostgreSQL 17 container,
publishes no ports, applies the complete baseline schema, loads synthetic data,
and extracts five native query bodies directly from UWE's Java source. The other
queries isolate shared service predicate shapes and existing timestamp coverage.

Fixture: 60,000 events, 540,000 measurement classification links, 100,000 resource
type links, 100,000 party identification links, 100,000 parties and party links,
4,000 arrangements, and 5,000 rows in each exercised taxonomy table. FK triggers
are bypassed only during fixture loading; this is a query-plan fixture, not a
domain-installation, security or lifecycle integration test.

Median execution times over five executions using `EXPLAIN (ANALYZE, BUFFERS,
FORMAT JSON)` on PostgreSQL 17.11:

| Query | Before (ms) | After (ms) |
|---|---:|---:|
| Actual timesheet measurement statistics | 485.580 | 2.166 |
| Actual measurement replay | 246.142 | 162.524 |
| Actual full-window measurement count | 70.124 | 78.387 |
| Actual arrangement timesheet hierarchy | 7.376 | 6.829 |
| Actual staff-by-farm loader | 0.704 | 0.663 |
| Existing timestamp-window count | 4.406 | 4.431 |
| Classification description | 0.426 | 0.029 |
| Event type description | 0.365 | 0.029 |
| Arrangement type description | 0.262 | 0.030 |
| Identification type description | 0.241 | 0.030 |
| Enterprise/type/barcode discriminator | 0.289 | 0.041 |
| Encrypted identification OR predicate | 69.471 | 0.101 |

These are synthetic observations, not production timing guarantees. The
full-window count did not improve: it still has to count all matching events.
Each query's row count and sorted result fingerprint remained identical before
and after the indexes. Applying the new script twice preserved the index count.
A separate complete schema exercised partitioned versions of the three affected
relationship tables: all ten child indexes were valid across existing and newly
created partitions. The follow-up history/replay audit compiles `FsdmSchema` and
executes all 24 registered resources, including the history bootstrap and
relationship-index correction. No application database was altered.

Reproduce from core:

```powershell
python src/test/scripts/uwe_query_index_audit.py --uwe C:/Java/UWE --output C:/Users/GedMarc/AppData/Local/Temp/uwe-query-index-audit.json
```

The native queries use fixture literals. Hibernate parameter type inference,
production plan statistics, deep historical versions and application startup
are not exercised by this fixture.

## Query issues that indexes do not fix

* `SessionLineService.countMeasurementEventsForSession` accepts a session id, but
  its SQL never binds or filters that id. It counts all UnitWeighed events in the
  timestamp window, including other sessions. Null windows are not implemented
  as optional predicates. Its error recovery returns zero on query failures.
* `RegenerationReplayService.PMU_SQL` applies session membership in `HAVING`
  after expanding and grouping the whole time window. Its `LEFT JOIN uw` does
  not exclude non-UnitWeighed type links. It also chooses classification ids
  from an unscoped description aggregate without effective/active predicates.
  An index improves traversal but cannot supply the missing semantics.
* `BlockService.getBlockData` refers to
  `classification.classificationxinvolvedparty` and `classificationvalue`.
  The current FSDM table is `party.involvedpartyxclassification`, with `value`.
  Its 21 joins also lack current/active predicates. Adding an index to the
  absent legacy table would conceal a schema mismatch rather than fix it.
* `ReportingRegenerationService.setSessionBonusConfiguredTrue` updates
  `warehouse.packingsession`; `DeleteScannerListener` updates `scanners`;
  `BarcodeDuplicateCheckListener` reads unqualified `received_barcodes`.
  Those relations are not defined by the current ordered core schema. Any
  deployment compatibility objects need to be checked before changing these
  paths. Do not introduce parallel UWE business tables into core for them.
* Core's `public.create_event_types_view` casts UUID join keys to text. Those
  expressions do not match the UUID indexes. Confirm which barcode view is
  installed and restore typed UUID joins before considering expression indexes
  solely to accommodate casts.
* Several native measurement, staff and timesheet queries omit enterprise,
  effective-date or active-flag predicates. Preserve those observations as
  correctness follow-ups; adding indexes does not make the reads scoped.

## Applying to an existing installation

Registration makes the script available through `FsdmSchema`; it does not
automatically invoke migrations in a running UWE database. The tracked runner
and SQL bundle commands in [database setup](database-setup.md#updating-a-running-database)
apply pending updates without a restart. Without history every file is pending;
an optional known baseline can adopt an older installation without replaying its files.
The new script is independently
replayable on an installation with the current FSDM tables. All nine indexes are
non-unique and additive, with no fixed tenant ids or time-dependent predicates.
The encrypted lookup definitions share the names in
`docs/sql/encrypted-value-lookup-indexes.sql` to avoid duplicate installations.

Index builds add storage and write maintenance. The script uses ordinary
`CREATE INDEX` for compatibility with both fresh schemas and partitioned parents.
For a busy existing deployment, plan the build window; PostgreSQL documents the
alternative of creating child indexes concurrently and attaching them to a
partitioned parent in its [partitioning documentation](https://www.postgresql.org/docs/17/ddl-partitioning.html).
