-- Shared FSDM domain indexes for Forum Master and Notification Master.
-- Appended after 24: preserve checksums of previously installed domain scripts.
-- Non-unique, scoped, history-aware paths; no fixed tenant IDs or clock predicates.
-- See core/docs/forum-notification-query-index-audit.md for PostgreSQL plans.

-- Reverse membership traversal: an actor's forums and newest notification window.
CREATE INDEX IF NOT EXISTS idx_fsdm_arrangement_party_actor_scope_to
    ON arrangement.arrangementxinvolvedparty
    (involvedpartyid, enterpriseid, systemid, effectivetodate, arrangementid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_event_party_actor_scope_to
    ON event.eventxinvolvedparty
    (involvedpartyid, enterpriseid, systemid, effectivetodate, eventid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

-- A forum's current post Events, traversed from the Arrangement.
CREATE INDEX IF NOT EXISTS idx_fsdm_event_arrangement_target_scope_to
    ON event.eventxarrangement
    (arrangementid, enterpriseid, systemid, effectivetodate, eventid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

-- Current context/title/header/state field pivots skip retained classifications.
CREATE INDEX IF NOT EXISTS idx_fsdm_arrangement_class_scope_to
    ON arrangement.arrangementxclassification
    (enterpriseid, systemid, arrangementid, effectivetodate, classificationid)
    INCLUDE (value, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_event_class_scope_to
    ON event.eventxclassification
    (enterpriseid, systemid, eventid, effectivetodate, classificationid)
    INCLUDE (value, effectivefromdate, activeflagid);
