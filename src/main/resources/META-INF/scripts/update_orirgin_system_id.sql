SET
    client_min_messages = notice;

CREATE OR REPLACE FUNCTION public.update_uuid_columns_to_nullable_and_set_null()
    RETURNS VOID AS
$$
DECLARE
    schema_name        TEXT;
    table_name         TEXT;
    target_column_name TEXT := 'originalsourcesystemuniqueid';
    alter_query        TEXT;
    update_query       TEXT;
BEGIN
    FOR schema_name, table_name IN
        SELECT t.table_schema, t.table_name
        FROM information_schema.tables t
                 JOIN information_schema.columns c
                      ON t.table_schema = c.table_schema AND t.table_name = c.table_name
        WHERE t.table_schema IN (
                                 'address', 'arrangement', 'classification', 'dbo', 'event',
                                 'geography', 'party', 'product', 'resource', 'rules',
                                 'security', 'time', 'public'
            )
          AND c.column_name = target_column_name
        LOOP
            -- Alter the column to allow NULL
            alter_query := FORMAT(
                    'ALTER TABLE %I.%I ALTER COLUMN %I DROP NOT NULL;',
                    schema_name, table_name, target_column_name
                           );
            --EXECUTE alter_query;
            RAISE NOTICE 'Column %.%.% being updated.', schema_name, table_name, target_column_name;

            -- Update empty strings to NULL
            update_query := FORMAT(
                    'UPDATE %I.%I SET %I = ''00000000-0000-0000-0000-000000000000'' WHERE %I = '''';',
                    schema_name, table_name, target_column_name, target_column_name
                            );
            EXECUTE update_query;
            RAISE NOTICE 'Updated empty strings to NULL in %.% column %.', schema_name, table_name, target_column_name;
        END LOOP;

    RAISE NOTICE 'UUID column update completed successfully.';
END;
$$ LANGUAGE plpgsql;

DO
$$
    BEGIN
        PERFORM public.update_uuid_columns_to_nullable_and_set_null();
    END
$$;
