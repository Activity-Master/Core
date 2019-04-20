package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum EventProductClassifications
		implements IEventClassification<EventProductClassifications>
{
	ProductEvent("Showed Interest In", EventXProduct),
	ShowedInterestIn("Showed Interest In", EventXProduct),
	Bought("Bought the Product", EventXProduct),
	Sold("Sold the Product", EventXProduct),
	MadeBidFor("Made a Bid For", EventXProduct),
	ChangedBidFor("Changed the Bid For", EventXProduct),
	RemovedBidFor("Removed the Bid For", EventXProduct),
	Cancelled("Didn't Want the Product", EventXProduct),
	DontShowProduct("Asks To Not Show the Product", EventXProduct),
	RemindMeOfTheProduct("Requests To Be Reminded of the Product", EventXProduct),
	ChangedTheCostOf("Changed the Cost Of the Product", EventXProduct),
	AddedTheInterestOf("Added Interest To the Product", EventXProduct),
	ChangedTheInterestOf("Changed the Interest Of the Product", EventXProduct),
	RatedTheProduct("Gave A Rating for the Product", EventXProduct),
	ChangedTheRatingOfTheProduct("Updated the Rating for the Product", EventXProduct),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	EventProductClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	EventProductClassifications(String classificationValue)
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
