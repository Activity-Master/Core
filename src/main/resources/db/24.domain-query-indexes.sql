-- Shared FSDM coverage for the query families measured in 21 and 23.
-- Append-only update: preserve already installed scripts and their checksums.
-- These non-unique indexes serve every module using the same physical tables.
-- No fixed enterprise/system IDs, time-dependent predicates, or binary payload indexes.
-- Current reads must still apply enterprise, system, effective-from and active filters.
-- Existing resource/event classification indexes, resource type index, party
-- identification encrypted indexes, and both Document indexes are reused.
-- Type links use the type/value family; entity links use pair and contents families.
-- Static time dimensions have no equivalent relationship tables to extend.
-- Deprecated resourceitemdata relationships are intentionally not expanded.
-- See core/docs/domain-query-index-audit.md for coverage and PostgreSQL validation.

-- Taxonomy description equality, extending 21; existing descriptions are reused.

CREATE INDEX IF NOT EXISTS idx_fsdm_resourceitemtype_description
    ON resource.resourceitemtype (resourceitemtypedesc);

CREATE INDEX IF NOT EXISTS idx_fsdm_producttype_description
    ON product.producttype (producttypedesc);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulestype_description
    ON rules.rulestype (rulestypedesc);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartytype_description
    ON party.involvedpartytype (involvedpartytypedesc);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyorganictype_description
    ON party.involvedpartyorganictype (involvedpartytypedesc);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartynametype_description
    ON party.involvedpartynametype (involvedpartynametypedescr);

CREATE INDEX IF NOT EXISTS idx_fsdm_addresstype_description
    ON address.addresstype (addresstypedesc);

-- Structured address type SCD lookup; its existing name index has no temporal key.

CREATE INDEX IF NOT EXISTS idx_fsdm_addresstype_ent_name_to
    ON address.addresstype (enterpriseid, addresstypename, effectivetodate);

-- Classification/value candidates and owner field pivots, extending the UWE patterns.

CREATE INDEX IF NOT EXISTS idx_fsdm_enterprisexclassification_class_value
    ON dbo.enterprisexclassification (classificationid, value, enterpriseid);

CREATE INDEX IF NOT EXISTS idx_fsdm_enterprisexclassification_owner_class
    ON dbo.enterprisexclassification (enterpriseid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_activeflagxclassification_class_value
    ON dbo.activeflagxclassification (classificationid, value, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_activeflagxclassification_owner_class
    ON dbo.activeflagxclassification (activeflagid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_systemxclassification_class_value
    ON dbo.systemxclassification (classificationid, value, systemid);

CREATE INDEX IF NOT EXISTS idx_fsdm_systemxclassification_owner_class
    ON dbo.systemxclassification (systemid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationdataconceptxclassification_class_value
    ON classification.classificationdataconceptxclassification (classificationid, value, classificationdataconceptid);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationdataconceptxclassification_owner_class
    ON classification.classificationdataconceptxclassification (classificationdataconceptid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxclassification_class_value
    ON address.addressxclassification (classificationid, value, addressid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxclassification_owner_class
    ON address.addressxclassification (addressid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementtypexclassification_class_value
    ON arrangement.arrangementtypexclassification (classificationid, value, arrangementtypeid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementtypexclassification_owner_class
    ON arrangement.arrangementtypexclassification (arrangementtypeid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxclassification_class_value
    ON arrangement.arrangementxclassification (classificationid, value, arrangementid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxclassification_owner_class
    ON arrangement.arrangementxclassification (arrangementid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_producttypexclassification_class_value
    ON product.producttypexclassification (classificationid, value, producttypeid);

CREATE INDEX IF NOT EXISTS idx_fsdm_producttypexclassification_owner_class
    ON product.producttypexclassification (producttypeid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxclassification_class_value
    ON product.productxclassification (classificationid, value, productid);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxclassification_owner_class
    ON product.productxclassification (productid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxclassification_class_value
    ON party.involvedpartyxclassification (classificationid, value, involvedpartyid);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxclassification_owner_class
    ON party.involvedpartyxclassification (involvedpartyid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulestypexclassification_class_value
    ON rules.rulestypexclassification (classificationid, value, rulestypeid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulestypexclassification_owner_class
    ON rules.rulestypexclassification (rulestypeid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxclassification_class_value
    ON rules.rulesxclassification (classificationid, value, rulesid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxclassification_owner_class
    ON rules.rulesxclassification (rulesid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_securitytokenxclassification_class_value
    ON security.securitytokenxclassification (classificationid, value, securitytokenid);

CREATE INDEX IF NOT EXISTS idx_fsdm_securitytokenxclassification_owner_class
    ON security.securitytokenxclassification (securitytokenid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_geographyxclassification_class_value
    ON geography.geographyxclassification (classificationid, value, geographyid);

CREATE INDEX IF NOT EXISTS idx_fsdm_geographyxclassification_owner_class
    ON geography.geographyxclassification (geographyid, classificationid)
    INCLUDE (value);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_classification_class_value
    ON transactions.transaction_x_classification (classificationid, value, entry_id);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_classification_owner_class
    ON transactions.transaction_x_classification (entry_id, classificationid)
    INCLUDE (value);

-- Enterprise/type/value/current lookup, extending resource type lookup in 21.

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxarrangementtype_type_value_to
    ON arrangement.arrangementxarrangementtype (enterpriseid, arrangementtypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxrulestype_type_value_to
    ON arrangement.arrangementxrulestype (enterpriseid, rulestypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxproducttype_type_value_to
    ON product.productxproducttype (enterpriseid, producttypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxinvolvedpartynametype_type_value_to
    ON party.involvedpartyxinvolvedpartynametype (enterpriseid, involvedpartynametypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxinvolvedpartytype_type_value_to
    ON party.involvedpartyxinvolvedpartytype (enterpriseid, involvedpartytypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxproducttype_type_value_to
    ON party.involvedpartyxproducttype (enterpriseid, producttypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxrulestype_type_value_to
    ON rules.rulesxrulestype (enterpriseid, rulestypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxeventtype_type_value_to
    ON event.eventxeventtype (enterpriseid, eventtypeid, value, effectivetodate);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_transaction_type_type_value_to
    ON transactions.transaction_x_transaction_type (enterprise_id, transaction_type_id, value, effectivetodate);

-- Scoped current pair probes and owner contents, extending both Document patterns.

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationdataconceptxresourceitem_pair_to
    ON classification.classificationdataconceptxresourceitem (classificationdataconceptid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationdataconceptxresourceitem_contents_to
    ON classification.classificationdataconceptxresourceitem (enterpriseid, systemid, classificationdataconceptid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationxclassification_pair_to
    ON classification.classificationxclassification (parentclassificationid, childclassificationid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationxclassification_contents_to
    ON classification.classificationxclassification (enterpriseid, systemid, parentclassificationid, effectivetodate, childclassificationid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationxresourceitem_pair_to
    ON classification.classificationxresourceitem (classificationid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_classificationxresourceitem_contents_to
    ON classification.classificationxresourceitem (enterpriseid, systemid, classificationid, effectivetodate, resourceitemid)
    INCLUDE (effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxgeography_pair_to
    ON address.addressxgeography (addressid, geographyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxgeography_contents_to
    ON address.addressxgeography (enterpriseid, systemid, addressid, effectivetodate, geographyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxresourceitem_pair_to
    ON address.addressxresourceitem (addressid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxresourceitem_contents_to
    ON address.addressxresourceitem (enterpriseid, systemid, addressid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxarrangement_pair_to
    ON arrangement.arrangementxarrangement (parentarrangementid, childarrangementid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxarrangement_contents_to
    ON arrangement.arrangementxarrangement (enterpriseid, systemid, parentarrangementid, effectivetodate, childarrangementid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxinvolvedparty_contents_to
    ON arrangement.arrangementxinvolvedparty (enterpriseid, systemid, arrangementid, effectivetodate, involvedpartyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxproduct_pair_to
    ON arrangement.arrangementxproduct (arrangementid, productid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxproduct_contents_to
    ON arrangement.arrangementxproduct (enterpriseid, systemid, arrangementid, effectivetodate, productid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxresourceitem_pair_to
    ON arrangement.arrangementxresourceitem (arrangementid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxrules_pair_to
    ON arrangement.arrangementxrules (arrangementid, rulesid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_arrangementxrules_contents_to
    ON arrangement.arrangementxrules (enterpriseid, systemid, arrangementid, effectivetodate, rulesid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxproduct_pair_to
    ON product.productxproduct (parentproductid, childproductid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxproduct_contents_to
    ON product.productxproduct (enterpriseid, systemid, parentproductid, effectivetodate, childproductid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxresourceitem_pair_to
    ON product.productxresourceitem (productid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_productxresourceitem_contents_to
    ON product.productxresourceitem (enterpriseid, systemid, productid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_resourceitemxresourceitem_pair_to
    ON resource.resourceitemxresourceitem (parentresourceitemid, childresourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_resourceitemxresourceitem_contents_to
    ON resource.resourceitemxresourceitem (enterpriseid, systemid, parentresourceitemid, effectivetodate, childresourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxaddress_pair_to
    ON party.involvedpartyxaddress (involvedpartyid, addressid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxaddress_contents_to
    ON party.involvedpartyxaddress (enterpriseid, systemid, involvedpartyid, effectivetodate, addressid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxinvolvedparty_pair_to
    ON party.involvedpartyxinvolvedparty (parentinvolvedpartyid, childinvolvedpartyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxinvolvedparty_contents_to
    ON party.involvedpartyxinvolvedparty (enterpriseid, systemid, parentinvolvedpartyid, effectivetodate, childinvolvedpartyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxproduct_pair_to
    ON party.involvedpartyxproduct (involvedpartyid, productid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxproduct_contents_to
    ON party.involvedpartyxproduct (enterpriseid, systemid, involvedpartyid, effectivetodate, productid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxresourceitem_pair_to
    ON party.involvedpartyxresourceitem (involvedpartyid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxresourceitem_contents_to
    ON party.involvedpartyxresourceitem (enterpriseid, systemid, involvedpartyid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxrules_pair_to
    ON party.involvedpartyxrules (involvedpartyid, rulesid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_involvedpartyxrules_contents_to
    ON party.involvedpartyxrules (enterpriseid, systemid, involvedpartyid, effectivetodate, rulesid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxarrangement_pair_to
    ON rules.rulesxarrangement (rulesid, arrangementid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxarrangement_contents_to
    ON rules.rulesxarrangement (enterpriseid, systemid, rulesid, effectivetodate, arrangementid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxinvolvedparty_pair_to
    ON rules.rulesxinvolvedparty (rulesid, involvedpartyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxinvolvedparty_contents_to
    ON rules.rulesxinvolvedparty (enterpriseid, systemid, rulesid, effectivetodate, involvedpartyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxproduct_pair_to
    ON rules.rulesxproduct (rulesid, productid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxproduct_contents_to
    ON rules.rulesxproduct (enterpriseid, systemid, rulesid, effectivetodate, productid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxresourceitem_pair_to
    ON rules.rulesxresourceitem (rulesid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxresourceitem_contents_to
    ON rules.rulesxresourceitem (enterpriseid, systemid, rulesid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxrules_pair_to
    ON rules.rulesxrules (parentrulesid, childrulesid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulesxrules_contents_to
    ON rules.rulesxrules (enterpriseid, systemid, parentrulesid, effectivetodate, childrulesid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxaddress_pair_to
    ON event.eventxaddress (eventid, addressid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxaddress_contents_to
    ON event.eventxaddress (enterpriseid, systemid, eventid, effectivetodate, addressid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxarrangement_pair_to
    ON event.eventxarrangement (eventid, arrangementid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxarrangement_contents_to
    ON event.eventxarrangement (enterpriseid, systemid, eventid, effectivetodate, arrangementid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxevent_pair_to
    ON event.eventxevent (parenteventid, childeventid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxevent_contents_to
    ON event.eventxevent (enterpriseid, systemid, parenteventid, effectivetodate, childeventid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxgeography_pair_to
    ON event.eventxgeography (eventid, geographyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxgeography_contents_to
    ON event.eventxgeography (enterpriseid, systemid, eventid, effectivetodate, geographyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxinvolvedparty_pair_to
    ON event.eventxinvolvedparty (eventid, involvedpartyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid, value);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxinvolvedparty_contents_to
    ON event.eventxinvolvedparty (enterpriseid, systemid, eventid, effectivetodate, involvedpartyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxproduct_pair_to
    ON event.eventxproduct (eventid, productid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxproduct_contents_to
    ON event.eventxproduct (enterpriseid, systemid, eventid, effectivetodate, productid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxresourceitem_pair_to
    ON event.eventxresourceitem (eventid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxresourceitem_contents_to
    ON event.eventxresourceitem (enterpriseid, systemid, eventid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxrules_pair_to
    ON event.eventxrules (eventid, rulesid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_eventxrules_contents_to
    ON event.eventxrules (enterpriseid, systemid, eventid, effectivetodate, rulesid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_geographyxgeography_pair_to
    ON geography.geographyxgeography (parentgeographyid, childgeographyid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_geographyxgeography_contents_to
    ON geography.geographyxgeography (enterpriseid, systemid, parentgeographyid, effectivetodate, childgeographyid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_geographyxresourceitem_pair_to
    ON geography.geographyxresourceitem (geographyid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_geographyxresourceitem_contents_to
    ON geography.geographyxresourceitem (enterpriseid, systemid, geographyid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_involved_party_pair_to
    ON transactions.transaction_x_involved_party (entry_id, involved_party_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_involved_party_contents_to
    ON transactions.transaction_x_involved_party (enterprise_id, systemid, entry_id, effectivetodate, involved_party_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_resource_item_pair_to
    ON transactions.transaction_x_resource_item (entry_id, resource_item_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_resource_item_contents_to
    ON transactions.transaction_x_resource_item (enterprise_id, systemid, entry_id, effectivetodate, resource_item_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_arrangement_pair_to
    ON transactions.transaction_x_arrangement (entry_id, arrangement_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_arrangement_contents_to
    ON transactions.transaction_x_arrangement (enterprise_id, systemid, entry_id, effectivetodate, arrangement_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_event_pair_to
    ON transactions.transaction_x_event (entry_id, event_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_event_contents_to
    ON transactions.transaction_x_event (enterprise_id, systemid, entry_id, effectivetodate, event_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_product_pair_to
    ON transactions.transaction_x_product (entry_id, product_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_product_contents_to
    ON transactions.transaction_x_product (enterprise_id, systemid, entry_id, effectivetodate, product_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_address_pair_to
    ON transactions.transaction_x_address (entry_id, address_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_address_contents_to
    ON transactions.transaction_x_address (enterprise_id, systemid, entry_id, effectivetodate, address_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_geography_pair_to
    ON transactions.transaction_x_geography (entry_id, geography_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_geography_contents_to
    ON transactions.transaction_x_geography (enterprise_id, systemid, entry_id, effectivetodate, geography_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_rules_pair_to
    ON transactions.transaction_x_rules (entry_id, rules_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_rules_contents_to
    ON transactions.transaction_x_rules (enterprise_id, systemid, entry_id, effectivetodate, rules_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_transaction_pair_to
    ON transactions.transaction_x_transaction (entry_id, transaction_id, enterprise_id, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_transaction_x_transaction_contents_to
    ON transactions.transaction_x_transaction (enterprise_id, systemid, entry_id, effectivetodate, transaction_id)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxaddress_pair_to
    ON address.addressxaddress (addressid, componentaddressid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_addressxaddress_contents_to
    ON address.addressxaddress (enterpriseid, systemid, addressid, effectivetodate, componentaddressid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulestypexresourceitem_pair_to
    ON rules.rulestypexresourceitem (rulestypeid, resourceitemid, enterpriseid, systemid, effectivetodate)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

CREATE INDEX IF NOT EXISTS idx_fsdm_rulestypexresourceitem_contents_to
    ON rules.rulestypexresourceitem (enterpriseid, systemid, rulestypeid, effectivetodate, resourceitemid)
    INCLUDE (classificationid, effectivefromdate, activeflagid);

-- Both address encrypted OR branches; same names and expressions as the optional install SQL.

CREATE INDEX IF NOT EXISTS am_address_lookup_value
    ON address.address (enterpriseid, value);
CREATE INDEX IF NOT EXISTS am_address_lookup_header
    ON address.address
        (enterpriseid, (coalesce(regexp_substr(value, '^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{64}:'), '')));
