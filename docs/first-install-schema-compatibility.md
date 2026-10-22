# First-install schema compatibility

The platform audit at commit `efc8c45` used ActivityMaster snapshot
`3.0.0-20261016.140641-3`. It reported two transaction type column mismatches
and an implicit `Quarters_Months` join table.

The current snapshot reconciles those mappings:

- `TransactionType.code` and `TransactionType.description` both have explicit
  length 255; canonical `db/16.transactions.sql` declares both as `varchar(255)`.
  `transactions.entry.unit` is also explicitly declared as `varchar(255)`.
  No new migration is added for this correction.
- `Quarters.lUMonthsList` is the inverse of `Months.quarterID`. Hibernate uses
  the existing `time.months.quarterid` column; no join table or migration is needed.

Refresh Maven snapshots (`-U`) and refresh any generated installer classpath or
image that pins a timestamped snapshot. Fresh installations use the corrected
canonical SQL. Existing tracked databases retain checksum enforcement: this
change does not rewrite their history or automatically convert existing columns.
Keep Hibernate schema validation enabled.

`FirstInstallSchemaMappingTest` builds Hibernate metadata for the complete core
entity graph and checks the transaction type SQL types and the inverse quarter
association, including the absence of an implicit join table. The focused
PostgreSQL audit checks fresh creation, replay, unchanged
history, constraints, and the 255-character description limit:

```powershell
mvn -f ActivityMaster/core/pom.xml test -Dtest=FirstInstallSchemaMappingTest
python -B ActivityMaster/core/src/test/scripts/fsdm_schema_update_audit.py --transaction-unit-only
```

The 76 accepted length differences from the platform audit are separate and
unchanged. These checks do not run ZA import or administrator bootstrap; rerun
the finite platform installation gate with the refreshed artifact.
