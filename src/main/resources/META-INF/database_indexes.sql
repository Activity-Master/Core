-- ActivityMaster Database Indexing Script
-- Generated based on ERD rules document

-- =============================================
-- Primary Key Indexes
-- =============================================
-- These are typically created automatically by the database system
-- but we include them for completeness

-- ActiveFlag
CREATE INDEX IF NOT EXISTS idx_activeflag_id ON dbo.ActiveFlag(ActiveFlagID);

-- Classification
CREATE INDEX IF NOT EXISTS idx_classification_id ON classification.Classification(ClassificationID);

-- ClassificationDataConcept
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_id ON classification.ClassificationDataConcept(ClassificationDataConceptID);

-- Enterprise
CREATE INDEX IF NOT EXISTS idx_enterprise_id ON dbo.Enterprise(EnterpriseID);

-- Systems
CREATE INDEX IF NOT EXISTS idx_systems_id ON dbo.Systems(SystemID);

-- Address
CREATE INDEX IF NOT EXISTS idx_address_id ON address.Address(AddressID);

-- Event
CREATE INDEX IF NOT EXISTS idx_event_id ON event.Event(EventID);

-- EventType
CREATE INDEX IF NOT EXISTS idx_eventtype_id ON event.EventType(EventTypeID);

-- Geography
CREATE INDEX IF NOT EXISTS idx_geography_id ON geography.Geography(GeographyID);

-- InvolvedParty
CREATE INDEX IF NOT EXISTS idx_involvedparty_id ON party.InvolvedParty(InvolvedPartyID);

-- InvolvedPartyType
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_id ON party.InvolvedPartyType(InvolvedPartyTypeID);

-- InvolvedPartyNameType
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_id ON party.InvolvedPartyNameType(InvolvedPartyNameTypeID);

-- InvolvedPartyIdentificationType
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_id ON party.InvolvedPartyIdentificationType(InvolvedPartyIdentificationTypeID);

-- InvolvedPartyNonOrganic
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_id ON party.InvolvedPartyNonOrganic(InvolvedPartyNonOrganicID);

-- InvolvedPartyOrganic
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_id ON party.InvolvedPartyOrganic(InvolvedPartyOrganicID);

-- InvolvedPartyOrganicType
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_id ON party.InvolvedPartyOrganicType(InvolvedPartyOrganicTypeID);

-- Product
CREATE INDEX IF NOT EXISTS idx_product_id ON product.Product(ProductID);

-- ProductType
CREATE INDEX IF NOT EXISTS idx_producttype_id ON product.ProductType(ProductTypeID);

-- ResourceItem
CREATE INDEX IF NOT EXISTS idx_resourceitem_id ON resource.ResourceItem(ResourceItemID);

-- ResourceItemData
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_id ON resource.ResourceItemData(ResourceItemDataID);

-- ResourceItemType
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_id ON resource.ResourceItemType(ResourceItemTypeID);

-- Rules
CREATE INDEX IF NOT EXISTS idx_rules_id ON rules.Rules(RulesID);

-- RulesType
CREATE INDEX IF NOT EXISTS idx_rulestype_id ON rules.RulesType(RulesTypeID);

-- Arrangement
CREATE INDEX IF NOT EXISTS idx_arrangement_id ON arrangement.Arrangement(ArrangementID);

-- ArrangementType
CREATE INDEX IF NOT EXISTS idx_arrangementtype_id ON arrangement.ArrangementType(ArrangementTypeID);

-- SecurityToken
CREATE INDEX IF NOT EXISTS idx_securitytoken_id ON security.SecurityToken(SecurityTokenID);

-- Time dimension tables
--CREATE INDEX IF NOT EXISTS idx_time_id ON Time.Time(TimePK);
CREATE INDEX IF NOT EXISTS idx_hours_id ON Time.Hours(HourID);
CREATE INDEX IF NOT EXISTS idx_days_id ON Time.Days(DayID);
CREATE INDEX IF NOT EXISTS idx_weeks_id ON Time.Weeks(WeekID);
CREATE INDEX IF NOT EXISTS idx_months_id ON Time.Months(MonthID);
CREATE INDEX IF NOT EXISTS idx_daynames_id ON Time.DayNames(DayNameID);
CREATE INDEX IF NOT EXISTS idx_quarters_id ON Time.Quarters(QuarterID);
CREATE INDEX IF NOT EXISTS idx_years_id ON Time.Years(YearID);

-- =============================================
-- Foreign Key Reference Indexes
-- =============================================

-- Enterprise references
CREATE INDEX IF NOT EXISTS idx_activeflag_enterpriseid ON dbo.ActiveFlag(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_classification_enterpriseid ON classification.Classification(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_enterpriseid ON classification.ClassificationDataConcept(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_systems_enterpriseid ON dbo.Systems(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_address_enterpriseid ON address.Address(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_event_enterpriseid ON event.Event(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_eventtype_enterpriseid ON event.EventType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_geography_enterpriseid ON geography.Geography(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedparty_enterpriseid ON party.InvolvedParty(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_enterpriseid ON party.InvolvedPartyType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_enterpriseid ON party.InvolvedPartyNameType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_enterpriseid ON party.InvolvedPartyIdentificationType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_enterpriseid ON party.InvolvedPartyNonOrganic(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_enterpriseid ON party.InvolvedPartyOrganic(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_enterpriseid ON party.InvolvedPartyOrganicType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_product_enterpriseid ON product.Product(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_producttype_enterpriseid ON product.ProductType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_resourceitem_enterpriseid ON resource.ResourceItem(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_enterpriseid ON resource.ResourceItemData(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_enterpriseid ON resource.ResourceItemType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_rules_enterpriseid ON rules.Rules(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_rulestype_enterpriseid ON rules.RulesType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_arrangement_enterpriseid ON arrangement.Arrangement(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_enterpriseid ON arrangement.ArrangementType(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_securitytoken_enterpriseid ON security.SecurityToken(EnterpriseID);

-- System references
CREATE INDEX IF NOT EXISTS idx_classification_systemid ON classification.Classification(SystemID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_systemid ON classification.ClassificationDataConcept(SystemID);
CREATE INDEX IF NOT EXISTS idx_address_systemid ON address.Address(SystemID);
CREATE INDEX IF NOT EXISTS idx_event_systemid ON event.Event(SystemID);
CREATE INDEX IF NOT EXISTS idx_eventtype_systemid ON event.EventType(SystemID);
CREATE INDEX IF NOT EXISTS idx_geography_systemid ON geography.Geography(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedparty_systemid ON party.InvolvedParty(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_systemid ON party.InvolvedPartyType(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_systemid ON party.InvolvedPartyNameType(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_systemid ON party.InvolvedPartyIdentificationType(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_systemid ON party.InvolvedPartyNonOrganic(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_systemid ON party.InvolvedPartyOrganic(SystemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_systemid ON party.InvolvedPartyOrganicType(SystemID);
CREATE INDEX IF NOT EXISTS idx_product_systemid ON product.Product(SystemID);
CREATE INDEX IF NOT EXISTS idx_producttype_systemid ON product.ProductType(SystemID);
CREATE INDEX IF NOT EXISTS idx_resourceitem_systemid ON resource.ResourceItem(SystemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_systemid ON resource.ResourceItemData(SystemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_systemid ON resource.ResourceItemType(SystemID);
CREATE INDEX IF NOT EXISTS idx_rules_systemid ON rules.Rules(SystemID);
CREATE INDEX IF NOT EXISTS idx_rulestype_systemid ON rules.RulesType(SystemID);
CREATE INDEX IF NOT EXISTS idx_arrangement_systemid ON arrangement.Arrangement(SystemID);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_systemid ON arrangement.ArrangementType(SystemID);
CREATE INDEX IF NOT EXISTS idx_securitytoken_systemid ON security.SecurityToken(SystemID);

-- ActiveFlag references
CREATE INDEX IF NOT EXISTS idx_classification_activeflagid ON classification.Classification(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_activeflagid ON classification.ClassificationDataConcept(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_address_activeflagid ON address.Address(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_event_activeflagid ON event.Event(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_eventtype_activeflagid ON event.EventType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_geography_activeflagid ON geography.Geography(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedparty_activeflagid ON party.InvolvedParty(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_activeflagid ON party.InvolvedPartyType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_activeflagid ON party.InvolvedPartyNameType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_activeflagid ON party.InvolvedPartyIdentificationType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_activeflagid ON party.InvolvedPartyNonOrganic(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_activeflagid ON party.InvolvedPartyOrganic(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_activeflagid ON party.InvolvedPartyOrganicType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_product_activeflagid ON product.Product(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_producttype_activeflagid ON product.ProductType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitem_activeflagid ON resource.ResourceItem(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_activeflagid ON resource.ResourceItemData(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_activeflagid ON resource.ResourceItemType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_rules_activeflagid ON rules.Rules(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_rulestype_activeflagid ON rules.RulesType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_arrangement_activeflagid ON arrangement.Arrangement(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_activeflagid ON arrangement.ArrangementType(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_securitytoken_activeflagid ON security.SecurityToken(ActiveFlagID);

-- Classification references (for tables with direct classification ID)
CREATE INDEX IF NOT EXISTS idx_address_classificationid ON address.Address(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_geography_classificationid ON geography.Geography(ClassificationID);

-- Time dimension references for Event
CREATE INDEX IF NOT EXISTS idx_event_dayid ON event.Event(DayID);
CREATE INDEX IF NOT EXISTS idx_event_hourid ON event.Event(HourID);
CREATE INDEX IF NOT EXISTS idx_event_minuteid ON event.Event(MinuteID);

-- ResourceItem references
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_resourceitemid ON resource.ResourceItemData(ResourceItemID);

-- =============================================
-- Name and Description Indexes
-- =============================================

CREATE INDEX IF NOT EXISTS idx_activeflag_name ON dbo.ActiveFlag(ActiveFlagName);
CREATE INDEX IF NOT EXISTS idx_classification_name ON classification.Classification(ClassificationName);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_name ON classification.ClassificationDataConcept(ClassificationDataConceptName);
CREATE INDEX IF NOT EXISTS idx_enterprise_name ON dbo.Enterprise(EnterpriseName);
CREATE INDEX IF NOT EXISTS idx_systems_name ON dbo.Systems(SystemName);
CREATE INDEX IF NOT EXISTS idx_eventtype_name ON event.EventType(EventTypeName);
CREATE INDEX IF NOT EXISTS idx_geography_name ON geography.Geography(GeographyName);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_name ON party.InvolvedPartyType(InvolvedPartyTypeName);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_name ON party.InvolvedPartyNameType(InvolvedPartyNameTypeName);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_name ON party.InvolvedPartyIdentificationType(InvolvedPartyIdentificationName);
--CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_name ON party.InvolvedPartyOrganicType(involvedpartytypename);
CREATE INDEX IF NOT EXISTS idx_product_name ON product.Product(ProductName);
CREATE INDEX IF NOT EXISTS idx_product_code ON product.Product(ProductCode);
CREATE INDEX IF NOT EXISTS idx_producttype_name ON product.ProductType(ProductTypeName);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_name ON resource.ResourceItemType(ResourceItemTypeName);
CREATE INDEX IF NOT EXISTS idx_rules_name ON rules.Rules(RuleSetName);
CREATE INDEX IF NOT EXISTS idx_rulestype_name ON rules.RulesType(RulesTypeName);
--CREATE INDEX IF NOT EXISTS idx_arrangement_name ON arrangement.Arrangement(ArrangementName);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_name ON arrangement.ArrangementType(ArrangementTypeName);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_name2 ON arrangement.ArrangementType(arrangementtypedescription);
CREATE INDEX IF NOT EXISTS idx_securitytoken_name ON security.SecurityToken(securitytokenfriendlyname);
CREATE INDEX IF NOT EXISTS idx_securitytoken_name2 ON security.SecurityToken(securitytokenfriendlydescription);

-- =============================================
-- Temporal Indexes
-- =============================================

-- Common temporal indexes for all entities
CREATE INDEX IF NOT EXISTS idx_activeflag_effectivedates ON dbo.ActiveFlag(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_classification_effectivedates ON classification.Classification(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_effectivedates ON classification.ClassificationDataConcept(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_enterprise_effectivedates ON dbo.Enterprise(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_systems_effectivedates ON dbo.Systems(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_address_effectivedates ON address.Address(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_event_effectivedates ON event.Event(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_eventtype_effectivedates ON event.EventType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_geography_effectivedates ON geography.Geography(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedparty_effectivedates ON party.InvolvedParty(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_effectivedates ON party.InvolvedPartyType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_effectivedates ON party.InvolvedPartyNameType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_effectivedates ON party.InvolvedPartyIdentificationType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_effectivedates ON party.InvolvedPartyNonOrganic(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_effectivedates ON party.InvolvedPartyOrganic(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_effectivedates ON party.InvolvedPartyOrganicType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_product_effectivedates ON product.Product(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_producttype_effectivedates ON product.ProductType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_resourceitem_effectivedates ON resource.ResourceItem(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_effectivedates ON resource.ResourceItemData(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_effectivedates ON resource.ResourceItemType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_rules_effectivedates ON rules.Rules(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_rulestype_effectivedates ON rules.RulesType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_arrangement_effectivedates ON arrangement.Arrangement(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_effectivedates ON arrangement.ArrangementType(EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_securitytoken_effectivedates ON security.SecurityToken(EffectiveFromDate, EffectiveToDate);

-- Warehouse timestamp indexes
CREATE INDEX IF NOT EXISTS idx_activeflag_warehousedates ON dbo.ActiveFlag(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_classification_warehousedates ON classification.Classification(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_warehousedates ON classification.ClassificationDataConcept(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_enterprise_warehousedates ON dbo.Enterprise(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_systems_warehousedates ON dbo.Systems(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_address_warehousedates ON address.Address(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_event_warehousedates ON event.Event(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_eventtype_warehousedates ON event.EventType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_geography_warehousedates ON geography.Geography(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedparty_warehousedates ON party.InvolvedParty(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_warehousedates ON party.InvolvedPartyType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_warehousedates ON party.InvolvedPartyNameType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_warehousedates ON party.InvolvedPartyIdentificationType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_warehousedates ON party.InvolvedPartyNonOrganic(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_warehousedates ON party.InvolvedPartyOrganic(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_warehousedates ON party.InvolvedPartyOrganicType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_product_warehousedates ON product.Product(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_producttype_warehousedates ON product.ProductType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_resourceitem_warehousedates ON resource.ResourceItem(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_warehousedates ON resource.ResourceItemData(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_warehousedates ON resource.ResourceItemType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_rules_warehousedates ON rules.Rules(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_rulestype_warehousedates ON rules.RulesType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_arrangement_warehousedates ON arrangement.Arrangement(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_warehousedates ON arrangement.ArrangementType(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);
CREATE INDEX IF NOT EXISTS idx_securitytoken_warehousedates ON security.SecurityToken(WarehouseCreatedTimestamp, WarehouseLastUpdatedTimestamp);

-- WarehouseFromDate indexes
CREATE INDEX IF NOT EXISTS idx_activeflag_warehousefromdate ON dbo.ActiveFlag(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_classification_warehousefromdate ON classification.Classification(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_warehousefromdate ON classification.ClassificationDataConcept(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_enterprise_warehousefromdate ON dbo.Enterprise(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_systems_warehousefromdate ON dbo.Systems(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_address_warehousefromdate ON address.Address(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_event_warehousefromdate ON event.Event(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_eventtype_warehousefromdate ON event.EventType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_geography_warehousefromdate ON geography.Geography(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedparty_warehousefromdate ON party.InvolvedParty(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_warehousefromdate ON party.InvolvedPartyType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_warehousefromdate ON party.InvolvedPartyNameType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtype_warehousefromdate ON party.InvolvedPartyIdentificationType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_warehousefromdate ON party.InvolvedPartyNonOrganic(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_warehousefromdate ON party.InvolvedPartyOrganic(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_warehousefromdate ON party.InvolvedPartyOrganicType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_product_warehousefromdate ON product.Product(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_producttype_warehousefromdate ON product.ProductType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_resourceitem_warehousefromdate ON resource.ResourceItem(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_warehousefromdate ON resource.ResourceItemData(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_warehousefromdate ON resource.ResourceItemType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_rules_warehousefromdate ON rules.Rules(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_rulestype_warehousefromdate ON rules.RulesType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_arrangement_warehousefromdate ON arrangement.Arrangement(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_warehousefromdate ON arrangement.ArrangementType(WarehouseFromDate);
CREATE INDEX IF NOT EXISTS idx_securitytoken_warehousefromdate ON security.SecurityToken(WarehouseFromDate);

-- =============================================
-- Composite Indexes for Common Queries
-- =============================================

-- Enterprise-System-ActiveFlag Indexes
CREATE INDEX IF NOT EXISTS idx_classification_enterprise_system_activeflag ON classification.Classification(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconcept_enterprise_system_activeflag ON classification.ClassificationDataConcept(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_address_enterprise_system_activeflag ON address.Address(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_event_enterprise_system_activeflag ON event.Event(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_eventtype_enterprise_system_activeflag ON event.EventType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_geography_enterprise_system_activeflag ON geography.Geography(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedparty_enterprise_system_activeflag ON party.InvolvedParty(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartytype_enterprise_system_activeflag ON party.InvolvedPartyType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynametype_enterprise_system_activeflag ON party.InvolvedPartyNameType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_ipidenttype_enterprise_system_activeflag ON party.InvolvedPartyIdentificationType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganic_enterprise_system_activeflag ON party.InvolvedPartyNonOrganic(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganic_enterprise_system_activeflag ON party.InvolvedPartyOrganic(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictype_enterprise_system_activeflag ON party.InvolvedPartyOrganicType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_product_enterprise_system_activeflag ON product.Product(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_producttype_enterprise_system_activeflag ON product.ProductType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitem_enterprise_system_activeflag ON resource.ResourceItem(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdata_enterprise_system_activeflag ON resource.ResourceItemData(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitemtype_enterprise_system_activeflag ON resource.ResourceItemType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_rules_enterprise_system_activeflag ON rules.Rules(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_rulestype_enterprise_system_activeflag ON rules.RulesType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_arrangement_enterprise_system_activeflag ON arrangement.Arrangement(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_arrangementtype_enterprise_system_activeflag ON arrangement.ArrangementType(EnterpriseID, SystemID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_securitytoken_enterprise_system_activeflag ON security.SecurityToken(EnterpriseID, SystemID, ActiveFlagID);

-- Classification-Based Search Indexes
CREATE INDEX IF NOT EXISTS idx_address_classification_value ON address.Address(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_geography_classification_name ON geography.Geography(ClassificationID, GeographyName);

-- Temporal-Classification Indexes
CREATE INDEX IF NOT EXISTS idx_address_classification_effectivedate ON address.Address(ClassificationID, EffectiveFromDate, EffectiveToDate);
CREATE INDEX IF NOT EXISTS idx_geography_classification_effectivedate ON geography.Geography(ClassificationID, EffectiveFromDate, EffectiveToDate);

-- Event-specific composite indexes
CREATE INDEX IF NOT EXISTS idx_event_day_hour_minute ON event.Event(DayID, HourID, MinuteID);

-- =============================================
-- Security Token Indexes
-- =============================================

-- ActiveFlagSecurityToken
CREATE INDEX IF NOT EXISTS idx_activeflagsecuritytoken_token_activeflag ON dbo.ActiveFlagSecurityToken(SecurityTokenID, SecurityTokenActiveFlagID);

-- ClassificationSecurityToken
CREATE INDEX IF NOT EXISTS idx_classificationsecuritytoken_token_classification ON classification.ClassificationSecurityToken(SecurityTokenID, ClassificationID);

-- EnterpriseSecurityToken
CREATE INDEX IF NOT EXISTS idx_enterprisesecuritytoken_token_enterprise ON dbo.EnterpriseSecurityToken(SecurityTokenID, EnterpriseID);

-- SystemsSecurityToken
CREATE INDEX IF NOT EXISTS idx_systemssecuritytoken_token_system ON dbo.SystemsSecurityToken(SecurityTokenID, SystemID);

-- AddressSecurityToken
CREATE INDEX IF NOT EXISTS idx_addresssecuritytoken_token_address ON address.AddressSecurityToken(SecurityTokenID, AddressID);

-- EventSecurityToken
CREATE INDEX IF NOT EXISTS idx_eventsecuritytoken_token_event ON event.EventSecurityToken(SecurityTokenID, EventsID);

-- EventTypesSecurityToken
CREATE INDEX IF NOT EXISTS idx_EventTypesSecurityToken_token_eventtype ON event.EventTypesSecurityToken(SecurityTokenID, EventTypesID);

-- GeographySecurityToken
CREATE INDEX IF NOT EXISTS idx_geographysecuritytoken_token_geography ON geography.GeographySecurityToken(SecurityTokenID, GeographyID);

-- InvolvedPartySecurityToken
CREATE INDEX IF NOT EXISTS idx_involvedpartysecuritytoken_token_involvedparty ON party.InvolvedPartySecurityToken(SecurityTokenID, InvolvedPartyID);

-- InvolvedPartyTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_involvedpartytypesecuritytoken_token_involvedpartytype ON party.InvolvedPartyTypeSecurityToken(SecurityTokenID, InvolvedPartyTypeID);

-- InvolvedPartyNameTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_ipnametypesectoken_token_ipnametype ON party.InvolvedPartyNameTypeSecurityToken(SecurityTokenID, InvolvedPartyNameTypeID);

-- InvolvedPartyIdentificationTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_involvedpartyontypetoken_involvedpartyidentificationtype ON party.InvolvedPartyIdentificationTypeSecurityToken(SecurityTokenID, InvolvedPartyIdentificationTypeID);

-- InvolvedPartyNonOrganicSecurityToken
CREATE INDEX IF NOT EXISTS idx_involvedpartcsecuritytoken_token_involvedpartynonorganic ON party.InvolvedPartyNonOrganicSecurityToken(SecurityTokenID, InvolvedPartyNonOrganicID);

-- InvolvedPartyOrganicSecurityToken
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganicsecken_involvedpartyorganic ON party.InvolvedPartyOrganicSecurityToken(SecurityTokenID, InvolvedPartyOrganicID);

-- InvolvedPartyOrganicTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_involvedpartyorguritytoken_token_involvedpartyorganictype ON party.InvolvedPartyOrganicTypeSecurityToken(SecurityTokenID, InvolvedPartyOrganicTypeID);

-- ProductSecurityToken
CREATE INDEX IF NOT EXISTS idx_productsecuritytoken_token_product ON product.ProductSecurityToken(SecurityTokenID, ProductID);

-- ProductTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_producttypesecuritytoken_token_producttype ON product.ProductTypesSecurityToken(SecurityTokenID, ProductTypesID);

-- ResourceItemSecurityToken
CREATE INDEX IF NOT EXISTS idx_resourceitemsecuritytoken_token_resourceitem ON resource.ResourceItemSecurityToken(SecurityTokenID, ResourceItemID);

-- ResourceItemDataSecurityToken
CREATE INDEX IF NOT EXISTS idx_resourceitemdatasecuritytoken_token_resourceitemdata ON resource.ResourceItemDataSecurityToken(SecurityTokenID, ResourceItemDataID);

-- ResourceItemTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_resourceitemtypesecuritytoken_token_resourceitemtype ON resource.ResourceItemTypeSecurityToken(SecurityTokenID, ResourceItemTypeID);

-- RulesSecurityToken
CREATE INDEX IF NOT EXISTS idx_rulessecuritytoken_token_rules ON rules.RulesSecurityToken(SecurityTokenID, RulesID);

-- RulesTypesSecurityToken
CREATE INDEX IF NOT EXISTS idx_RulesTypesSecurityToken_token_rulestype ON rules.RulesTypesSecurityToken(SecurityTokenID, RulesTypesID);

-- ArrangementSecurityToken
CREATE INDEX IF NOT EXISTS idx_arrangementsecuritytoken_token_arrangement ON arrangement.ArrangementSecurityToken(SecurityTokenID, ArrangementID);

-- ArrangementTypeSecurityToken
CREATE INDEX IF NOT EXISTS idx_arrangementtypesecuritytoken_token_arrangementtype ON arrangement.ArrangementTypeSecurityToken(SecurityTokenID, ArrangementTypeID);

-- SecurityTokensSecurityToken
CREATE INDEX IF NOT EXISTS idx_SecurityTokensSecurityToken_token_securitytoken ON security.SecurityTokensSecurityToken(SecurityTokenID, SecurityTokenID);

-- =============================================
-- Permission Indexes
-- =============================================

-- ActiveFlagSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_activeflagsecuritytoken_token_crud ON dbo.ActiveFlagSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ClassificationSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_classificationsecuritytoken_token_crud ON classification.ClassificationSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- EnterpriseSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_enterprisesecuritytoken_token_crud ON dbo.EnterpriseSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- SystemsSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_systemssecuritytoken_token_crud ON dbo.SystemsSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- AddressSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_addresssecuritytoken_token_crud ON address.AddressSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- EventSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_eventsecuritytoken_token_crud ON event.EventSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- EventTypesSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_EventTypesSecurityToken_token_crud ON event.EventTypesSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- GeographySecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_geographysecuritytoken_token_crud ON geography.GeographySecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartySecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartysecuritytoken_token_crud ON party.InvolvedPartySecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartyTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartytypesecuritytoken_token_crud ON party.InvolvedPartyTypeSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartyNameTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartynametypesecuritytoken_token_crud ON party.InvolvedPartyNameTypeSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartyIdentificationTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartyidentificationtypesecuritytoken_token_crud ON party.InvolvedPartyIdentificationTypeSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartyNonOrganicSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartynonorganicsecuritytoken_token_crud ON party.InvolvedPartyNonOrganicSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartyOrganicSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganicsecuritytoken_token_crud ON party.InvolvedPartyOrganicSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- InvolvedPartyOrganicTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_involvedpartyorganictypesecuritytoken_token_crud ON party.InvolvedPartyOrganicTypeSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ProductSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_productsecuritytoken_token_crud ON product.ProductSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ProductTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_producttypesecuritytoken_token_crud ON product.ProductTypesSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ResourceItemSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_resourceitemsecuritytoken_token_crud ON resource.ResourceItemSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ResourceItemDataSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_resourceitemdatasecuritytoken_token_crud ON resource.ResourceItemDataSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ResourceItemTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_resourceitemtypesecuritytoken_token_crud ON resource.ResourceItemTypeSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- RulesSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_rulessecuritytoken_token_crud ON rules.RulesSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- RulesTypesSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_RulesTypesSecurityToken_token_crud ON rules.RulesTypesSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ArrangementSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_arrangementsecuritytoken_token_crud ON arrangement.ArrangementSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- ArrangementTypeSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_arrangementtypesecuritytoken_token_crud ON arrangement.ArrangementTypeSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- SecurityTokensSecurityToken permissions
CREATE INDEX IF NOT EXISTS idx_SecurityTokensSecurityToken_token_crud ON security.SecurityTokensSecurityToken(SecurityTokenID, CreateAllowed, ReadAllowed, UpdateAllowed, DeleteAllowed);

-- =============================================
-- Relationship Table Indexes
-- =============================================

-- ActiveFlagXClassification
CREATE INDEX IF NOT EXISTS idx_activeflagxclassification_activeflagid ON dbo.ActiveFlagXClassification(ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_activeflagxclassification_classificationid ON dbo.ActiveFlagXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_activeflagxclassification_value ON dbo.ActiveFlagXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_activeflagxclassification_activeflag_classification ON dbo.ActiveFlagXClassification(ActiveFlagID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_activeflagxclassification_classification_activeflag ON dbo.ActiveFlagXClassification(ClassificationID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_activeflagxclassification_classification_value ON dbo.ActiveFlagXClassification(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_activeflagxclassifi_activefsification_enterprise_activeflag ON dbo.ActiveFlagXClassification(ActiveFlagID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_activeflagxclas_classiftiveflag_enterprise_activeflag ON dbo.ActiveFlagXClassification(ClassificationID, ActiveFlagID, EnterpriseID, ActiveFlagID);

-- ClassificationDataConceptXClassification
CREATE INDEX IF NOT EXISTS idx_classificationdatassification_classificationdataconceptid ON classification.ClassificationDataConceptXClassification(ClassificationDataConceptID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconceptxclassification_classificationid ON classification.ClassificationDataConceptXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconceptxclassification_value ON classification.ClassificationDataConceptXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_classificationdatacfissificationdataconcept_classification ON classification.ClassificationDataConceptXClassification(ClassificationDataConceptID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_classificacon_classification_classificationdataconcept ON classification.ClassificationDataConceptXClassification(ClassificationID, ClassificationDataConceptID);
CREATE INDEX IF NOT EXISTS idx_classificationdatacontion_classification_value ON classification.ClassificationDataConceptXClassification(ClassificationID, Value);

-- EnterpriseXClassification
CREATE INDEX IF NOT EXISTS idx_enterprisexclassification_enterpriseid ON dbo.EnterpriseXClassification(EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_enterprisexclassification_classificationid ON dbo.EnterpriseXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_enterprisexclassification_value ON dbo.EnterpriseXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_enterprisexclassification_enterprise_classification ON dbo.EnterpriseXClassification(EnterpriseID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_enterprisexclassification_classification_enterprise ON dbo.EnterpriseXClassification(ClassificationID, EnterpriseID);
CREATE INDEX IF NOT EXISTS idx_enterprisexclassification_classification_value ON dbo.EnterpriseXClassification(ClassificationID, Value);

-- SystemsXClassification
CREATE INDEX IF NOT EXISTS idx_systemsxclassification_systemsid ON dbo.SystemXClassification(SystemID);
CREATE INDEX IF NOT EXISTS idx_systemsxclassification_classificationid ON dbo.SystemXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_systemsxclassification_value ON dbo.SystemXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_systemsxclassification_systems_classification ON dbo.SystemXClassification(SystemID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_systemsxclassification_classification_systems ON dbo.SystemXClassification(ClassificationID, SystemID);
CREATE INDEX IF NOT EXISTS idx_systemsxclassification_classification_value ON dbo.SystemXClassification(ClassificationID, Value);

-- AddressXClassification
CREATE INDEX IF NOT EXISTS idx_addressxclassification_addressid ON address.AddressXClassification(AddressID);
CREATE INDEX IF NOT EXISTS idx_addressxclassification_classificationid ON address.AddressXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_addressxclassification_value ON address.AddressXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_addressxclassification_address_classification ON address.AddressXClassification(AddressID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_addressxclassification_classification_address ON address.AddressXClassification(ClassificationID, AddressID);
CREATE INDEX IF NOT EXISTS idx_addressxclassification_classification_value ON address.AddressXClassification(ClassificationID, Value);

-- AddressXGeography
CREATE INDEX IF NOT EXISTS idx_addressxgeography_addressid ON address.AddressXGeography(AddressID);
CREATE INDEX IF NOT EXISTS idx_addressxgeography_geographyid ON address.AddressXGeography(GeographyID);
CREATE INDEX IF NOT EXISTS idx_addressxgeography_value ON address.AddressXGeography(Value);
CREATE INDEX IF NOT EXISTS idx_addressxgeography_address_geography ON address.AddressXGeography(AddressID, GeographyID);
CREATE INDEX IF NOT EXISTS idx_addressxgeography_geography_address ON address.AddressXGeography(GeographyID, AddressID);
CREATE INDEX IF NOT EXISTS idx_addressxgeography_classification_value ON address.AddressXGeography(ClassificationID, Value);

-- EventXEventType
CREATE INDEX IF NOT EXISTS idx_eventxeventtype_eventid ON event.EventXEventType(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxeventtype_EventTypesID ON event.EventXEventType(EventTypeID);
CREATE INDEX IF NOT EXISTS idx_eventxeventtype_value ON event.EventXEventType(Value);
CREATE INDEX IF NOT EXISTS idx_eventxeventtype_event_eventtype ON event.EventXEventType(EventID, EventTypeID);
CREATE INDEX IF NOT EXISTS idx_eventxeventtype_eventtype_event ON event.EventXEventType(EventTypeID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxeventtype_classification_value ON event.EventXEventType(ClassificationID, Value);

-- EventXAddress
CREATE INDEX IF NOT EXISTS idx_eventxaddress_eventid ON event.EventXAddress(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxaddress_addressid ON event.EventXAddress(AddressID);
CREATE INDEX IF NOT EXISTS idx_eventxaddress_value ON event.EventXAddress(Value);
CREATE INDEX IF NOT EXISTS idx_eventxaddress_event_address ON event.EventXAddress(EventID, AddressID);
CREATE INDEX IF NOT EXISTS idx_eventxaddress_address_event ON event.EventXAddress(AddressID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxaddress_classification_value ON event.EventXAddress(ClassificationID, Value);

-- EventXArrangement
CREATE INDEX IF NOT EXISTS idx_eventxarrangement_eventid ON event.EventXArrangement(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxarrangement_arrangementid ON event.EventXArrangement(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_eventxarrangement_value ON event.EventXArrangement(Value);
CREATE INDEX IF NOT EXISTS idx_eventxarrangement_event_arrangement ON event.EventXArrangement(EventID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_eventxarrangement_arrangement_event ON event.EventXArrangement(ArrangementID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxarrangement_classification_value ON event.EventXArrangement(ClassificationID, Value);

-- EventXGeography
CREATE INDEX IF NOT EXISTS idx_eventxgeography_eventid ON event.EventXGeography(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxgeography_geographyid ON event.EventXGeography(GeographyID);
CREATE INDEX IF NOT EXISTS idx_eventxgeography_value ON event.EventXGeography(Value);
CREATE INDEX IF NOT EXISTS idx_eventxgeography_event_geography ON event.EventXGeography(EventID, GeographyID);
CREATE INDEX IF NOT EXISTS idx_eventxgeography_geography_event ON event.EventXGeography(GeographyID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxgeography_classification_value ON event.EventXGeography(ClassificationID, Value);

-- EventXInvolvedParty
CREATE INDEX IF NOT EXISTS idx_eventxinvolvedparty_eventid ON event.EventXInvolvedParty(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxinvolvedparty_involvedpartyid ON event.EventXInvolvedParty(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_eventxinvolvedparty_value ON event.EventXInvolvedParty(Value);
CREATE INDEX IF NOT EXISTS idx_eventxinvolvedparty_event_involvedparty ON event.EventXInvolvedParty(EventID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_eventxinvolvedparty_involvedparty_event ON event.EventXInvolvedParty(InvolvedPartyID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxinvolvedparty_classification_value ON event.EventXInvolvedParty(ClassificationID, Value);

-- EventXProduct
CREATE INDEX IF NOT EXISTS idx_eventxproduct_eventid ON event.EventXProduct(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxproduct_productid ON event.EventXProduct(ProductID);
CREATE INDEX IF NOT EXISTS idx_eventxproduct_value ON event.EventXProduct(Value);
CREATE INDEX IF NOT EXISTS idx_eventxproduct_event_product ON event.EventXProduct(EventID, ProductID);
CREATE INDEX IF NOT EXISTS idx_eventxproduct_product_event ON event.EventXProduct(ProductID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxproduct_classification_value ON event.EventXProduct(ClassificationID, Value);

-- EventXResourceItem
CREATE INDEX IF NOT EXISTS idx_eventxresourceitem_eventid ON event.EventXResourceItem(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxresourceitem_resourceitemid ON event.EventXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_eventxresourceitem_value ON event.EventXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_eventxresourceitem_event_resourceitem ON event.EventXResourceItem(EventID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_eventxresourceitem_resourceitem_event ON event.EventXResourceItem(ResourceItemID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxresourceitem_classification_value ON event.EventXResourceItem(ClassificationID, Value);

-- EventXRules
CREATE INDEX IF NOT EXISTS idx_eventxrules_eventid ON event.EventXRules(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxrules_rulesid ON event.EventXRules(RulesID);
CREATE INDEX IF NOT EXISTS idx_eventxrules_value ON event.EventXRules(Value);
CREATE INDEX IF NOT EXISTS idx_eventxrules_event_rules ON event.EventXRules(EventID, RulesID);
CREATE INDEX IF NOT EXISTS idx_eventxrules_rules_event ON event.EventXRules(RulesID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxrules_classification_value ON event.EventXRules(ClassificationID, Value);

-- EventXEvent
CREATE INDEX IF NOT EXISTS idx_eventxevent_parenteventid ON event.EventXEvent(ParentEventID);
CREATE INDEX IF NOT EXISTS idx_eventxevent_childeventid ON event.EventXEvent(ChildEventID);
CREATE INDEX IF NOT EXISTS idx_eventxevent_value ON event.EventXEvent(Value);
CREATE INDEX IF NOT EXISTS idx_eventxevent_parent_child ON event.EventXEvent(ParentEventID, ChildEventID);
CREATE INDEX IF NOT EXISTS idx_eventxevent_child_parent ON event.EventXEvent(ChildEventID, ParentEventID);
CREATE INDEX IF NOT EXISTS idx_eventxevent_classification_value ON event.EventXEvent(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_eventxevent_parent_classification_enterprise_activeflag ON event.EventXEvent(ParentEventID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_eventxevent_child_classification_enterprise_activeflag ON event.EventXEvent(ChildEventID, ClassificationID, EnterpriseID, ActiveFlagID);

-- EventXClassification
CREATE INDEX IF NOT EXISTS idx_eventxclassification_eventid ON event.EventXClassification(EventID);
CREATE INDEX IF NOT EXISTS idx_eventxclassification_classificationid ON event.EventXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_eventxclassification_value ON event.EventXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_eventxclassification_event_classification ON event.EventXClassification(EventID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_eventxclassification_classification_event ON event.EventXClassification(ClassificationID, EventID);
CREATE INDEX IF NOT EXISTS idx_eventxclassification_classification_value ON event.EventXClassification(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_eventxevent_child_classifactiveflag ON event.EventXClassification( ClassificationID, EnterpriseID, ActiveFlagID);


-- GeographyXClassification
CREATE INDEX IF NOT EXISTS idx_geographyxclassification_geographyid ON geography.GeographyXClassification(GeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxclassification_classificationid ON geography.GeographyXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_geographyxclassification_value ON geography.GeographyXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_geographyxclassification_geography_classification ON geography.GeographyXClassification(GeographyID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_geographyxclassification_classification_geography ON geography.GeographyXClassification(ClassificationID, GeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxclassification_classification_value ON geography.GeographyXClassification(ClassificationID, Value);

-- GeographyXGeography
CREATE INDEX IF NOT EXISTS idx_geographyxgeography_parentgeographyid ON geography.GeographyXGeography(ParentGeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxgeography_childgeographyid ON geography.GeographyXGeography(ChildGeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxgeography_value ON geography.GeographyXGeography(Value);
CREATE INDEX IF NOT EXISTS idx_geographyxgeography_parent_child ON geography.GeographyXGeography(ParentGeographyID, ChildGeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxgeography_child_parent ON geography.GeographyXGeography(ChildGeographyID, ParentGeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxgeography_classification_value ON geography.GeographyXGeography(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_geographyxgeograpclassification_enterprise_activeflag ON geography.GeographyXGeography(ParentGeographyID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_geographyxgclassification_enterprise_activeflag ON geography.GeographyXGeography(ChildGeographyID, ClassificationID, EnterpriseID, ActiveFlagID);

-- GeographyXResourceItem
CREATE INDEX IF NOT EXISTS idx_geographyxresourceitem_geographyid ON geography.GeographyXResourceItem(GeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxresourceitem_resourceitemid ON geography.GeographyXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_geographyxresourceitem_value ON geography.GeographyXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_geographyxresourceitem_geography_resourceitem ON geography.GeographyXResourceItem(GeographyID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_geographyxresourceitem_resourceitem_geography ON geography.GeographyXResourceItem(ResourceItemID, GeographyID);
CREATE INDEX IF NOT EXISTS idx_geographyxresourceitem_classification_value ON geography.GeographyXResourceItem(ClassificationID, Value);

-- InvolvedPartyXClassification
CREATE INDEX IF NOT EXISTS idx_involvedpartyxclassification_involvedpartyid ON party.InvolvedPartyXClassification(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxclassification_classificationid ON party.InvolvedPartyXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxclassification_value ON party.InvolvedPartyXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxclassification_involvedparty_classification ON party.InvolvedPartyXClassification(InvolvedPartyID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxclassification_classification_involvedparty ON party.InvolvedPartyXClassification(ClassificationID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxclassification_classification_value ON party.InvolvedPartyXClassification(ClassificationID, Value);

-- InvolvedPartyXInvolvedParty
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedparty_parentinvolvedpartyid ON party.InvolvedPartyXInvolvedParty(ParentInvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedparty_childinvolvedpartyid ON party.InvolvedPartyXInvolvedParty(ChildInvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedparty_value ON party.InvolvedPartyXInvolvedParty(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedparty_parent_child ON party.InvolvedPartyXInvolvedParty(ParentInvolvedPartyID, ChildInvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedparty_child_parent ON party.InvolvedPartyXInvolvedParty(ChildInvolvedPartyID, ParentInvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedparty_classification_value ON party.InvolvedPartyXInvolvedParty(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolve_classification_enterprise_activeflag ON party.InvolvedPartyXInvolvedParty(ParentInvolvedPartyID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvchild_classification_enterprise_activeflag ON party.InvolvedPartyXInvolvedParty(ChildInvolvedPartyID, ClassificationID, EnterpriseID, ActiveFlagID);

-- InvolvedPartyXAddress
CREATE INDEX IF NOT EXISTS idx_involvedpartyxaddress_involvedpartyid ON party.InvolvedPartyXAddress(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxaddress_addressid ON party.InvolvedPartyXAddress(AddressID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxaddress_value ON party.InvolvedPartyXAddress(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxaddress_involvedparty_address ON party.InvolvedPartyXAddress(InvolvedPartyID, AddressID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxaddress_address_involvedparty ON party.InvolvedPartyXAddress(AddressID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxaddress_classification_value ON party.InvolvedPartyXAddress(ClassificationID, Value);

-- InvolvedPartyXProduct
CREATE INDEX IF NOT EXISTS idx_involvedpartyxproduct_involvedpartyid ON party.InvolvedPartyXProduct(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxproduct_productid ON party.InvolvedPartyXProduct(ProductID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxproduct_value ON party.InvolvedPartyXProduct(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxproduct_involvedparty_product ON party.InvolvedPartyXProduct(InvolvedPartyID, ProductID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxproduct_product_involvedparty ON party.InvolvedPartyXProduct(ProductID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxproduct_classification_value ON party.InvolvedPartyXProduct(ClassificationID, Value);

-- InvolvedPartyXResourceItem
CREATE INDEX IF NOT EXISTS idx_involvedpartyxresourceitem_involvedpartyid ON party.InvolvedPartyXResourceItem(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxresourceitem_resourceitemid ON party.InvolvedPartyXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxresourceitem_value ON party.InvolvedPartyXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxresourceitem_involvedparty_resourceitem ON party.InvolvedPartyXResourceItem(InvolvedPartyID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxresourceitem_resourceitem_involvedparty ON party.InvolvedPartyXResourceItem(ResourceItemID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxresourceitem_classification_value ON party.InvolvedPartyXResourceItem(ClassificationID, Value);

-- InvolvedPartyXRules
CREATE INDEX IF NOT EXISTS idx_involvedpartyxrules_involvedpartyid ON party.InvolvedPartyXRules(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxrules_rulesid ON party.InvolvedPartyXRules(RulesID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxrules_value ON party.InvolvedPartyXRules(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxrules_involvedparty_rules ON party.InvolvedPartyXRules(InvolvedPartyID, RulesID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxrules_rules_involvedparty ON party.InvolvedPartyXRules(RulesID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxrules_classification_value ON party.InvolvedPartyXRules(ClassificationID, Value);

-- InvolvedPartyXInvolvedPartyType
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartytype_involvedpartyid ON party.InvolvedPartyXInvolvedPartyType(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartytype_involvedpartytypeid ON party.InvolvedPartyXInvolvedPartyType(InvolvedPartyTypeID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartytype_value ON party.InvolvedPartyXInvolvedPartyType(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinpe_involvedparty_involvedpartytype ON party.InvolvedPartyXInvolvedPartyType(InvolvedPartyID, InvolvedPartyTypeID);
CREATE INDEX IF NOT EXISTS idx_involvedvedpartytype_involvedpartytype_involvedparty ON party.InvolvedPartyXInvolvedPartyType(InvolvedPartyTypeID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartytype_classification_value ON party.InvolvedPartyXInvolvedPartyType(ClassificationID, Value);

-- InvolvedPartyXInvolvedPartyNameType
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartynametype_involvedpartyid ON party.InvolvedPartyXInvolvedPartyNameType(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartynametype_involvedpartynametypeid ON party.InvolvedPartyXInvolvedPartyNameType(InvolvedPartyNameTypeID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartynametype_value ON party.InvolvedPartyXInvolvedPartyNameType(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvoinvolvedparty_involvedpartynametype ON party.InvolvedPartyXInvolvedPartyNameType(InvolvedPartyID, InvolvedPartyNameTypeID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvenvolvedpartynametype_involvedparty ON party.InvolvedPartyXInvolvedPartyNameType(InvolvedPartyNameTypeID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartynametype_classification_value ON party.InvolvedPartyXInvolvedPartyNameType(ClassificationID, Value);

-- InvolvedPartyXInvolvedPartyIdentificationType
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedtiontype_involvedpartyid ON party.InvolvedPartyXInvolvedPartyIdentificationType(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxentifiinvolvedpartyidentificationtypeid ON party.InvolvedPartyXInvolvedPartyIdentificationType(InvolvedPartyIdentificationTypeID);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxinvolvedpartyidentificationtype_value ON party.InvolvedPartyXInvolvedPartyIdentificationType(Value);
CREATE INDEX IF NOT EXISTS idx_involvedpartyxintificatdpartyidentificationtype ON party.InvolvedPartyXInvolvedPartyIdentificationType(InvolvedPartyID, InvolvedPartyIdentificationTypeID);
CREATE INDEX IF NOT EXISTS idx_involvedparttiontypedentificationtype_involvedparty ON party.InvolvedPartyXInvolvedPartyIdentificationType(InvolvedPartyIdentificationTypeID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_involvedpcationtype_classification_value ON party.InvolvedPartyXInvolvedPartyIdentificationType(ClassificationID, Value);

-- ProductXClassification
CREATE INDEX IF NOT EXISTS idx_productxclassification_productid ON product.ProductXClassification(ProductID);
CREATE INDEX IF NOT EXISTS idx_productxclassification_classificationid ON product.ProductXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_productxclassification_value ON product.ProductXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_productxclassification_product_classification ON product.ProductXClassification(ProductID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_productxclassification_classification_product ON product.ProductXClassification(ClassificationID, ProductID);
CREATE INDEX IF NOT EXISTS idx_productxclassification_classification_value ON product.ProductXClassification(ClassificationID, Value);

-- ProductXProduct
CREATE INDEX IF NOT EXISTS idx_productxproduct_parentproductid ON product.ProductXProduct(ParentProductID);
CREATE INDEX IF NOT EXISTS idx_productxproduct_childproductid ON product.ProductXProduct(ChildProductID);
CREATE INDEX IF NOT EXISTS idx_productxproduct_value ON product.ProductXProduct(Value);
CREATE INDEX IF NOT EXISTS idx_productxproduct_parent_child ON product.ProductXProduct(ParentProductID, ChildProductID);
CREATE INDEX IF NOT EXISTS idx_productxproduct_child_parent ON product.ProductXProduct(ChildProductID, ParentProductID);
CREATE INDEX IF NOT EXISTS idx_productxproduct_classification_value ON product.ProductXProduct(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_productxproduct_parent_classification_enterprise_activeflag ON product.ProductXProduct(ParentProductID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_productxproduct_child_classification_enterprise_activeflag ON product.ProductXProduct(ChildProductID, ClassificationID, EnterpriseID, ActiveFlagID);

-- ProductXResourceItem
CREATE INDEX IF NOT EXISTS idx_productxresourceitem_productid ON product.ProductXResourceItem(ProductID);
CREATE INDEX IF NOT EXISTS idx_productxresourceitem_resourceitemid ON product.ProductXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_productxresourceitem_value ON product.ProductXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_productxresourceitem_product_resourceitem ON product.ProductXResourceItem(ProductID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_productxresourceitem_resourceitem_product ON product.ProductXResourceItem(ResourceItemID, ProductID);
CREATE INDEX IF NOT EXISTS idx_productxresourceitem_classification_value ON product.ProductXResourceItem(ClassificationID, Value);

-- ProductXProductType
CREATE INDEX IF NOT EXISTS idx_productxproducttype_productid ON product.ProductXProductType(ProductID);
CREATE INDEX IF NOT EXISTS idx_productxproducttype_ProductTypesID ON product.ProductXProductType(ProductTypeID);
CREATE INDEX IF NOT EXISTS idx_productxproducttype_value ON product.ProductXProductType(Value);
CREATE INDEX IF NOT EXISTS idx_productxproducttype_product_producttype ON product.ProductXProductType(ProductID, ProductTypeID);
CREATE INDEX IF NOT EXISTS idx_productxproducttype_producttype_product ON product.ProductXProductType(ProductTypeID, ProductID);
CREATE INDEX IF NOT EXISTS idx_productxproducttype_classification_value ON product.ProductXProductType(ClassificationID, Value);

-- ResourceItemXClassification
CREATE INDEX IF NOT EXISTS idx_resourceitemxclassification_resourceitemid ON resource.ResourceItemXClassification(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxclassification_classificationid ON resource.ResourceItemXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxclassification_value ON resource.ResourceItemXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_resourceitemxclassification_resourceitem_classification ON resource.ResourceItemXClassification(ResourceItemID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxclassification_classification_resourceitem ON resource.ResourceItemXClassification(ClassificationID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxclassification_classification_value ON resource.ResourceItemXClassification(ClassificationID, Value);

-- ResourceItemXResourceItem
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitem_parentresourceitemid ON resource.ResourceItemXResourceItem(ParentResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitem_childresourceitemid ON resource.ResourceItemXResourceItem(ChildResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitem_value ON resource.ResourceItemXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitem_parent_child ON resource.ResourceItemXResourceItem(ParentResourceItemID, ChildResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitem_child_parent ON resource.ResourceItemXResourceItem(ChildResourceItemID, ParentResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitem_classification_value ON resource.ResourceItemXResourceItem(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceification_enterprise_activeflag ON resource.ResourceItemXResourceItem(ParentResourceItemID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxred_classification_enterprise_activeflag ON resource.ResourceItemXResourceItem(ChildResourceItemID, ClassificationID, EnterpriseID, ActiveFlagID);

-- ResourceItemXResourceItemType
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitemtype_resourceitemid ON resource.ResourceItemXResourceItemType(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitemtype_resourceitemtypeid ON resource.ResourceItemXResourceItemType(ResourceItemTypeID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitemtype_value ON resource.ResourceItemXResourceItemType(Value);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitemtype_resourceitem_resourceitemtype ON resource.ResourceItemXResourceItemType(ResourceItemID, ResourceItemTypeID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitemtype_resourceitemtype_resourceitem ON resource.ResourceItemXResourceItemType(ResourceItemTypeID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_resourceitemxresourceitemtype_classification_value ON resource.ResourceItemXResourceItemType(ClassificationID, Value);

-- ResourceItemDataXClassification
CREATE INDEX IF NOT EXISTS idx_resourceitemdataxclassification_resourceitemdataid ON resource.ResourceItemDataXClassification(ResourceItemDataID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdataxclassification_classificationid ON resource.ResourceItemDataXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdataxclassification_value ON resource.ResourceItemDataXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_resourceitemdataxclassirceitemdata_classification ON resource.ResourceItemDataXClassification(ResourceItemDataID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdataxclssification_resourceitemdata ON resource.ResourceItemDataXClassification(ClassificationID, ResourceItemDataID);
CREATE INDEX IF NOT EXISTS idx_resourceitemdataxclassification_classification_value ON resource.ResourceItemDataXClassification(ClassificationID, Value);

-- RulesXClassification
CREATE INDEX IF NOT EXISTS idx_rulesxclassification_rulesid ON rules.RulesXClassification(RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxclassification_classificationid ON rules.RulesXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_rulesxclassification_value ON rules.RulesXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxclassification_rules_classification ON rules.RulesXClassification(RulesID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_rulesxclassification_classification_rules ON rules.RulesXClassification(ClassificationID, RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxclassification_classification_value ON rules.RulesXClassification(ClassificationID, Value);

-- RulesXRules
CREATE INDEX IF NOT EXISTS idx_rulesxrules_parentrulesid ON rules.RulesXRules(ParentRulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_childrulesid ON rules.RulesXRules(ChildRulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_value ON rules.RulesXRules(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_parent_child ON rules.RulesXRules(ParentRulesID, ChildRulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_child_parent ON rules.RulesXRules(ChildRulesID, ParentRulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_classification_value ON rules.RulesXRules(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_parent_classification_enterprise_activeflag ON rules.RulesXRules(ParentRulesID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_rulesxrules_child_classification_enterprise_activeflag ON rules.RulesXRules(ChildRulesID, ClassificationID, EnterpriseID, ActiveFlagID);

-- RulesXRulesType
CREATE INDEX IF NOT EXISTS idx_rulesxrulestype_rulesid ON rules.RulesXRulesType(RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxrulestype_RulesTypesID ON rules.RulesXRulesType(RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_rulesxrulestype_value ON rules.RulesXRulesType(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxrulestype_rules_rulestype ON rules.RulesXRulesType(RulesID, RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_rulesxrulestype_rulestype_rules ON rules.RulesXRulesType(RulesTypeID, RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxrulestype_classification_value ON rules.RulesXRulesType(ClassificationID, Value);

-- RulesXArrangement
CREATE INDEX IF NOT EXISTS idx_rulesxarrangement_rulesid ON rules.RulesXArrangement(RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxarrangement_arrangementid ON rules.RulesXArrangement(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_rulesxarrangement_value ON rules.RulesXArrangement(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxarrangement_rules_arrangement ON rules.RulesXArrangement(RulesID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_rulesxarrangement_arrangement_rules ON rules.RulesXArrangement(ArrangementID, RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxarrangement_classification_value ON rules.RulesXArrangement(ClassificationID, Value);

-- RulesXInvolvedParty
CREATE INDEX IF NOT EXISTS idx_rulesxinvolvedparty_rulesid ON rules.RulesXInvolvedParty(RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxinvolvedparty_involvedpartyid ON rules.RulesXInvolvedParty(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_rulesxinvolvedparty_value ON rules.RulesXInvolvedParty(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxinvolvedparty_rules_involvedparty ON rules.RulesXInvolvedParty(RulesID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_rulesxinvolvedparty_involvedparty_rules ON rules.RulesXInvolvedParty(InvolvedPartyID, RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxinvolvedparty_classification_value ON rules.RulesXInvolvedParty(ClassificationID, Value);

-- RulesXProduct
CREATE INDEX IF NOT EXISTS idx_rulesxproduct_rulesid ON rules.RulesXProduct(RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxproduct_productid ON rules.RulesXProduct(ProductID);
CREATE INDEX IF NOT EXISTS idx_rulesxproduct_value ON rules.RulesXProduct(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxproduct_rules_product ON rules.RulesXProduct(RulesID, ProductID);
CREATE INDEX IF NOT EXISTS idx_rulesxproduct_product_rules ON rules.RulesXProduct(ProductID, RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxproduct_classification_value ON rules.RulesXProduct(ClassificationID, Value);

-- RulesXResourceItem
CREATE INDEX IF NOT EXISTS idx_rulesxresourceitem_rulesid ON rules.RulesXResourceItem(RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxresourceitem_resourceitemid ON rules.RulesXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_rulesxresourceitem_value ON rules.RulesXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_rulesxresourceitem_rules_resourceitem ON rules.RulesXResourceItem(RulesID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_rulesxresourceitem_resourceitem_rules ON rules.RulesXResourceItem(ResourceItemID, RulesID);
CREATE INDEX IF NOT EXISTS idx_rulesxresourceitem_classification_value ON rules.RulesXResourceItem(ClassificationID, Value);

-- RulesTypeXClassification
CREATE INDEX IF NOT EXISTS idx_rulestypexclassification_RulesTypesID ON rules.RulesTypeXClassification(RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_rulestypexclassification_classificationid ON rules.RulesTypeXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_rulestypexclassification_value ON rules.RulesTypeXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_rulestypexclassification_rulestype_classification ON rules.RulesTypeXClassification(RulesTypeID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_rulestypexclassification_classification_rulestype ON rules.RulesTypeXClassification(ClassificationID, RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_rulestypexclassification_classification_value ON rules.RulesTypeXClassification(ClassificationID, Value);

-- RulesTypeXResourceItem
CREATE INDEX IF NOT EXISTS idx_rulestypexresourceitem_RulesTypesID ON rules.RulesTypeXResourceItem(RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_rulestypexresourceitem_resourceitemid ON rules.RulesTypeXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_rulestypexresourceitem_value ON rules.RulesTypeXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_rulestypexresourceitem_rulestype_resourceitem ON rules.RulesTypeXResourceItem(RulesTypeID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_rulestypexresourceitem_resourceitem_rulestype ON rules.RulesTypeXResourceItem(ResourceItemID, RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_rulestypexresourceitem_classification_value ON rules.RulesTypeXResourceItem(ClassificationID, Value);

-- ArrangementXClassification
CREATE INDEX IF NOT EXISTS idx_arrangementxclassification_arrangementid ON arrangement.ArrangementXClassification(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxclassification_classificationid ON arrangement.ArrangementXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_arrangementxclassification_value ON arrangement.ArrangementXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxclassification_arrangement_classification ON arrangement.ArrangementXClassification(ArrangementID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_arrangementxclassification_classification_arrangement ON arrangement.ArrangementXClassification(ClassificationID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxclassification_classification_value ON arrangement.ArrangementXClassification(ClassificationID, Value);

-- ArrangementXArrangement
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangement_parentarrangementid ON arrangement.ArrangementXArrangement(ParentArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangement_childarrangementid ON arrangement.ArrangementXArrangement(ChildArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangement_value ON arrangement.ArrangementXArrangement(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangement_parent_child ON arrangement.ArrangementXArrangement(ParentArrangementID, ChildArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangement_child_parent ON arrangement.ArrangementXArrangement(ChildArrangementID, ParentArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangement_classification_value ON arrangement.ArrangementXArrangement(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxarranclassification_enterprise_activeflag ON arrangement.ArrangementXArrangement(ParentArrangementID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_arrangementxard_classification_enterprise_activeflag ON arrangement.ArrangementXArrangement(ChildArrangementID, ClassificationID, EnterpriseID, ActiveFlagID);

-- ArrangementXArrangementType
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangementtype_arrangementid ON arrangement.ArrangementXArrangementType(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangementtype_arrangementtypeid ON arrangement.ArrangementXArrangementType(ArrangementTypeID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangementtype_value ON arrangement.ArrangementXArrangementType(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangementtype_arrangement_arrangementtype ON arrangement.ArrangementXArrangementType(ArrangementID, ArrangementTypeID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangementtype_arrangementtype_arrangement ON arrangement.ArrangementXArrangementType(ArrangementTypeID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxarrangementtype_classification_value ON arrangement.ArrangementXArrangementType(ClassificationID, Value);

-- ArrangementXInvolvedParty
CREATE INDEX IF NOT EXISTS idx_arrangementxinvolvedparty_arrangementid ON arrangement.ArrangementXInvolvedParty(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxinvolvedparty_involvedpartyid ON arrangement.ArrangementXInvolvedParty(InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_arrangementxinvolvedparty_value ON arrangement.ArrangementXInvolvedParty(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxinvolvedparty_arrangement_involvedparty ON arrangement.ArrangementXInvolvedParty(ArrangementID, InvolvedPartyID);
CREATE INDEX IF NOT EXISTS idx_arrangementxinvolvedparty_involvedparty_arrangement ON arrangement.ArrangementXInvolvedParty(InvolvedPartyID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxinvolvedparty_classification_value ON arrangement.ArrangementXInvolvedParty(ClassificationID, Value);

-- ArrangementXProduct
CREATE INDEX IF NOT EXISTS idx_arrangementxproduct_arrangementid ON arrangement.ArrangementXProduct(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxproduct_productid ON arrangement.ArrangementXProduct(ProductID);
CREATE INDEX IF NOT EXISTS idx_arrangementxproduct_value ON arrangement.ArrangementXProduct(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxproduct_arrangement_product ON arrangement.ArrangementXProduct(ArrangementID, ProductID);
CREATE INDEX IF NOT EXISTS idx_arrangementxproduct_product_arrangement ON arrangement.ArrangementXProduct(ProductID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxproduct_classification_value ON arrangement.ArrangementXProduct(ClassificationID, Value);

-- ArrangementXResourceItem
CREATE INDEX IF NOT EXISTS idx_arrangementxresourceitem_arrangementid ON arrangement.ArrangementXResourceItem(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxresourceitem_resourceitemid ON arrangement.ArrangementXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_arrangementxresourceitem_value ON arrangement.ArrangementXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxresourceitem_arrangement_resourceitem ON arrangement.ArrangementXResourceItem(ArrangementID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_arrangementxresourceitem_resourceitem_arrangement ON arrangement.ArrangementXResourceItem(ResourceItemID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxresourceitem_classification_value ON arrangement.ArrangementXResourceItem(ClassificationID, Value);

-- ArrangementXRules
CREATE INDEX IF NOT EXISTS idx_arrangementxrules_arrangementid ON arrangement.ArrangementXRules(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrules_rulesid ON arrangement.ArrangementXRules(RulesID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrules_value ON arrangement.ArrangementXRules(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxrules_arrangement_rules ON arrangement.ArrangementXRules(ArrangementID, RulesID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrules_rules_arrangement ON arrangement.ArrangementXRules(RulesID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrules_classification_value ON arrangement.ArrangementXRules(ClassificationID, Value);

-- ArrangementXRulesType
CREATE INDEX IF NOT EXISTS idx_arrangementxrulestype_arrangementid ON arrangement.ArrangementXRulesType(ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrulestype_RulesTypesID ON arrangement.ArrangementXRulesType(RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrulestype_value ON arrangement.ArrangementXRulesType(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementxrulestype_arrangement_rulestype ON arrangement.ArrangementXRulesType(ArrangementID, RulesTypeID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrulestype_rulestype_arrangement ON arrangement.ArrangementXRulesType(RulesTypeID, ArrangementID);
CREATE INDEX IF NOT EXISTS idx_arrangementxrulestype_classification_value ON arrangement.ArrangementXRulesType(ClassificationID, Value);

-- ArrangementTypeXClassification
CREATE INDEX IF NOT EXISTS idx_arrangementtypexclassification_arrangementtypeid ON arrangement.ArrangementTypeXClassification(ArrangementTypeID);
CREATE INDEX IF NOT EXISTS idx_arrangementtypexclassification_classificationid ON arrangement.ArrangementTypeXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_arrangementtypexclassification_value ON arrangement.ArrangementTypeXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_arrangementtypexsangemeype_classification ON arrangement.ArrangementTypeXClassification(ArrangementTypeID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_arrangementtypclassication_arrangementtype ON arrangement.ArrangementTypeXClassification(ClassificationID, ArrangementTypeID);
CREATE INDEX IF NOT EXISTS idx_arrangementtypexclassification_classification_value ON arrangement.ArrangementTypeXClassification(ClassificationID, Value);

-- SecurityTokenXClassification
CREATE INDEX IF NOT EXISTS idx_securitytokenxclassification_securitytokenid ON security.SecurityTokenXClassification(SecurityTokenID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxclassification_classificationid ON security.SecurityTokenXClassification(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxclassification_value ON security.SecurityTokenXClassification(Value);
CREATE INDEX IF NOT EXISTS idx_securitytokenxclassification_securitytoken_classification ON security.SecurityTokenXClassification(SecurityTokenID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxclassification_classification_securitytoken ON security.SecurityTokenXClassification(ClassificationID, SecurityTokenID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxclassification_classification_value ON security.SecurityTokenXClassification(ClassificationID, Value);

-- SecurityTokenXSecurityToken
CREATE INDEX IF NOT EXISTS idx_securitytokenxsecuritytoken_parentsecuritytokenid ON security.SecurityTokenXSecurityToken(ParentSecurityTokenID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxsecuritytoken_childsecuritytokenid ON security.SecurityTokenXSecurityToken(ChildSecurityTokenID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxsecuritytoken_value ON security.SecurityTokenXSecurityToken(Value);
CREATE INDEX IF NOT EXISTS idx_securitytokenxsecuritytoken_parent_child ON security.SecurityTokenXSecurityToken(ParentSecurityTokenID, ChildSecurityTokenID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxsecuritytoken_child_parent ON security.SecurityTokenXSecurityToken(ChildSecurityTokenID, ParentSecurityTokenID);
CREATE INDEX IF NOT EXISTS idx_securitytokenxsecuritytoken_classification_value ON security.SecurityTokenXSecurityToken(ClassificationID, Value);
CREATE INDEX IF NOT EXISTS idx_securitytokenxseenterprise_activeflag ON security.SecurityTokenXSecurityToken(ParentSecurityTokenID, ClassificationID, EnterpriseID, ActiveFlagID);
CREATE INDEX IF NOT EXISTS idx_securitytokenassifinterprise_activeflag ON security.SecurityTokenXSecurityToken(ChildSecurityTokenID, ClassificationID, EnterpriseID, ActiveFlagID);

-- ClassificationXResourceItem
CREATE INDEX IF NOT EXISTS idx_classificationxresourceitem_classificationid ON classification.ClassificationXResourceItem(ClassificationID);
CREATE INDEX IF NOT EXISTS idx_classificationxresourceitem_resourceitemid ON classification.ClassificationXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_classificationxresourceitem_value ON classification.ClassificationXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_classificationxresourceitem_classification_resourceitem ON classification.ClassificationXResourceItem(ClassificationID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_classificationxresourceitem_resourceitem_classification ON classification.ClassificationXResourceItem(ResourceItemID, ClassificationID);
CREATE INDEX IF NOT EXISTS idx_classificationxresourceitem_classification_value ON classification.ClassificationXResourceItem(ClassificationID, Value);

-- ClassificationDataConceptXResourceItem
CREATE INDEX IF NOT EXISTS idx_classificationdaeitem_classificationdataconceptid ON classification.ClassificationDataConceptXResourceItem(ClassificationDataConceptID);
CREATE INDEX IF NOT EXISTS idx_classificationdataonceptxresourceitem_resourceitemid ON classification.ClassificationDataConceptXResourceItem(ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconceptxresourceitem_value ON classification.ClassificationDataConceptXResourceItem(Value);
CREATE INDEX IF NOT EXISTS idx_classificationdataficationdataconcept_resourceitem ON classification.ClassificationDataConceptXResourceItem(ClassificationDataConceptID, ResourceItemID);
CREATE INDEX IF NOT EXISTS idx_classificationourceitem_classificationdataconcept ON classification.ClassificationDataConceptXResourceItem(ResourceItemID, ClassificationDataConceptID);
CREATE INDEX IF NOT EXISTS idx_classificationdataconceptxresourceitem_classification_value ON classification.ClassificationDataConceptXResourceItem(ClassificationID, Value);
