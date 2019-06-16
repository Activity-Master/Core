package com.armineasy.activitymaster.activitymaster.services.classifications.yesno;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.product.IProductClassification;

public enum YesNoClassifications
		implements IProductClassification<YesNoClassifications>
{

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	YesNoClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	YesNoClassifications(String classificationValue)
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
