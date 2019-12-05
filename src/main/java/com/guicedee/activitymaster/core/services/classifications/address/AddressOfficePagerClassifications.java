package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressOfficePagerClassifications
		implements IAddressClassification<AddressOfficePagerClassifications>
{

	OfficePagerNumber("A Pager  (Office) Number", Address),
	OfficePagerCountryCode("The country code for the Pager number", Address),
	OfficePagerExtensionNumber("Pager Number for the Pager number", Address),
	OfficePagerAreaCode("The area code for the Pager number", Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressOfficePagerClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressOfficePagerClassifications(String classificationValue)
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
