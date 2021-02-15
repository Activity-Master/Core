package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.ClassificationsDataConceptService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.activitymaster.core.services.system.IClassificationDataConceptService.*;


public class ClassificationsDataConceptSystem
		extends ActivityMasterDefaultSystem<ClassificationsDataConceptSystem>
		implements IActivityMasterSystem<ClassificationsDataConceptSystem>
{
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, getSystemName(), getSystemDescription());
	}
	
	@SuppressWarnings("Duplicates")
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Classification Data Concept System", "Checking/Creating Base Concepts", progressMonitor);
		
		ClassificationsDataConceptService service = GuiceContext.get(ClassificationsDataConceptService.class);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName, "Any general classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, "No classification is applicable", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXClassification, "Security Token Classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, "Security Token Hierarchy", activityMasterSystem);
		
		logProgress("Classification Data Concept System", "Loading base concepts", 4, progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlagXClassification, "Any active flag classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Address, "Addresses are a physical location in a certain geography", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXGeography, "Any classifications for Address Geography groupings", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXResourceItem, "Any classification for resource items", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXClassification, "Any classifications for arrangements", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangementType, "Any classifications for the type specified in the arrangement",
		                          activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangement, "Any classifications for the Relationships between Arrangement and X Arrangements",
		                          activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXInvolvedParty, "Any Address Relationship with Involved Parties", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXResourceItem, "Any Arrangement Relationship with a Resource Item", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXProduct, "Arrangement classifications with product", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXResourceItem,
		                          "Any classification type that exists for classification and resource items, usually icons etc", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXClassification, "All classifications that relate to events", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXAddress, "Any classifications that relate to Events and Classifications, such as occured at",
		                          activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXArrangement, "Any classifications for event and arrangements", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXInvolvedParty,
		                          "Any classifications for involved parties and events like bought, purchased, called, emailed", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXProduct, "Any Product that exists for X product classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXResourceItem, "Any event resource items, usually json or data received in some force", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXClassification, "All Geography Classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXGeography, "All Geography Relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXResourceItem, "All geography resource items", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Geography, "Specific to a geography item", activityMasterSystem);
		
		logProgress("Classification Data Concept System", "Still loading base concepts..", 20, progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
		                          "All classifications for identification type and involved party relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, "All classifications for involved party and product relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXRules, "All classifications for involved party and product relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, "All classifications for involved party and resource item relationships",
		                          activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProduct, "Product Hierarchy", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXResourceItem, "All product and resource item relationship classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXClassification, "all resource item classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, "Any relationships for the arrangement and the involved party", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, "All classifications for the data concepts", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, "Resource Items for data concepts", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXClassification, "The Classification Hierarchy", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EnterpriseXClassification, "Classifications for an enterprise", activityMasterSystem);
		service.createDataConcept(ArrangementXRules, "Classifications for an arrangements link to a set of rules", activityMasterSystem);
		service.createDataConcept(EventXRules, "An events link to a set of rules", activityMasterSystem);
		service.createDataConcept(EventXEventType, "Classifications for events", activityMasterSystem);
		service.createDataConcept(InvolvedPartyXAddress, "All involved party identification types", activityMasterSystem);
		service.createDataConcept(InvolvedPartyXClassification, "All involved party custom classifications", activityMasterSystem);
		service.createDataConcept(InvolvedPartyXInvolvedParty, "Involved Party Hierarchy", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, "Involved Party Name Types", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, "Involved Party Types", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXClassification, "All product classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProductType, "Product Types", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, "Resource Item Data Classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.YesNoXClassification, "Yes No Classifications", activityMasterSystem);
		
		logProgress("Classification Data Concept System", "Loaded all Base Concepts", 21, progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlag, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXClassification, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Arrangement, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Classification, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConcept, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Enterprise, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Event, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXGeography, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Geography, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedParty, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNameType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Product, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItem, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemData, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, "Standard Table Based Classification", activityMasterSystem);
		
		service.createDataConcept(Rules, "Calculated rules", activityMasterSystem);
		service.createDataConcept(RulesType, "Calculated rules types", activityMasterSystem);
		service.createDataConcept(RulesTypeXClassification, "Rule Types Calculations", activityMasterSystem);
		service.createDataConcept(RulesXProduct, "Rule Types Calculations", activityMasterSystem);
		service.createDataConcept(RulesXResourceItem, "Rule Types Calculations", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.Systems, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SystemXClassification, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.YesNo, "Standard Table Based Classification", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityToken, "Standard Table Based Classification", activityMasterSystem);
		
		logProgress("Classification Data Concept System", "Loaded all Table Base Concepts", 4, progressMonitor);
	}
	
	@Override
	public int totalTasks()
	{
		return 7;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 3;
	}
	
	@Override
	public String getSystemName()
	{
		return ClassificationDataConceptSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for handling classification data concepts";
	}
	
}
