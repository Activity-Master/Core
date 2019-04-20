package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressOfficePagerClassifications
		implements IAddressClassification<AddressOfficePagerClassifications>
{

	OfficePagerNumber("A Pager  (Office) Number", Address),
	OfficePagerCountryCode("The country code for the Pager number", Address),
	OfficePagerExtensionNumber("Pager Number for the Pager number", Address),
	OfficePagerAreaCode("The area code for the Pager number", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressOfficePagerClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
