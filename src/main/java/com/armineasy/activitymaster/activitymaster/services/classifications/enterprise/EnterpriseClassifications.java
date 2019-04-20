package com.armineasy.activitymaster.activitymaster.services.classifications.enterprise;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum EnterpriseClassifications
		implements IEnterpriseClassification<EnterpriseClassifications>
{
	Version("The version of the current Activity Master database", EnterpriseXClassification),
	RequiresUpdate("If the enterprise requires an update", EnterpriseXClassification),
	EnterpriseIdentity("The root UUID of an enterprise", EnterpriseXClassification),


	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	EnterpriseClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
