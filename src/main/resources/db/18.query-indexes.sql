-- Indexes driven by measured query plans, not by the foreign-key inventory in 17.
--
-- Measured against 200k notifications / 500k party links / 50k security tokens on postgres:17.
-- Each entry below records the before/after it was added for; anything that did not pay for itself
-- was left out deliberately (see core/docs/database-setup.md).

-- Every authenticated request in Notification, Conversation, Marketplace, Wallet and Payment Master
-- resolves the caller's credential by its token value before doing anything else. Without this the
-- lookup is a sequential scan of the whole token table, on every single request.
-- Measured: 4.0 ms -> 0.07 ms at 50k tokens.
CREATE INDEX IF NOT EXISTS idx_securitytoken_securitytoken
    ON security.securitytoken (securitytoken);

-- Taxonomy is resolved by name several times per operation: every classified link a service reads
-- or writes has to turn a role name into a classification id first.
-- Measured: 0.58 ms -> 0.07 ms at 5k classifications, and it is called 6-10 times per operation.
CREATE INDEX IF NOT EXISTS idx_classification_classificationname
    ON classification.classification (classificationname);

-- Same pattern for event types, which every event create and every typed read resolves by name.
-- Cheap insurance: the table is small today, so the gain is small, but it grows with the taxonomy.
CREATE INDEX IF NOT EXISTS idx_eventtype_eventtypename
    ON event.eventtype (eventtypename);

-- The same name-resolution pattern on the remaining SCD lookup tables. Services turn a type or
-- concept name into an id on nearly every operation, and none of these columns was indexed.
-- Measured on classification.classificationdataconcept, which the generic SCD lookup hits:
-- 283 us -> 26 us, an 11x improvement, at 4k rows.
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_name
    ON classification.classificationdataconcept (classificationdataconceptname);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_name
    ON resource.resourceitemtype (resourceitemtypename);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_name
    ON arrangement.arrangementtype (arrangementtypename);
CREATE INDEX IF NOT EXISTS idx_producttype_name
    ON product.producttype (producttypename);
CREATE INDEX IF NOT EXISTS idx_rulestype_name
    ON rules.rulestype (rulestypename);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_name
    ON party.involvedpartytype (involvedpartytypename);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_name
    ON party.involvedpartyidentificationtype (involvedpartyidentificationname);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_name
    ON party.involvedpartynametype (involvedpartynametypename);
CREATE INDEX IF NOT EXISTS idx_activeflag_activeflagname
    ON dbo.activeflag (activeflagname);

-- SCD version skipping.
--
-- Every lookup above is really "the row with this name, in this enterprise, that is current now".
-- With only the name indexed, PostgreSQL fetches every historical version of the key and discards
-- all but one; with effectivetodate as the third column the superseded versions are skipped in the
-- index. Measured at 200 versions per key: 34.9 us -> 19.6 us.
--
-- This only works if the temporal predicate uses a STABLE function. clock_timestamp() is VOLATILE
-- and can never be an index condition, so with it the third column is dead weight. Use
-- statement_timestamp() or now().
CREATE INDEX IF NOT EXISTS idx_classification_ent_name_to
    ON classification.classification (enterpriseid, classificationname, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_ent_name_to
    ON classification.classificationdataconcept (enterpriseid, classificationdataconceptname, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_eventtype_ent_name_to
    ON event.eventtype (enterpriseid, eventtypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_ent_name_to
    ON resource.resourceitemtype (enterpriseid, resourceitemtypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_ent_name_to
    ON arrangement.arrangementtype (enterpriseid, arrangementtypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_producttype_ent_name_to
    ON product.producttype (enterpriseid, producttypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_rulestype_ent_name_to
    ON rules.rulestype (enterpriseid, rulestypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_ent_name_to
    ON party.involvedpartytype (enterpriseid, involvedpartytypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidtype_ent_name_to
    ON party.involvedpartyidentificationtype (enterpriseid, involvedpartyidentificationname, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_ent_name_to
    ON party.involvedpartynametype (enterpriseid, involvedpartynametypename, effectivetodate);
CREATE INDEX IF NOT EXISTS idx_activeflag_ent_name_to
    ON dbo.activeflag (enterpriseid, activeflagname, effectivetodate);

-- The credential lookup on every authenticated request is the same shape, and security tokens are
-- reissued, so this table versions too.
CREATE INDEX IF NOT EXISTS idx_securitytoken_ent_token_to
    ON security.securitytoken (enterpriseid, securitytoken, effectivetodate);

-- The plain single-column name indexes above are kept as well. Every name lookup found in the
-- codebase is enterprise-scoped, so they are strictly redundant with these composites and could be
-- dropped; they are retained because these are small, rarely-written tables and the cost of being
-- wrong about an un-scoped lookup is a sequential scan.
