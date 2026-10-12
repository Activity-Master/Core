"""Verify tracked script 26 against populated legacy and mapped security tables.

Run from core: python src/test/scripts/fsdm_rules_security_audit.py
Uses disposable PostgreSQL 17 without published ports or an application database.
"""
import pathlib
import shutil
import tempfile
import time
import uuid

from fsdm_schema_update_audit import CORE, SOURCE, CLASS, run


LEGACY = "rulesx" + "rulestypes" + "securitytoken"
MAPPED = "rulesx" + "rulestype" + "securitytoken"
SCRIPT = "26.rules-type-security-table.sql"


def main():
    container = "am-rules-security-audit-" + uuid.uuid4().hex[:12]
    with tempfile.TemporaryDirectory(prefix="fsdm-rules-security-") as directory:
        scratch = pathlib.Path(directory)
        run("javac", "-d", str(scratch), str(SOURCE))
        shutil.copytree(CORE / "src/main/resources/db", scratch / "db")
        updates = scratch / "updates.sql"
        run("java", "-cp", str(scratch), CLASS, "--updates", str(updates))
        complete = updates.read_text(encoding="utf-8")
        prefix, final = complete.rsplit("BEGIN;\n", 1)
        final = "BEGIN;\n" + final
        raw = (scratch / "db" / SCRIPT).read_text()
        run("docker", "run", "--rm", "-d", "--name", container,
            "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")

        def sql(statement, database="postgres", expected_error=None):
            return run("docker", "exec", "-i", container, "psql", "-X", "-qAt",
                       "-v", "ON_ERROR_STOP=1", "-U", "postgres", "-d", database,
                       input=statement, expected_error=expected_error)

        def snapshot(database):
            return sql("SELECT scriptname,checksum,appliedat FROM dbo.fsdmschemaupdate "
                       "ORDER BY scriptsequence", database)

        def seed(table):
            columns = [table + "id", "activeflagid", "enterpriseid", "securitytokenid",
                       "systemid", "rulesxrulestypeid"]
            return (f"INSERT INTO rules.{table} (" + ",".join(columns)
                    + ",createallowed,readallowed,updateallowed,deleteallowed) VALUES ("
                    + ",".join(["'11111111-1111-1111-1111-111111111111'"] * len(columns))
                    + ",1,1,0,0);")

        rename = (f"ALTER TABLE rules.{LEGACY} RENAME TO {MAPPED}; "
                  f"ALTER TABLE rules.{MAPPED} RENAME COLUMN {LEGACY}id TO {MAPPED}id;")
        try:
            for _ in range(60):
                try:
                    sql("SELECT 1")
                    break
                except RuntimeError:
                    time.sleep(0.5)
            else:
                raise RuntimeError("PostgreSQL did not start")

            sql(prefix)
            sql(seed(LEGACY) + f"CREATE ROLE security_reader; GRANT SELECT ON rules.{LEGACY} TO security_reader;")
            before = snapshot("postgres")
            oid = sql(f"SELECT 'rules.{LEGACY}'::regclass::oid")
            indexes = sql(f"SELECT string_agg(indexname,',' ORDER BY indexname) FROM pg_indexes "
                          f"WHERE schemaname='rules' AND tablename='{LEGACY}'")
            sql(final)
            assert sql(f"SELECT 'rules.{MAPPED}'::regclass::oid") == oid
            assert sql(f"SELECT string_agg(indexname,',' ORDER BY indexname) FROM pg_indexes "
                       f"WHERE schemaname='rules' AND tablename='{MAPPED}'") == indexes
            assert sql(f"SELECT has_table_privilege('security_reader','rules.{MAPPED}','SELECT')") == "t"
            assert sql(f"SELECT {MAPPED}id,readallowed,updateallowed FROM rules.{MAPPED}") == (
                "11111111-1111-1111-1111-111111111111|1|0")
            assert snapshot("postgres").startswith(before + "\n")
            recorded = snapshot("postgres")
            sql(complete)
            sql(raw)
            assert snapshot("postgres") == recorded
            print("PASS: populated legacy rename preserves rows, grants, indexes, table identity and history; replay skips", flush=True)

            # Reproduce an existing Hibernate-created table with no tracking history.
            sql("CREATE DATABASE mapped_existing")
            sql((scratch / "db/11.rules.sql").read_text() + rename + seed(MAPPED), "mapped_existing")
            oid = sql(f"SELECT 'rules.{MAPPED}'::regclass::oid", "mapped_existing")
            sql(complete, "mapped_existing")
            assert sql(f"SELECT 'rules.{MAPPED}'::regclass::oid", "mapped_existing") == oid
            assert sql(f"SELECT count(*) FROM rules.{MAPPED} WHERE readallowed=1 AND updateallowed=0", "mapped_existing") == "1"
            assert sql(f"SELECT to_regclass('rules.{LEGACY}') IS NULL", "mapped_existing") == "t"
            assert sql(f"SELECT count(*) FROM dbo.fsdmschemaupdate WHERE scriptname='{SCRIPT}'", "mapped_existing") == "1"
            print("PASS: populated mapped database without history upgrades through all scripts; empty legacy duplicate removed", flush=True)

            sql("CREATE DATABASE conflict")
            sql(prefix, "conflict")
            sql(seed(LEGACY) + f"CREATE TABLE rules.{MAPPED} (LIKE rules.{LEGACY} INCLUDING ALL); "
                f"ALTER TABLE rules.{MAPPED} RENAME COLUMN {LEGACY}id TO {MAPPED}id; " + seed(MAPPED), "conflict")
            before = snapshot("conflict")
            sql(final, "conflict", "legacy table contains rows")
            assert snapshot("conflict") == before
            for table in (LEGACY, MAPPED):
                assert sql(f"SELECT count(*) FROM rules.{table}", "conflict") == "1"
            # Explicit reconciliation in this fixture makes the same pending update retryable.
            sql(f"DELETE FROM rules.{LEGACY}", "conflict")
            sql(final, "conflict")
            assert sql(f"SELECT count(*) FROM dbo.fsdmschemaupdate WHERE scriptname='{SCRIPT}'", "conflict") == "1"
            print("PASS: populated dual tables stop without row/history loss; pending update retries after reconciliation", flush=True)

            sql(f"DROP TABLE rules.{MAPPED}", "conflict")
            sql(raw, "conflict", "security table is missing")
            print("PASS: missing tables still fail explicitly", flush=True)
        finally:
            run("docker", "stop", container)


if __name__ == "__main__":
    main()
