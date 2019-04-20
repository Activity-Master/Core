package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressRemoteSystemClassifications
		implements IAddressClassification<AddressRemoteSystemClassifications>
{
	RemoteAddress("The remote address of a connection", Address),
	RemoteAddressIPAddress("The remote address IP Address", Address),
	RemoteAddressHostName("The remote address hostname", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressRemoteSystemClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
