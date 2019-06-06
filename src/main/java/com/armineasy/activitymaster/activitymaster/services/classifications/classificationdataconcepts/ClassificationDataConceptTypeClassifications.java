package com.armineasy.activitymaster.activitymaster.services.classifications.classificationdataconcepts;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassificationClassificationDataConceptType;

public enum ClassificationDataConceptTypeClassifications
		implements IClassificationClassificationDataConceptType<ClassificationDataConceptTypeClassifications>
{

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	ClassificationDataConceptTypeClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	ClassificationDataConceptTypeClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
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
