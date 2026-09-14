# ActivityMaster shared pool configuration

`ActivityMasterDBModule` now resolves `ActivityMasterPoolConfiguration` before the
generic persistence module's catch/log boundary. Invalid policy fails Guice
construction. Hibernate's `hibernate.vertx.pool` and the connection-info object's
`toPooledDatasource()` return the same pool instance. Repeated managed lookups no
longer call the generic PostgreSQL builder, which used a second pool with disabled
TLS. The retained pool does not use Vert.x global named-pool sharing; borrowers
share the actual object supplied by this module.

## Settings and compatibility

Existing `FSDM_DBSERVER`, `FSDM_DBPORT`, `FSDM_DBNAME`, `FSDM_USER` and
`FSDM_PASSWORD` settings remain the connection inputs, resolved through GuicedEE
Environment. A nonblank password is now required; there is no default password.
Credentials and connection options are not included in the new objects' string
representations or validation exceptions.

| Setting | Default | Allowed values |
| --- | --- | --- |
| `FSDM_SSL_MODE` | `verify-full` | `verify-full`, or the explicit local exception below |
| `FSDM_SSL_CA_FILE` | Required for TLS | PEM trust file, 1?65536 bytes |
| `FSDM_POOL_MAX_SIZE` | 16 | 1?500 |
| `FSDM_POOL_MAX_WAIT_QUEUE` | 64 | 0?4096; zero means no queued acquisitions |
| `FSDM_POOL_ACQUIRE_TIMEOUT_MS` | 1000 | 1?30000 |
| `FSDM_CONNECT_TIMEOUT_MS` | 2000 | 1?30000; reconnect attempts disabled |
| `FSDM_TLS_TIMEOUT_MS` | 2000 | 1?30000 |
| `FSDM_STATEMENT_TIMEOUT_MS` | 30000 | 1?600000 |
| `FSDM_LOCK_TIMEOUT_MS` | 1000 | 1?60000 |

Numeric settings accept plain decimal integers, with no whitespace, signs or
fractions. Absent/empty optional settings use defaults. TLS verifies both trust
and hostname. There is no `prefer`, `require`, `verify-ca`, trust-all or plaintext
fallback. Pipelining is 1; idle retirement is 300 seconds and maximum connection
lifetime is 3 minutes. These budgets have different scopes and do not replace
request deadlines or role/authorization checks.

This intentionally changes the previous 500-connection, implicit-plaintext
configuration. Existing development consumers can explicitly set
`FSDM_SSL_MODE=disable` only with `ENVIRONMENT=local`, `development` or `test` and a
literal `127.0.0.1` or `::1` host. Hostnames, private-network IPs and production do
not qualify for that exception. NE1's secured acceptance uses verified TLS.
Consumers with longer installation/batch work must explicitly size their bounded
budgets; no normal configuration or database was modified by this code change.

## Verification and limits

NE1 consumer tests load the locally installed `activity-master:3.0.0-SNAPSHOT`
artifact. Fourteen focused checks exercise actual Guice policy rejection, strict
configuration, PostgreSQL TLS and connection identity, pool saturation/queue timeout,
statement cancellation, lock waits and recovery, wrong-host/untrusted TLS denial,
plaintext denial without authentication bytes, stalled TLS, and resource closure.
Tests use a disposable PostgreSQL container and temporary certificates, not normal
data. Shared-pool closure leaves the borrowed Vertx usable. An explicit close after
a failed acquisition is required in the fixture before asserting socket closure;
a timed-out acquisition is not treated as proof of completed cleanup.

Final verification on 14 September: 47 selected NE1 checks passed (7 registry,
40 core), zero failures/skips and zero ERROR events in
`target/activitymaster-pool-final-tests.log`. This includes the fourteen pool
checks plus launcher, integrity and transport regressions. The Surefire module
path and bytecode verify the configured attachment in the consumed artifact;
`target/am-pool-consumer-artifact.json` records its SHA-256. Ten changed files
passed secret scanning and whitespace checks. The owned PostgreSQL fixture was
stopped and its removal verified. Docker Desktop was started to run the fixture;
no normal PostgreSQL configuration, schema, accounts or keys were modified.

The ActivityMaster module was compiled/installed with its existing snapshot version;
its broad historical domain suite was not run. The focused tests run in NE1 with
the corrected client/inject dependencies and an unexpected-error log guard.
The local snapshot changes need review/publication before CI or deployment use.

## Shutdown dependency

The companion GuicedEE lifecycle changes now consume the explicit
`guicedee.persistence.ownedPool` marker attached beside `hibernate.vertx.pool`.
Both refer to the identical pool. Persistence retains that owner before injector
construction can fail, subscribes to service stop, then closes owned pools. Vertx
uses a separate shutdown priority and awaits closure last. Unmarked external pools
remain borrowed. Use the reviewed client/inject/vertx/persistence changes together;
the pool marker alone does not make older persistence implementations close it.

NE1's resolved consumer checks cover the real destroy runner with fixture services,
including failed injection, failing persistence stop, delayed Vertx destruction and
pending startup. The actual Hibernate/domain bootstrap and full two-process secured
workflow remain acceptance gates. This draft does not change running applications,
publish library artifacts, or migrate existing database settings.
