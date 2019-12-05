package com.guicedee.activitymaster.core.services.classifications.classificationdataconcepts;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationClassificationDataConceptTypes;

public enum ClassificationDataConceptTypesClassifications
		implements IClassificationClassificationDataConceptTypes<ClassificationDataConceptTypesClassifications>
{

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	ClassificationDataConceptTypesClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	ClassificationDataConceptTypesClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
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
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}

	@Override
	public String classificationValue()
	{
		return classificationValue;
	}
}
