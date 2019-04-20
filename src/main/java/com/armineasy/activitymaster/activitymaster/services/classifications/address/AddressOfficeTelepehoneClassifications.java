package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressOfficeTelepehoneClassifications
		implements IAddressClassification<AddressOfficeTelepehoneClassifications>
{

	OfficeTelephoneNumber("Any given office telephone number", Address),
	OfficeTelephoneCountryCode("The country code for the telephone number", Address),
	OfficeTelephoneExtensionNumber("Office Telephone Number Extension", Address),
	OfficeTelephoneAreaCode("The area code for the address", Address),

	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressOfficeTelepehoneClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressOfficeTelepehoneClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationDescription()
	{
		return classificationValue;
	}
	@Override
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
