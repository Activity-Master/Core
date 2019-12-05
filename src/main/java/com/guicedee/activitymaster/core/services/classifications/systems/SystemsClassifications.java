package com.guicedee.activitymaster.core.services.classifications.systems;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum SystemsClassifications
		implements ISystemsClassification<SystemsClassifications>
{
	SystemIdentity("Defines an identity classification relationship", SystemXClassification)
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	SystemsClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	SystemsClassifications(String classificationValue)
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
