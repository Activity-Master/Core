"""Verify time seeds against an isolated local PostgreSQL cluster.

Run from core: python src/test/scripts/time_seed_audit.py
Requires initdb, pg_ctl and psql on PATH. Never connects to an application DB.
"""
import pathlib
import shutil
import socket
import subprocess
import tempfile

CORE = pathlib.Path(__file__).resolve().parents[3]


def run(*args, input=None):
    # File handles avoid waiting for inherited Windows server pipe handles at startup.
    with tempfile.TemporaryFile(mode="w+", encoding="utf-8") as output, tempfile.TemporaryFile(mode="w+", encoding="utf-8") as errors:
        result = subprocess.run(args, input=input, text=True, encoding="utf-8", stdout=output, stderr=errors)
        output.seek(0)
        errors.seek(0)
        stdout, stderr = output.read(), errors.read()
        if result.returncode:
            raise RuntimeError(stderr or stdout)
        return stdout.strip()


def main():
    psql = shutil.which("psql")
    if not psql:
        raise RuntimeError("PostgreSQL binaries must be on PATH")
    binaries = pathlib.Path(psql).parent
    with tempfile.TemporaryDirectory(prefix="am-time-seeds-") as directory:
        cluster = pathlib.Path(directory) / "data"
        run(str(binaries / "initdb"), "-D", str(cluster), "-U", "postgres", "-A", "trust", "--no-locale", "-E", "UTF8")
        with socket.socket() as listener:
            listener.bind(("127.0.0.1", 0))
            port = listener.getsockname()[1]
        run(str(binaries / "pg_ctl"), "-D", str(cluster), "-l", str(pathlib.Path(directory) / "postgres.log"),
            "-o", f"-h 127.0.0.1 -p {port}", "-w", "start")
        try:
            def sql(statement):
                return run(psql, "-X", "-qAt", "-h", "127.0.0.1", "-p", str(port), "-U", "postgres",
                           "-d", "postgres", "-v", "ON_ERROR_STOP=1", input=statement)

            seed = (CORE / "src/main/resources/db/15.time.sql").read_text(encoding="utf-8")
            tables = [("monthofyear", "monthofyearid", "monthofyearname", 12),
                      ("dayparts", "daypartid", "daypartname", 11),
                      ("daynames", "daynameid", "dayname", 7)]
            sql(seed)
            def snapshot(table, key):
                return sql(f'SELECT row_to_json(t) FROM "time".{table} t ORDER BY {key}')
            before = [snapshot(table, key) for table, key, _, _ in tables]
            sql(seed)
            assert before == [snapshot(table, key) for table, key, _, _ in tables]
            for table, key, name, _ in tables:
                sql(f'UPDATE "time".{table} SET {name}=\'Retained value\' WHERE {key}=2; '
                    f'DELETE FROM "time".{table} WHERE {key}=3;')
            sql(seed)
            for table, key, name, count in tables:
                assert sql(f'SELECT count(*) FROM "time".{table}') == str(count)
                assert sql(f'SELECT {name} FROM "time".{table} WHERE {key}=2') == "Retained value"
                assert sql(f'SELECT count(*) FROM "time".{table} WHERE {key}=3') == "1"
            print("PASS: fresh seeds, unchanged replay, partial-data repair and existing-value preservation")
        finally:
            run(str(binaries / "pg_ctl"), "-D", str(cluster), "-m", "fast", "-w", "stop")


if __name__ == "__main__":
    main()
