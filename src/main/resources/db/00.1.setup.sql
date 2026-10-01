-- Remove ordinary indexes before installing the canonical definitions below.
-- Constraint-backed indexes (including unique indexes referenced by foreign keys),
-- PostgreSQL catalogs, extension-owned indexes and attached partition indexes remain.
-- Dropping a partitioned parent index removes its ordinary child indexes as well.
DO $fsdm_reset_indexes$
DECLARE
    existing_index record;
BEGIN
    FOR existing_index IN
        SELECT n.nspname, c.relname
        FROM pg_catalog.pg_index i
        JOIN pg_catalog.pg_class c ON c.oid = i.indexrelid
        JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace
        WHERE n.nspname <> 'information_schema'
          AND n.nspname !~ '^pg_'
          AND NOT EXISTS (
              SELECT 1 FROM pg_catalog.pg_constraint constraint_row
              WHERE constraint_row.conindid = i.indexrelid
          )
          AND NOT EXISTS (
              SELECT 1 FROM pg_catalog.pg_inherits inheritance
              WHERE inheritance.inhrelid = i.indexrelid
          )
          AND NOT EXISTS (
              SELECT 1 FROM pg_catalog.pg_depend dependency
              WHERE dependency.classid = 'pg_catalog.pg_class'::regclass
                AND dependency.objid = i.indexrelid
                AND dependency.deptype = 'e'
          )
        ORDER BY n.nspname, c.relname
    LOOP
        EXECUTE format('DROP INDEX %I.%I', existing_index.nspname, existing_index.relname);
    END LOOP;
END;
$fsdm_reset_indexes$;

SET client_min_messages = NOTICE;
CREATE OR REPLACE FUNCTION public.uuid_equal_varchar(text, uuid)
    RETURNS boolean AS
'SELECT $1::text = $2::text;' LANGUAGE sql IMMUTABLE;

DO $fsdm_operator$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_operator o JOIN pg_namespace n ON n.oid = o.oprnamespace
        WHERE n.nspname = 'public' AND o.oprname = '='
          AND o.oprleft = 'text'::regtype AND o.oprright = 'uuid'::regtype
    ) THEN
CREATE OPERATOR public.= (
    leftarg = text,
    rightarg = uuid,
    procedure = public.uuid_equal_varchar,
    commutator = =
    );
    END IF;
END;
$fsdm_operator$;
CREATE
    EXTENSION IF NOT EXISTS tablefunc WITH SCHEMA public;
COMMENT
    ON EXTENSION tablefunc IS 'functions that manipulate whole tables, including crosstab';

SET
    default_tablespace = '';
SET
    default_table_access_method = heap;
SET
    statement_timeout = 0;
SET
    lock_timeout = 0;
SET
    idle_in_transaction_session_timeout = 0;
SET
    client_encoding = 'UTF8';
SET
    standard_conforming_strings = on;
SET
    check_function_bodies = false;
SET
    xmloption = content;
SET
    client_min_messages = notice;
SET
    row_security = off;
