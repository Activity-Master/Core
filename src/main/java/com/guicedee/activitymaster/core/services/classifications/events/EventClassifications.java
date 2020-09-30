package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EventClassifications
		implements IEventClassification<EventClassifications>
{
	NotifiesInvolvedParty("Notifies Involved Party", EventXInvolvedParty),
	UpdatedPassword(" updated the password ", EventXInvolvedParty),
	UpdatedUsername(" Created a new User Name", EventXInvolvedParty),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;
	
	EventClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
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
