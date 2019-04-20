package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressHomeCellClassifications
		implements IAddressClassification<AddressHomeCellClassifications>
{

	HomeCellNumber("A Cellphone (Home) Number", Address),
	HomeCellCountryCode("The country code for the Cell number", Address),
	HomeCellExtensionNumber("Cell Number for the Cell number", Address),
	HomeCellAreaCode("The area code for the Cell number", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressHomeCellClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressHomeCellClassifications(String classificationValue)
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
