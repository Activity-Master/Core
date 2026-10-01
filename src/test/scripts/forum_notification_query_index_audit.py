"""Measure Forum/Notification SQL and shared indexes on disposable PostgreSQL 17.

Run from core: python -B -X utf8 src/test/scripts/forum_notification_query_index_audit.py
SQL helpers are compiled from current Java sources. No application DB is accessed;
the fixture publishes no ports and stops only the container it creates.
"""
import argparse
import base64
import hashlib
import json
import pathlib
import re
import statistics
import time
import uuid

from uwe_query_index_audit import CORE, DB, literal, run

ADDITION = "25.forum-notification-query-indexes.sql"
ACTIVITY = CORE.parent
FORUM = ACTIVITY / "forums/src/main/java/com/guicedee/activitymaster/forums/ForumService.java"
NOTIFICATION = ACTIVITY / "notifications/src/main/java/com/guicedee/activitymaster/notifications/NotificationService.java"
TAXONOMY = NOTIFICATION.with_name("NotificationTaxonomy.java")


def key(text):
    return "md5(" + literal(text) + ")::uuid"


def helper(source, name):
    found = re.findall(r"private static String " + name + r"\([^)]*\)\s*\{.*?\}", source, re.S)
    assert found, name
    return "\n".join(found)


def native(source, prefix, result_class):
    start = source.index('"' + prefix)
    end = source.index(", " + result_class + ".class", start)
    return source[start:end]


def assigned(source, method):
    start = source.index("String sql =", source.index(method)) + len("String sql =")
    return source[start:source.index(";", start)]


def production_queries(scratch):
    """Compile the exact production string helpers, avoiding hand-copied SQL."""
    forum = FORUM.read_text(encoding="utf-8")
    notification = NOTIFICATION.read_text(encoding="utf-8")
    constants = "\n".join(re.findall(r"public static final String \w+\s*=\s*\"[^\"]*\";",
                                   TAXONOMY.read_text(encoding="utf-8")))
    integers = "\n".join(re.findall(r"public static final int \w+\s*=\s*[\w +_]+;", notification))
    posts = native(forum, "select e.eventid, s.involvedpartyid, convert_from", "Object[]")
    subscribers = native(forum, "select m.involvedpartyid from arrangement.arrangementxinvolvedparty m ", "UUID")
    title = native(forum, "select x.value from arrangement.arrangementxclassification x ", "String")
    find = assigned(notification, "private Uni<Notification> findWith")
    delivery = assigned(notification, "public Uni<Page<Delivery>> deliveries")
    java = '''
import java.util.Base64;
import java.nio.charset.StandardCharsets;
public class ForumNotificationSql {
  static class Forums {
    %s
    %s
    static String posts() { return %s; }
    static String subscribers() { return %s; }
    static String title() { return %s; }
  }
  static class Notifications {
    %s
    %s
    %s
    static String find() { return %s; }
    static String deliveries() { return %s; }
    static String list(String filter) {
      return "select n.eventid, m.category, m.severity, m.subject, cast(null as text), cast(null as text), pub.publisher, n.created, st.state from "
        + recipientWindow(LIST_SCAN_CEILING) + markerPivot() + statePivot() + publisherPivot()
        + " where " + filter + " order by n.created desc, n.eventid desc";
    }
    static String counts() {
      return "select count(*) filter(where q.state is null), count(*) filter(where coalesce(q.state,'')<>'DISMISSED'), count(*) from (select st.state as state from "
        + recipientWindow(COUNT_CEILING) + statePivot() + ") q";
    }
    static String readAll() {
      return "select n.eventid from " + recipientWindow(LIST_SCAN_CEILING) + markerPivot() + statePivot()
        + " where st.state is null order by n.created desc, n.eventid desc";
    }
  }
  static void emit(String name, String sql) {
    System.out.println(name + "=" + Base64.getEncoder().encodeToString(sql.getBytes(StandardCharsets.UTF_8)));
  }
  public static void main(String[] args) {
    emit("forum_list", "select distinct a.arrangementid" + Forums.forumWhere() + " order by a.arrangementid limit 51");
    emit("forum_membership", "select 1" + Forums.forumWhere() + " and a.arrangementid=:id for share of a,m");
    emit("forum_moderator", "select 1" + Forums.forumWhere("ForumModerator") + " and a.arrangementid=:id for update of a");
    emit("forum_subscribers", Forums.subscribers());
    emit("forum_title", Forums.title());
    emit("forum_posts", Forums.posts() + " limit 51");
    emit("notification_window", "select * from " + Notifications.recipientWindow(Notifications.LIST_SCAN_CEILING));
    emit("notification_list", Notifications.list("coalesce(st.state,'')<>'DISMISSED'") + " limit 51");
    emit("notification_unread", Notifications.list("st.state is null") + " limit 51");
    emit("notification_category", Notifications.list("m.category=:category and st.state is null") + " limit 51");
    emit("notification_find", Notifications.find() + " limit 1");
    emit("notification_counts", Notifications.counts());
    emit("notification_read_all", Notifications.readAll() + " limit 200");
    emit("notification_deliveries", Notifications.deliveries() + " limit 51");
  }
}
''' % (helper(forum, "live"), helper(forum, "forumWhere"), posts, subscribers, title,
       constants, integers, "\n".join(helper(notification, name) for name in
              ["live", "recipientWindow", "markerPivot", "statePivot", "publisherPivot", "bodyPivot"]),
       find, delivery)
    file = scratch / "ForumNotificationSql.java"
    file.write_text(java, encoding="utf-8")
    run("javac", "-d", str(scratch), str(file))
    output = run("java", "-cp", str(scratch), "ForumNotificationSql")
    return {name: base64.b64decode(encoded).decode("utf-8") for name, encoded in
            (line.split("=", 1) for line in output.splitlines())}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=pathlib.Path, default=CORE / "target/forum-notification-query-index-audit.json")
    args = parser.parse_args()
    scratch = CORE / "target/forum-notification-sql"
    scratch.mkdir(parents=True, exist_ok=True)
    raw_queries = production_queries(scratch)
    migration = (DB / ADDITION).read_text(encoding="utf-8")
    declarations = re.findall(r"CREATE INDEX IF NOT EXISTS (\w+)\s+ON (\w+\.\w+)", migration)
    assert len(declarations) == migration.count("CREATE INDEX")
    name = "am-forum-notification-index-" + uuid.uuid4().hex[:12]
    run("docker", "run", "--rm", "-d", "--name", name,
        "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")

    def sql(statement):
        # The entrypoint's temporary bootstrap server accepts Unix sockets only;
        # TCP readiness waits for the final server before applying the schema.
        return run("docker", "exec", "-i", name, "psql", "-h", "127.0.0.1", "-X", "-qAt", "-v", "ON_ERROR_STOP=1",
                   "-U", "postgres", input=statement)

    def insert(table, overrides, rows="generate_series(1,1) i"):
        schema, short = table.split(".")
        columns = json.loads(sql("select json_agg(json_build_object('name',column_name,'type',data_type,"
                                "'required',is_nullable='NO' and column_default is null)) "
                                "from information_schema.columns where table_schema=" + literal(schema) +
                                " and table_name=" + literal(short)))
        pk = sql("select a.attname from pg_index x join pg_attribute a on a.attrelid=x.indrelid "
                 "and a.attnum=x.indkey[0] where x.indisprimary and x.indrelid=" + literal(table) + "::regclass")
        defaults = {"uuid": key("zero"), "integer": "0", "smallint": "0", "boolean": "false",
                    "timestamp with time zone": "'2000-01-01'::timestamptz", "date": "'2000-01-01'::date"}
        values = {c["name"]: defaults.get(c["type"], "'fixture'") for c in columns if c["required"]}
        values[pk] = "md5(" + literal(table + ":") + "||i::text)::uuid"
        shared = {"enterpriseid": key("ent0"), "systemid": key("sys0"), "activeflagid": key("active"),
                  "effectivefromdate": "'2000-01-01'::timestamptz", "effectivetodate": "'2999-01-01'::timestamptz"}
        names = {c["name"] for c in columns}
        values.update({k: v for k, v in shared.items() if k in names})
        assert set(overrides) <= names, (table, set(overrides) - names)
        values.update(overrides)
        sql("set session_replication_role=replica; insert into " + table + " (" + ",".join(values) + ") select " +
            ",".join(values.values()) + " from " + rows)

    def measure(query):
        output = sql("set statement_timeout='90s'; " +
                     ("explain (analyze,buffers,format json) " + query + ";\n") * 5)
        decoder, plans = json.JSONDecoder(), []
        while output.strip():
            output = output.lstrip()
            value, end = decoder.raw_decode(output)
            plans.append(value[0])
            output = output[end:]
        assert len(plans) == 5
        def indexes(node):
            return ([node["Index Name"]] if "Index Name" in node else []) + [
                item for child in node.get("Plans", []) for item in indexes(child)]
        return {"execution_ms": round(statistics.median(p["Execution Time"] for p in plans), 3),
                "planning_ms": round(statistics.median(p["Planning Time"] for p in plans), 3),
                "indexes": sorted(set(indexes(plans[-1]["Plan"]))), "plan": plans[-1]}

    def fingerprint(query):
        # Locks are exercised by EXPLAIN itself; omit them only for result hashing.
        query = re.sub(r" for (?:share|update) of [\w,]+$", "", query)
        return sql("select count(*),md5(coalesce(string_agg(r::text,E'\\n' order by r::text),'')) from (" + query + ") r")

    try:
        for _ in range(60):
            try:
                sql("select 1")
                break
            except RuntimeError:
                time.sleep(0.5)
        else:
            raise RuntimeError("PostgreSQL did not start")
        source = (CORE / "src/main/java/com/guicedee/activitymaster/fsdm/db/FsdmSchema.java").read_text()
        scripts = ["00.0.schema-update-history.sql"] + re.findall(r'"([\w.\-]+\.sql)"', source)[1:]
        for script in scripts[:scripts.index(ADDITION)]:
            sql((DB / script).read_text(encoding="utf-8"))
        print("Applied all preceding domain indexes", flush=True)
        insert("dbo.activeflag", {"activeflagid": key("active"), "allowaccess": "1"})

        roles = re.findall(r'public static final String \w+\s*=\s*"([^"]*)";', TAXONOMY.read_text(encoding="utf-8"))
        roles += ["ForumContext", "ForumTitle", "ForumSubscriber", "ForumModerator", "ForumPost", "ForumPoster", "ForumPostBody"]
        role_rows = ",".join("(" + literal(role) + ")" for role in sorted(set(roles)))
        for table, col, typ in [("classification.classification", "classification", "role"),
                                ("event.eventtype", "eventtype", "event"),
                                ("arrangement.arrangementtype", "arrangementtype", "arrangement")]:
            choices = role_rows if typ == "role" else "('Notification'),('Notification State'),('Notification Delivery')" if typ == "event" else "('Forum')"
            overrides = {col + "id": "md5(v.name||s::text)::uuid", col + "name": "v.name",
                         "enterpriseid": "md5('ent'||(s%2)::text)::uuid", "systemid": "md5('sys'||s::text)::uuid"}
            insert(table, overrides, "(values " + choices + ") v(name) cross join generate_series(0,13) s cross join generate_series(1,1) i")

        n, forums, history = 6000, 1200, 20
        scope = {"enterpriseid": "md5('ent'||(i%2)::text)::uuid", "systemid": "md5('sys'||(i%4)::text)::uuid"}
        def rid(role): return "md5(" + literal(role) + "||(i%4)::text)::uuid"
        def event_id(kind, suffix=""): return "md5(" + literal(kind) + "||i::text" + suffix + ")::uuid"
        base = "generate_series(1," + str(n) + ") i"
        insert("event.event", dict(scope, eventid=event_id("notice:"), warehousecreatedtimestamp="'2025-01-01'::timestamptz+i*interval '1 second'"), base)
        insert("event.eventxeventtype", dict(scope, eventid=event_id("notice:"), eventtypeid=rid("Notification"),
               classificationid=rid("NotificationType")), base)
        old = "case when h=0 then '2999-01-01' else '2001-01-01' end::timestamptz"
        graph = base + " cross join generate_series(0," + str(history) + ") h"
        insert("event.eventxinvolvedparty", dict(scope, eventxinvolvedpartyid=event_id("rec:", "||':'||h::text"),
               eventid=event_id("notice:"), involvedpartyid="md5('party'||(i%10)::text)::uuid", classificationid=rid("NotificationRecipient"),
               effectivetodate=old), graph)
        insert("event.eventxinvolvedparty", dict(scope, eventxinvolvedpartyid=event_id("pub:"), eventid=event_id("notice:"),
               involvedpartyid=key("party19"), classificationid=rid("NotificationPublisher")), base)
        marker_rows = base + " cross join (values ('NotificationCategory','forum.post'),('NotificationSeverity','INFO'),('NotificationSubject','Fixture')) v(role,value) cross join generate_series(0," + str(history) + ") h"
        insert("event.eventxclassification", dict(scope, eventxclassificationid=event_id("head:", "||v.role||h::text"),
               eventid=event_id("notice:"), classificationid="md5(v.role||(i%4)::text)::uuid", value="v.value", effectivetodate=old), marker_rows)

        for kind, count, link, role, actor_role in [("state:", 3, "NotificationStateOf", "NotificationState", "NotificationStateActor"),
                                                  ("delivery:", 2, "NotificationDeliveryOf", None, "NotificationDeliveryRecipient")]:
            rows = base + " cross join generate_series(1," + str(count) + ") j"
            if role:
                rows = "(select i from generate_series(1," + str(n) + ") i where i%3<>0) notices cross join generate_series(1," + str(count) + ") j"
            child = event_id(kind, "||':'||j::text")
            insert("event.event", dict(scope, eventid=child, warehousecreatedtimestamp="'2025-01-02'::timestamptz+i*interval '1 second'+j*interval '1 hour'"), rows)
            insert("event.eventxevent", dict(scope, eventxeventid=event_id("link:"+kind,"||':'||j::text||':'||h::text"),
                   parenteventid=event_id("notice:"), childeventid=child, classificationid=rid(link), effectivetodate=old),
                   rows + " cross join generate_series(0," + str(history) + ") h")
            insert("event.eventxinvolvedparty", dict(scope, eventxinvolvedpartyid=event_id("actor:"+kind,"||':'||j::text||':'||h::text"),
                   eventid=child, involvedpartyid="md5('party'||(i%10)::text)::uuid", classificationid=rid(actor_role), effectivetodate=old),
                   rows + " cross join generate_series(0," + str(history) + ") h")
            markers = "(values ('NotificationState','READ'))" if role else "(values ('NotificationDeliveryChannel','EVENT_BUS'),('NotificationDeliveryResult','DELIVERED'),('NotificationDeliveryDetail','fixture'))"
            insert("event.eventxclassification", dict(scope, eventxclassificationid=event_id("mark:"+kind,"||':'||j::text||v.role||h::text"),
                   eventid=child, classificationid="md5(v.role||(i%4)::text)::uuid", value="v.value", effectivetodate=old),
                   rows + " cross join " + markers + " v(role,value) cross join generate_series(0," + str(history) + ") h")

        fbase = "generate_series(1," + str(forums) + ") i"
        fscope = {"enterpriseid": "md5('ent'||(i%2)::text)::uuid", "systemid": "md5('sys'||(10+i%4)::text)::uuid"}
        def frid(role): return "md5(" + literal(role) + "||(10+i%4)::text)::uuid"
        insert("arrangement.arrangement", dict(fscope, arrangementid=event_id("forum:")), fbase)
        insert("arrangement.arrangementxarrangementtype", dict(fscope, arrangementid=event_id("forum:"), arrangementtypeid=frid("Forum")), fbase)
        insert("arrangement.arrangementxclassification", dict(fscope, arrangementxclassificationid=event_id("fmark:", "||v.role||h::text"),
               arrangementid=event_id("forum:"), classificationid="md5(v.role||(10+i%4)::text)::uuid", value="v.value", effectivetodate=old),
               fbase + " cross join (values ('ForumContext','SOCIAL:fixture'),('ForumTitle','Fixture forum')) v(role,value) cross join generate_series(0," + str(history) + ") h")
        insert("arrangement.arrangementxinvolvedparty", dict(fscope, arrangementxinvolvedpartyid=event_id("fmember:", "||v.role||h::text"),
               arrangementid=event_id("forum:"), involvedpartyid="md5('party'||(i%10)::text)::uuid", classificationid="md5(v.role||(10+i%4)::text)::uuid", effectivetodate=old),
               fbase + " cross join (values ('ForumSubscriber'),('ForumModerator')) v(role) cross join generate_series(0," + str(history) + ") h")
        insert("party.involvedparty", {"involvedpartyid": "md5('party'||i::text)::uuid"}, "generate_series(0,19) i")
        insert("party.involvedpartyorganic", {"involvedpartyorganicid": "md5('party'||i::text)::uuid"}, "generate_series(0,19) i")

        posts = fbase + " cross join generate_series(1,10) j"
        post = event_id("post:", "||':'||j::text")
        insert("event.event", dict(fscope, eventid=post, warehousecreatedtimestamp="'2025-01-01'::timestamptz+i*interval '1 second'+j*interval '1 hour'"), posts)
        insert("event.eventxarrangement", dict(fscope, eventxarrangementsid=event_id("postforum:","||':'||j::text||':'||h::text"),
               eventid=post, arrangementid=event_id("forum:"), classificationid=frid("ForumPost"), effectivetodate=old),
               posts + " cross join generate_series(0," + str(history) + ") h")
        insert("event.eventxinvolvedparty", dict(fscope, eventxinvolvedpartyid=event_id("poster:","||':'||j::text"),
               eventid=post, involvedpartyid=key("party0"), classificationid=frid("ForumPoster")), posts)
        insert("resource.resourceitem", dict(fscope, resourceitemid=post, resourceitemdatatype="'text/plain'"), posts)
        insert("event.eventxresourceitem", dict(fscope, eventxresourceitemid=event_id("postbody:","||':'||j::text"),
               eventid=post, resourceitemid=post, classificationid=frid("ForumPostBody")), posts)
        insert("resource.resourceitemdatavalue", {"resourceitemdatavalueid": post, "resourceitemdatavalue": "convert_to('fixture post','UTF8')"}, posts)
        sql("vacuum analyze")
        print("Seeded multi-scope Events, memberships and 20 historical relationship versions", flush=True)

        result = {"server": sql("select version()"), "new_indexes": len(declarations), "history_versions": history,
                  "sources": {str(p.relative_to(ACTIVITY)): hashlib.sha256(p.read_bytes()).hexdigest() for p in [FORUM, NOTIFICATION, TAXONOMY]}, "queries": {}}
        for label, raw in raw_queries.items():
            system = 10 if label.startswith("forum_") else 0
            values = {"enterprise": key("ent0"), "system": key("sys"+str(system)), "actor": key("party0"),
                      "id": key("forum:20" if system==10 else "notice:20"), "context": "'SOCIAL:fixture'", "social": "true", "category": "'forum.post'"}
            query = re.sub(r"(?<!:):(\w+)", lambda m: values[m[1]], raw)
            result["queries"][label] = {"sql": query, "fingerprint": fingerprint(query), "before": measure(query)}
            assert int(result["queries"][label]["fingerprint"].split('|')[0]) > 0, label
            print("Baseline " + label + ": " + str(result["queries"][label]["before"]["execution_ms"]) + " ms", flush=True)
        sql(migration)
        installed = sql("select count(*) from pg_index")
        sql(migration)
        assert installed == sql("select count(*) from pg_index"), "Replay duplicated indexes"
        assert sql("select count(*) from pg_index where not indisvalid or not indisready") == "0"
        for label, entry in result["queries"].items():
            assert fingerprint(entry["sql"]) == entry["fingerprint"], label
            entry["after"] = measure(entry["sql"])
            print("After " + label + ": " + str(entry["after"]["execution_ms"]) + " ms", flush=True)
        selected = {name for entry in result["queries"].values() for name in entry["after"]["indexes"]}
        result["unselected_indexes"] = sorted({name for name, _ in declarations} - selected)
        # Domain tables can be partitioned by the installed system scope. Check
        # indexes inherited by both existing and subsequently attached partitions.
        sql("create schema index_partition_audit")
        for number, (index, table) in enumerate(declarations):
            parent = "index_partition_audit.p" + str(number)
            sql("create table " + parent + " (like " + table + " including defaults) partition by hash (systemid); "
                "create table " + parent + "_a partition of " + parent + " for values with (modulus 2,remainder 0)")
            statement = re.search(r"CREATE INDEX IF NOT EXISTS " + index + r"\b.*?;", migration, re.S)[0]
            sql(statement.replace(index, "pa_" + str(number)).replace("ON " + table, "ON " + parent))
            sql("create table " + parent + "_b partition of " + parent + " for values with (modulus 2,remainder 1)")
        assert sql("select count(*) from pg_index where not indisvalid or not indisready") == "0"
        assert int(sql("select count(*) from pg_index i join pg_class c on c.oid=i.indexrelid "
                       "join pg_namespace n on n.oid=c.relnamespace where n.nspname='index_partition_audit'")) == 3 * len(declarations)
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(result, indent=2), encoding="utf-8")
        print("PASS: unchanged results, valid replay and partition inheritance; unselected additions: " + str(result["unselected_indexes"]), flush=True)
    finally:
        run("docker", "stop", name)


if __name__ == "__main__":
    main()
