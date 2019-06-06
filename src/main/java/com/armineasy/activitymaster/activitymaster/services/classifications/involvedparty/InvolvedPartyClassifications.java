package com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum InvolvedPartyClassifications
		implements IInvolvedPartyClassification<InvolvedPartyClassifications>
{
	SecurityPassword("Defines a password if any is required for a security token",InvolvedPartyXClassification),
	SecurityPasswordSalt("Defines the salt used for the password",InvolvedPartyXClassification),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	InvolvedPartyClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	InvolvedPartyClassifications(String classificationValue)
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
