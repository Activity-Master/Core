package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressWebClassifications
		implements IAddressClassification<AddressWebClassifications>
{

	WebAddress("An HTTP or HTTPS URL Address", Address),
	WebAddressProtocol("The Protocol used for a web address", Address),
	WebAddressPort("The port used to connect to the web address", Address),
	WebAddressSubDomain("The Sub domain portion of an address", Address),
	WebAddressDomain("The domain portion of an address", Address),
	WebAddressUrl("The complete URL of a given address", Address),
	WebAddressQueryParameters("The query parameters of an address", Address),
	WebAddressSite("The site portion of a web address", Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressWebClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressWebClassifications(String classificationValue)
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
