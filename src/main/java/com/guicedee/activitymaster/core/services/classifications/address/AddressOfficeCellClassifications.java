package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressOfficeCellClassifications
		implements IAddressClassification<AddressOfficeCellClassifications>
{
	OfficeCellNumber("A Cellphone (Office) Number", Address),
	OfficeCellCountryCode("The country code for the Cell number", Address),
	OfficeCellExtensionNumber("Cell Number for the Cell number", Address),
	OfficeCellAreaCode("The area code for the Cell number", Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressOfficeCellClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressOfficeCellClassifications(String classificationValue)
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
