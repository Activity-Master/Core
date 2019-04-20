package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressBoxClassifications
		implements IAddressClassification<AddressBoxClassifications>
{

	BoxAddress("A Post Box Address", Address),
	BoxNumber("The number of the box", Address),
	BoxIdentifier("Identifier of the box, E.g. PO BOX", Address),
	BoxCity("The city of the postal box", Address),
	BoxPostalCode("The postal code of the post box", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressBoxClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	AddressBoxClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
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
