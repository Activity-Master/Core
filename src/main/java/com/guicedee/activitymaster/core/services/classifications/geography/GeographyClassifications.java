package com.guicedee.activitymaster.core.services.classifications.geography;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.classifications.product.IProductClassification;

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
