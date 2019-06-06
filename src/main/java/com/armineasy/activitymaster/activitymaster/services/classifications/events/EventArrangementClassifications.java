package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EventArrangementClassifications
		implements IEventClassification<EventArrangementClassifications>
{
	ArrangementEvents("The arrangement was modified these ways by this event", EventXArrangement),
	Started("Started the Arrangement", EventXArrangement),
	Concluded("Concluded the Arrangement", EventXArrangement),
	AffectedThe("Affected the Arrangement", EventXArrangement),
	RestartedThe("Restarted the Arrangement", EventXArrangement),
	SkippedThe("Skipped the Arrangement", EventXArrangement),
	AlteredRiskValue("Changed the Risk Value of the Arrangement", EventXArrangement),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	EventArrangementClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	EventArrangementClassifications(String classificationValue)
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
