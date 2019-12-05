package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressInternalSystemClassifications
		implements IAddressClassification<AddressInternalSystemClassifications>
{
	InternalAddress("The internal address of a connection", Address),
	InternalAddressIPAddress("The internal address IP Address", Address),
	InternalAddressHostName("The internal address hostname", Address),
	InternalAddressSubnet("The internal address subnet mask", Address),
	InternalAddressDns("The internal address DNS", Address),
	InternalAddressGateway("The internal address gateway", Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressInternalSystemClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressInternalSystemClassifications(String classificationValue)
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
