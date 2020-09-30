package com.guicedee.activitymaster.core.services.classifications.resourceitems;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum ResourceItemTypes
		implements IResourceType<ResourceItemTypes>
{
	PhysicalDocuments("This is a reference to a physical document", ResourceItem),
	Documents("An electronic document", ResourceItem),
	JsonPacket("A JSON packet", ResourceItem),
	XMLPacket("A XML packet", ResourceItem),
	Invoices("An invoice", ResourceItem),
	Statements("A statement", ResourceItem),
	ElectronicDocuments("A collection of electronic documents", ResourceItem),
	Icon("An Icon", ResourceItem),
	Logo("A Logo", ResourceItem),
	Flag("A Flag", ResourceItem),
	Banner("A Banner", ResourceItem),
	Gravatar("A Gravatar", ResourceItem),
	Screenshot("A Gravatar", ResourceItem),
	Background("A given background with the applied style", ResourceItem),
	StyleSheets("A CSS StyleSheet", ResourceItem),
	JavaScriptTemplates("A JavaScript Template", ResourceItem),
	HtmlTemplate("A HTML 5 Template", ResourceItem),
	MobileDevice("A physical mobile device", ResourceItem),
	
	BrowserInformation("Browser Identifying Information", ResourceItem),
	;
	
	
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;
	
	ResourceItemTypes(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}
	
	ResourceItemTypes(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}
	
	@Override
	public String classificationName()
	{
		return name();
	}
	
	@Override
	public String classificationValue()
	{
		return this.classificationValue;
	}
	
	@Override
	public String classificationDescription()
	{
		return this.classificationValue;
	}
	
	@Override
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
