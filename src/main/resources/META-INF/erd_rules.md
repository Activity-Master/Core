# ActivityMaster Entity Relationship Diagram (ERD) Rules

This document describes the entities in the ActivityMaster system, their columns, registered column names, and relationships.

## Table of Contents

1. [Base Entity Structure](#base-entity-structure)
2. [Entities](#entities)
   - [ActiveFlag](#activeflag)
   - [Classification](#classification)
   - [ClassificationDataConcept](#classificationdataconcept)
   - [Enterprise](#enterprise)
   - [Systems](#systems)
   - [Address](#address)
   - [Event](#event)
   - [EventType](#eventtype)
   - [Geography](#geography)
   - [InvolvedParty](#involvedparty)
   - [InvolvedPartyType](#involvedpartytype)
   - [InvolvedPartyNameType](#involvedpartynametype)
   - [InvolvedPartyIdentificationType](#involvedpartyidentificationtype)
   - [InvolvedPartyNonOrganic](#involvedpartynonorganic)
   - [InvolvedPartyOrganic](#involvedpartyorganic)
   - [InvolvedPartyOrganicType](#involvedpartyorganictype)
   - [Product](#product)
   - [ProductType](#producttype)
   - [ResourceItem](#resourceitem)
   - [ResourceItemData](#resourceitemdata)
   - [ResourceItemType](#resourceitemtype)
   - [Rules](#rules)
   - [RulesType](#rulestype)
   - [Arrangement](#arrangement)
   - [ArrangementType](#arrangementtype)
   - [SecurityToken](#securitytoken)
3. [Time Dimension Model](#time-dimension-model)
   - [Time](#time)
   - [Hours](#hours)
   - [Days](#days)
   - [Weeks](#weeks)
   - [Months](#months)
   - [DayNames](#daynames)
   - [Quarters](#quarters)
   - [Years](#years)
4. [Relationship Structure](#relationship-structure)
5. [Security Structure](#security-structure)
6. [Common Patterns](#common-patterns)
7. [Database Indexing Strategy](#database-indexing-strategy)

## Base Entity Structure

All entities in the ActivityMaster system inherit from a hierarchy of base classes that provide common functionality and properties:

1. **SCDEntity** (from com.entityassist package)
   - `effectiveFromDate` (Column: EffectiveFromDate) - The date from which the entity is effective
   - `effectiveToDate` (Column: EffectiveToDate) - The date until which the entity is effective
   - `warehouseCreatedTimestamp` (Column: WarehouseCreatedTimestamp) - The timestamp when the entity was created
   - `warehouseLastUpdatedTimestamp` (Column: WarehouseLastUpdatedTimestamp) - The timestamp when the entity was last updated

2. **WarehouseBaseTable** extends SCDEntity
   - `warehouseFromDate` (Column: WarehouseFromDate) - The database partition which the entity is stored in the warehouse
   - Adds methods for expiring entities after a specified duration

3. **WarehouseCoreTable** extends WarehouseBaseTable
   - Adds security-related functionality
   - Provides methods for creating default security access for different user groups

4. **WarehouseTable** extends WarehouseCoreTable
   - `originalSourceSystemUniqueID` (Column: OriginalSourceSystemUniqueID) - UUID of the original source system
   - `originalSourceSystemID` (Column: OriginalSourceSystemID) - Reference to the original source system, for audit purposes only (not used in queries)
   - `enterpriseID` (Column: EnterpriseID) - Reference to the enterprise

5. **WarehouseSCDTable** extends WarehouseTable
   - `activeFlagID` (Column: ActiveFlagID) - Reference to the active flag
   - `systemID` (Column: SystemID) - Reference to the system that produced the record, for audit purposes only (not used in queries)

6. **WarehouseRelationshipTable** extends WarehouseSCDTable
   - `value` (Column: Value) - The value of the relationship

7. **WarehouseClassificationRelationshipTable** extends WarehouseRelationshipTable
   - `classificationID` (Column: ClassificationID) - Reference to the classification

## Entities

### ActiveFlag
- **Table**: dbo.ActiveFlag
- **Primary Key**: `id` (Column: ActiveFlagID)
- **Columns**:
  - `enterpriseID` (Column: EnterpriseID) - Reference to the enterprise
  - `name` (Column: ActiveFlagName) - Name of the active flag
  - `description` (Column: ActiveFlagDescription) - Description of the active flag
  - `allowAccess` (Column: AllowAccess) - Whether access is allowed
  - Inherits all columns from WarehouseCoreTable
- **Relationships**:
  - One-to-Many with ActiveFlagSecurityToken (mapped by "base")
  - Many-to-Many with Classification through ActiveFlagXClassification

### ActiveFlagSecurityToken
- **Table**: dbo.ActiveFlagSecurityToken
- **Primary Key**: `id` (Column: ActiveFlagSecurityTokenID)
- **Columns**:
  - `SecurityTokenActiveFlagID` (Column: SecurityTokenActiveFlagID) - Reference to the ActiveFlag
  - Inherits all columns from WarehouseSecurityTable including:
    - `securityTokenID` (Column: SecurityTokenID) - Reference to the security token
    - `createAllowed` (Column: CreateAllowed) - Whether creation is allowed
    - `readAllowed` (Column: ReadAllowed) - Whether reading is allowed
    - `updateAllowed` (Column: UpdateAllowed) - Whether updating is allowed
    - `deleteAllowed` (Column: DeleteAllowed) - Whether deletion is allowed

### ActiveFlagXClassification
- **Table**: dbo.ActiveFlagXClassification
- **Primary Key**: `id` (Column: ActiveFlagXClassificationID)
- **Columns**:
  - `activeFlagID` (Column: ActiveFlagID) - Reference to the active flag
  - `classificationID` (Column: ClassificationID) - Reference to the classification
  - `value` (Column: Value) - The value of the relationship
  - Inherits all columns from WarehouseClassificationRelationshipTable
- **Relationships**:
  - Many-to-One with ActiveFlag
  - Many-to-One with Classification
  - One-to-Many with ActiveFlagXClassificationSecurityToken (mapped by "base")

### Classification
- **Table**: classification.Classification
- **Primary Key**: `id` (Column: ClassificationID)
- **Columns**:
  - `name` (Column: ClassificationName) - Name of the classification
  - `description` (Column: ClassificationDescription) - Description of the classification
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with ActiveFlag through ActiveFlagXClassification
  - Many-to-Many with other entities through respective X tables
  - One-to-Many with ClassificationSecurityToken (mapped by "base")

### ClassificationDataConcept
- **Table**: classification.ClassificationDataConcept
- **Primary Key**: `id` (Column: ClassificationDataConceptID)
- **Columns**:
  - `name` (Column: ClassificationDataConceptName) - Name of the classification data concept
  - `description` (Column: ClassificationDataConceptDescription) - Description of the classification data concept
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through ClassificationDataConceptXClassification
  - Many-to-Many with ResourceItem through ClassificationDataConceptXResourceItem
  - One-to-Many with ClassificationDataConceptSecurityToken (mapped by "base")

### Enterprise
- **Table**: dbo.Enterprise
- **Primary Key**: `id` (Column: EnterpriseID)
- **Columns**:
  - `name` (Column: EnterpriseName) - Name of the enterprise
  - `description` (Column: EnterpriseDescription) - Description of the enterprise
  - Inherits all columns from WarehouseCoreTable
- **Relationships**:
  - One-to-Many with most other entities
  - Many-to-Many with Classification through EnterpriseXClassification
  - One-to-Many with EnterpriseSecurityToken (mapped by "base")

### Systems
- **Table**: systems.Systems
- **Primary Key**: `id` (Column: SystemID)
- **Columns**:
  - `name` (Column: SystemName) - Name of the system
  - `description` (Column: SystemDescription) - Description of the system
  - `enterpriseID` (Column: EnterpriseID) - Reference to the enterprise
  - Inherits all columns from WarehouseTable
- **Relationships**:
  - Many-to-One with Enterprise
  - One-to-Many with most other entities
  - Many-to-Many with Classification through SystemsXClassification
  - One-to-Many with SystemsSecurityToken (mapped by "base")

### Address
- **Table**: address.Address
- **Primary Key**: `id` (Column: AddressID)
- **Columns**:
  - `value` (Column: Value) - Value of the address
  - `classificationID` (Column: ClassificationID) - Reference to the classification
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through AddressXClassification
  - Many-to-Many with Geography through AddressXGeography
  - Many-to-Many with ResourceItem through AddressXResourceItem
  - Many-to-Many with Event through EventXAddress
  - Many-to-Many with InvolvedParty through InvolvedPartyXAddress
  - One-to-Many with AddressSecurityToken (mapped by "base")

### Event
- **Table**: event.Event
- **Primary Key**: `id` (Column: EventID)
- **Columns**:
  - `dayID` (Column: DayID) - Reference to the day
  - `hourID` (Column: HourID) - Reference to the hour
  - `minuteID` (Column: MinuteID) - Reference to the minute
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with EventType through EventXEventType
  - Many-to-Many with Address through EventXAddress
  - Many-to-Many with Arrangement through EventXArrangement
  - Many-to-Many with Geography through EventXGeography
  - Many-to-Many with InvolvedParty through EventXInvolvedParty
  - Many-to-Many with Product through EventXProduct
  - Many-to-Many with ResourceItem through EventXResourceItem
  - Many-to-Many with Rules through EventXRules
  - Many-to-Many with Event through EventXEvent
  - Many-to-Many with Classification through EventXClassification
  - One-to-Many with EventSecurityToken (mapped by "base")

### EventSecurityToken
- **Table**: event.EventSecurityToken
- **Primary Key**: `id` (Column: EventsSecurityTokenID)
- **Columns**:
  - `EventsID` (Column: EventsID) - Reference to the Event
  - Inherits all columns from WarehouseSecurityTable including:
    - `securityTokenID` (Column: SecurityTokenID) - Reference to the security token
    - `createAllowed` (Column: CreateAllowed) - Whether creation is allowed
    - `readAllowed` (Column: ReadAllowed) - Whether reading is allowed
    - `updateAllowed` (Column: UpdateAllowed) - Whether updating is allowed
    - `deleteAllowed` (Column: DeleteAllowed) - Whether deletion is allowed

### EventType
- **Table**: event.EventType
- **Primary Key**: `id` (Column: EventTypeID)
- **Columns**:
  - `name` (Column: EventTypeName) - Name of the event type
  - `description` (Column: EventTypeDescription) - Description of the event type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Event through EventXEventType
  - Many-to-Many with Classification through EventTypeXClassification
  - One-to-Many with EventTypeSecurityToken (mapped by "base")

### Geography
- **Table**: geography.Geography
- **Primary Key**: `id` (Column: GeographyID)
- **Columns**:
  - `name` (Column: GeographyName) - Name of the geography
  - `description` (Column: GeographyDescription) - Description of the geography
  - `classificationID` (Column: ClassificationID) - Reference to the classification
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through GeographyXClassification
  - Many-to-Many with Geography through GeographyXGeography
  - Many-to-Many with ResourceItem through GeographyXResourceItem
  - Many-to-Many with Address through AddressXGeography
  - Many-to-Many with Event through EventXGeography
  - One-to-Many with GeographySecurityToken (mapped by "base")

### InvolvedParty
- **Table**: party.InvolvedParty
- **Primary Key**: `id` (Column: InvolvedPartyID)
- **Columns**:
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through InvolvedPartyXClassification
  - Many-to-Many with InvolvedParty through InvolvedPartyXInvolvedParty
  - Many-to-Many with Address through InvolvedPartyXAddress
  - Many-to-Many with Product through InvolvedPartyXProduct
  - Many-to-Many with ResourceItem through InvolvedPartyXResourceItem
  - Many-to-Many with Rules through InvolvedPartyXRules
  - Many-to-Many with InvolvedPartyType through InvolvedPartyXInvolvedPartyType
  - Many-to-Many with InvolvedPartyNameType through InvolvedPartyXInvolvedPartyNameType
  - Many-to-Many with InvolvedPartyIdentificationType through InvolvedPartyXInvolvedPartyIdentificationType
  - Many-to-Many with Event through EventXInvolvedParty
  - Many-to-Many with Arrangement through ArrangementXInvolvedParty
  - One-to-Many with InvolvedPartySecurityToken (mapped by "base")
  - One-to-One with InvolvedPartyOrganic (joined on InvolvedPartyID = InvolvedPartyOrganicID)
  - One-to-One with InvolvedPartyNonOrganic (joined on InvolvedPartyID = InvolvedPartyNonOrganicID)

### InvolvedPartyType
- **Table**: party.InvolvedPartyType
- **Primary Key**: `id` (Column: InvolvedPartyTypeID)
- **Columns**:
  - `name` (Column: InvolvedPartyTypeName) - Name of the involved party type
  - `description` (Column: InvolvedPartyTypeDescription) - Description of the involved party type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through InvolvedPartyTypeXClassification
  - Many-to-Many with InvolvedParty through InvolvedPartyXInvolvedPartyType
  - One-to-Many with InvolvedPartyTypeSecurityToken (mapped by "base")

### InvolvedPartyNameType
- **Table**: party.InvolvedPartyNameType
- **Primary Key**: `id` (Column: InvolvedPartyNameTypeID)
- **Columns**:
  - `name` (Column: InvolvedPartyNameTypeName) - Name of the involved party name type
  - `description` (Column: InvolvedPartyNameTypeDescription) - Description of the involved party name type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through InvolvedPartyNameTypeXClassification
  - Many-to-Many with InvolvedParty through InvolvedPartyXInvolvedPartyNameType
  - One-to-Many with InvolvedPartyNameTypeSecurityToken (mapped by "base")

### InvolvedPartyIdentificationType
- **Table**: party.InvolvedPartyIdentificationType
- **Primary Key**: `id` (Column: InvolvedPartyIdentificationTypeID)
- **Columns**:
  - `name` (Column: InvolvedPartyIdentificationTypeName) - Name of the involved party identification type
  - `description` (Column: InvolvedPartyIdentificationTypeDescription) - Description of the involved party identification type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through InvolvedPartyIdentificationTypeXClassification
  - Many-to-Many with InvolvedParty through InvolvedPartyXInvolvedPartyIdentificationType
  - One-to-Many with InvolvedPartyIdentificationTypeSecurityToken (mapped by "base")

### InvolvedPartyNonOrganic
- **Table**: party.InvolvedPartyNonOrganic
- **Primary Key**: `id` (Column: InvolvedPartyNonOrganicID)
- **Columns**:
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - One-to-Many with InvolvedPartyNonOrganicSecurityToken (mapped by "base")
  - One-to-One with InvolvedParty (joined on InvolvedPartyNonOrganicID = InvolvedPartyID)

### InvolvedPartyOrganic
- **Table**: party.InvolvedPartyOrganic
- **Primary Key**: `id` (Column: InvolvedPartyOrganicID)
- **Columns**:
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - One-to-Many with InvolvedPartyOrganicSecurityToken (mapped by "base")
  - One-to-One with InvolvedParty (joined on InvolvedPartyOrganicID = InvolvedPartyID)

### InvolvedPartyOrganicSecurityToken
- **Table**: party.InvolvedPartyOrganicSecurityToken
- **Primary Key**: `id` (Column: InvolvedPartyOrganicSecurityTokenID)
- **Columns**:
  - `InvolvedPartyOrganicID` (Column: InvolvedPartyOrganicID) - Reference to the InvolvedPartyOrganic
  - Inherits all columns from WarehouseSecurityTable including:
    - `securityTokenID` (Column: SecurityTokenID) - Reference to the security token
    - `createAllowed` (Column: CreateAllowed) - Whether creation is allowed
    - `readAllowed` (Column: ReadAllowed) - Whether reading is allowed
    - `updateAllowed` (Column: UpdateAllowed) - Whether updating is allowed
    - `deleteAllowed` (Column: DeleteAllowed) - Whether deletion is allowed

### InvolvedPartyOrganicType
- **Table**: party.InvolvedPartyOrganicType
- **Primary Key**: `id` (Column: InvolvedPartyOrganicTypeID)
- **Columns**:
  - `name` (Column: InvolvedPartyOrganicTypeName) - Name of the involved party organic type
  - `description` (Column: InvolvedPartyOrganicTypeDescription) - Description of the involved party organic type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - One-to-Many with InvolvedPartyOrganicTypeSecurityToken (mapped by "base")

### Product
- **Table**: product.Product
- **Primary Key**: `id` (Column: ProductID)
- **Columns**:
  - `name` (Column: ProductName) - Name of the product
  - `description` (Column: ProductDesc) - Description of the product
  - `productCode` (Column: ProductCode) - Code of the product
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through ProductXClassification
  - Many-to-Many with Product through ProductXProduct
  - Many-to-Many with ResourceItem through ProductXResourceItem
  - Many-to-Many with ProductType through ProductXProductType
  - Many-to-Many with Event through EventXProduct
  - Many-to-Many with InvolvedParty through InvolvedPartyXProduct
  - Many-to-Many with Rules through RulesXProduct
  - Many-to-Many with Arrangement through ArrangementXProduct
  - One-to-Many with ProductSecurityToken (mapped by "base")

### ProductSecurityToken
- **Table**: product.ProductSecurityToken
- **Primary Key**: `id` (Column: ProductSecurityTokenID)
- **Columns**:
  - `ProductID` (Column: ProductID) - Reference to the Product
  - Inherits all columns from WarehouseSecurityTable including:
    - `securityTokenID` (Column: SecurityTokenID) - Reference to the security token
    - `createAllowed` (Column: CreateAllowed) - Whether creation is allowed
    - `readAllowed` (Column: ReadAllowed) - Whether reading is allowed
    - `updateAllowed` (Column: UpdateAllowed) - Whether updating is allowed
    - `deleteAllowed` (Column: DeleteAllowed) - Whether deletion is allowed

### ProductType
- **Table**: product.ProductType
- **Primary Key**: `id` (Column: ProductTypeID)
- **Columns**:
  - `name` (Column: ProductTypeName) - Name of the product type
  - `description` (Column: ProductTypeDescription) - Description of the product type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through ProductTypeXClassification
  - Many-to-Many with Product through ProductXProductType
  - One-to-Many with ProductTypeSecurityToken (mapped by "base")

### ResourceItem
- **Table**: resource.ResourceItem
- **Primary Key**: `id` (Column: ResourceItemID)
- **Columns**:
  - `resourceItemDataType` (Column: ResourceItemDataType) - Data type of the resource item
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through ResourceItemXClassification
  - Many-to-Many with ResourceItem through ResourceItemXResourceItem
  - Many-to-Many with ResourceItemType through ResourceItemXResourceItemType
  - Many-to-Many with Address through AddressXResourceItem
  - Many-to-Many with Geography through GeographyXResourceItem
  - Many-to-Many with Event through EventXResourceItem
  - Many-to-Many with InvolvedParty through InvolvedPartyXResourceItem
  - Many-to-Many with Product through ProductXResourceItem
  - Many-to-Many with Rules through RulesXResourceItem
  - Many-to-Many with RulesType through RulesTypeXResourceItem
  - Many-to-Many with ClassificationDataConcept through ClassificationDataConceptXResourceItem
  - Many-to-Many with Classification through ClassificationXResourceItem
  - Many-to-Many with Arrangement through ArrangementXResourceItem
  - One-to-Many with ResourceItemData (mapped by "resourceItemID")
  - One-to-Many with ResourceItemSecurityToken (mapped by "base")

### ResourceItemData
- **Table**: resource.ResourceItemData
- **Primary Key**: `id` (Column: ResourceItemDataID)
- **Columns**:
  - `resourceItemID` (Column: ResourceItemID) - Reference to the resource item
  - `resourceItemData` (Column: ResourceItemData) - Data of the resource item
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-One with ResourceItem
  - Many-to-Many with Classification through ResourceItemDataXClassification
  - One-to-Many with ResourceItemDataSecurityToken (mapped by "base")

### ResourceItemType
- **Table**: resource.ResourceItemType
- **Primary Key**: `id` (Column: ResourceItemTypeID)
- **Columns**:
  - `name` (Column: ResourceItemTypeName) - Name of the resource item type
  - `description` (Column: ResourceItemTypeDescription) - Description of the resource item type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with ResourceItem through ResourceItemXResourceItemType
  - One-to-Many with ResourceItemTypeSecurityToken (mapped by "base")

### Rules
- **Table**: rules.Rules
- **Primary Key**: `id` (Column: RulesID)
- **Columns**:
  - `name` (Column: RuleSetName) - Name of the rules
  - `description` (Column: RuleSetDescription) - Description of the rules
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through RulesXClassification
  - Many-to-Many with Rules through RulesXRules
  - Many-to-Many with RulesType through RulesXRulesType
  - Many-to-Many with Arrangement through RulesXArrangement
  - Many-to-Many with InvolvedParty through RulesXInvolvedParty
  - Many-to-Many with Product through RulesXProduct
  - Many-to-Many with ResourceItem through RulesXResourceItem
  - Many-to-Many with Event through EventXRules
  - One-to-Many with RulesSecurityToken (mapped by "base")

### RulesType
- **Table**: rules.RulesType
- **Primary Key**: `id` (Column: RulesTypeID)
- **Columns**:
  - `name` (Column: RulesTypeName) - Name of the rules type
  - `description` (Column: RulesTypeDesc) - Description of the rules type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through RulesTypeXClassification
  - Many-to-Many with ResourceItem through RulesTypeXResourceItem
  - Many-to-Many with Rules through RulesXRulesType
  - Many-to-Many with Arrangement through ArrangementXRulesType
  - One-to-Many with RulesTypeSecurityToken (mapped by "base")

### Arrangement
- **Table**: arrangement.Arrangement
- **Primary Key**: `id` (Column: ArrangementID)
- **Columns**:
  - `name` (Column: ArrangementName) - Name of the arrangement
  - `description` (Column: ArrangementDescription) - Description of the arrangement
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through ArrangementXClassification
  - Many-to-Many with Arrangement through ArrangementXArrangement
  - Many-to-Many with ArrangementType through ArrangementXArrangementType
  - Many-to-Many with InvolvedParty through ArrangementXInvolvedParty
  - Many-to-Many with Product through ArrangementXProduct
  - Many-to-Many with ResourceItem through ArrangementXResourceItem
  - Many-to-Many with Rules through ArrangementXRules
  - Many-to-Many with RulesType through ArrangementXRulesType
  - Many-to-Many with Event through EventXArrangement
  - One-to-Many with ArrangementSecurityToken (mapped by "base")

### ArrangementType
- **Table**: arrangement.ArrangementType
- **Primary Key**: `id` (Column: ArrangementTypeID)
- **Columns**:
  - `name` (Column: ArrangementTypeName) - Name of the arrangement type
  - `description` (Column: ArrangementTypeDescription) - Description of the arrangement type
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through ArrangementTypeXClassification
  - Many-to-Many with Arrangement through ArrangementXArrangementType
  - One-to-Many with ArrangementTypeSecurityToken (mapped by "base")

### SecurityToken
- **Table**: security.SecurityToken
- **Primary Key**: `id` (Column: SecurityTokenID)
- **Columns**:
  - `name` (Column: SecurityTokenName) - Name of the security token
  - `description` (Column: SecurityTokenDescription) - Description of the security token
  - Inherits all columns from WarehouseSCDTable
- **Relationships**:
  - Many-to-Many with Classification through SecurityTokenXClassification
  - Many-to-Many with SecurityToken through SecurityTokenXSecurityToken
  - One-to-Many with security token tables for other entities
  - One-to-Many with SecurityTokenSecurityToken (mapped by "base")

## Time Dimension Model

The ActivityMaster system includes a separate set of entities for time dimension modeling, which follows a different pattern than the warehouse entities:

### Time
- **Table**: Time.Time
- **Primary Key**: Composite key `id` (TimePK with hourID and minuteID)
- **Columns**:
  - `twelveHoursDesc` (Column: TwelveHourClockDesc) - 12-hour clock description
  - `twentyFourHoursDesc` (Column: TwentyFourHourClockDesc) - 24-hour clock description
  - `amPmDesc` (Column: AmPmDesc) - AM/PM indicator
  - `previousHourID` (Column: PreviousHourID) - Reference to the previous hour
  - `previousMinuteID` (Column: PreviousMinuteID) - Reference to the previous minute
- **Relationships**:
  - Many-to-One with Hours

### Hours
- **Table**: Time.Hours
- **Primary Key**: `id` (Column: HourID)
- **Columns**:
  - `hourValue` (Column: HourValue) - Hour value
  - `hourDesc` (Column: HourDesc) - Hour description
- **Relationships**:
  - One-to-Many with Time

### Days
- **Table**: Time.Days
- **Primary Key**: `id` (Column: DayID)
- **Columns**:
  - `dayDate` (Column: DayDate) - Date of the day
  - `dayDateTime` (Column: DayDateTime) - Date and time of the day
  - `dayInMonth` (Column: DayInMonth) - Day number within the month
  - `dayInYear` (Column: DayInYear) - Day number within the year
  - `quarterID` (Column: QuarterID) - Reference to the quarter
  - `yearID` (Column: YearID) - Reference to the year
  - `lastDayID` (Column: LastDayID) - Reference to the previous day
  - `lastMonthID` (Column: LastMonthID) - Reference to the previous month
  - `lastQuarterID` (Column: LastQuarterID) - Reference to the previous quarter
  - `lastYearID` (Column: LastYearID) - Reference to the previous year
  - Various date format descriptions
  - `dayIsPublicHoliday` (Column: DayIsPublicHoliday) - Indicates if the day is a public holiday
- **Relationships**:
  - Many-to-One with Weeks
  - Many-to-One with Months
  - Many-to-One with DayNames

### Weeks
- **Table**: Time.Weeks
- **Primary Key**: `id` (Column: WeekID)
- **Columns**:
  - `weekValue` (Column: WeekValue) - Week value
  - `weekDesc` (Column: WeekDesc) - Week description
  - `weekOfYearValue` (Column: WeekOfYearValue) - Week number within the year
  - `weekOfMonthValue` (Column: WeekOfMonthValue) - Week number within the month
- **Relationships**:
  - One-to-Many with Days

### Months
- **Table**: Time.Months
- **Primary Key**: `id` (Column: MonthID)
- **Columns**:
  - `monthValue` (Column: MonthValue) - Month value
  - `monthDesc` (Column: MonthDesc) - Month description
  - `monthOfYearValue` (Column: MonthOfYearValue) - Month number within the year
- **Relationships**:
  - One-to-Many with Days

### DayNames
- **Table**: Time.DayNames
- **Primary Key**: `id` (Column: DayNameID)
- **Columns**:
  - `dayNameValue` (Column: DayNameValue) - Day name value
  - `dayNameDesc` (Column: DayNameDesc) - Day name description
- **Relationships**:
  - One-to-Many with Days

### Quarters
- **Table**: Time.Quarters
- **Primary Key**: `id` (Column: QuarterID)
- **Columns**:
  - `quarterValue` (Column: QuarterValue) - Quarter value
  - `quarterDesc` (Column: QuarterDesc) - Quarter description
  - `quarterOfYearValue` (Column: QuarterOfYearValue) - Quarter number within the year

### Years
- **Table**: Time.Years
- **Primary Key**: `id` (Column: YearID)
- **Columns**:
  - `yearValue` (Column: YearValue) - Year value
  - `yearDesc` (Column: YearDesc) - Year description

## Relationship Structure

Relationships between entities are modeled using cross-reference (X) tables that extend either WarehouseRelationshipTable or WarehouseClassificationRelationshipTable. 

### Base Relationship Table Structure

All relationship tables inherit from a hierarchy of base classes that provide common functionality and properties:

1. **SCDEntity** (from com.entityassist package)
   - `effectiveFromDate` (Column: EffectiveFromDate) - The date from which the entity is effective
   - `effectiveToDate` (Column: EffectiveToDate) - The date until which the entity is effective
   - `warehouseCreatedTimestamp` (Column: WarehouseCreatedTimestamp) - The timestamp when the entity was created
   - `warehouseLastUpdatedTimestamp` (Column: WarehouseLastUpdatedTimestamp) - The timestamp when the entity was last updated

2. **WarehouseBaseTable** extends SCDEntity
   - `warehouseFromDate` (Column: WarehouseFromDate) - The database partition which the entity is stored in the warehouse
   - Inherits all columns from SCDEntity

3. **WarehouseCoreTable** extends WarehouseBaseTable
   - Adds security-related functionality
   - Inherits all columns from WarehouseBaseTable

4. **WarehouseTable** extends WarehouseCoreTable
   - `originalSourceSystemUniqueID` (Column: OriginalSourceSystemUniqueID) - UUID of the original source system
   - `originalSourceSystemID` (Column: OriginalSourceSystemID) - Reference to the original source system, for audit purposes only (not used in queries)
   - `enterpriseID` (Column: EnterpriseID) - Reference to the enterprise
   - Inherits all columns from WarehouseCoreTable

5. **WarehouseSCDTable** extends WarehouseTable
   - `activeFlagID` (Column: ActiveFlagID) - Reference to the active flag
   - `systemID` (Column: SystemID) - Reference to the system that produced the record, for audit purposes only (not used in queries)
   - Inherits all columns from WarehouseTable

6. **WarehouseRelationshipTable** extends WarehouseSCDTable
   - `value` (Column: Value) - The value of the relationship
   - Inherits all columns from WarehouseSCDTable

7. **WarehouseClassificationRelationshipTable** extends WarehouseRelationshipTable
   - `classificationID` (Column: ClassificationID) - Reference to the classification
   - Inherits all columns from WarehouseRelationshipTable

### Standard Relationship Tables (without Classification ID)

Relationship tables that extend WarehouseRelationshipTable typically have:

1. A primary key column
2. Foreign key columns to the related entities
3. A value column that stores the relationship value
4. System and enterprise references
5. Active flag reference
6. Security token references
7. All inherited columns from the base class hierarchy

### Classification Relationship Tables (with Classification ID)

Relationship tables that extend WarehouseClassificationRelationshipTable include all the columns from standard relationship tables plus:

- `classificationID` (Column: ClassificationID) - Reference to the classification that categorizes the relationship

### Important Note on Classification IDs

#### Base Tables with Classification IDs

Most base tables in the ActivityMaster system do not have a direct classification ID column. However, some specific entities like Address and Geography are exceptions and do include a classification ID directly in their base table:

- **Address**: Has a `classificationID` column directly in the base table
- **Geography**: Has a `classificationID` column directly in the base table

This is an important distinction as it affects how these entities are queried and indexed.

#### Relationship Tables and Classification IDs

**All relationship tables in the ActivityMaster system include a classification ID.** 

Based on the codebase examination, all relationship tables in the system either:
1. Directly extend WarehouseClassificationRelationshipTable, or
2. Extend a class that inherits from WarehouseClassificationRelationshipTable (such as WarehouseClassificationRelationshipTypesTable)

This means that every relationship (X table) in the system includes a classification ID column that categorizes the relationship. This design choice allows for more flexible and powerful querying and filtering of relationships based on their classification.

### Example Relationship Table

For example, the ActiveFlagXClassification table represents a many-to-many relationship between ActiveFlag and Classification entities. It extends WarehouseClassificationRelationshipTable and has the following structure:

- **Table**: dbo.ActiveFlagXClassification
- **Primary Key**: `id` (Column: ActiveFlagXClassificationID)
- **Columns**:
  - `activeFlagID` (Column: ActiveFlagID) - Reference to the active flag
  - `classificationID` (Column: ClassificationID) - Reference to the classification
  - `value` (Column: Value) - The value of the relationship
  - `systemID` (Column: SystemID) - Reference to the system that produced the record, for audit purposes only (not used in queries)
  - `enterpriseID` (Column: EnterpriseID) - Reference to the enterprise
  - `originalSourceSystemID` (Column: OriginalSourceSystemID) - Reference to the original source system, for audit purposes only (not used in queries)
  - `originalSourceSystemUniqueID` (Column: OriginalSourceSystemUniqueID) - UUID of the original source system
  - `effectiveFromDate` (Column: EffectiveFromDate) - The date from which the relationship is effective
  - `effectiveToDate` (Column: EffectiveToDate) - The date until which the relationship is effective
  - `warehouseCreatedTimestamp` (Column: WarehouseCreatedTimestamp) - The timestamp when the relationship was created
  - `warehouseLastUpdatedTimestamp` (Column: WarehouseLastUpdatedTimestamp) - The timestamp when the relationship was last updated
  - `warehouseFromDate` (Column: WarehouseFromDate) - The database partition which the entity is stored in the warehouse
- **Relationships**:
  - Many-to-One with ActiveFlag
  - Many-to-One with Classification
  - One-to-Many with ActiveFlagXClassificationSecurityToken (mapped by "base")

## Security Structure

Each entity has an associated security token table that extends WarehouseSecurityTable. These tables typically have:

1. A reference to the security token
2. A reference to the base entity
3. Flags for create, read, update, and delete permissions

For example, the ActiveFlagSecurityToken table represents security permissions for ActiveFlag entities. It has the following structure:

- **Primary Key**: `id` (Column: ActiveFlagSecurityTokenID)
- **Columns**:
  - `SecurityTokenActiveFlagID` (Column: SecurityTokenActiveFlagID) - Reference to the ActiveFlag
  - Inherits all columns from WarehouseSecurityTable including:
    - `securityTokenID` (Column: SecurityTokenID) - Reference to the security token
    - `createAllowed` (Column: CreateAllowed) - Whether creation is allowed
    - `readAllowed` (Column: ReadAllowed) - Whether reading is allowed
    - `updateAllowed` (Column: UpdateAllowed) - Whether updating is allowed
    - `deleteAllowed` (Column: DeleteAllowed) - Whether deletion is allowed
  - Inherits all columns from WarehouseSCDTable including:
    - `systemID` (Column: SystemID) - Reference to the system that produced the record, for audit purposes only (not used in queries)
    - `activeFlagID` (Column: ActiveFlagID) - Reference to the active flag
  - Inherits all columns from WarehouseTable including:
    - `enterpriseID` (Column: EnterpriseID) - Reference to the enterprise
    - `originalSourceSystemID` (Column: OriginalSourceSystemID) - Reference to the original source system, for audit purposes only (not used in queries)
    - `originalSourceSystemUniqueID` (Column: OriginalSourceSystemUniqueID) - UUID of the original source system
  - Inherits all columns from WarehouseBaseTable and SCDEntity including:
    - `effectiveFromDate` (Column: EffectiveFromDate) - The date from which the security permission is effective
    - `effectiveToDate` (Column: EffectiveToDate) - The date until which the security permission is effective
    - `warehouseCreatedTimestamp` (Column: WarehouseCreatedTimestamp) - The timestamp when the security permission was created
    - `warehouseLastUpdatedTimestamp` (Column: WarehouseLastUpdatedTimestamp) - The timestamp when the security permission was last updated
    - `warehouseFromDate` (Column: WarehouseFromDate) - The database partition which the entity is stored in the warehouse

## Common Patterns

1. **Entity Naming**:
   - Base entities: [EntityName]
   - Security token tables: [EntityName]SecurityToken
   - Cross-reference tables: [EntityName1]X[EntityName2]

2. **Column Naming**:
   - Primary keys: [EntityName]ID
   - Foreign keys: [ReferencedEntityName]ID
   - Properties: [PropertyName]

3. **Schema**:
   - Most entities are in their own schema (e.g., dbo, address, arrangement, classification, event, geography, party, product, resource, rules, security, time, systems)
   - Some specialized entities may be in their own schemas

4. **Inheritance**:
   - All entities inherit from the base class hierarchy
   - Security token tables extend WarehouseSecurityTable
   - Relationship tables extend WarehouseRelationshipTable or WarehouseClassificationRelationshipTable

5. **Temporal Data Management**:
   - Entities use Slowly Changing Dimension (SCD) pattern for tracking changes over time
   - `effectiveFromDate` and `effectiveToDate` columns track validity periods
   - `warehouseCreatedTimestamp` and `warehouseLastUpdatedTimestamp` track creation and modification times
   - Active flags indicate current status (active, deleted, archived)
   - Historical records are preserved when data is updated
   - New records are created with current timestamp as `effectiveFromDate`
   - Old records have their `effectiveToDate` set to current timestamp
   - Future-dated records can be created with `effectiveFromDate` set to a future date
   - Queries can be constrained to specific time periods using `inDateRange()` method
   - The `expireIn()` method allows setting an expiration time for records

6. **Security**:
   - Each entity has security permissions
   - Permissions are managed through security token tables
   - Default security is created for new entities

## Database Indexing Strategy

The ActivityMaster system implements a comprehensive indexing strategy to ensure optimal query performance across all entity types and their relationships:

### Index Types

1. **Primary Key Indexes**:
   - Automatically created for all primary key columns
   - Example: `idx_activeflag_id` on `dbo.activeflag(id)`
   - Critical for entity retrieval by ID and for join operations

2. **Foreign Key Reference Indexes**:
   - Created on all reference columns (foreign keys)
   - Example: `idx_address_enterpriseid` on `address.address(enterpriseID)`
   - Ensures efficient joins between related tables

3. **Name and Description Indexes**:
   - Created on name and description columns in lookup tables
   - Example: `idx_activeflag_name` on `dbo.activeflag(name)`
   - Supports efficient text-based searches and filtering

4. **Temporal Indexes**:
   - Created on date/time columns used for filtering
   - Example: `idx_event_day_hour_minute` on `event.event(dayid, hourid, minuteid)`
   - Example: `idx_entity_effectivefromdate_effectivetodate` on `table(effectivefromdate, effectivetodate)`
   - Supports efficient temporal queries and date range filtering

5. **Composite Indexes for Common Queries**:
   - Created for common query patterns based on usage analysis
   - Example: `idx_address_classification_activeflag` on `address.address(classificationid, activeflagid)`
   - Example: `idx_event_type_day` on `event.event(eventtypeid, dayid)`
   - Optimizes performance for frequently executed queries

6. **Security Token Indexes**:
   - Created for security token relationships
   - Example: `idx_activeflagsecuritytoken_token_activeflag` on `dbo.activeflagsecuritytoken(securitytokenid, activeflagid)`
   - Ensures efficient security permission checks

7. **Permission Indexes**:
   - Created for permission checks
   - Example: `idx_activeflagsecuritytoken_token_read` on `dbo.activeflagsecuritytoken(securitytokenid, readallowed)`
   - Optimizes security filtering in queries

8. **Relationship Table Indexes**:
   - Created on both sides of relationship tables
   - Example: `idx_addressxgeography_addressid` on `address.addressxgeography(addressid)`
   - Example: `idx_addressxgeography_geographyid` on `address.addressxgeography(geographyid)`
   - Ensures efficient traversal of relationships in both directions

9. **Classification Indexes**:
   - Created on classification references in all tables
   - Example: `idx_entity_classificationid` on `table(classificationid)`
   - Supports efficient filtering by classification

10. **Value Indexes for Relationship Tables**:
    - Created on value columns in relationship tables
    - Example: `idx_entityxentity_value` on `schema.entityxentity(value)`
    - Supports efficient filtering by relationship value

### Complex Index Combinations

To support complex queries that involve multiple filtering criteria, the following composite index patterns are implemented:

1. **Enterprise-System-ActiveFlag Indexes**:
   - Example: `idx_entity_enterprise_system_activeflag` on `table(enterpriseid, systemid, activeflagid)`
   - Optimizes the most common filtering pattern used in almost all queries

2. **Classification-Based Search Indexes**:
   - Example: `idx_entity_classification_name` on `table(classificationid, entityname)`
   - Supports efficient searches within specific classifications

3. **Temporal-Classification Indexes**:
   - Example: `idx_entity_classification_effectivedate` on `table(classificationid, effectivefromdate, effectivetodate)`
   - Optimizes time-based queries within classifications

4. **Relationship-Classification Indexes**:
   - Example: `idx_entityxentity_classification_value` on `schema.entityxentity(classificationid, value)`
   - Supports efficient filtering of relationships by classification and value

5. **Relationship Parent/Child Indexes**:
   - Example: `idx_entityxentity_parent_child_classification` on `schema.entityxentity(parentid, childid, classificationid)`
   - Example: `idx_entityxentity_parent_classification_enterprise_activeflag` on `schema.entityxentity(parentid, classificationid, enterpriseid, activeflagid)`
   - Example: `idx_entityxentity_child_classification_enterprise_activeflag` on `schema.entityxentity(childid, classificationid, enterpriseid, activeflagid)`
   - Optimizes common relationship queries that filter by parent/child, classification, enterprise, and active flag
   - Critical for efficient hierarchical data retrieval and filtering

6. **Security-Based Access Indexes**:
   - Example: `idx_entitysecuritytoken_token_crud` on `schema.entitysecuritytoken(securitytokenid, createallowed, readallowed, updateallowed, deleteallowed)`
   - Optimizes permission checks for all CRUD operations in a single index

### Indexing Guidelines

1. **Reference Column Indexing**:
   - All reference columns (foreign keys) are indexed
   - No foreign key constraints are used, but indexes ensure efficient joins
   - Both sides of many-to-many relationships are indexed

2. **Lookup Table Indexing**:
   - Name columns in lookup tables are indexed for efficient searches
   - Description columns may also be indexed for full-text searches
   - Combination indexes with classification and active flag are created

3. **Temporal Data Indexing**:
   - All effective date ranges are indexed
   - Warehouse timestamps are indexed for audit queries
   - Date-based filtering is optimized with composite indexes

4. **Security Indexing**:
   - Security token references are indexed
   - Permission flags are indexed in combination with security tokens
   - Enterprise and system combinations are indexed with security tokens

5. **Index Creation Order**:
   - Base tables are indexed first
   - Lookup tables are indexed next
   - Cross-reference tables are indexed last
   - Security tables are indexed after their base tables

6. **Composite Index Strategy**:
   - Composite indexes are created based on query analysis
   - Most frequent query patterns receive dedicated composite indexes
   - Index column order is optimized for common WHERE clause patterns
   - Leading columns in composite indexes also have individual indexes

7. **Index Maintenance**:
   - Indexes are regularly analyzed for usage patterns
   - Unused indexes are candidates for removal
   - New indexes are added based on query performance analysis
   - Index fragmentation is monitored and addressed

This comprehensive indexing strategy ensures that the ActivityMaster system can efficiently query and retrieve data, even with large volumes of records and complex relationships. The strategy is designed to support all common query patterns while minimizing the overhead of maintaining unnecessary indexes.

This ERD rules document provides a comprehensive overview of the entity structure in the ActivityMaster system. It can be used as a reference for understanding the database schema and relationships between entities.
