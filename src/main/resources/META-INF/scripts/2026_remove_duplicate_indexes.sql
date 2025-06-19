-- PostgreSQL 17 Script to Remove Duplicate Indexes
-- This script identifies and removes duplicate indexes based on their structure
-- (table, columns, type, uniqueness) rather than just name patterns

-- Step 1: Create a function to generate DROP statements for duplicate indexes
CREATE OR REPLACE FUNCTION remove_duplicate_indexes(dry_run BOOLEAN DEFAULT TRUE)
RETURNS TABLE(
    action_type TEXT,
    schema_name TEXT,
    index_name TEXT,
    table_name TEXT,
    columns TEXT,
    reason TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    drop_statement TEXT;
    rec RECORD;
BEGIN
    -- Log start
    RAISE NOTICE 'Starting duplicate index analysis (dry_run: %)', dry_run;

    -- Find duplicate indexes based on structure
    FOR rec IN
        WITH index_details AS (
            SELECT
                schemaname,
                tablename,
                indexname,
                indexdef,
                -- Extract column list from index definition
                REGEXP_REPLACE(
                    REGEXP_REPLACE(indexdef, '.*\((.*)\).*', '\1'),
                    '\s+', ' ', 'g'
                ) as column_list,
                -- Determine if unique
                CASE WHEN indexdef LIKE '%UNIQUE%' THEN 'UNIQUE' ELSE 'NON_UNIQUE' END as uniqueness,
                -- Extract index type (btree, gin, gist, etc.)
                CASE
                    WHEN indexdef LIKE '%USING btree%' THEN 'btree'
                    WHEN indexdef LIKE '%USING hash%' THEN 'hash'
                    WHEN indexdef LIKE '%USING gin%' THEN 'gin'
                    WHEN indexdef LIKE '%USING gist%' THEN 'gist'
                    WHEN indexdef LIKE '%USING spgist%' THEN 'spgist'
                    WHEN indexdef LIKE '%USING brin%' THEN 'brin'
                    ELSE 'btree'  -- default
                END as index_type
            FROM pg_indexes
            WHERE schemaname NOT IN ('information_schema', 'pg_catalog', 'pg_toast')
        ),
        duplicate_groups AS (
            SELECT
                schemaname,
                tablename,
                column_list,
                uniqueness,
                index_type,
                array_agg(indexname ORDER BY
                    -- Prefer indexes without numeric suffixes
                    CASE WHEN indexname ~ '\d+$' THEN 1 ELSE 0 END,
                    -- Then prefer shorter names
                    length(indexname),
                    -- Finally alphabetical
                    indexname
                ) as index_names,
                count(*) as duplicate_count
            FROM index_details
            GROUP BY schemaname, tablename, column_list, uniqueness, index_type
            HAVING count(*) > 1
        )
        SELECT
            schemaname,
            tablename,
            column_list,
            uniqueness,
            index_type,
            index_names,
            duplicate_count
        FROM duplicate_groups
        ORDER BY schemaname, tablename, column_list
    LOOP
        -- Keep the first index (preferred name), drop the rest
        FOR i IN 2..array_length(rec.index_names, 1) LOOP
            IF dry_run THEN
                -- Return information about what would be dropped
                action_type := 'DROP (DRY RUN)';
                schema_name := rec.schemaname;
                index_name := rec.index_names[i];
                table_name := rec.tablename;
                columns := rec.column_list;
                reason := format('Duplicate of %s (keeping %s)',
                               rec.index_names[i], rec.index_names[1]);
                RETURN NEXT;
            ELSE
                -- Actually drop the duplicate index
                drop_statement := format('DROP INDEX IF EXISTS %I.%I;',
                                       rec.schemaname, rec.index_names[i]);

                BEGIN
                    EXECUTE drop_statement;

                    action_type := 'DROPPED';
                    schema_name := rec.schemaname;
                    index_name := rec.index_names[i];
                    table_name := rec.tablename;
                    columns := rec.column_list;
                    reason := format('Duplicate of %s (kept %s)',
                                   rec.index_names[i], rec.index_names[1]);
                    RETURN NEXT;

                    RAISE NOTICE 'Dropped duplicate index: %.%', rec.schemaname, rec.index_names[i];
                EXCEPTION
                    WHEN OTHERS THEN
                        action_type := 'ERROR';
                        schema_name := rec.schemaname;
                        index_name := rec.index_names[i];
                        table_name := rec.tablename;
                        columns := rec.column_list;
                        reason := format('Failed to drop: %s', SQLERRM);
                        RETURN NEXT;

                        RAISE WARNING 'Failed to drop index %.%: %',
                                    rec.schemaname, rec.index_names[i], SQLERRM;
                END;
            END IF;
        END LOOP;
    END LOOP;

    RAISE NOTICE 'Duplicate index analysis complete';
END;
$$;

-- Step 2: Run in DRY RUN mode first to see what would be removed
SELECT
    action_type,
    schema_name,
    table_name,
    index_name,
    columns,
    reason
FROM remove_duplicate_indexes(dry_run := TRUE)
ORDER BY schema_name, table_name, index_name;

-- Step 3: Uncomment the line below to actually remove the duplicates
-- (Only run this after reviewing the dry run results!)
SELECT * FROM remove_duplicate_indexes(dry_run := FALSE);

-- Step 4: Alternative simple approach for name-based duplicates
-- If you specifically want to target indexes with numeric suffixes:


-- Simple approach: Drop indexes ending with numbers (be very careful!)
DO $$
DECLARE
    rec RECORD;
    drop_stmt TEXT;
BEGIN
    RAISE NOTICE 'Finding indexes with numeric suffixes...';

    FOR rec IN
        SELECT schemaname, indexname, tablename
        FROM pg_indexes
        WHERE schemaname NOT IN ('information_schema', 'pg_catalog', 'pg_toast')
        AND indexname ~ '\d+$'  -- ends with one or more digits
        AND EXISTS (
            -- Only drop if a similar index without suffix exists
            SELECT 1 FROM pg_indexes pi2
            WHERE pi2.schemaname = pg_indexes.schemaname
            AND pi2.tablename = pg_indexes.tablename
            AND pi2.indexname = REGEXP_REPLACE(pg_indexes.indexname, '\d+$', '')
        )
        ORDER BY schemaname, tablename, indexname
    LOOP
        drop_stmt := format('DROP INDEX IF EXISTS %I.%I', rec.schemaname, rec.indexname);
        RAISE NOTICE 'Would execute: %', drop_stmt;

        -- Uncomment the next line to actually drop:
         EXECUTE drop_stmt;
    END LOOP;
END;
$$;


-- Step 5: Clean up the function after use (optional)
-- DROP FUNCTION IF EXISTS remove_duplicate_indexes(BOOLEAN);

-- Additional utility: View current index summary
SELECT
    schemaname,
    tablename,
    count(*) as index_count,
    string_agg(indexname, ', ' ORDER BY indexname) as index_names
FROM pg_indexes
WHERE schemaname NOT IN ('information_schema', 'pg_catalog', 'pg_toast')
GROUP BY schemaname, tablename
HAVING count(*) > 3  -- Tables with many indexes
ORDER BY count(*) DESC, schemaname, tablename;