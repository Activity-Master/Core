package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressEmailClassifications
		implements IAddressClassification<AddressEmailClassifications>
{
	EmailAddress("An E-Mail address", Address),
	EmailAddressHost("The host server of the email address", Address),
	EmailAddressDomain("The email address domain", Address),
	EmailAddressUser("The user section of the email address", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressEmailClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressEmailClassifications(String classificationValue)
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
