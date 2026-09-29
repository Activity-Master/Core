
-- if ResourceItemID is PK you're mostly covered, but effective filtering can still benefit:
CREATE INDEX IF NOT EXISTS ri_active_eff_idx
    ON resource.ResourceItem
        (ResourceItemID, ActiveFlagID, EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS ne1_lobby_document_lookup
    ON arrangement.arrangementxresourceitem (enterpriseid, systemid, classificationid, arrangementid)
    INCLUDE (resourceitemid, value, activeflagid, effectivefromdate, effectivetodate);

