package com.armineasy.activitymaster.activitymaster.services.classifications.classificationdataconcepts;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum DataConceptClassifications
		implements IClassificationDataConcept<DataConceptClassifications>
{

	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	DataConceptClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	DataConceptClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
