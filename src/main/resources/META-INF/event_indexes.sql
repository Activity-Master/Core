CREATE INDEX CONCURRENTLY IF NOT EXISTS ix_event_fromdate_eventid
    ON event.event (warehousefromdate, eventid)
    INCLUDE (warehousecreatedtimestamp);

CREATE INDEX CONCURRENTLY IF NOT EXISTS ix_exet_eventtypeid_eventid
    ON event.eventxeventtype (eventtypeid, eventid)
    INCLUDE (value);

CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS ix_eventtype_desc
    ON event.eventtype (eventtypedesc);

CREATE INDEX CONCURRENTLY IF NOT EXISTS ix_classification_desc
    ON classification.classification (classificationdesc)
    INCLUDE (classificationid);

CREATE INDEX CONCURRENTLY IF NOT EXISTS ix_etc_eventid_classificationid
    ON event.eventxclassification (eventid, classificationid)
    INCLUDE (value);

