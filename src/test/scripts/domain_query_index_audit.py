"""Validate shared FSDM indexes on an isolated PostgreSQL 17 container.

Run from core: python src/test/scripts/domain_query_index_audit.py
No application database is accessed and no ports are published. Fixtures measure
predicate families, not service authorization or production workload latency.
"""
import argparse
import json
import pathlib
import re
import statistics
import time
import uuid

from uwe_query_index_audit import CORE, DB, ZERO, literal, run, uid

ADDITION = "24.domain-query-indexes.sql"


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=pathlib.Path, default=CORE / "target/domain-query-index-audit.json")
    args = parser.parse_args()
    migration = (DB / ADDITION).read_text(encoding="utf-8")
    declarations = re.findall(r"CREATE INDEX IF NOT EXISTS (\w+)\s+ON (\w+\.\w+)\s*\((.*?)\);", migration, re.S)
    assert len(declarations) == migration.count("CREATE INDEX")
    assert len({name for name, _, _ in declarations}) == len(declarations)
    assert all(len(name) <= 63 for name, _, _ in declarations)
    scripts = ["00.0.schema-update-history.sql"] + re.findall(
        r'"([\w.\-]+\.sql)"', (CORE / "src/main/java/com/guicedee/activitymaster/fsdm/db/FsdmSchema.java").read_text())[1:]
    # Preserve this audit's before/after boundary when later migrations are added.
    scripts = scripts[:scripts.index(ADDITION) + 1]
    name = "am-domain-index-audit-" + uuid.uuid4().hex[:12]
    run("docker", "run", "--rm", "-d", "--name", name,
        "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")

    def sql(statement):
        return run("docker", "exec", "-i", name, "psql", "-X", "-qAt",
                   "-v", "ON_ERROR_STOP=1", "-U", "postgres", input=statement)

    def fingerprint(query):
        return sql("SELECT count(*),md5(coalesce(string_agg(row::text,E'\\n' ORDER BY row::text),'')) "
                   "FROM (" + query + ") row")

    def measure(query):
        remaining = sql(
            "SET statement_timeout='60s'; " +
            ("EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON) " + query + ";\n") * 5)
        plans = []
        decoder = json.JSONDecoder()
        while remaining.strip():
            remaining = remaining.lstrip()
            value, end = decoder.raw_decode(remaining)
            plans.append(value[0])
            remaining = remaining[end:]
        assert len(plans) == 5

        def indexes(node):
            return ([node["Index Name"]] if "Index Name" in node else []) + [
                item for child in node.get("Plans", []) for item in indexes(child)]

        return {"execution_ms": round(statistics.median(p["Execution Time"] for p in plans), 3),
                "planning_ms": round(statistics.median(p["Planning Time"] for p in plans), 3),
                "indexes": sorted(set(indexes(plans[-1]["Plan"]))), "plan": plans[-1]}

    try:
        for _ in range(60):
            try:
                sql("SELECT 1")
                break
            except RuntimeError:
                time.sleep(0.5)
        else:
            raise RuntimeError("PostgreSQL did not start")
        for script in scripts[:-1]:
            sql((DB / script).read_text(encoding="utf-8"))

        catalog = json.loads(sql("SELECT json_agg(json_build_object('table',n.nspname||'.'||t.relname,"
                                 "'keys',pg_get_indexdef(i.indexrelid),'name',x.relname)) "
                                 "FROM pg_index i JOIN pg_class t ON t.oid=i.indrelid "
                                 "JOIN pg_namespace n ON n.oid=t.relnamespace "
                                 "JOIN pg_class x ON x.oid=i.indexrelid WHERE i.indisvalid"))
        queries = {}
        for table in sorted({t for _, t, _ in declarations}):
            schema, short = table.split('.')
            columns = json.loads(sql("SELECT json_agg(json_build_object('name',column_name,'type',data_type,"
                                     "'required',is_nullable='NO' AND column_default IS NULL) ORDER BY ordinal_position) "
                                     "FROM information_schema.columns WHERE table_schema=" + literal(schema) +
                                     " AND table_name=" + literal(short)))
            pk = sql("SELECT a.attname FROM pg_index i JOIN pg_attribute a "
                     "ON a.attrelid=i.indrelid AND a.attnum=i.indkey[0] "
                     "WHERE i.indisprimary AND i.indrelid=" + literal(table) + "::regclass")
            values = {}
            defaults = {"uuid": literal(ZERO) + "::uuid", "integer": "0", "smallint": "0",
                        "boolean": "false", "timestamp with time zone": "now()", "date": "current_date"}
            related = [d for d in declarations if d[1] == table]
            is_relation = any(n.endswith(("_pair_to", "_contents_to")) for n, _, _ in related)
            is_taxonomy = any(n.endswith("_description") for n, _, _ in related)
            count = 5000 if is_taxonomy else 20000
            for c in columns:
                col, typ = c["name"], c["type"]
                if c["required"]:
                    values[col] = defaults.get(typ, "'fixture'")
                if col == pk:
                    values[col] = "md5(" + literal(table + ':') + "||i::text)::uuid"
                elif typ == "uuid" and col not in ["enterpriseid", "enterprise_id", "systemid", "activeflagid",
                                                     "originalsourcesystemuniqueid", "originalsourcesystemid"]:
                    values[col] = uid("(i-1)/200+1")
                if col == "classificationid":
                    values[col] = uid("i%20+1")
                if col == "value":
                    values[col] = "'disc'||((i/20)%200)::text"
                if col == "effectivefromdate":
                    values[col] = "'2020-01-01'::timestamptz + i*interval '1 microsecond'"
                if col == "effectivetodate":
                    condition = "i%200>190 OR i%200=0" if is_relation else "i%2=1"
                    values[col] = "CASE WHEN " + condition + " THEN '2999-12-31' ELSE '2021-01-01' END::timestamptz"
            # Override keys to produce selective candidates and deep relationship history.
            for index, _, expression in related:
                keys = expression.split(")", 1)[0].split(',')
                keys = [k.strip() for k in keys]
                if index.endswith("_description"):
                    values[keys[0]] = "'description-'||i"
                    queries[index] = "SELECT " + pk + " FROM " + table + " WHERE " + keys[0] + "='description-1'"
                elif index.endswith("_ent_name_to"):
                    values[keys[1]] = "'name-'||i"
                    queries[index] = "SELECT " + pk + " FROM " + table + " WHERE enterpriseid=" + literal(ZERO) + \
                        " AND " + keys[1] + "='name-1' AND effectivetodate>=now()"
                elif index.endswith("_class_value"):
                    queries[index] = "SELECT " + keys[2] + " FROM " + table + " WHERE classificationid=" + uid("1") + " AND value='disc0'"
                elif index.endswith("_owner_class"):
                    owner_value = literal(ZERO) if keys[0] in ["enterpriseid", "systemid", "activeflagid"] else uid("1")
                    queries[index] = "SELECT classificationid,value FROM " + table + " WHERE " + keys[0] + "=" + owner_value
                elif index.endswith("_type_value_to"):
                    values[keys[1]] = uid("(i-1)/200%20+1")
                    if table.startswith("transactions."):
                        values["entry_id"] = uid("i")
                    queries[index] = "SELECT value FROM " + table + " WHERE " + keys[0] + "=" + literal(ZERO) + \
                        " AND " + keys[1] + "=" + uid("1") + " AND value='disc0' AND effectivetodate>=now() AND effectivefromdate<=now()"
                elif index.endswith("_pair_to"):
                    owner, target, ent = keys[:3]
                    values[owner], values[target] = uid("(i-1)/200+1"), uid("(i-1)%10+1")
                    projected = re.search(r"INCLUDE \((.*)$", expression, re.S)[1].split(',')
                    queries[index] = "SELECT " + ','.join(projected) + " FROM " + table + " WHERE " + owner + "=" + uid("1") + \
                        " AND " + target + "=" + uid("1") + " AND " + ent + "=" + literal(ZERO) + \
                        " AND systemid=" + literal(ZERO) + " AND effectivetodate>=now() AND effectivefromdate<=now()"
                elif index.endswith("_contents_to"):
                    ent, _, owner, _, target = keys
                    values[owner], values[target] = uid("(i-1)/200+1"), uid("(i-1)%10+1")
                    queries[index] = "SELECT " + target + " FROM " + table + " WHERE " + ent + "=" + literal(ZERO) + \
                        " AND systemid=" + literal(ZERO) + " AND " + owner + "=" + uid("1") + \
                        " AND effectivetodate>=now() AND effectivefromdate<=now()"
            # Trigger on transaction type links validates canonical entries; this fixture
            # exercises index traversal independently of domain mutation semantics.
            sql("SET session_replication_role=replica; INSERT INTO " + table + " (" + ','.join(values) +
                ") SELECT " + ','.join(values.values()) + " FROM generate_series(1," + str(count) + ") i;")
            print("Seeded " + table, flush=True)

        sql("UPDATE address.address SET value=CASE WHEN value='disc0' THEN "
            "'amenc:2:key:'||md5('0')||md5('0')||':payload' ELSE value END; VACUUM ANALYZE;")
        queries["address_encrypted_or"] = "SELECT addressid FROM address.address WHERE enterpriseid=" + literal(ZERO) + \
            " AND (value='disc0' OR value='legacy0' OR coalesce(regexp_substr(value," + \
            "'^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{64}:'),'')='amenc:2:key:'||md5('0')||md5('0')||':')"
        result = {"server": sql("SELECT version()"), "new_indexes": len(declarations),
                  "tables": len({t for _, t, _ in declarations}), "queries": {}, "baseline_indexes": catalog}
        for label, query in queries.items():
            result["queries"][label] = {"sql": query, "fingerprint": fingerprint(query), "before": measure(query)}
            assert int(result["queries"][label]["fingerprint"].split('|')[0]) > 0, label
        print("Measured baseline for " + str(len(queries)) + " predicate shapes", flush=True)
        sql(migration)
        total = sql("SELECT count(*) FROM pg_index")
        sql(migration)
        assert total == sql("SELECT count(*) FROM pg_index"), "Replay created extra indexes"
        invalid = sql("SELECT count(*) FROM pg_index WHERE NOT indisvalid OR NOT indisready")
        assert invalid == "0", invalid
        for label, query in queries.items():
            entry = result["queries"][label]
            assert fingerprint(query) == entry["fingerprint"], label
            entry["after"] = measure(query)
        selected = {i for entry in result["queries"].values() for i in entry["after"]["indexes"]}
        result["unselected_indexes"] = sorted({n for n, _, _ in declarations} - selected)

        # Exercise every definition on partitioned parents, including inherited indexes
        # for partitions present before installation and partitions attached afterwards.
        sql("CREATE SCHEMA index_partition_audit")
        for number, table in enumerate(sorted({t for _, t, _ in declarations})):
            parent = "index_partition_audit.p" + str(number)
            sql("CREATE TABLE " + parent + " (LIKE " + table + " INCLUDING DEFAULTS) PARTITION BY HASH (systemid); "
                "CREATE TABLE " + parent + "_a PARTITION OF " + parent + " FOR VALUES WITH (MODULUS 2,REMAINDER 0)")
            for index, source_table, _ in declarations:
                if source_table != table:
                    continue
                statement = re.search(r"CREATE INDEX IF NOT EXISTS " + index + r"\b.*?;", migration, re.S)[0]
                statement = statement.replace(index, "pa_" + str(number) + "_" + index[-40:]).replace("ON " + table, "ON " + parent)
                sql(statement)
            sql("CREATE TABLE " + parent + "_b PARTITION OF " + parent + " FOR VALUES WITH (MODULUS 2,REMAINDER 1)")
        assert sql("SELECT count(*) FROM pg_index WHERE NOT indisvalid OR NOT indisready") == "0"
        assert int(sql("SELECT count(*) FROM pg_index i JOIN pg_class c ON c.oid=i.indexrelid "
                       "JOIN pg_namespace n ON n.oid=c.relnamespace WHERE n.nspname='index_partition_audit'")) == 3 * len(declarations)
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(result, indent=2), encoding="utf-8")
        print("PASS: all queries preserve results; replay and partition indexes valid. Report: " + str(args.output), flush=True)
        print("Indexes not selected by fixture: " + str(result["unselected_indexes"]), flush=True)
    finally:
        run("docker", "stop", name)


if __name__ == "__main__":
    main()
