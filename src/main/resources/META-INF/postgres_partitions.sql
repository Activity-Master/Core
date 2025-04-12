
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
BEGIN
    FOR year IN start_year..end_year
        LOOP
            IF NOT is_daily THEN
                -- Monthly partitioning
                FOR month IN 1..12
                    LOOP
                        start_date := DATE(year || '-' || lpad(month::TEXT, 2, '0') || '-01');
                        end_date := start_date + INTERVAL '1 month';

                        -- Shortened partition name generation
                        partition_name := format('part_%s_y%s_m%s',
                                                 left(table_name, 20), -- Shorten the table name to avoid truncation
                                                 year,
                                                 lpad(month::TEXT, 2, '0'));

                        partition_full_name := format('%I.%I', schema_name, partition_name);


                        -- Check if partition already exists
                        SELECT EXISTS (SELECT 1
                                       FROM pg_inherits
                                                JOIN pg_class ON pg_inherits.inhrelid = pg_class.oid
                                                JOIN pg_namespace ON pg_class.relnamespace = pg_namespace.oid
                                       WHERE pg_class.relname = partition_name
                                         AND pg_namespace.nspname = schema_name)
                        INTO partition_already_exists;

                        IF NOT partition_already_exists THEN
                            -- Print the query to the console
                            RAISE NOTICE 'Creating partition: %s', partition_full_name;

                            EXECUTE format('
                            CREATE TABLE %I.%I PARTITION OF %I.%I
                            FOR VALUES FROM (%L) TO (%L)',
                                           schema_name, partition_name,
                                           schema_name, table_name,
                                           start_date, end_date
                                    );
                        ELSE
                            RAISE NOTICE 'Partition already exists: %s. Skipping...', partition_full_name;
                        END IF;
                    END LOOP;
            ELSE
                -- Daily partitioning
                start_date := DATE(year || '-01-01');
                WHILE start_date < DATE((year + 1) || '-01-01')
                    LOOP
                        end_date := start_date + INTERVAL '1 day';

                        -- Generate partition name
                        partition_name := format('%s_y%s_d%s', table_name, year,
                                                 lpad(EXTRACT(DOY FROM start_date)::TEXT, 3, '0'));
                        partition_full_name := format('%I.%I', schema_name, partition_name);

                        -- Check if partition already exists
                        SELECT EXISTS (SELECT 1
                                       FROM pg_inherits
                                                JOIN pg_class ON pg_inherits.inhrelid = pg_class.oid
                                                JOIN pg_namespace ON pg_class.relnamespace = pg_namespace.oid
                                       WHERE pg_class.relname = partition_name
                                         AND pg_namespace.nspname = schema_name)
                        INTO partition_already_exists;

                        IF NOT partition_already_exists THEN
                            -- Print the query to the console
                            RAISE NOTICE 'Creating partition: %s', partition_full_name;

                            -- Execute the query

                            EXECUTE format('
                            CREATE TABLE %I.%I PARTITION OF %I.%I
                            FOR VALUES FROM (%L) TO (%L)',
                                           schema_name, partition_name,
                                           schema_name, table_name,
                                           start_date, end_date
                                    );
                        ELSE
                            RAISE NOTICE 'Partition already exists: %s. Skipping...', partition_full_name;
                        END IF;

                        -- Increment start_date
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
        partition_column TEXT   := 'warehousecreateddate'; -- Adjust column as needed
        start_year       INT    := 2022;
        end_year         INT    := 2022;
        is_partitioned   BOOLEAN;
        daily_tables     TEXT[] := ARRAY ['eventxclassification', 'resourcexclassification','resourceitemdata','resourceitemdataxclassification','arrangementxclassification'];
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
                    LOOP
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
                            RAISE NOTICE 'Skipping non-partitioned table or view: %.%', rec.table_schema, rec.table_name;
                            CONTINUE;
                        END IF;
                        PERFORM dbo.create_partitions(
                                schema_name := schema_rec.schema_name,
                                table_name := rec.table_name,
                                partition_by := partition_column,
                                start_year := start_year,
                                end_year := end_year,
                                is_daily := rec.table_name = ANY (daily_tables)
                                );
                    END LOOP;
            END LOOP;
    END
$$;



DO
$$
    DECLARE
        rec              RECORD;
        schema_rec       RECORD;
        partition_column TEXT   := 'warehousecreateddate'; -- Adjust column as needed
        start_year       INT    := 2023;
        end_year         INT    := 2023;
        is_partitioned   BOOLEAN;
        daily_tables     TEXT[] := ARRAY ['eventxclassification', 'resourcexclassification','resourceitemdata','resourceitemdataxclassification','arrangementxclassification'];
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
                    LOOP
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
                            RAISE NOTICE 'Skipping non-partitioned table or view: %.%', rec.table_schema, rec.table_name;
                            CONTINUE;
                        END IF;
                        PERFORM dbo.create_partitions(
                                schema_name := schema_rec.schema_name,
                                table_name := rec.table_name,
                                partition_by := partition_column,
                                start_year := start_year,
                                end_year := end_year,
                                is_daily := rec.table_name = ANY (daily_tables)
                                );
                    END LOOP;
            END LOOP;
    END
$$;


DO
$$
    DECLARE
        rec              RECORD;
        schema_rec       RECORD;
        partition_column TEXT   := 'warehousecreateddate'; -- Adjust column as needed
        start_year       INT    := 2024;
        end_year         INT    := 2024;
        is_partitioned   BOOLEAN;
        daily_tables     TEXT[] := ARRAY ['eventxclassification', 'resourcexclassification','resourceitemdata','resourceitemdataxclassification','arrangementxclassification'];
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
                    LOOP
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
                            RAISE NOTICE 'Skipping non-partitioned table or view: %.%', rec.table_schema, rec.table_name;
                            CONTINUE;
                        END IF;
                        PERFORM dbo.create_partitions(
                                schema_name := schema_rec.schema_name,
                                table_name := rec.table_name,
                                partition_by := partition_column,
                                start_year := start_year,
                                end_year := end_year,
                                is_daily := rec.table_name = ANY (daily_tables)
                                );
                    END LOOP;
            END LOOP;
    END
$$;


DO
$$
    DECLARE
        rec              RECORD;
        schema_rec       RECORD;
        partition_column TEXT   := 'warehousecreateddate'; -- Adjust column as needed
        start_year       INT    := 2025;
        end_year         INT    := 2025;
        is_partitioned   BOOLEAN;
        daily_tables     TEXT[] := ARRAY ['eventxclassification', 'resourcexclassification','resourceitemdata','resourceitemdataxclassification','arrangementxclassification'];
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
                    LOOP
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
                            RAISE NOTICE 'Skipping non-partitioned table or view: %.%', rec.table_schema, rec.table_name;
                            CONTINUE;
                        END IF;
                        PERFORM dbo.create_partitions(
                                schema_name := schema_rec.schema_name,
                                table_name := rec.table_name,
                                partition_by := partition_column,
                                start_year := start_year,
                                end_year := end_year,
                                is_daily := rec.table_name = ANY (daily_tables)
                                );
                    END LOOP;
            END LOOP;
    END
$$;



DO
$$
    DECLARE
        rec              RECORD;
        schema_rec       RECORD;
        partition_column TEXT   := 'warehousecreateddate'; -- Adjust column as needed
        start_year       INT    := 2026;
        end_year         INT    := 2026;
        is_partitioned   BOOLEAN;
        daily_tables     TEXT[] := ARRAY ['eventxclassification', 'resourcexclassification','resourceitemdata','resourceitemdataxclassification','arrangementxclassification'];
    BEGIN
        -- Loop through all user schemas
        FOR schema_rec IN
            SELECT nspname AS schema_name
            FROM pg_catalog.pg_namespace
            WHERE nspname NOT IN ('pg_catalog', 'information_schema', 'pg_toast', 'public')
              AND nspname NOT LIKE 'pg_temp_%'
              AND nspname NOT LIKE 'pg_toast_temp_%'
            LOOP
                -- Loop through all tables in each schema
                FOR rec IN
                    SELECT table_schema, table_name
                    FROM information_schema.tables
                    WHERE table_schema = schema_rec.schema_name
                      AND table_type = 'BASE TABLE'
                    LOOP
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
                            RAISE NOTICE 'Skipping non-partitioned table or view: %.%', rec.table_schema, rec.table_name;
                            CONTINUE;
                        END IF;
                        PERFORM dbo.create_partitions(
                                schema_name := schema_rec.schema_name,
                                table_name := rec.table_name,
                                partition_by := partition_column,
                                start_year := start_year,
                                end_year := end_year,
                                is_daily := rec.table_name = ANY (daily_tables)
                                );
                    END LOOP;
            END LOOP;
    END
$$;
