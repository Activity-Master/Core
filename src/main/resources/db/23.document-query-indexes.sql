-- Document Master: scoped current membership and bucket contents.
-- Non-unique indexes also serve other FSDM consumers of these relationships.
-- Keep SCD history; no volatile "current time" predicates or tenant-specific IDs.
-- See documents/docs/query-performance.md for the measured workload and plans.

-- Point access and bucket enumeration skip retained membership history.
CREATE INDEX IF NOT EXISTS idx_document_member_bucket_actor_to
    ON arrangement.arrangementxinvolvedparty
    (arrangementid, involvedpartyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_document_bucket_contents_to
    ON arrangement.arrangementxresourceitem
    (enterpriseid, systemid, arrangementid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);
