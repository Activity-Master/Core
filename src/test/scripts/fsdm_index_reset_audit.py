"""Check setup index removal and the tracked upgrade on an isolated PostgreSQL 17.

Run from core: python src/test/scripts/fsdm_index_reset_audit.py
Requires Docker and a JDK. Publishes no ports and never accesses an application DB.
"""
import pathlib
import shutil
import tempfile
import time
import uuid

from fsdm_schema_update_audit import CORE, SOURCE, CLASS, run


def main():
    name = "am-index-reset-audit-" + uuid.uuid4().hex[:12]
    with tempfile.TemporaryDirectory(prefix="fsdm-index-reset-") as directory:
        scratch = pathlib.Path(directory)
        run("javac", "-d", str(scratch), str(SOURCE))
        shutil.copytree(CORE / "src/main/resources/db", scratch / "db")
        updates = scratch / "updates.sql"

        def bundle():
            run("java", "-cp", str(scratch), CLASS, "--updates", str(updates))
            return updates.read_text(encoding="utf-8")

        run("docker", "run", "--rm", "-d", "--name", name,
            "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")

        def sql(statement, expected_error=None):
            return run("docker", "exec", "-i", name, "psql", "-X", "-qAt",
                       "-v", "ON_ERROR_STOP=1", "-U", "postgres",
                       input=statement, expected_error=expected_error)

        def indexes():
            return sql("SELECT n.nspname,c.relname,pg_get_indexdef(i.indexrelid) "
                       "FROM pg_index i JOIN pg_class c ON c.oid=i.indexrelid "
                       "JOIN pg_namespace n ON n.oid=c.relnamespace "
                       "WHERE n.nspname !~ '^pg_' AND n.nspname <> 'information_schema' "
                       "ORDER BY n.nspname,c.relname")

        def history():
            return sql("SELECT scriptname,scriptsequence,checksum,appliedat,executionmode "
                       "FROM dbo.fsdmschemaupdate ORDER BY scriptsequence")

        try:
            for _ in range(60):
                try:
                    sql("SELECT 1")
                    break
                except RuntimeError:
                    time.sleep(0.5)
            else:
                raise RuntimeError("PostgreSQL did not start")
            sql('''CREATE SCHEMA "Index Audit";
                CREATE TABLE "Index Audit"."Odd Table" (
                    id integer PRIMARY KEY, label text UNIQUE,
                    area circle, EXCLUDE USING gist (area WITH &&));
                INSERT INTO "Index Audit"."Odd Table" VALUES (1,'kept',circle(point(0,0),1));
                CREATE INDEX "odd index" ON "Index Audit"."Odd Table" (label);
                CREATE TABLE "Index Audit".parent(id integer);
                CREATE UNIQUE INDEX referenced_unique ON "Index Audit".parent(id);
                CREATE TABLE "Index Audit".child(id integer REFERENCES "Index Audit".parent(id));
                CREATE TABLE "Index Audit".partitioned(id integer PRIMARY KEY, value integer)
                    PARTITION BY RANGE (id);
                CREATE TABLE "Index Audit".part_one PARTITION OF "Index Audit".partitioned
                    FOR VALUES FROM (0) TO (100);
                CREATE INDEX partitioned_value ON "Index Audit".partitioned(value);''')
            catalog_count = sql("SELECT count(*) FROM pg_indexes WHERE schemaname='pg_catalog'")
            constraint_indexes = sql("SELECT conname,conindid FROM pg_constraint "
                                     "WHERE connamespace='\"Index Audit\"'::regnamespace ORDER BY conname")
            complete = bundle()
            sql(complete)
            assert sql("SELECT to_regclass('\"Index Audit\".\"odd index\"') IS NULL "
                       "AND to_regclass('\"Index Audit\".partitioned_value') IS NULL") == "t"
            assert sql("SELECT count(*) FROM pg_indexes WHERE schemaname='pg_catalog'") == catalog_count
            assert sql("SELECT conname,conindid FROM pg_constraint "
                       "WHERE connamespace='\"Index Audit\"'::regnamespace ORDER BY conname") == constraint_indexes
            assert sql('SELECT label FROM "Index Audit"."Odd Table" WHERE id=1') == "kept"
            expected_indexes = indexes()
            print("PASS: setup removes quoted/partitioned ordinary indexes; constraints, FK-backed unique indexes, rows and catalogs survive", flush=True)

            # Reproduce an installed database with the exact original setup checksum.
            sql("DELETE FROM dbo.fsdmschemaupdate WHERE scriptname='26.reset-query-indexes.sql'; "
                "UPDATE dbo.fsdmschemaupdate SET checksum="
                "'121fc5eaa313dbd1e062dd11f1445e665a1b90540efb0a8e8223f1f539248e84' "
                "WHERE scriptname='00.1.setup.sql'; "
                "DROP INDEX dbo.idx_ent_eff_from; "
                "CREATE INDEX idx_ent_eff_from ON dbo.enterprise (enterpriseid); "
                "CREATE INDEX stale_query_index ON dbo.enterprise (enterprisename)")
            installed_history = history()
            old_indexes = indexes()
            migration = scratch / "db/26.reset-query-indexes.sql"
            original = migration.read_text(encoding="utf-8")
            migration.write_text(original + "\nSELECT 1/0;\n", encoding="utf-8")
            sql(bundle(), expected_error="division by zero")
            assert indexes() == old_indexes
            assert history() == installed_history
            print("PASS: failed rebuild restores every previous index and leaves history unchanged", flush=True)

            migration.write_text(original, encoding="utf-8")
            complete = bundle()
            sql(complete)
            assert indexes() == expected_indexes
            assert sql("SELECT checksum FROM dbo.fsdmschemaupdate WHERE scriptname='00.1.setup.sql'") == \
                "121fc5eaa313dbd1e062dd11f1445e665a1b90540efb0a8e8223f1f539248e84"
            assert sql("SELECT scriptname FROM dbo.fsdmschemaupdate ORDER BY scriptsequence DESC LIMIT 1") == "26.reset-query-indexes.sql"
            print("PASS: original setup history accepted; tracked upgrade removes stale indexes and restores all canonical definitions", flush=True)

            sql("CREATE INDEX keep_after_upgrade ON dbo.enterprise (enterprisename)")
            after_upgrade = indexes()
            after_history = history()
            sql(complete)
            assert indexes() == after_upgrade and history() == after_history
            print("PASS: repeated startup does not reset indexes again", flush=True)

            setup = scratch / "db/00.1.setup.sql"
            setup.write_text(setup.read_text(encoding="utf-8") + "\n-- unapproved change\n", encoding="utf-8")
            sql(bundle(), expected_error="changed after application")
            print("PASS: further setup edits still fail checksum validation", flush=True)
        finally:
            run("docker", "stop", name)


if __name__ == "__main__":
    main()
