package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.IInvolvedPartyClassification;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EventInvolvedPartiesClassifications
		implements IEventClassification<EventInvolvedPartiesClassifications>,
				           IInvolvedPartyClassification<EventInvolvedPartiesClassifications>
{
	InvolvedPartyEvents("Events that Involved Parties can perform", EventXInvolvedParty),
	PerformedBy("Defines the involved party as the one who performed the action", EventXInvolvedParty),
	OnBehalfOf("Defines the involved party as the one who it was done on behalf of. The impersonated user", EventXInvolvedParty),
	For("Defines the involved party who this event was for", EventXInvolvedParty),
	OwnedBy("Defines who owns the event", EventXInvolvedParty),

	CreatedBy("The Event was created by this user", EventXInvolvedParty),
	UpdatedBy("The Event was updated by this user", EventXInvolvedParty),
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
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
