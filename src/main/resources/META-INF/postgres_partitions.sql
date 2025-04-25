
CREATE OR REPLACE FUNCTION dbo.create_partitions(
    schema_name TEXT,
    table_name TEXT,
    partition_by TEXT,
    start_year INT,
    end_year INT,
    is_daily BOOLEAN DEFAULT FALSE
)
    RETURNS VOID AS
$$
DECLARE
    year                     INT;
    month                    INT;
    start_date               DATE;
    end_date                 DATE;
    partition_name           TEXT;
    partition_full_name      TEXT;
    partition_already_exists BOOLEAN;
    table_hash              TEXT;
BEGIN
    -- Create a unique hash for the table name (first 8 characters of MD5)
    table_hash := substr(md5(schema_name || '.' || table_name), 1, 8);

    FOR year IN start_year..end_year
        LOOP
            IF NOT is_daily THEN
                -- Monthly partitioning
                FOR month IN 1..12
                    LOOP
                        start_date := DATE(year || '-' || lpad(month::TEXT, 2, '0') || '-01');
                        end_date := start_date + INTERVAL '1 month';

                        partition_name := format('p_%s_y%s_m%s',
                                                 table_hash,
                                                 year,
                                                 lpad(month::TEXT, 2, '0'));

                        -- Better check for existing partition
                        SELECT EXISTS (
                            SELECT 1
                            FROM pg_class c
                                     JOIN pg_namespace n ON c.relnamespace = n.oid
                                     JOIN pg_inherits i ON c.oid = i.inhrelid
                                     JOIN pg_class parent ON i.inhparent = parent.oid
                            WHERE n.nspname = schema_name
                              AND c.relname = partition_name
                              AND parent.relname = table_name
                        ) INTO partition_already_exists;

                        IF NOT partition_already_exists THEN
                            RAISE NOTICE 'Creating partition: %s for table %.%', partition_name, schema_name, table_name;

                            EXECUTE format('
                            CREATE TABLE %I.%I PARTITION OF %I.%I
                            FOR VALUES FROM (%L) TO (%L)',
                                           schema_name, partition_name,
                                           schema_name, table_name,
                                           start_date, end_date
                                    );
                        ELSE
                            RAISE NOTICE 'Partition already exists: %s. Skipping...', partition_name;
                        END IF;
                    END LOOP;
            ELSE
                -- Daily partitioning
                start_date := DATE(year || '-01-01');
                WHILE start_date < DATE((year + 1) || '-01-01')
                    LOOP
                        end_date := start_date + INTERVAL '1 day';

                        partition_name := format('p_%s_y%s_d%s',
                                                 table_hash,
                                                 year,
                                                 lpad(EXTRACT(DOY FROM start_date)::TEXT, 3, '0'));

                        -- Better check for existing partition
                        SELECT EXISTS (
                            SELECT 1
                            FROM pg_class c
                                     JOIN pg_namespace n ON c.relnamespace = n.oid
                                     JOIN pg_inherits i ON c.oid = i.inhrelid
                                     JOIN pg_class parent ON i.inhparent = parent.oid
                            WHERE n.nspname = schema_name
                              AND c.relname = partition_name
                              AND parent.relname = table_name
                        ) INTO partition_already_exists;

                        IF NOT partition_already_exists THEN
                            RAISE NOTICE 'Creating partition: %s for table %.%', partition_name, schema_name, table_name;

                            EXECUTE format('
                            CREATE TABLE %I.%I PARTITION OF %I.%I
                            FOR VALUES FROM (%L) TO (%L)',
                                           schema_name, partition_name,
                                           schema_name, table_name,
                                           start_date, end_date
                                    );
                        ELSE
                            RAISE NOTICE 'Partition already exists: %s. Skipping...', partition_name;
                        END IF;

                        start_date := end_date;
                    END LOOP;
            END IF;
        END LOOP;
END;
$$ LANGUAGE plpgsql;


DO
$$
    DECLARE
        rec              RECORD;
        schema_rec       RECORD;
        partition_column TEXT   := 'warehousecreateddate';
        start_year       INT    := 2021;  -- Changed to 2023
        end_year         INT    := 2035;
        is_partitioned   BOOLEAN;
        daily_tables     TEXT[] := ARRAY ['eventxclassification', 'resourcexclassification','resourceitemdata','resourceitemdataxclassification','arrangementxclassification'];
        error_count      INT    := 0;
    BEGIN
        -- Loop through all user schemas
        FOR schema_rec IN
            SELECT nspname AS schema_name
            FROM pg_catalog.pg_namespace
            WHERE nspname NOT IN ('pg_catalog', 'information_schema', 'pg_toast')
              AND nspname NOT LIKE 'pg_temp_%'
              AND nspname NOT LIKE 'pg_toast_temp_%'
            LOOP
                -- Loop through all tables in each schema
                FOR rec IN
                    SELECT table_schema, table_name
                    FROM information_schema.tables
                    WHERE table_schema = schema_rec.schema_name
                      AND table_type = 'BASE TABLE'
                      and table_name not ilike 'part_%'
                      AND table_name NOT LIKE '%_y20%'
                       -- and table_name = 'arrangementxarrangementtype'
                    LOOP
                        BEGIN  -- Start of exception block for each table
                        -- Check if the current table is partitioned
                        SELECT EXISTS (SELECT 1
                                       FROM pg_partitioned_table pt
                                                JOIN pg_class c ON pt.partrelid = c.oid
                                                JOIN pg_namespace n ON c.relnamespace = n.oid
                                       WHERE n.nspname = rec.table_schema
                                         AND c.relname = rec.table_name)
                        INTO is_partitioned;

                        -- Skip non-partitioned tables
                        IF NOT is_partitioned THEN
                           -- RAISE NOTICE 'Skipping non-partitioned table or view: %.%', rec.table_schema, rec.table_name;
                            CONTINUE;
                        END IF;

                        RAISE NOTICE 'Processing table: %.%', rec.table_schema, rec.table_name;

                        PERFORM dbo.create_partitions(
                                schema_name := schema_rec.schema_name,
                                table_name := rec.table_name,
                                partition_by := partition_column,
                                start_year := start_year,
                                end_year := end_year,
                                is_daily := rec.table_name = ANY (daily_tables)
                                );

                        EXCEPTION WHEN OTHERS THEN
                            -- Log the error and continue with next table
                            RAISE WARNING 'Error processing table %.%: % (State: %, Error: %)',
                                rec.table_schema,
                                rec.table_name,
                                SQLERRM,
                                SQLSTATE,
                                SQLERRM;
                            error_count := error_count + 1;
                        END;  -- End of exception block
                    END LOOP;
            END LOOP;

        -- Final status report
        IF error_count > 0 THEN
            RAISE NOTICE 'Partition creation completed with % errors. Check the logs for details.', error_count;
        ELSE
            RAISE NOTICE 'Partition creation completed successfully for all tables.';
        END IF;
    END
$$;

