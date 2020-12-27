package com.guicedee.activitymaster.core.services.classifications.resourceitems;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum ResourceItemTypes
		implements IResourceType<ResourceItemTypes>
{
	PhysicalDocuments("This is a reference to a physical document", ResourceItemType),
	Documents("An electronic document", ResourceItemType),
	JsonPacket("A JSON packet", ResourceItemType),
	XMLPacket("A XML packet", ResourceItemType),
	Invoices("An invoice", ResourceItemType),
	Statements("A statement", ResourceItemType),
	ElectronicDocuments("A collection of electronic documents", ResourceItemType),
	Icon("An Icon", ResourceItemType),
	Logo("A Logo", ResourceItemType),
	Flag("A Flag", ResourceItemType),
	Banner("A Banner", ResourceItemType),
	Gravatar("A Gravatar", ResourceItemType),
	Screenshot("A Gravatar", ResourceItemType),
	Background("A given background with the applied style", ResourceItemType),
	StyleSheets("A CSS StyleSheet", ResourceItemType),
	JavaScriptTemplates("A JavaScript Template", ResourceItemType),
	HtmlTemplate("A HTML 5 Template", ResourceItemType),
	MobileDevice("A physical mobile device", ResourceItemType),
	
	BrowserInformation("Browser Identifying Information", ResourceItemType),
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
