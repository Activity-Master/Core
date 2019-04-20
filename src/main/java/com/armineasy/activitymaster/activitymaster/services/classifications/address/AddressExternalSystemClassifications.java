package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum AddressExternalSystemClassifications
		implements IAddressClassification<AddressExternalSystemClassifications>
{
	ExternalAddress("The external address of a connection", Address),
	ExternalAddressIPAddress("The external address IP Address", Address),
	ExternalAddressHostName("The external address hostname", Address),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	AddressExternalSystemClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
