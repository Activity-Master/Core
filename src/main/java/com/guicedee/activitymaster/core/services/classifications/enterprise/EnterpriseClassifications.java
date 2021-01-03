package com.guicedee.activitymaster.core.services.classifications.enterprise;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum EnterpriseClassifications
		implements IEnterpriseClassification<EnterpriseClassifications>
{
	LastUpdateDate("The assigned date of the last update", EnterpriseXClassification),
	UpdateClass("The class file for an update", EnterpriseXClassification),
	EnterpriseIdentity("The root UUID of an enterprise", EnterpriseXClassification),
	
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	EnterpriseClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	EnterpriseClassifications(String classificationValue)
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
