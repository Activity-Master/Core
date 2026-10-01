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
