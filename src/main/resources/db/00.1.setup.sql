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
