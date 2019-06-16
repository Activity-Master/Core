package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressHomeTelephoneClassifications
		implements IAddressClassification<AddressHomeTelephoneClassifications>
{
	HomeTelephoneNumber("Any given home telephone number", Address),
	HomeTelephoneCountryCode("The country code for the telephone number", Address),
	HomeTelephoneExtensionNumber("Telephone Number Extension", Address),
	HomeTelephoneAreaCode("The area code for the address", Address),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressHomeTelephoneClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressHomeTelephoneClassifications(String classificationValue)
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
