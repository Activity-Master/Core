package com.guicedee.activitymaster.core.services.classifications.arrangement;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum ArrangementInvolvedPartyClassifications
		implements IArrangementClassification<ArrangementInvolvedPartyClassifications>
{
	InvolvedPartyArrangements("Arrangements from the Involved Party", ArrangementXInvolvedParty),
	PurchasedBy("This arrangement was purchased by", ArrangementXInvolvedParty),
	SoldBy("This arrangement was sold by", ArrangementXInvolvedParty),
	OwnedBy("This arrangement is owned by", ArrangementXInvolvedParty),
	ManagedBy("This arrangement is managed by", ArrangementXInvolvedParty),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	ArrangementInvolvedPartyClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	ArrangementInvolvedPartyClassifications(String classificationValue)
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
