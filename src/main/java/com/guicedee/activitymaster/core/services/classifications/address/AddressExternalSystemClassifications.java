package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressExternalSystemClassifications
		implements IAddressClassification<AddressExternalSystemClassifications>
{
	ExternalAddress("The external address of a connection", Address),
	ExternalAddressIPAddress("The external address IP Address", Address),
	ExternalAddressHostName("The external address hostname", Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressExternalSystemClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressExternalSystemClassifications(String classificationValue)
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
