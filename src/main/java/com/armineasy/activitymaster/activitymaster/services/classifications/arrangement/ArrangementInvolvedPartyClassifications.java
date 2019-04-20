package com.armineasy.activitymaster.activitymaster.services.classifications.arrangement;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

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
	private IDataConceptValue<?> dataConceptValue;

	ArrangementInvolvedPartyClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
