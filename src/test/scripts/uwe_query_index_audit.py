"""Exercise UWE query indexes on an isolated PostgreSQL 17 container.

Run: python src/test/scripts/uwe_query_index_audit.py --uwe C:/Java/UWE
Requires Python 3 and Docker; publishes no ports and removes only its own container.
Fixtures deliberately bypass FK triggers while loading unrelated warehouse domains;
this checks SQL/plans/result stability, not ActivityMaster authorization or lifecycle.
"""
import argparse
import hashlib
import json
import pathlib
import re
import statistics
import subprocess
import time
import uuid

CORE = pathlib.Path(__file__).resolve().parents[3]
DB = CORE / "src/main/resources/db"
ZERO = "00000000-0000-0000-0000-000000000000"


def uid(number):
    return f"md5(({number})::text)::uuid"


def literal(value):
    return "'" + str(value).replace("'", "''") + "'"


def run(*args, input=None):
    result = subprocess.run(args, input=input, text=True, encoding="utf-8", capture_output=True)
    if result.returncode:
        raise RuntimeError(result.stderr[-6000:] or result.stdout[-6000:])
    return result.stdout.strip()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--uwe", required=True, type=pathlib.Path)
    parser.add_argument("--output", type=pathlib.Path)
    args = parser.parse_args()
    name = "am-uwe-index-audit-" + uuid.uuid4().hex[:12]
    run("docker", "run", "--rm", "-d", "--name", name,
        "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")

    def sql(statement, database="postgres"):
        return run("docker", "exec", "-i", name, "psql", "-X", "-qAt",
                   "-v", "ON_ERROR_STOP=1", "-U", "postgres", "-d", database, input=statement)

    # Fill required metadata uniformly; only the query's data columns vary.
    def insert(table, expressions, source):
        columns = json.loads(sql("SELECT json_agg(json_build_object('name',column_name,'type',data_type)) "
                                 "FROM information_schema.columns WHERE table_schema=" + literal(table.split('.')[0]) +
                                 " AND table_name=" + literal(table.split('.')[1]) +
                                 " AND is_nullable='NO' AND column_default IS NULL"))
        defaults = {"uuid": literal(ZERO) + "::uuid", "integer": "0",
                    "boolean": "false", "timestamp with time zone": "now()",
                    "date": "current_date", "bytea": "''::bytea"}
        values = {c["name"]: defaults.get(c["type"], "'fixture'") for c in columns}
        values.update(expressions)
        sql("SET session_replication_role=replica; INSERT INTO " + table + " (" +
            ",".join(values) + ") SELECT " + ",".join(values.values()) + " FROM " + source)

    def java_sql(relative, variable):
        text = (args.uwe / relative).read_text(encoding="utf-8")
        match = re.search(r'\b' + variable + r'\s*=\s*"""(.*?)"""', text, re.S)
        if not match:
            raise AssertionError(f"Cannot extract {variable} from {relative}")
        return match[1].strip()

    def bind(query):
        values = {"sessionId": literal(str(uuid.UUID(hex=hashlib.md5(b'1').hexdigest()))),
                  "farmId": literal(str(uuid.UUID(hex=hashlib.md5(b'1').hexdigest()))),
                  "stationNumber": "'1'", "fromDate": "'2026-01-01'::timestamptz",
                  "toDate": "'2026-01-02'::timestamptz"}
        return re.sub(r"(?<!:):(sessionId|farmId|stationNumber|fromDate|toDate)\b", lambda m: values[m[1]], query)

    def fingerprint(query):
        return sql("SELECT count(*),md5(coalesce(string_agg(row::text, E'\\n' ORDER BY row::text),'')) "
                   "FROM (" + query + ") row")

    def measure(query):
        plans = [json.loads(sql("SET statement_timeout='60s'; EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON) " + query))[0]
                 for _ in range(5)]
        def indexes(node):
            found = [node["Index Name"]] if "Index Name" in node else []
            return found + [item for child in node.get("Plans", []) for item in indexes(child)]
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
            raise RuntimeError("Isolated PostgreSQL did not start")
        schema_java = (CORE / "src/main/java/com/guicedee/activitymaster/fsdm/db/FsdmSchema.java").read_text()
        scripts = re.findall(r'"([\w.\-]+\.sql)"', schema_java)
        addition = "21.uwe-query-indexes.sql"
        for script in scripts:
            if script != addition:
                sql((DB / script).read_text(encoding="utf-8"))
        print(f"Applied {len(scripts) - (addition in scripts)} baseline schema scripts", flush=True)

        descriptions = ["MeasurementUnitIsWaste", "MeasurementUnitIsBarcode", "MeasurementUnitKgs",
                        "MeasurementUnitCount", "MeasurementUnitPackInstructionId", "MeasurementUnitStationId",
                        "MeasurementUnitSessionId", "MeasurementUnitGeneratedByString", "MeasurementUnitCreatedDate"]
        names = "ARRAY[" + ",".join(map(literal, descriptions)) + "]"
        insert("classification.classification", {"classificationid": uid("i"), "classificationsequencenumber": "i",
               "classificationname": f"CASE WHEN i<=9 THEN ({names})[i] ELSE 'Other'||i END",
               "classificationdesc": f"CASE WHEN i<=9 THEN ({names})[i] ELSE 'Other'||i END"},
               "generate_series(1,5000) i")
        insert("event.eventtype", {"eventtypeid": uid("i"), "eventtypename": "'Type'||i",
               "eventtypedesc": "CASE WHEN i=1 THEN 'UnitWeighed' ELSE 'Other'||i END"}, "generate_series(1,5000) i")
        arrangement_types = "ARRAY['PackingSession','PackingSessionLine','PackingSessionGrader','PackingStaffTimesheet']"
        insert("arrangement.arrangementtype", {"arrangementtypeid": uid("i"), "arrangementtypename": "'Type'||i",
               "arrangementtypedescription": f"CASE WHEN i<=4 THEN ({arrangement_types})[i] ELSE 'Other'||i END"},
               "generate_series(1,5000) i")
        insert("arrangement.arrangement", {"arrangementid": uid("i")}, "generate_series(1,4000) i")
        insert("arrangement.arrangementxarrangementtype", {"arrangementxarrangementtypeid": uid("i"),
               "arrangementid": uid("i"), "arrangementtypeid": uid("(i-1)/1000+1")}, "generate_series(1,4000) i")
        insert("arrangement.arrangementxarrangement", {"arrangementxarrangementid": uid("i"),
               "childarrangementid": uid("i"), "parentarrangementid": uid("i-1000")}, "generate_series(1001,4000) i")
        insert("party.involvedpartyidentificationtype", {"involvedpartyidentificationtypeid": uid("i"),
               "involvedpartyidentificationname": "'Type'||i",
               "involvedpartyidentificationdesc": "CASE WHEN i=1 THEN 'An individuals username' ELSE 'Other'||i END"},
               "generate_series(1,5000) i")
        insert("party.involvedparty", {"involvedpartyid": uid("i")}, "generate_series(1,100000) i")
        insert("party.involvedpartyxinvolvedparty", {"involvedpartyxinvolvedpartyid": uid("i"),
               "parentinvolvedpartyid": uid("(i-1)/100+1"), "childinvolvedpartyid": uid("i"),
               "classificationid": uid("10")}, "generate_series(1,100000) i")
        sql("UPDATE classification.classification SET classificationdesc='StaffMemberClassifications' WHERE classificationid=" + uid("10"))
        insert("event.event", {"eventid": uid("i"), "warehousecreatedtimestamp":
               "'2026-01-01'::timestamptz + (i%86400)*interval '1 second'"}, "generate_series(1,60000) i")
        insert("event.eventxeventtype", {"eventxeventtypeid": uid("i"), "eventid": uid("i"),
               "eventtypeid": uid("1")}, "generate_series(1,60000) i")
        vals = ["'false'", "'false'", "'2.5'", "'1'", "'10'", "((i/100)%10+1)::text",
                f"{uid('(i-1)/100+1')}::text", "'grader'",
                "('2026-01-01'::timestamptz+(i%86400)*interval '1 second')::text"]
        value_case = "CASE k " + " ".join(f"WHEN {k} THEN {v}" for k, v in enumerate(vals, 1)) + " END"
        insert("event.eventxclassification", {"eventxclassificationid": uid("i*10+k"), "eventid": uid("i"),
               "classificationid": uid("k"), "value": value_case},
               "generate_series(1,60000) i CROSS JOIN generate_series(1,9) k")
        insert("resource.resourceitemxresourceitemtype", {"resourceitemxresourceitemtypeid": uid("i"),
               "resourceitemid": uid("i"), "resourceitemtypeid": uid("i%20+1"),
               "enterpriseid": uid("i%5+1"), "value": "(i%5000)::text"}, "generate_series(1,100000) i")
        insert("party.involvedpartyxinvolvedpartyidentificationtype", {
               "involvedpartyxinvolvedpartyidentificationtypeid": uid("i"), "involvedpartyid": uid("i"),
               "involvedpartyidentificationtypeid": uid("i%20+1"), "enterpriseid": uid("i%5+1"),
               "value": "CASE WHEN i%2=0 THEN 'amenc:2:key:'||md5((i%5000)::text)||md5((i%5000)::text)||':payload'||i ELSE (i%5000)::text END"},
               "generate_series(1,100000) i")
        sql("VACUUM ANALYZE;")
        queries = {
            "timesheet_statistics": bind(java_sql("UWE-Staff-Server/src/main/java/za/co/uweassist/staff/server/TimesheetStatisticsLoader.java", "SQL")),
            "measurement_replay": bind(java_sql("UWE-Server/src/main/java/za/co/uweassist/server/RegenerationReplayService.java", "PMU_SQL")),
            "measurement_count": bind(java_sql("UWE-Server/src/main/java/za/co/uweassist/server/SessionLineService.java", "COUNT_MEASUREMENT_EVENTS_SQL")),
            "timesheet_hierarchy": bind(java_sql("UWE-Staff-Server/src/main/java/za/co/uweassist/staff/server/StaffTimesheetService.java", "sqlString")),
            "staff_by_farm": bind(java_sql("UWE-Staff-Server/src/main/java/za/co/uweassist/staff/server/StaffService.java", "sqlString")),
            "event_window_count": "SELECT count(*) FROM event.event WHERE warehousecreatedtimestamp>='2026-01-01' AND warehousecreatedtimestamp<'2026-01-02'",
            "classification_description": "SELECT classificationid FROM classification.classification WHERE classificationdesc='MeasurementUnitSessionId'",
            "eventtype_description": "SELECT eventtypeid FROM event.eventtype WHERE eventtypedesc='UnitWeighed'",
            "arrangementtype_description": "SELECT arrangementtypeid FROM arrangement.arrangementtype WHERE arrangementtypedescription='PackingStaffTimesheet'",
            "identificationtype_description": "SELECT involvedpartyidentificationtypeid FROM party.involvedpartyidentificationtype WHERE involvedpartyidentificationdesc='An individuals username'",
            "typed_barcode_value": f"SELECT resourceitemid FROM resource.resourceitemxresourceitemtype WHERE enterpriseid={uid('1')} AND resourceitemtypeid={uid('1')} AND value='0' AND effectivefromdate<=now() AND effectivetodate>=now()",
            "identification_value": f"SELECT involvedpartyid FROM party.involvedpartyxinvolvedpartyidentificationtype WHERE enterpriseid={uid('1')} AND involvedpartyidentificationtypeid={uid('1')} AND (value='0' OR value='legacy0' OR coalesce(regexp_substr(value,'^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{{64}}:'),'')='amenc:2:key:'||md5('0')||md5('0')||':') AND effectivefromdate<=now() AND effectivetodate>=now()",
        }
        result = {"server": sql("SELECT version()"), "baseline_scripts": [s for s in scripts if s != addition],
                  "fixture": {"events": 60000, "event_classifications": 540000, "types": 5000,
                              "classifications": 5000, "resource_types": 100000, "identifications": 100000,
                              "parties": 100000, "party_links": 100000, "arrangements": 4000},
                  "queries": {}}
        for label, query in queries.items():
            result["queries"][label] = {"fingerprint": fingerprint(query), "before": measure(query)}
            print(label + " before " + str(result["queries"][label]["before"]["execution_ms"]), flush=True)
        sql((DB / addition).read_text(encoding="utf-8"))
        index_count = sql("SELECT count(*) FROM pg_indexes")
        sql((DB / addition).read_text(encoding="utf-8"))
        assert sql("SELECT count(*) FROM pg_indexes") == index_count, "Index script is not idempotent"
        sql("ANALYZE;")
        for label, query in queries.items():
            assert fingerprint(query) == result["queries"][label]["fingerprint"], f"Results changed: {label}"
            after = measure(query)
            result["queries"][label]["after"] = after
            print(f"{label} after {after['execution_ms']} ms {after['indexes']}", flush=True)
        result["idempotent"] = True
        result["results_unchanged"] = True

        # Check inherited indexes on existing and subsequently created partitions.
        # The ordered schema is unpartitioned; older UWE installations partition
        # the large warehouse tables. Never test this by changing a live database.
        sql("CREATE DATABASE partition_audit")
        for script in scripts:
            if script != addition:
                sql((DB / script).read_text(encoding="utf-8"), "partition_audit")
        partition_tables = ["event.eventxclassification", "resource.resourceitemxresourceitemtype",
                            "party.involvedpartyxinvolvedpartyidentificationtype"]
        for table in partition_tables:
            sql(f"ALTER TABLE {table} RENAME TO audit_original_{table.split('.')[1]}; "
                f"CREATE TABLE {table} (LIKE {table.split('.')[0]}.audit_original_{table.split('.')[1]} "
                f"INCLUDING DEFAULTS) PARTITION BY RANGE (warehousefromdate); "
                f"CREATE TABLE {table}_2026 PARTITION OF {table} FOR VALUES FROM ('2026-01-01') TO ('2027-01-01');",
                "partition_audit")
        sql((DB / addition).read_text(encoding="utf-8"), "partition_audit")
        for table in partition_tables:
            sql(f"CREATE TABLE {table}_2027 PARTITION OF {table} FOR VALUES FROM ('2027-01-01') TO ('2028-01-01');",
                "partition_audit")
        names = re.findall(r"CREATE INDEX IF NOT EXISTS (\w+)", (DB / addition).read_text())
        attached = sql("SELECT count(*),bool_and(child.indisvalid) FROM pg_inherits inh "
                       "JOIN pg_class parent ON parent.oid=inh.inhparent "
                       "JOIN pg_index child ON child.indexrelid=inh.inhrelid "
                       "WHERE parent.relname IN (" + ",".join(map(literal, names)) + ")", "partition_audit")
        assert attached == "10|t", f"Missing or invalid partition indexes: {attached}"
        result["partition_indexes"] = "10 valid child indexes across existing/new partitions"
        print("PASS: existing and future partitions inherit all five relationship indexes", flush=True)
        if args.output:
            args.output.write_text(json.dumps(result, indent=2), encoding="utf-8")
        print("PASS: ordered schema, index idempotency and result equivalence", flush=True)
    finally:
        run("docker", "stop", name)


if __name__ == "__main__":
    main()
