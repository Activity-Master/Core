package com.guicedee.activitymaster.core.services.classifications.address;

import com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.Address;

public enum AddressClassifications
		implements IAddressClassification<AddressClassifications>
{

	Address("An Address", EnterpriseClassificationDataConcepts.Address),
	LocationAddress("An address that is a physical location", EnterpriseClassificationDataConcepts.Address),
	ContactAddress("An address for contacting an involved party", EnterpriseClassificationDataConcepts.Address),
	PostalAddress("An address for posting to an involved party", EnterpriseClassificationDataConcepts.Address),
	CallAddress("An address for making a call to an involved party", EnterpriseClassificationDataConcepts.Address),
	InternetAddress("An address that is an IP for an involved party", EnterpriseClassificationDataConcepts.Address),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressClassifications(String classificationValue)
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
