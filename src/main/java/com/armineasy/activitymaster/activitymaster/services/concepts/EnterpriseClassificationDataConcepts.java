package com.armineasy.activitymaster.activitymaster.services.concepts;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

public enum EnterpriseClassificationDataConcepts
		implements IClassificationDataConceptValue<EnterpriseClassificationDataConcepts>
{
	NoClassificationDataConceptName("No Classification"),
	GlobalClassificationsDataConceptName("GlobalClassifications"),

	ActiveFlag,
	ActiveFlagXClassification,
	Address,
	AddressXClassification,
	AddressXGeography,
	AddressXInvolvedParty,
	AddressXResourceItem,
	Arrangement,
	ArrangementType,
	ArrangementXArrangement,
	ArrangementXArrangementType,
	ArrangementXClassification,
	ArrangementXInvolvedParty,
	ArrangementXProduct,
	ArrangementXResourceItem,
	Classification,
	ClassificationDataConcept,
	ClassificationDataConceptXClassification,
	ClassificationDataConceptXResourceItem,

	ClassificationXClassification,

	ClassificationXResourceItem,
	Enterprise,
	EnterpriseXClassification,
	Event,
	EventType,
	EventXAddress,
	EventXArrangement,
	EventXClassification,
	EventXEventType,
	EventXGeography,
	EventXInvolvedParty,
	EventXProduct,
	EventXResourceItem,
	Geography,
	GeographyXClassification,
	GeographyXGeography,
	GeographyXResourceItem,
	InvolvedParty,
	InvolvedPartyIdentificationType,
	InvolvedPartyNameType,
	InvolvedPartyNonOrganic,
	InvolvedPartyOrganic,
	InvolvedPartyOrganicType,
	InvolvedPartyType,
	InvolvedPartyXAddress,
	InvolvedPartyXClassification,
	InvolvedPartyXInvolvedParty,
	InvolvedPartyXInvolvedPartyIdentificationType,
	InvolvedPartyXInvolvedPartyNameType,
	InvolvedPartyXInvolvedPartyType,
	InvolvedPartyXProduct,
	InvolvedPartyXResourceItem,
	Product,
	ProductXClassification,
	ProductXProduct,
	ProductXProductType,
	ProductXResourceItem,
	ResourceItem,
	ResourceItemData,
	ResourceItemDataXClassification,
	ResourceItemType,
	ResourceItemXClassification,
	ResourceItemXResourceItemType,
	SecurityToken,
	SecurityTokenXClassification,
	SecurityTokenXSecurityToken,
	Systems,
	SystemXClassification,
	YesNo,
	YesNoXClassification,


	;

	private String classificationValue;

	EnterpriseClassificationDataConcepts()
	{
		this.classificationValue = name();
	}

	EnterpriseClassificationDataConcepts(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	public static EnterpriseClassificationDataConcepts fromClassName(Class clazz)
	{
		return EnterpriseClassificationDataConcepts.valueOf(clazz.getSimpleName());
	}

	@Override
	public String classificationValue()
	{
		return classificationValue;
	}
}
