package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

public enum EventClassifications
		implements IEventClassification<EventClassifications>
{
	NotifiesInvolvedParty("Notifies Involved Party"),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	EventClassifications(String classificationValue)
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
