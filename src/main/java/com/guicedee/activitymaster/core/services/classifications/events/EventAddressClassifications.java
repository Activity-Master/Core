package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EventAddressClassifications
		implements IEventClassification<EventAddressClassifications>,
		           IAddressClassification<EventAddressClassifications>
{
	AddressEvents("Address Events", EventXAddress),
	
	AddedAddress("Added Address", EventXAddress),
	
	SignedAt("Signed At", EventXAddress),
	OccurredAt("Occurred At", EventXAddress),
	RemoteAddress("With Remote Address", EventXAddress),
	LocalAddress("Local Address Was", EventXAddress),
	PhonedNumber("Called The Number", EventXAddress),
	SentAFax("Sent a Fax To", EventXAddress),
	Emailed("Emailed To", EventXAddress),
	SMSd("Sent a SMS To", EventXAddress),
	MMSd("Sent a MMS To", EventXAddress),
	Posted("Sent By Post To", EventXAddress),
	RegisteredPost("Sent By Registered Post To", EventXAddress),
	
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;
	
	EventAddressClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}
	
	EventAddressClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}
	
	@Override
	public String classificationDescription()
	{
		return classificationValue;
	}
	
	@Override
	public String classificationValue()
	{
		return name();
	}
	
	@Override
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
