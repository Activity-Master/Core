
SET
    client_min_messages = notice;


CREATE OR REPLACE FUNCTION drop_all_indexes_for_schemas()
    RETURNS void AS
$$
DECLARE
    idx_name              TEXT; -- Index name
    idx_table             TEXT; -- Table where the index is defined
    idx_schema            TEXT; -- Schema where the index resides
    drop_index_sql        TEXT; -- Query to drop the index
    is_used_by_constraint BOOLEAN; -- Flag for constraint dependency
BEGIN
    -- Loop through indexes in the specified schemas
    FOR idx_schema, idx_table, idx_name IN
        SELECT n.nspname AS schema_name,
               t.relname AS table_name,
               c.relname AS index_name
        FROM pg_index i
                 JOIN pg_class c ON c.oid = i.indexrelid
                 JOIN pg_class t ON t.oid = i.indrelid
                 JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE n.nspname IN ('address', 'arrangement', 'classification', 'dbo', 'event',
                            'geography', 'party', 'product', 'resource','public',
                            'rules', 'security', 'time')
          AND c.relkind = 'i' -- Only include indexes
          AND NOT i.indisprimary -- Exclude primary key indexes
        LOOP
            -- Check if the index is used by a constraint
            SELECT EXISTS (SELECT 1
                           FROM pg_constraint
                           WHERE conindid = (SELECT oid FROM pg_class WHERE relname = idx_name))
            INTO is_used_by_constraint;

            IF is_used_by_constraint THEN
                -- Log a notice if the index cannot be dropped
                RAISE NOTICE 'Cannot drop index % in schema %: it is used by a constraint.', idx_name, idx_schema;
            ELSE
                -- Construct the SQL query to drop the index since it's not used by a constraint
                drop_index_sql := format('DROP INDEX IF EXISTS %I.%I;', idx_schema, idx_name);

                -- Print the query for debugging (optional)
                RAISE NOTICE '%', drop_index_sql;

                -- Execute the SQL query
                EXECUTE drop_index_sql;
            END IF;
        END LOOP;
END;
$$ LANGUAGE plpgsql;
select drop_all_indexes_for_schemas();