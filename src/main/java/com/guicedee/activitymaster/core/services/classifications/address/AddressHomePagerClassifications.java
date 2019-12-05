package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressHomePagerClassifications
		implements IAddressClassification<AddressHomePagerClassifications>
{

	HomePagerNumber("A Pager  (Home) Number", Address),
	HomePagerCountryCode("The country code for the Pager number", Address),
	HomePagerExtensionNumber("Pager Number extension for the Pager number", Address),
	HomePagerAreaCode("The area code for the Pager number", Address),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressHomePagerClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressHomePagerClassifications(String classificationValue)
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
