-- Managed migration after the canonical FSDM schema. PostgreSQL identifiers
-- below match the existing lowercase ActivityMaster/NE1 database.
CREATE SCHEMA IF NOT EXISTS transactions;

CREATE TABLE IF NOT EXISTS transactions.transaction_type
(
    transaction_type_id uuid PRIMARY KEY,
    enterprise_id       uuid     NOT NULL,
    code                text     NOT NULL CHECK (code ~ '^[a-z][a-z0-9_.-]{0,79}$'),
    direction           smallint NOT NULL CHECK (direction IN (-1, 1)),
    active              boolean  NOT NULL DEFAULT true,
    UNIQUE (enterprise_id, code),
    UNIQUE (transaction_type_id, direction)
);

-- The existing Event owns the business action. Retry identity lives on its
-- immutable transaction lines; participants are classified FSDM relationships.
CREATE TABLE IF NOT EXISTS transactions.entry
(
    entry_id            uuid PRIMARY KEY,
    event_id            uuid           NOT NULL,
    enterprise_id       uuid           NOT NULL,
    operation_key       uuid           NOT NULL,
    line_no             integer        NOT NULL CHECK (line_no > 0),
    arrangement_id      uuid           NOT NULL ,
    transaction_type_id uuid           NOT NULL,
    direction           smallint       NOT NULL,
    amount              numeric(38, 8) NOT NULL CHECK (amount > 0),
    unit                text           NOT NULL CHECK (unit ~ '^[A-Z][A-Z0-9_]{0,15}$'),
    signed_amount       numeric(39, 8) GENERATED ALWAYS AS (amount * direction) STORED,
    created_at          timestamptz    NOT NULL DEFAULT now(),
    UNIQUE (event_id, line_no),
    UNIQUE (enterprise_id, operation_key, line_no),
    FOREIGN KEY (transaction_type_id, direction)
        REFERENCES transactions.transaction_type (transaction_type_id, direction)
);
CREATE INDEX IF NOT EXISTS transaction_entry_arrangement_unit
    ON transactions.entry (arrangement_id, unit, event_id);

-- FSDM warehouse metadata for the ActivityMaster/EntityAssist mappings. These
-- columns match the other ActivityMaster warehouse domains.
ALTER TABLE transactions.transaction_type
    ADD COLUMN IF NOT EXISTS description                   text NOT NULL DEFAULT '',
    ADD COLUMN IF NOT EXISTS effectivefromdate             timestamptz,
    ADD COLUMN IF NOT EXISTS effectivetodate               timestamptz,
    ADD COLUMN IF NOT EXISTS warehousecreatedtimestamp     timestamptz,
    ADD COLUMN IF NOT EXISTS warehouselastupdatedtimestamp timestamptz,
    ADD COLUMN IF NOT EXISTS warehousefromdate             date,
    ADD COLUMN IF NOT EXISTS originalsourcesystemuniqueid  uuid,
    ADD COLUMN IF NOT EXISTS originalsourcesystemid        uuid,
    ADD COLUMN IF NOT EXISTS activeflagid                  uuid,
    ADD COLUMN IF NOT EXISTS systemid                      uuid;
ALTER TABLE transactions.entry
    ADD COLUMN IF NOT EXISTS effectivefromdate             timestamptz,
    ADD COLUMN IF NOT EXISTS effectivetodate               timestamptz,
    ADD COLUMN IF NOT EXISTS warehousecreatedtimestamp     timestamptz,
    ADD COLUMN IF NOT EXISTS warehouselastupdatedtimestamp timestamptz,
    ADD COLUMN IF NOT EXISTS warehousefromdate             date,
    ADD COLUMN IF NOT EXISTS originalsourcesystemuniqueid  uuid,
    ADD COLUMN IF NOT EXISTS originalsourcesystemid        uuid,
    ADD COLUMN IF NOT EXISTS activeflagid                  uuid,
    ADD COLUMN IF NOT EXISTS systemid                      uuid;

-- Bounded wallet history is ordered newest first within one arrangement/unit.
CREATE INDEX IF NOT EXISTS transaction_entry_history
    ON transactions.entry (arrangement_id, unit, warehousecreatedtimestamp DESC, entry_id);

CREATE TABLE IF NOT EXISTS transactions.transaction_x_transaction_type
(
    transaction_x_transaction_type_id uuid PRIMARY KEY,
    entry_id                          uuid        NOT NULL,
    transaction_type_id               uuid        NOT NULL,
    classificationid                  uuid        NOT NULL,
    enterprise_id                     uuid        NOT NULL,
    value                             text        NOT NULL DEFAULT '1',
    effectivefromdate                 timestamptz NOT NULL DEFAULT now(),
    effectivetodate                   timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp         timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                 date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid      uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid            uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                      uuid        NOT NULL,
    systemid                          uuid        NOT NULL,
    UNIQUE (entry_id, transaction_type_id)
);

CREATE TABLE IF NOT EXISTS transactions.transaction_type_security_token
(
    transaction_type_security_token_id uuid PRIMARY KEY,
    transaction_type_id                uuid        NOT NULL,
    enterprise_id                      uuid        NOT NULL,
    securitytokenid                    uuid        NOT NULL,
    createallowed                      integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                      integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                      integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                        integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                  timestamptz NOT NULL DEFAULT now(),
    effectivetodate                    timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp          timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp      timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                  date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid       uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid             uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                       uuid        NOT NULL,
    systemid                           uuid        NOT NULL,
    UNIQUE (transaction_type_id, securitytokenid)
);

CREATE TABLE IF NOT EXISTS transactions.entry_security_token
(
    entry_security_token_id       uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    securitytokenid               uuid        NOT NULL,
    createallowed                 integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                 integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                 integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                   integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, securitytokenid)
);

CREATE TABLE IF NOT EXISTS transactions.transaction_x_transaction_type_security_token
(
    transaction_x_transaction_type_security_token_id uuid PRIMARY KEY,
    transaction_x_transaction_type_id                uuid        NOT NULL REFERENCES transactions.transaction_x_transaction_type (transaction_x_transaction_type_id),
    enterprise_id                                    uuid        NOT NULL,
    securitytokenid                                  uuid        NOT NULL,
    createallowed                                    integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                                    integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                                    integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                                      integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                                timestamptz NOT NULL DEFAULT now(),
    effectivetodate                                  timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                        timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp                    timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                                date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid                     uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                           uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                                     uuid        NOT NULL,
    systemid                                         uuid        NOT NULL,
    UNIQUE (transaction_x_transaction_type_id, securitytokenid)
);

CREATE OR REPLACE FUNCTION transactions.validate_type_link() RETURNS trigger
    LANGUAGE plpgsql AS
$function$
BEGIN
    IF NOT EXISTS (SELECT 1
                   FROM transactions.entry e
                   WHERE e.entry_id = NEW.entry_id
                     AND e.transaction_type_id = NEW.transaction_type_id
                     AND e.enterprise_id = NEW.enterprise_id) THEN
        RAISE EXCEPTION 'Transaction type relationship mismatch' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END
$function$;
DROP TRIGGER IF EXISTS transaction_type_link_validate ON transactions.transaction_x_transaction_type;
CREATE TRIGGER transaction_type_link_validate
    BEFORE INSERT
    ON transactions.transaction_x_transaction_type
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_type_link();

-- Enforce FSDM tenant, event and arrangement relationships even if a writer
-- bypasses the Java service. Current actor and token permissions remain service
-- checks because the database row carries no authenticated end-user identity.
CREATE OR REPLACE FUNCTION transactions.validate_posting() RETURNS trigger
    LANGUAGE plpgsql AS
$function$
DECLARE
    owner_enterprise uuid;
BEGIN
    -- Lock the business Event, including direct SQL writers. Previously committed
    -- lines seal an Event; all new lines must share one retry key in one SQL transaction.
    SELECT enterpriseid INTO owner_enterprise FROM event.event WHERE eventid = NEW.event_id FOR UPDATE;
    IF NEW.enterprise_id IS DISTINCT FROM owner_enterprise THEN
        RAISE EXCEPTION 'Transaction enterprise mismatch' USING ERRCODE = '23514';
    END IF;
    IF EXISTS (SELECT 1
               FROM transactions.entry e
               WHERE e.event_id = NEW.event_id
                 AND (e.operation_key <> NEW.operation_key OR e.xmin::text <> pg_current_xact_id()::text)) THEN
        RAISE EXCEPTION 'Transaction Event already posted or retry key mismatch' USING ERRCODE = '23514';
    END IF;
    IF NOT EXISTS (SELECT 1
                   FROM arrangement.arrangement a
                            JOIN arrangement.arrangementxarrangementtype xt ON xt.arrangementid = a.arrangementid
                       AND xt.enterpriseid = a.enterpriseid
                            JOIN arrangement.arrangementtype t ON t.arrangementtypeid = xt.arrangementtypeid
                       AND t.enterpriseid = a.enterpriseid
                            JOIN event.eventxarrangement ea ON ea.arrangementid = a.arrangementid
                       AND ea.enterpriseid = a.enterpriseid AND ea.eventid = NEW.event_id
                   WHERE a.arrangementid = NEW.arrangement_id
                     AND a.enterpriseid = owner_enterprise
                     AND t.arrangementtypename IN ('Wallet', 'Wallet Clearing')
                     AND a.effectivefromdate <= statement_timestamp()
                     AND a.effectivetodate > statement_timestamp()
                     AND xt.effectivefromdate <= statement_timestamp()
                     AND xt.effectivetodate > statement_timestamp()
                     AND ea.effectivefromdate <= statement_timestamp()
                     AND ea.effectivetodate > statement_timestamp())
        OR NOT EXISTS (SELECT 1
                       FROM transactions.transaction_type
                       WHERE transaction_type_id = NEW.transaction_type_id
                         AND enterprise_id = owner_enterprise
                         AND direction = NEW.direction
                         AND active = true) THEN
        RAISE EXCEPTION 'Transaction entry FSDM relationship denied' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END
$function$;
DROP TRIGGER IF EXISTS transaction_entry_validate ON transactions.entry;
CREATE TRIGGER transaction_entry_validate
    BEFORE INSERT
    ON transactions.entry
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_posting();

-- Require one positive and one negative movement and a zero sum for every unit
-- at commit. A deposit or withdrawal therefore includes a clearing Arrangement.
CREATE OR REPLACE FUNCTION transactions.check_balanced_posting() RETURNS trigger
    LANGUAGE plpgsql AS
$function$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM transactions.entry WHERE event_id = NEW.event_id)
        OR EXISTS (SELECT 1
                   FROM transactions.entry
                   WHERE event_id = NEW.event_id
                   GROUP BY unit
                   HAVING count(*) < 2
                       OR sum(signed_amount) <> 0
                       OR count(*) FILTER (WHERE direction = 1) = 0
                       OR count(*) FILTER (WHERE direction = -1) = 0) THEN
        RAISE EXCEPTION 'Unbalanced transaction event %, lines %, sum %', NEW.event_id,
            (SELECT count(*) FROM transactions.entry WHERE event_id = NEW.event_id),
            (SELECT sum(signed_amount) FROM transactions.entry WHERE event_id = NEW.event_id)
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END
$function$;
DROP TRIGGER IF EXISTS transaction_event_balanced ON transactions.entry;
CREATE CONSTRAINT TRIGGER transaction_event_balanced
    AFTER INSERT
    ON transactions.entry DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW
EXECUTE FUNCTION transactions.check_balanced_posting();

-- Posted history is corrected by a new reversing Event, not an update/delete.
CREATE OR REPLACE FUNCTION transactions.reject_posted_change() RETURNS trigger
    LANGUAGE plpgsql AS
$function$
BEGIN
    RAISE EXCEPTION 'Posted transactions are immutable' USING ERRCODE = '23514';
END
$function$;
DROP TRIGGER IF EXISTS transaction_entry_immutable ON transactions.entry;
CREATE TRIGGER transaction_entry_immutable
    BEFORE UPDATE OR DELETE
    ON transactions.entry
    FOR EACH ROW
EXECUTE FUNCTION transactions.reject_posted_change();
DROP TRIGGER IF EXISTS transaction_type_link_immutable ON transactions.transaction_x_transaction_type;
CREATE TRIGGER transaction_type_link_immutable
    BEFORE UPDATE OR DELETE
    ON transactions.transaction_x_transaction_type
    FOR EACH ROW
EXECUTE FUNCTION transactions.reject_posted_change();
REVOKE ALL ON transactions.transaction_type,transactions.entry FROM PUBLIC;
REVOKE ALL ON transactions.transaction_x_transaction_type,
    transactions.transaction_type_security_token,transactions.entry_security_token,
    transactions.transaction_x_transaction_type_security_token FROM PUBLIC;
REVOKE ALL ON FUNCTION transactions.validate_posting(),transactions.check_balanced_posting(),
    transactions.reject_posted_change(),transactions.validate_type_link() FROM PUBLIC;


-- Classified relationships to the existing FSDM domains.
CREATE TABLE IF NOT EXISTS transactions.transaction_x_involved_party
(
    transaction_x_involved_party_id uuid PRIMARY KEY,
    entry_id                        uuid        NOT NULL,
    involved_party_id               uuid        NOT NULL,
    classificationid                uuid        NOT NULL,
    enterprise_id                   uuid        NOT NULL,
    value                           text        NOT NULL DEFAULT '1',
    effectivefromdate               timestamptz NOT NULL DEFAULT now(),
    effectivetodate                 timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp       timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp   timestamptz NOT NULL DEFAULT now(),
    warehousefromdate               date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid    uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid          uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                    uuid        NOT NULL,
    systemid                        uuid        NOT NULL,
    UNIQUE (entry_id, involved_party_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_involved_party_entry ON transactions.transaction_x_involved_party (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_involved_party_security_token
(
    transaction_x_involved_party_security_token_id uuid PRIMARY KEY,
    transaction_x_involved_party_id                uuid        NOT NULL,
    enterprise_id                                  uuid        NOT NULL,
    securitytokenid                                uuid        NOT NULL,
    createallowed                                  integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                                  integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                                  integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                                    integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                              timestamptz NOT NULL DEFAULT now(),
    effectivetodate                                timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                      timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp                  timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                              date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid                   uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                         uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                                   uuid        NOT NULL,
    systemid                                       uuid        NOT NULL,
    UNIQUE (transaction_x_involved_party_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_involved_party, transactions.transaction_x_involved_party_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_resource_item
(
    transaction_x_resource_item_id uuid PRIMARY KEY,
    entry_id                       uuid        NOT NULL,
    resource_item_id               uuid        NOT NULL,
    classificationid               uuid        NOT NULL,
    enterprise_id                  uuid        NOT NULL,
    value                          text        NOT NULL DEFAULT '1',
    effectivefromdate              timestamptz NOT NULL DEFAULT now(),
    effectivetodate                timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp      timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp  timestamptz NOT NULL DEFAULT now(),
    warehousefromdate              date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid   uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid         uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                   uuid        NOT NULL,
    systemid                       uuid        NOT NULL,
    UNIQUE (entry_id, resource_item_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_resource_item_entry ON transactions.transaction_x_resource_item (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_resource_item_security_token
(
    transaction_x_resource_item_security_token_id uuid PRIMARY KEY,
    transaction_x_resource_item_id                uuid        NOT NULL,
    enterprise_id                                 uuid        NOT NULL,
    securitytokenid                               uuid        NOT NULL,
    createallowed                                 integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                                 integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                                 integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                                   integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                             timestamptz NOT NULL DEFAULT now(),
    effectivetodate                               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp                 timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid                  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                                  uuid        NOT NULL,
    systemid                                      uuid        NOT NULL,
    UNIQUE (transaction_x_resource_item_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_resource_item, transactions.transaction_x_resource_item_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_arrangement
(
    transaction_x_arrangement_id  uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    arrangement_id                uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, arrangement_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_arrangement_entry ON transactions.transaction_x_arrangement (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_arrangement_security_token
(
    transaction_x_arrangement_security_token_id uuid PRIMARY KEY,
    transaction_x_arrangement_id                uuid        NOT NULL,
    enterprise_id                               uuid        NOT NULL,
    securitytokenid                             uuid        NOT NULL,
    createallowed                               integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                               integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                               integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                                 integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                           timestamptz NOT NULL DEFAULT now(),
    effectivetodate                             timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                   timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp               timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                           date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid                uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                      uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                                uuid        NOT NULL,
    systemid                                    uuid        NOT NULL,
    UNIQUE (transaction_x_arrangement_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_arrangement, transactions.transaction_x_arrangement_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_event
(
    transaction_x_event_id        uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    event_id                      uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, event_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_event_entry ON transactions.transaction_x_event (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_event_security_token
(
    transaction_x_event_security_token_id uuid PRIMARY KEY,
    transaction_x_event_id                uuid        NOT NULL,
    enterprise_id                         uuid        NOT NULL,
    securitytokenid                       uuid        NOT NULL,
    createallowed                         integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                         integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                         integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                           integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                     timestamptz NOT NULL DEFAULT now(),
    effectivetodate                       timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp             timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp         timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                     date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid          uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                          uuid        NOT NULL,
    systemid                              uuid        NOT NULL,
    UNIQUE (transaction_x_event_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_event, transactions.transaction_x_event_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_product
(
    transaction_x_product_id      uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    product_id                    uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, product_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_product_entry ON transactions.transaction_x_product (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_product_security_token
(
    transaction_x_product_security_token_id uuid PRIMARY KEY,
    transaction_x_product_id                uuid        NOT NULL,
    enterprise_id                           uuid        NOT NULL,
    securitytokenid                         uuid        NOT NULL,
    createallowed                           integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                           integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                           integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                             integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                       timestamptz NOT NULL DEFAULT now(),
    effectivetodate                         timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp               timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp           timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                       date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid            uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                            uuid        NOT NULL,
    systemid                                uuid        NOT NULL,
    UNIQUE (transaction_x_product_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_product, transactions.transaction_x_product_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_address
(
    transaction_x_address_id      uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    address_id                    uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, address_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_address_entry ON transactions.transaction_x_address (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_address_security_token
(
    transaction_x_address_security_token_id uuid PRIMARY KEY,
    transaction_x_address_id                uuid        NOT NULL,
    enterprise_id                           uuid        NOT NULL,
    securitytokenid                         uuid        NOT NULL,
    createallowed                           integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                           integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                           integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                             integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                       timestamptz NOT NULL DEFAULT now(),
    effectivetodate                         timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp               timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp           timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                       date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid            uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                            uuid        NOT NULL,
    systemid                                uuid        NOT NULL,
    UNIQUE (transaction_x_address_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_address, transactions.transaction_x_address_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_geography
(
    transaction_x_geography_id    uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    geography_id                  uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, geography_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_geography_entry ON transactions.transaction_x_geography (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_geography_security_token
(
    transaction_x_geography_security_token_id uuid PRIMARY KEY,
    transaction_x_geography_id                uuid        NOT NULL,
    enterprise_id                             uuid        NOT NULL,
    securitytokenid                           uuid        NOT NULL,
    createallowed                             integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                             integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                             integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                               integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                         timestamptz NOT NULL DEFAULT now(),
    effectivetodate                           timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                 timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp             timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                         date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid              uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                    uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                              uuid        NOT NULL,
    systemid                                  uuid        NOT NULL,
    UNIQUE (transaction_x_geography_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_geography, transactions.transaction_x_geography_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_rules
(
    transaction_x_rules_id        uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    rules_id                      uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, rules_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_rules_entry ON transactions.transaction_x_rules (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_rules_security_token
(
    transaction_x_rules_security_token_id uuid PRIMARY KEY,
    transaction_x_rules_id                uuid        NOT NULL,
    enterprise_id                         uuid        NOT NULL,
    securitytokenid                       uuid        NOT NULL,
    createallowed                         integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                         integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                         integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                           integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                     timestamptz NOT NULL DEFAULT now(),
    effectivetodate                       timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp             timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp         timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                     date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid          uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                          uuid        NOT NULL,
    systemid                              uuid        NOT NULL,
    UNIQUE (transaction_x_rules_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_rules, transactions.transaction_x_rules_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_transaction
(
    transaction_x_transaction_id  uuid PRIMARY KEY,
    entry_id                      uuid        NOT NULL,
    transaction_id                uuid        NOT NULL,
    classificationid              uuid        NOT NULL,
    enterprise_id                 uuid        NOT NULL,
    value                         text        NOT NULL DEFAULT '1',
    effectivefromdate             timestamptz NOT NULL DEFAULT now(),
    effectivetodate               timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp     timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp timestamptz NOT NULL DEFAULT now(),
    warehousefromdate             date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid  uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid        uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                  uuid        NOT NULL,
    systemid                      uuid        NOT NULL,
    UNIQUE (entry_id, transaction_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_transaction_entry ON transactions.transaction_x_transaction (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_transaction_security_token
(
    transaction_x_transaction_security_token_id uuid PRIMARY KEY,
    transaction_x_transaction_id                uuid        NOT NULL,
    enterprise_id                               uuid        NOT NULL,
    securitytokenid                             uuid        NOT NULL,
    createallowed                               integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                               integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                               integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                                 integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                           timestamptz NOT NULL DEFAULT now(),
    effectivetodate                             timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                   timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp               timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                           date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid                uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                      uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                                uuid        NOT NULL,
    systemid                                    uuid        NOT NULL,
    UNIQUE (transaction_x_transaction_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_transaction, transactions.transaction_x_transaction_security_token FROM PUBLIC;

CREATE TABLE IF NOT EXISTS transactions.transaction_x_classification
(
    transaction_x_classification_id uuid PRIMARY KEY,
    entry_id                        uuid        NOT NULL,
    classificationid                uuid        NOT NULL,
    enterprise_id                   uuid        NOT NULL,
    value                           text        NOT NULL DEFAULT '1',
    effectivefromdate               timestamptz NOT NULL DEFAULT now(),
    effectivetodate                 timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp       timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp   timestamptz NOT NULL DEFAULT now(),
    warehousefromdate               date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid    uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid          uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                    uuid        NOT NULL,
    systemid                        uuid        NOT NULL,
    UNIQUE (entry_id, classificationid, effectivefromdate)
);
CREATE INDEX IF NOT EXISTS transaction_x_classification_entry ON transactions.transaction_x_classification (entry_id);
CREATE TABLE IF NOT EXISTS transactions.transaction_x_classification_security_token
(
    transaction_x_classification_security_token_id uuid PRIMARY KEY,
    transaction_x_classification_id                uuid        NOT NULL,
    enterprise_id                                  uuid        NOT NULL,
    securitytokenid                                uuid        NOT NULL,
    createallowed                                  integer     NOT NULL CHECK (createallowed IN (0, 1)),
    updateallowed                                  integer     NOT NULL CHECK (updateallowed IN (0, 1)),
    deleteallowed                                  integer     NOT NULL CHECK (deleteallowed IN (0, 1)),
    readallowed                                    integer     NOT NULL CHECK (readallowed IN (0, 1)),
    effectivefromdate                              timestamptz NOT NULL DEFAULT now(),
    effectivetodate                                timestamptz NOT NULL DEFAULT '2999-12-31 23:59:59.999+00',
    warehousecreatedtimestamp                      timestamptz NOT NULL DEFAULT now(),
    warehouselastupdatedtimestamp                  timestamptz NOT NULL DEFAULT now(),
    warehousefromdate                              date        NOT NULL DEFAULT current_date,
    originalsourcesystemuniqueid                   uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    originalsourcesystemid                         uuid        NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',
    activeflagid                                   uuid        NOT NULL,
    systemid                                       uuid        NOT NULL,
    UNIQUE (transaction_x_classification_id, securitytokenid)
);

REVOKE ALL ON transactions.transaction_x_classification, transactions.transaction_x_classification_security_token FROM PUBLIC;

-- A relationship and its role belong to the transaction's enterprise.
CREATE OR REPLACE FUNCTION transactions.validate_relationship() RETURNS trigger
    LANGUAGE plpgsql AS
$function$
DECLARE
    linked_enterprise uuid;
    target_enterprise uuid;
BEGIN
    SELECT enterprise_id INTO linked_enterprise FROM transactions.entry WHERE entry_id = NEW.entry_id;
    IF linked_enterprise IS DISTINCT FROM NEW.enterprise_id
        OR NOT EXISTS (SELECT 1
                       FROM classification.classification c
                       WHERE c.classificationid = NEW.classificationid
                         AND c.enterpriseid = NEW.enterprise_id) THEN
        RAISE EXCEPTION 'Transaction relationship enterprise mismatch' USING ERRCODE = '23514';
    END IF;
    EXECUTE format('SELECT %I FROM %I.%I WHERE %I=$1', TG_ARGV[4], TG_ARGV[0], TG_ARGV[1], TG_ARGV[2])
        INTO target_enterprise USING (to_jsonb(NEW) ->> TG_ARGV[3])::uuid;
    IF target_enterprise IS DISTINCT FROM NEW.enterprise_id THEN
        RAISE EXCEPTION 'Transaction relationship target enterprise mismatch' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END
$function$;
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_involved_party;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_involved_party
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('party', 'involvedparty', 'involvedpartyid', 'involved_party_id',
                                                    'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_resource_item;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_resource_item
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('resource', 'resourceitem', 'resourceitemid', 'resource_item_id',
                                                    'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_arrangement;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_arrangement
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('arrangement', 'arrangement', 'arrangementid', 'arrangement_id',
                                                    'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_event;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_event
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('event', 'event', 'eventid', 'event_id', 'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_product;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_product
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('product', 'product', 'productid', 'product_id', 'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_address;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_address
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('address', 'address', 'addressid', 'address_id', 'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_geography;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_geography
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('geography', 'geography', 'geographyid', 'geography_id',
                                                    'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_rules;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_rules
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('rules', 'rules', 'rulesid', 'rules_id', 'enterpriseid');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_transaction;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_transaction
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('transactions', 'entry', 'entry_id', 'transaction_id',
                                                    'enterprise_id');
DROP TRIGGER IF EXISTS relationship_validate ON transactions.transaction_x_classification;
CREATE TRIGGER relationship_validate
    BEFORE INSERT OR UPDATE
    ON transactions.transaction_x_classification
    FOR EACH ROW
EXECUTE FUNCTION transactions.validate_relationship('classification', 'classification', 'classificationid',
                                                    'classificationid', 'enterpriseid');
REVOKE ALL ON FUNCTION transactions.validate_relationship() FROM PUBLIC;
