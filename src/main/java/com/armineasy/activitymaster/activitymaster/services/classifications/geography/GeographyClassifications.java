package com.armineasy.activitymaster.activitymaster.services.classifications.geography;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.product.IProductClassification;

public enum GeographyClassifications
		implements IProductClassification<GeographyClassifications>
{

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	GeographyClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	GeographyClassifications(String classificationValue)
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
