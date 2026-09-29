SET client_min_messages = NOTICE;
SET log_statement = 'all';
CREATE OR REPLACE FUNCTION uuid_equal_varchar(text, uuid)
    RETURNS boolean AS
'SELECT $1::text = $2::text;' LANGUAGE sql IMMUTABLE;

CREATE OPERATOR = (
    leftarg = text,
    rightarg = uuid,
    procedure = uuid_equal_varchar,
    commutator = =
    );
CREATE
    EXTENSION IF NOT EXISTS tablefunc WITH SCHEMA public;
COMMENT
    ON EXTENSION tablefunc IS 'functions that manipulate whole tables, including crosstab';

SET SESSION AUTHORIZATION 'postgres';

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
