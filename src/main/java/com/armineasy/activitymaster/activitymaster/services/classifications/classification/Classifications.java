package com.armineasy.activitymaster.activitymaster.services.classifications.classification;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationClassificationDataConceptTypes;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum Classifications
		implements IClassificationClassificationDataConceptTypes<Classifications>
{
	HierarchyTypeClassification("Denotes a hierarchy structure type", GlobalClassificationsDataConceptName),
	NoClassification("No Classification", GlobalClassificationsDataConceptName),
	DefaultClassification("Default Classification", GlobalClassificationsDataConceptName),
	Security("No Classification", GlobalClassificationsDataConceptName),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	Classifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	Classifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
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
