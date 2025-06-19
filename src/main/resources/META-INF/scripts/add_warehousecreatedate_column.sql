DO $$
    DECLARE
        tbl_name TEXT;                           -- Table name in schema
        schema_name TEXT;                        -- Schema name
    BEGIN
        -- Iterate through all schemas
        FOR schema_name IN
            SELECT UNNEST(ARRAY['address', 'arrangement', 'classification', 'dbo', 'event',
                'geography', 'party', 'product', 'resource', 'rules',
                'security', 'public'])
            LOOP
                -- Log entry into schema processing
                RAISE NOTICE 'Processing schema: %', schema_name;

                -- Iterate through all tables in the current schema
                FOR tbl_name IN
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = schema_name
                      AND table_type = 'BASE TABLE'
                    LOOP
                        -- Log entry into table processing
                        RAISE NOTICE 'Processing table: %.%', schema_name, tbl_name;

                        BEGIN -- Begin try block for each table
                            -- Check if the column "warehousefromdate" exists in the given table
                            IF EXISTS (
                                SELECT 1
                                FROM information_schema.columns
                                WHERE table_schema = schema_name
                                  AND table_name = tbl_name
                                  AND column_name = 'warehousefromdate'
                            ) THEN
                                -- Log that column exists and is being dropped
                                RAISE NOTICE 'Column warehousefromdate found in %.%, dropping column ...', schema_name, tbl_name;
                                EXECUTE FORMAT('ALTER TABLE %I.%I DROP COLUMN warehousefromdate', schema_name, tbl_name);
                            ELSE
                                -- Log that the column does not exist
                                RAISE NOTICE 'Column warehousefromdate not found in %.%', schema_name, tbl_name;
                            END IF;

                            -- Add the column "warehousefromdate" as a regular DATE column
                            RAISE NOTICE 'Adding column warehousefromdate to %.%', schema_name, tbl_name;
                            EXECUTE FORMAT('ALTER TABLE %I.%I ADD COLUMN warehousefromdate DATE', schema_name, tbl_name);

                            -- Check if the "warehousefromdate" column exists before updating
                            IF EXISTS (
                                SELECT 1
                                FROM information_schema.columns
                                WHERE table_schema = schema_name
                                  AND table_name = tbl_name
                                  AND column_name = 'warehousefromdate'
                            ) THEN
                                -- Log that column exists and update is being performed
                                RAISE NOTICE 'Updating column warehousefromdate for %.% using effectivefromdate ...', schema_name, tbl_name;
                                EXECUTE FORMAT(
                                        'UPDATE %I.%I SET warehousefromdate = (effectivefromdate AT TIME ZONE ''UTC'')::DATE',
                                        schema_name, tbl_name
                                        );
                                RAISE NOTICE 'Successfully updated table %.%', schema_name, tbl_name;
                            ELSE
                                -- Log that the update cannot be performed as the column is missing
                                RAISE NOTICE 'Column warehousefromdate not found in %.%, skipping update.', schema_name, tbl_name;
                            END IF;
                        EXCEPTION
                            WHEN OTHERS THEN
                                -- Log the error and continue with the next table
                                RAISE WARNING 'Error processing table %.%: %', schema_name, tbl_name, SQLERRM;
                        END; -- End try block
                    END LOOP;

                -- Log schema processing completion
                RAISE NOTICE 'Finished processing schema: %', schema_name;
            END LOOP;

        -- Log final completion of all processing
        RAISE NOTICE 'All schemas processed successfully!';
    END $$;
