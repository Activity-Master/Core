SET
    client_min_messages = notice;

CREATE OR REPLACE FUNCTION drop_all_indexes_for_schemas()
    RETURNS void AS $$
DECLARE
    idx_name TEXT;
    idx_table TEXT;
    idx_schema TEXT;
    drop_index_sql TEXT;
    is_used_by_constraint BOOLEAN;
BEGIN
    FOR idx_schema, idx_table, idx_name IN
        SELECT
            n.nspname AS schema_name,
            t.relname AS table_name,
            c.relname AS index_name
        FROM pg_index i
                 JOIN pg_class c ON c.oid = i.indexrelid
                 JOIN pg_class t ON t.oid = i.indrelid
                 JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE n.nspname IN ('address', 'arrangement', 'classification', 'dbo', 'event',
                            'geography', 'party', 'product', 'resource','public',
                            'rules', 'security', 'time')
          AND c.relkind = 'i'
          AND NOT i.indisprimary
        LOOP
            SELECT EXISTS (
                SELECT 1
                FROM pg_constraint
                WHERE conindid = (SELECT oid FROM pg_class WHERE relname = idx_name)
            ) INTO is_used_by_constraint;

            IF is_used_by_constraint THEN
                RAISE NOTICE 'Cannot drop index % in schema %: it is used by a constraint.', idx_name, idx_schema;
            ELSE
                drop_index_sql := format('DROP INDEX IF EXISTS %I.%I;', idx_schema, idx_name);
                RAISE NOTICE '%', drop_index_sql;
                EXECUTE drop_index_sql;
            END IF;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT drop_all_indexes_for_schemas();