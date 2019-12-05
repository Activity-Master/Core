package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EventTypeClassifications
		implements IEventTypeClassification<EventTypeClassifications>
{

	TypeOfEvents("Event Also has Types", EventXEventType),
	HasTheType("Has The Type", EventXEventType),
	CanBeIdentifiedBy("Can Be Identified the Type", EventXEventType),
	SubType("Has the Subtype", EventXEventType),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	EventTypeClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	EventTypeClassifications(String classificationValue)
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

	@Override
	public String classificationValue()
	{
		return name();
	}
}
