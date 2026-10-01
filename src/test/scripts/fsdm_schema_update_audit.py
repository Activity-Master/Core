"""Test the actual FsdmSchema-generated update SQL against isolated PostgreSQL 17.

Run from core: python src/test/scripts/fsdm_schema_update_audit.py
Requires Docker and a JDK on PATH. Publishes no ports; alters no application DB.
"""
import concurrent.futures
import hashlib
import pathlib
import re
import shutil
import subprocess
import tempfile
import time
import uuid

CORE = pathlib.Path(__file__).resolve().parents[3]
SOURCE = CORE / "src/main/java/com/guicedee/activitymaster/fsdm/db/FsdmSchema.java"
CLASS = "com.guicedee.activitymaster.fsdm.db.FsdmSchema"


def run(*command, input=None, expected_error=None):
    result = subprocess.run(command, input=input, encoding="utf-8", text=True, capture_output=True)
    if expected_error:
        assert result.returncode and expected_error in result.stderr, result.stderr[-4000:]
    elif result.returncode:
        raise RuntimeError(result.stderr[-6000:] or result.stdout[-6000:])
    return result.stdout.strip()


def main():
    name = "am-fsdm-update-audit-" + uuid.uuid4().hex[:12]
    with tempfile.TemporaryDirectory(prefix="fsdm-schema-updates-") as directory:
        scratch = pathlib.Path(directory)
        run("javac", "-d", str(scratch), str(SOURCE))
        shutil.copytree(CORE / "src/main/resources/db", scratch / "db")
        history = "00.0.schema-update-history.sql"
        scripts = [history] + re.findall(r'"([\w.\-]+\.sql)"', SOURCE.read_text())[1:]
        assert len(scripts) == len(set(scripts))
        for script in scripts:
            resource = (scratch / "db" / script).read_text()
            # Check every declaration, including dynamic CREATE INDEX strings.
            for declaration in re.finditer(r"CREATE\s+(?:UNIQUE\s+)?(?:TABLE|INDEX|SCHEMA)\b", resource, re.I):
                assert re.match(r"\s+IF\s+NOT\s+EXISTS\b", resource[declaration.end():], re.I), script
            for schema in re.finditer(r'CREATE SCHEMA IF NOT EXISTS ("?\w+"?);', resource):
                name = schema.group(1).strip('"')
                assert f"FROM pg_catalog.pg_namespace WHERE nspname = '{name}'" in resource[:schema.start()], script
            assert not re.search(r"\bREFERENCES\s+[\w\"]|\bFOREIGN\s+KEY\s*\(", resource, re.I), script
        assert (CORE / "docs/sql/structured-party-addresses.sql").read_text() == (scratch / "db/19.structured-party-addresses.sql").read_text()
        updates = scratch / "updates.sql"

        def bundle():
            run("java", "-cp", str(scratch), CLASS, "--updates", str(updates))
            return updates.read_text(encoding="utf-8")

        run("docker", "run", "--rm", "-d", "--name", name,
            "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")

        def sql(statement, database="postgres", expected_error=None):
            return run("docker", "exec", "-i", name, "psql", "-X", "-qAt",
                       "-v", "ON_ERROR_STOP=1", "-U", "postgres", "-d", database,
                       input=statement, expected_error=expected_error)

        def history_snapshot(database="postgres"):
            return sql("SELECT scriptname,scriptsequence,checksum,appliedat,executionmode "
                       "FROM dbo.fsdmschemaupdate ORDER BY scriptsequence", database)

        try:
            for _ in range(60):
                try:
                    sql("SELECT 1")
                    break
                except RuntimeError:
                    time.sleep(0.5)
            else:
                raise RuntimeError("PostgreSQL did not start")
            complete = bundle()
            assert "FROM pg_catalog.pg_namespace WHERE nspname = 'dbo'" in complete
            assert "FROM pg_catalog.pg_namespace WHERE nspname = 'time'" in complete
            sql(complete)
            assert int(sql("SELECT count(*) FROM dbo.fsdmschemaupdate")) == len(scripts)
            assert sql("SELECT scriptname FROM dbo.fsdmschemaupdate ORDER BY scriptsequence DESC LIMIT 1") == scripts[-1]
            assert sql("SELECT count(*) FROM pg_constraint WHERE contype='f'") == "0"
            before = history_snapshot()
            recorded = dict(row.split("|") for row in sql("SELECT scriptname,trim(checksum) FROM dbo.fsdmschemaupdate").splitlines())
            for script in scripts:
                raw = (scratch / "db" / script).read_text(encoding="utf-8").replace("\r\n", "\n").replace("\r", "\n")
                assert recorded[script] == hashlib.sha256(raw.encode("utf-8")).hexdigest(), script
            sql(complete)
            assert history_snapshot() == before
            print(f"PASS: fresh install, {len(scripts)} tracked scripts, no foreign keys, repeat run unchanged", flush=True)

            # Even CREATE SCHEMA IF NOT EXISTS checks database CREATE permission.
            # A runtime role with history access must skip bootstrap DDL entirely.
            sql("CREATE ROLE fsdm_schema_reader; REVOKE CREATE ON DATABASE postgres FROM PUBLIC; "
                "GRANT USAGE ON SCHEMA dbo TO fsdm_schema_reader; "
                "GRANT SELECT ON dbo.fsdmschemaupdate TO fsdm_schema_reader;")
            sql("SET ROLE fsdm_schema_reader; CREATE SCHEMA IF NOT EXISTS dbo;", expected_error="permission denied")
            sql("SET ROLE fsdm_schema_reader; " + complete)
            assert history_snapshot() == before
            print("PASS: existing history skips bootstrap DDL without database/schema CREATE privileges", flush=True)

            # A schema owner can create its tables while lacking database CREATE.
            # Apply an actual pending enterprise script with dbo already present.
            sql("CREATE DATABASE guarded_enterprise")
            sql("CREATE SCHEMA dbo AUTHORIZATION fsdm_schema_reader; "
                "REVOKE CREATE ON DATABASE guarded_enterprise FROM PUBLIC;", "guarded_enterprise")
            assert sql("SELECT to_regclass('dbo.enterprise') IS NULL", "guarded_enterprise") == "t"
            sql("SET ROLE fsdm_schema_reader; " + (scratch / "db/01.enterprise.sql").read_text(), "guarded_enterprise")
            assert sql("SELECT to_regclass('dbo.enterprise') IS NOT NULL AND "
                       "(SELECT count(*) FROM pg_indexes WHERE schemaname='dbo') > 0", "guarded_enterprise") == "t"
            print("PASS: raw SQL skips only existing CREATE SCHEMA; following tables and indexes still created", flush=True)
            baseline = scratch / "guarded-baseline.sql"
            run("java", "-cp", str(scratch), CLASS, "--baseline", "00.1.setup.sql", str(baseline))
            sql(baseline.read_text(), "guarded_enterprise")
            sql("ALTER SCHEMA dbo OWNER TO fsdm_schema_reader; "
                "DO $$DECLARE t record; BEGIN FOR t IN SELECT tablename FROM pg_tables WHERE schemaname='dbo' LOOP "
                "EXECUTE format('ALTER TABLE dbo.%I OWNER TO fsdm_schema_reader',t.tablename); END LOOP; END $$; "
                "REVOKE CREATE ON DATABASE guarded_enterprise FROM PUBLIC;", "guarded_enterprise")
            enterprise_update = "BEGIN;\n" + complete.split("BEGIN;\n")[scripts.index("01.enterprise.sql") + 1]
            sql("SET ROLE fsdm_schema_reader; " + enterprise_update, "guarded_enterprise")
            assert sql("SELECT scriptname FROM dbo.fsdmschemaupdate ORDER BY scriptsequence DESC LIMIT 1", "guarded_enterprise") == "01.enterprise.sql"
            print("PASS: pending script skips existing schema creation without database CREATE privileges", flush=True)

            # With no update history, existing business tables do not block files.
            sql("CREATE DATABASE untracked_existing")
            sql((scratch / "db/01.enterprise.sql").read_text(), "untracked_existing")
            sql("INSERT INTO dbo.enterprise(enterpriseid,enterprisedesc,enterprisename) "
                "VALUES ('11111111-1111-1111-1111-111111111111','existing fixture','existing fixture')", "untracked_existing")
            sql(complete, "untracked_existing")
            assert int(sql("SELECT count(*) FROM dbo.fsdmschemaupdate", "untracked_existing")) == len(scripts)
            assert sql("SELECT count(*) FROM dbo.enterprise WHERE enterpriseid='11111111-1111-1111-1111-111111111111'", "untracked_existing") == "1"
            print("PASS: existing schema/tables without history still execute all pending files and preserve data", flush=True)

            # Raw resources must also replay, independently of the history guard.
            sql("INSERT INTO dbo.enterprise(enterpriseid,enterprisedesc,enterprisename) "
                "VALUES ('11111111-1111-1111-1111-111111111111','replay fixture','replay fixture')")
            tables_and_indexes = sql("SELECT (SELECT count(*) FROM pg_tables WHERE schemaname NOT IN ('pg_catalog','information_schema'))," 
                                    "(SELECT count(*) FROM pg_indexes WHERE schemaname NOT IN ('pg_catalog','information_schema'))")
            for script in scripts:
                sql((scratch / "db" / script).read_text())
            assert history_snapshot() == before
            assert sql("SELECT count(*) FROM dbo.enterprise WHERE enterpriseid='11111111-1111-1111-1111-111111111111'") == "1"
            assert sql("SELECT (SELECT count(*) FROM pg_tables WHERE schemaname NOT IN ('pg_catalog','information_schema'))," 
                       "(SELECT count(*) FROM pg_indexes WHERE schemaname NOT IN ('pg_catalog','information_schema'))") == tables_and_indexes
            print("PASS: every raw schema script replays without duplicate tables/indexes or data loss", flush=True)

            # A recorded checksum cannot silently change, even if the SQL is harmless.
            last = scratch / "db" / scripts[-1]
            original = last.read_text()
            last.write_text(original + "\nSELECT 1;\n", encoding="utf-8")
            sql(bundle(), expected_error="changed after application")
            assert history_snapshot() == before
            last.write_text(original, encoding="utf-8")
            complete = bundle()
            print("PASS: checksum drift stops execution and preserves history", flush=True)

            sql("UPDATE dbo.fsdmschemaupdate SET scriptsequence=100 WHERE scriptname='22.relationship-indexes.sql'")
            sql(complete, expected_error="update sequence changed")
            sql(f"UPDATE dbo.fsdmschemaupdate SET scriptsequence={scripts.index('22.relationship-indexes.sql')} WHERE scriptname='22.relationship-indexes.sql'")
            assert history_snapshot() == before
            print("PASS: recorded script sequence cannot silently change", flush=True)

            # Script 09 must preserve real legacy payloads through migration and replay.
            sql("CREATE DATABASE legacy_payload")
            sql("CREATE SCHEMA resource; CREATE TABLE resource.resourceitemdata "
                "(resourceitemdataid uuid PRIMARY KEY, resourceitemid uuid NOT NULL, "
                "resourceitemdata bytea NOT NULL, effectivefromdate timestamptz DEFAULT now(), "
                "effectivetodate timestamptz DEFAULT now(), warehousecreatedtimestamp timestamptz DEFAULT now(), "
                "warehouselastupdatedtimestamp timestamptz DEFAULT now(), warehousefromdate date DEFAULT current_date, "
                "enterpriseid uuid, activeflagid uuid, systemid uuid); "
                "INSERT INTO resource.resourceitemdata(resourceitemdataid,resourceitemid,resourceitemdata) VALUES "
                "('11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222222',decode('010203ff','hex'));", "legacy_payload")
            payload_sql = (scratch / "db/09.resourceitem.sql").read_text()
            for _ in range(2):
                sql(payload_sql, "legacy_payload")
                assert sql("SELECT encode(v.resourceitemdatavalue,'hex') FROM resource.resourceitemdata d "
                           "JOIN resource.resourceitemdatavalue v ON v.resourceitemdatavalueid=d.resourceitemdatavalueid", "legacy_payload") == "010203ff"
            print("PASS: legacy payload migration and raw replay preserve bytes and links", flush=True)

            # Execute only the last update on an empty DB: predecessor must be present.
            sql("CREATE DATABASE out_of_order")
            final_update = complete.split("BEGIN;\n")[-1]
            sql("BEGIN;\n" + final_update, "out_of_order", "Previous FSDM update must be applied")
            assert sql("SELECT to_regclass('dbo.fsdmschemaupdate') IS NULL", "out_of_order") == "t"
            print("PASS: out-of-order update rolls back its bootstrap", flush=True)

            # Inject a transactional failure into a real update, then resume with
            # the unchanged actual resource. No manual history repair is required.
            sql("CREATE DATABASE failed_update")
            failure_script = "19.structured-party-addresses.sql"
            failing = scratch / "db" / failure_script
            original = failing.read_text()
            failing.write_text(original + "\nCREATE TABLE dbo.must_rollback(id int); SELECT 1/0;\n", encoding="utf-8")
            sql(bundle(), "failed_update", "division by zero")
            assert int(sql("SELECT count(*) FROM dbo.fsdmschemaupdate", "failed_update")) == scripts.index(failure_script)
            assert sql("SELECT to_regclass('dbo.must_rollback') IS NULL AND to_regclass('address.addresstype') IS NULL", "failed_update") == "t"
            failing.write_text(original, encoding="utf-8")
            complete = bundle()
            sql(complete, "failed_update")
            assert int(sql("SELECT count(*) FROM dbo.fsdmschemaupdate", "failed_update")) == len(scripts)
            print("PASS: failed update rolls back both DDL and history; retry resumes", flush=True)

            # Simultaneous independent updaters must serialize and apply once.
            sql("CREATE DATABASE concurrent_update")
            with concurrent.futures.ThreadPoolExecutor(max_workers=2) as pool:
                results = [pool.submit(sql, complete, "concurrent_update") for _ in range(2)]
                for result in results:
                    result.result()
            assert int(sql("SELECT count(*) FROM dbo.fsdmschemaupdate", "concurrent_update")) == len(scripts)
            print("PASS: concurrent updaters apply each script once", flush=True)

            # Adopt an old, untracked schema. Simulate previously generated FKs,
            # including a missing join index and an unrelated external FK.
            sql("CREATE DATABASE legacy_update")
            correction = scripts.index("22.relationship-indexes.sql")
            for script in scripts[1:correction]:
                sql((scratch / "db" / script).read_text(), "legacy_update")
            sql("ALTER TABLE address.addressxaddress ADD CONSTRAINT old_component_fk FOREIGN KEY(componentaddressid) REFERENCES address.address(addressid); "
                "ALTER TABLE address.addressxaddress ADD CONSTRAINT old_classification_fk FOREIGN KEY(classificationid) REFERENCES classification.classification(classificationid); "
                "ALTER TABLE transactions.entry ADD CONSTRAINT old_entry_type_fk FOREIGN KEY(transaction_type_id,direction) REFERENCES transactions.transaction_type(transaction_type_id,direction); "
                "CREATE SCHEMA outside_fsdm; CREATE TABLE outside_fsdm.parent(id int PRIMARY KEY); "
                "CREATE TABLE outside_fsdm.child(id int REFERENCES outside_fsdm.parent(id));", "legacy_update")
            baseline = scratch / "baseline.sql"
            run("java", "-cp", str(scratch), CLASS, "--baseline", scripts[correction - 1], str(baseline))
            sql(baseline.read_text(), "legacy_update")
            sql(complete, "legacy_update")
            assert sql("SELECT executionmode FROM dbo.fsdmschemaupdate ORDER BY scriptsequence DESC LIMIT 1", "legacy_update") == "applied"
            assert int(sql("SELECT count(*) FROM dbo.fsdmschemaupdate WHERE executionmode='baseline'", "legacy_update")) == correction
            assert sql("SELECT count(*) FROM pg_constraint c JOIN pg_namespace n ON n.oid=c.connamespace WHERE c.contype='f' AND n.nspname<>'outside_fsdm'", "legacy_update") == "0"
            assert sql("SELECT count(*) FROM pg_constraint c JOIN pg_namespace n ON n.oid=c.connamespace WHERE c.contype='f' AND n.nspname='outside_fsdm'", "legacy_update") == "1"
            assert sql("SELECT count(*) FROM pg_indexes WHERE schemaname='address' AND tablename='addressxaddress' AND indexdef LIKE '%(classificationid)%'", "legacy_update") == "1"
            sql((scratch / "db" / scripts[correction]).read_text(), "legacy_update")
            sql(baseline.read_text(), "legacy_update", "history already exists")
            print("PASS: explicit legacy baseline, indexed FK removal, external FK preserved, corrective update replay", flush=True)
        finally:
            run("docker", "stop", name)


if __name__ == "__main__":
    main()
