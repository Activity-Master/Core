package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressHomePagerClassifications
		implements IAddressClassification<AddressHomePagerClassifications>
{

	HomePagerNumber("A Pager  (Home) Number", Address),
	HomePagerCountryCode("The country code for the Pager number", Address),
	HomePagerExtensionNumber("Pager Number extension for the Pager number", Address),
	HomePagerAreaCode("The area code for the Pager number", Address),

	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressHomePagerClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
