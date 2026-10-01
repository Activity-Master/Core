-- UWE FSDM query workload; see docs/uwe-query-index-audit.md and the isolated
-- PostgreSQL fixture in src/test/scripts/uwe_query_index_audit.py.
-- Non-unique, tenant-neutral definitions: never embed installation-specific UUIDs.
-- Preserve the existing FK, temporal, name and payload primary-key indexes.

-- Native measurement queries resolve taxonomy by DESCRIPTION, whereas the shared
-- service lookups resolve it by NAME (already indexed in 18.query-indexes.sql).
CREATE INDEX IF NOT EXISTS idx_classification_description
    ON classification.classification (classificationdesc);
CREATE INDEX IF NOT EXISTS idx_eventtype_description
    ON event.eventtype (eventtypedesc);
-- StaffTimesheetService.loadTimeSheets and StaffService.loadStaffIds use the
-- same description lookup on arrangement and identification types.
CREATE INDEX IF NOT EXISTS idx_arrangementtype_description
    ON arrangement.arrangementtype (arrangementtypedescription);
CREATE INDEX IF NOT EXISTS idx_identificationtype_description
    ON party.involvedpartyidentificationtype (involvedpartyidentificationdesc);

-- TimesheetStatisticsLoader: candidate session/station events are selected with
-- classificationid + value equality. The old (classificationid, warehousefromdate)
-- index reads that classification for every session before filtering the value.
CREATE INDEX IF NOT EXISTS idx_eventxclassification_class_value_event
    ON event.eventxclassification (classificationid, value, eventid);

-- Expand those candidates into measurement fields, and aggregate the replay
-- window, without fetching the large warehouse metadata tuple for every field.
CREATE INDEX IF NOT EXISTS idx_eventxclassification_event_class_value
    ON event.eventxclassification (eventid, classificationid) INCLUDE (value);

-- ResourceItemService.findByResourceItemType: Barcode/BarcodeBatch lookup by
-- enterprise + type + short discriminator; also supports type-only enumeration
-- through its first two columns. Equality keys precede the current-version range.
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_ent_type_value_to
    ON resource.resourceitemxresourceitemtype
    (enterpriseid, resourceitemtypeid, value, effectivetodate);

-- Identification reads use EncryptedValuePredicate: plaintext/legacy equality OR
-- authenticated lookup-header equality. Both OR branches need an index. Keep the
-- expression identical to LOOKUP_HEADER_PATTERN and its coalesce in Java.
-- These names intentionally match docs/sql/encrypted-value-lookup-indexes.sql so
-- installations that applied that optional script do not get duplicate indexes.
CREATE INDEX IF NOT EXISTS am_party_identification_lookup_value
    ON party.involvedpartyxinvolvedpartyidentificationtype
    (enterpriseid, involvedpartyidentificationtypeid, value);
CREATE INDEX IF NOT EXISTS am_party_identification_lookup_header
    ON party.involvedpartyxinvolvedpartyidentificationtype
    (enterpriseid, involvedpartyidentificationtypeid,
     (coalesce(regexp_substr(value, '^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{64}:'), '')));
