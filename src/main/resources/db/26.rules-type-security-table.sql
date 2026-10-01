-- The original RulesXRulesType security table was created with an extra 's'
-- in both its table name and primary-key column. Match the mapped entity
-- without replacing the table or losing existing grants, indexes and rows.
DO $$
BEGIN
    IF to_regclass('rules.rulesxrulestypessecuritytoken') IS NOT NULL THEN
        IF to_regclass('rules.rulesxrulestypesecuritytoken') IS NOT NULL THEN
            RAISE EXCEPTION 'Both RulesXRulesType security table names exist; manual reconciliation required';
        END IF;
        ALTER TABLE rules.rulesxrulestypessecuritytoken RENAME TO rulesxrulestypesecuritytoken;
        ALTER TABLE rules.rulesxrulestypesecuritytoken
            RENAME COLUMN rulesxrulestypessecuritytokenid TO rulesxrulestypesecuritytokenid;
    END IF;

    IF to_regclass('rules.rulesxrulestypesecuritytoken') IS NULL THEN
        RAISE EXCEPTION 'RulesXRulesType security table is missing';
    END IF;
END
$$;
