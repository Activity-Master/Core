package com.armineasy.activitymaster.activitymaster.services.classifications.systems;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.product.IProductClassification;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum SystemsClassifications
		implements ISystemsClassification<SystemsClassifications>
{
	SystemIdentity("Defines an identity classification relationship", SystemXClassification)
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	SystemsClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
