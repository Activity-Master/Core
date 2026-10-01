# Shared FSDM query indexes

`24.domain-query-indexes.sql` extends the predicate families identified in
`21.uwe-query-indexes.sql` and `23.document-query-indexes.sql` across equivalent
FSDM tables. Those earlier indexes already benefit every service using their
physical tables; their module names do not restrict their use.

The update is appended to `FsdmSchema.ORDERED`. Scripts 21 and 23 are unchanged,
so existing migration checksums remain valid. This update creates no tables,
constraints, uniqueness rules, payload indexes or tenant-specific objects.

## Coverage

| Family | Added indexes | Access pattern |
|---|---:|---|
| Taxonomy descriptions | 7 | Description equality on resource, product, rules, party, organic party, party name and address types |
| Address type current name | 1 | Enterprise/name/effective-to lookup, complementing the structured address name index |
| Classification candidates | 15 | `(classificationid, value, ownerid)` |
| Classification field pivots | 15 | `(ownerid, classificationid) INCLUDE (value)` |
| Typed discriminators | 9 | `(enterpriseid, typeid, value, effectivetodate)` |
| Current entity pair | 43 | `(ownerid, targetid, enterpriseid, systemid, effectivetodate)` with classification, effective-from and active flag coverage |
| Current owner contents | 43 | `(enterpriseid, systemid, ownerid, effectivetodate, targetid)` with classification, effective-from and active flag coverage |
| Encrypted address equality | 2 | Enterprise/plain value and enterprise/authenticated header branches of `EncryptedValuePredicate` |

The 135 additions touch 76 tables across `dbo`, `classification`, `address`,
`arrangement`, `product`, `resource`, `party`, `rules`, `security`, `event`,
`geography` and `transactions`. Transaction relationships use their actual
`enterprise_id`, `entry_id` and target column names. Static time dimensions have
no equivalent relationship tables and retain their existing dimension indexes.

Classification coverage includes primary entities and classification-bearing
type/data-concept entities. Entity relationship coverage includes cross-domain
links, resource attachments, structured address components and same-domain
parent/child links. Hierarchy owners are parent IDs. The pair index supports
exact owner/target probes; the contents index supports current children or
attachments under one owner. Existing target-leading indexes continue to serve
reverse traversal.

Type links use the typed-discriminator family instead of another pair/contents
family. This includes arrangement/rules types and party/product types as well as
each entity's own type. Address base rows already have the structured
enterprise/address-type/value index from script 19.

## Existing coverage reused

* Event classification candidate and field-pivot indexes from 21.
* Resource classification candidate and owner/classification/effective-window
  covering indexes from 09.
* Enterprise/resource-type/value/current lookup from 21.
* Party identification plain/legacy and authenticated encrypted-header indexes
  from 21. Its existing lookup still evaluates temporal visibility after lookup.
* Arrangement/party pair and arrangement/resource contents indexes from 23.
* Taxonomy name, effective-date, foreign-relationship and payload primary-key
  indexes already installed by the preceding scripts.

Encrypted address indexes use exactly the names and expressions in the optional
`docs/sql/encrypted-value-lookup-indexes.sql`, so installing either first does
not duplicate them. No encrypted expression indexes are added to unencrypted
domains. Deprecated `ResourceItemData` relationships retain their existing
migration support without additional indexes.

Only type descriptions are extended. This does not index every free-text
description or long data-concept description. These indexes serve equality
lookups; they do not accelerate substring search or arbitrary text predicates.

## Validation

`src/test/scripts/domain_query_index_audit.py` creates its own PostgreSQL 17
container with no published ports. It applies the complete preceding schema,
then loads 5,000 rows per exercised taxonomy and 20,000 rows per other table.
Relationship fixtures include 20 historical versions per owner/target pair.
Transaction fixtures preserve their unique keys with distinct effective-from
timestamps; canonical mutation validation triggers are bypassed while seeding.

It compares sorted result fingerprints and five-run median execution/planning
times before and after 24 for each added predicate shape. The encrypted address
branches are measured together as the actual OR expression. It applies 24 twice
and verifies the index count and validity. Every definition is also installed on
a hash-partitioned copy, with a partition present before installation and a
partition created afterwards, checking all parent and child indexes.

This is a synthetic predicate-family audit. It does not measure every service's
full joins, security checks, real distributions, partition pruning or production
latency. The original full UWE and Document service audits remain linked below.
More indexes add storage and write maintenance; individual production workloads
can still choose older indexes or scans when those are cheaper.

Measured on PostgreSQL 17.11 with 1,415,000 fixture rows. Each query uses the
median of five executions; the table summarizes the median across queries in
each family. Times are milliseconds, with all preceding indexes retained.

| Family | Queries | Before execution | After execution | Before planning | After planning |
|---|---:|---:|---:|---:|---:|
| Type description | 7 | 0.179 | 0.012 | 0.019 | 0.026 |
| Address type current name | 1 | 0.010 | 0.011 | 0.031 | 0.033 |
| Current entity pair | 43 | 0.059 | 0.010 | 0.068 | 0.072 |
| Current owner contents | 43 | 0.026 | 0.011 | 0.062 | 0.065 |
| Classification/value candidates | 15 | 0.045 | 0.011 | 0.043 | 0.045 |
| Owner classification pivots | 15 | 0.032 | 0.025 | 0.038 | 0.041 |
| Typed discriminators | 9 | 0.077 | 0.016 | 0.058 | 0.060 |
| Encrypted address OR | 1 | 8.225 | 0.032 | 0.098 | 0.070 |

All 134 result fingerprints were unchanged. Replay created no duplicate indexes,
and all 405 parent/child indexes in the partition fixture were valid. Plans used
130 of the 135 additions. The five unselected definitions are the address plain
value index, address type current-name index and the three `dbo` classification
owner indexes. They remain to provide the requested equivalent coverage: this
fixture has a single enterprise/system/active flag, shallow taxonomy history,
and an existing plain address value index. No timing gain is claimed for them.
The largest execution regression was 0.021 ms for the unselective system-field
pivot. The encrypted address gain came from the authenticated header branch;
the existing plain-value index continued to serve its OR alternatives.

The tracked schema update audit separately exercises fresh installation,
idempotent replay, checksum enforcement, rollback/retry, concurrent installation
and explicit upgrades from an existing schema. Core packaging also verifies
the migration is included in the shipped JAR.

Reproduce from `ActivityMaster/core`:

```powershell
python src/test/scripts/domain_query_index_audit.py
python src/test/scripts/fsdm_schema_update_audit.py
```

The default JSON report is `target/domain-query-index-audit.json`. A compact
before/after report is recorded in `docs/domain-query-index-audit.csv`.
Apply pending updates through the tracked runner described in
[database setup](database-setup.md); registration alone does not migrate an
already running application's database.

Related full-query evidence: [UWE audit](uwe-query-index-audit.md) and
[Document audit](../../documents/docs/query-performance.md).

[Forum and Notification service audit](forum-notification-query-index-audit.md)
adds five measured reverse-traversal and current-classification indexes in script
25. It keeps this script's checksums and measurements unchanged. The family audit
stops at script 24 even when subsequent migrations are registered.
