package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

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
	private IDataConceptValue<?> dataConceptValue;

	AddressInternalSystemClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
