package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressOfficeFaxClassifications
		implements IAddressClassification<AddressOfficeFaxClassifications>
{
	OfficeFaxNumber("Office A Fax Number", Address),
	OfficeFaxCountryCode("The country code for the Fax number", Address),
	OfficeFaxExtensionNumber("Office Fax Number for the phone number", Address),
	OfficeFaxAreaCode("The area code for the fax number", Address),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressOfficeFaxClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressOfficeFaxClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationDescription()
	{
		return classificationValue;
	}
	@Override
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
