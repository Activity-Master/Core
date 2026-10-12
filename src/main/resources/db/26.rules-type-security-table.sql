-- The original RulesXRulesType security table was created with an extra 's'
-- in both its table name and primary-key column. Match the mapped entity
-- without replacing the table or losing existing grants, indexes and rows.
DO $$
BEGIN
    IF to_regclass('rules.rulesxrulestypessecuritytoken') IS NOT NULL THEN
        IF to_regclass('rules.rulesxrulestypesecuritytoken') IS NOT NULL THEN
            -- Replaying script 11 on a Hibernate-created database can leave an
            -- empty legacy table beside the populated mapped table. Lock before
            -- checking so a concurrent insert cannot be discarded by the drop.
            LOCK TABLE rules.rulesxrulestypessecuritytoken IN ACCESS EXCLUSIVE MODE;
            IF EXISTS (SELECT 1 FROM rules.rulesxrulestypessecuritytoken) THEN
                RAISE EXCEPTION 'Both RulesXRulesType security table names exist and the legacy table contains rows; manual reconciliation required';
            END IF;
            DROP TABLE rules.rulesxrulestypessecuritytoken;
        ELSE
            ALTER TABLE rules.rulesxrulestypessecuritytoken RENAME TO rulesxrulestypesecuritytoken;
            ALTER TABLE rules.rulesxrulestypesecuritytoken
                RENAME COLUMN rulesxrulestypessecuritytokenid TO rulesxrulestypesecuritytokenid;
        END IF;
    END IF;

    IF to_regclass('rules.rulesxrulestypesecuritytoken') IS NULL THEN
        RAISE EXCEPTION 'RulesXRulesType security table is missing';
    END IF;
END
$$;
