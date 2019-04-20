package com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum ResourceItemClassifications
		implements IResourceItemClassification<ResourceItemClassifications>
{
	FileResourceItemClassifications("Resource items relating to a file",ResourceItemXClassification),

	AddedANewDevice("The Involved Party registered a new device", ResourceItemXClassification),
	HadNewConnectionDetails("The Involved Party registered a set of details identifying their browser connection", ResourceItemXClassification),
	Description("The description of the file resource item", ResourceItemXClassification),
	Extension("The extension of the file resource item", ResourceItemXClassification),
	Size("The File Size in Bytes", ResourceItemXClassification),
	Icon("Any custom icon for the file", ResourceItemXClassification),
	FileLocation("A set location of somewhere for this file", ResourceItemXClassification),
	UUID("A unique UUID for a resource item", ResourceItemXClassification),

	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	ResourceItemClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	ResourceItemClassifications(String classificationValue)
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
