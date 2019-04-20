package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressOfficeCellClassifications
		implements IAddressClassification<AddressOfficeCellClassifications>
{
	OfficeCellNumber("A Cellphone (Office) Number", Address),
	OfficeCellCountryCode("The country code for the Cell number", Address),
	OfficeCellExtensionNumber("Cell Number for the Cell number", Address),
	OfficeCellAreaCode("The area code for the Cell number", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressOfficeCellClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
