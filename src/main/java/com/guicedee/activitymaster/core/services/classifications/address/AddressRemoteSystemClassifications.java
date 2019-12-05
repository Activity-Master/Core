package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressRemoteSystemClassifications
		implements IAddressClassification<AddressRemoteSystemClassifications>
{
	RemoteAddress("The remote address of a connection", Address),
	RemoteAddressIPAddress("The remote address IP Address", Address),
	RemoteAddressHostName("The remote address hostname", Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressRemoteSystemClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressRemoteSystemClassifications(String classificationValue)
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
