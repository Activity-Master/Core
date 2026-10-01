-- Administrative schema history, independent of FSDM business/security rows.
DO $fsdm_schema$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace WHERE nspname = 'dbo') THEN
        CREATE SCHEMA IF NOT EXISTS dbo;
    END IF;
END;
$fsdm_schema$;
-- Guard only this table's creation as well; later statements still run.
DO $fsdm_history_table$
BEGIN
    IF pg_catalog.to_regclass('dbo.fsdmschemaupdate') IS NULL THEN
        CREATE TABLE IF NOT EXISTS dbo.fsdmschemaupdate
        (
            scriptname varchar(150) PRIMARY KEY,
            scriptsequence integer NOT NULL UNIQUE,
            checksum char(64) NOT NULL,
            appliedat timestamptz NOT NULL DEFAULT clock_timestamp(),
            executionmode varchar(10) NOT NULL CHECK (executionmode IN ('applied', 'baseline'))
        );
    END IF;
END;
$fsdm_history_table$;
