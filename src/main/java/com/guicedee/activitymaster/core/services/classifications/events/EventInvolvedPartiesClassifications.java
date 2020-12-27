package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EventInvolvedPartiesClassifications
		implements IEventClassification<EventInvolvedPartiesClassifications>,
		           IInvolvedPartyClassification<EventInvolvedPartiesClassifications>,
		           IAddressClassification<EventInvolvedPartiesClassifications>
{
	InvolvedPartyEvents("Events that Involved Parties can perform", EventXInvolvedParty),
	PerformedBy("Defines the involved party as the one who performed the action", EventXInvolvedParty),
	OnBehalfOf("Defines the involved party as the one who it was done on behalf of. The impersonated user", EventXInvolvedParty),
	For("Defines the involved party who this event was for", EventXInvolvedParty),
	OwnedBy("Defines who owns the event", EventXInvolvedParty),
	
	Created("The Event created the Involved Party", EventXInvolvedParty),
	Added("The Event added the involved party", EventXInvolvedParty),
	Updated("The Event updated the Involved Party", EventXInvolvedParty),
	
	CreatedBy("The Event was created by this user", EventXInvolvedParty),
	UpdatedBy("The Event was updated by this user", EventXInvolvedParty),
	CompletedBy("The Event was completed by this user", EventXInvolvedParty),
	SecurityCredentialsOf("This Event was updated with the security permissions of", EventXInvolvedParty),
	MeantFor("Is Meant For", EventXInvolvedParty),
	Notifies("Creates a Notification For", EventXInvolvedParty),
	
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;
	
	EventInvolvedPartiesClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}
	
	EventInvolvedPartiesClassifications(String classificationValue)
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
