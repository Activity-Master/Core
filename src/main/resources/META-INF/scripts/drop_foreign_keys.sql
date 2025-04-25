
SET
    client_min_messages = notice;

CREATE OR REPLACE FUNCTION drop_and_execute_foreign_keys_for_specific_schemas()
    RETURNS void AS
$$
DECLARE
    fk_table_name    TEXT; -- Table containing the foreign key
    fk_schema_name   TEXT; -- Schema containing the table
    foreign_key_name TEXT; -- Foreign key constraint name
    fk_drop_query    TEXT; -- Query to drop the foreign key
BEGIN
    -- Loop through all foreign key constraints directly from pg_constraint
    FOR fk_schema_name, fk_table_name, foreign_key_name IN
        SELECT n.nspname AS schema_name,
               c.relname AS table_name,
               conname AS constraint_name
        FROM pg_constraint con
                 JOIN pg_class c ON c.oid = con.conrelid
                 JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE con.contype = 'f' -- 'f' for foreign key
          /*AND n.nspname IN (
                            'address', 'arrangement', 'classification', 'dbo', 'event',
                            'geography', 'party', 'product', 'resource', 'rules','public',
                            'security', 'time'
            )*/
        LOOP
            -- Construct the foreign key drop query
            fk_drop_query := format('ALTER TABLE %I.%I DROP CONSTRAINT %I;',
                                    fk_schema_name, fk_table_name, foreign_key_name);

            -- Print the foreign key drop query for verification
            RAISE NOTICE '%', fk_drop_query;

            -- Run the foreign key drop query immediately
            EXECUTE fk_drop_query;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

select drop_and_execute_foreign_keys_for_specific_schemas();
