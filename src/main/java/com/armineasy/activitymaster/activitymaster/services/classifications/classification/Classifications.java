package com.armineasy.activitymaster.activitymaster.services.classifications.classification;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.classificationdataconcepts.IClassificationDataConcept;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum Classifications
		implements IClassificationDataConcept<Classifications>
{
	HierarchyTypeClassification("Denotes a hierarchy structure type",GlobalClassificationsDataConceptName),
	NoClassification("No Classification",GlobalClassificationsDataConceptName),
	Security("No Classification", GlobalClassificationsDataConceptName),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	Classifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	Classifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
