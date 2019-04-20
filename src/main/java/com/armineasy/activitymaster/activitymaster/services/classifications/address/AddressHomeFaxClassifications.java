package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressHomeFaxClassifications
		implements IAddressClassification<AddressHomeFaxClassifications>
{
	HomeFaxNumber("A Fax Number", Address),
	HomeFaxCountryCode("The country code for the Fax number", Address),
	HomeFaxExtensionNumber("Fax Number for the phone number", Address),
	HomeFaxAreaCode("The area code for the fax number", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressHomeFaxClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressHomeFaxClassifications(String classificationValue)
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
