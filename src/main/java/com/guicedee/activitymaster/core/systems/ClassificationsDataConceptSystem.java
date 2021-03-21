package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.core.ClassificationsDataConceptService;

import static com.guicedee.activitymaster.client.services.IClassificationDataConceptService.*;
import static com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.activitymaster.core.SystemsService.*;


public class ClassificationsDataConceptSystem
		extends ActivityMasterDefaultSystem<ClassificationsDataConceptSystem>
		implements IActivityMasterSystem<ClassificationsDataConceptSystem>
{
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Inject
	private ClassificationsDataConceptService service;
	
	@Override
	public void registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, getSystemName(), getSystemDescription());
	}
	
	@SuppressWarnings("Duplicates")
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Classification Data Concept System", "Checking/Creating Base Concepts", progressMonitor);
		
		createDefaultConcepts(progressMonitor);
		createActiveFlagConcepts(progressMonitor);

		createAddressConcepts(progressMonitor);
		createArrangementConcepts(progressMonitor);
		createClassificationsConcepts(progressMonitor);
		createInvolvedPartyConcepts(progressMonitor);
		createEventsConcepts(progressMonitor);
		createProductConcepts(progressMonitor);
		createGeographyConcepts(progressMonitor);
		createResourceItemConcepts(progressMonitor);
		
		createRulesConcepts(progressMonitor);
		
	}
	
	private void createDefaultConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Base Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName, "Any general classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, "No classification is applicable", activityMasterSystem);
		//security
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityToken, "A security token identifies an entity possible to perform events on the system with", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXClassification, "Security Token Quick-Find Entries", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, "Security Token Hierarchy", activityMasterSystem);
		//Enterprise
		service.createDataConcept(EnterpriseClassificationDataConcepts.Enterprise, "An enterprise is the top level", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EnterpriseXClassification, "Classifications for an enterprise", activityMasterSystem);
		//System
		service.createDataConcept(EnterpriseClassificationDataConcepts.Systems, "Systems are sub-applications that provide functions and features to an enterprise", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SystemXClassification, "A set of system classifications", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.YesNo, "Designations for boolean entry fields", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.YesNoXClassification, "Different classifications for boolean fields", activityMasterSystem);
	}
	
	
	private void createInvolvedPartyConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Involved Party Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedParty, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNameType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyType, "Standard Table Based Classification", activityMasterSystem);
		
		service.createDataConcept(InvolvedPartyXAddress, "All involved party identification types", activityMasterSystem);
		service.createDataConcept(InvolvedPartyXClassification, "All involved party custom classifications", activityMasterSystem);
		service.createDataConcept(InvolvedPartyXInvolvedParty, "Involved Party Hierarchy", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, "Involved Party Name Types", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, "Involved Party Types", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
				"All classifications for identification type and involved party relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, "All classifications for involved party and product relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXRules, "All classifications for involved party and product relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, "All classifications for involved party and resource item relationships",
				activityMasterSystem);
	}
	
	
	private void createProductConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Product Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.Product, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProductType, "Product Types", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProduct, "Product Hierarchy", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXResourceItem, "All product and resource item relationship classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXClassification, "All product classifications", activityMasterSystem);
		
	}
	
	private void createResourceItemConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Resource Item Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItem, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemData, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, "Resource Item Data Classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXClassification, "all resource item classifications", activityMasterSystem);
		
	}
	
	private void createRulesConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Rules Concepts", progressMonitor);
		
		service.createDataConcept(Rules, "Calculated rules", activityMasterSystem);
		service.createDataConcept(RulesType, "Calculated rules types", activityMasterSystem);
		service.createDataConcept(RulesTypeXClassification, "Rule Types Calculations", activityMasterSystem);
		service.createDataConcept(RulesXProduct, "Rule Types Calculations", activityMasterSystem);
		service.createDataConcept(RulesXResourceItem, "Rule Types Calculations", activityMasterSystem);
		
		
	}
	
	
	private void createActiveFlagConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Active Flag Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlag, "An Active Flag", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlagXClassification, "Any active flag classifications", activityMasterSystem);
		
	}
	
	
	private void createGeographyConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Active Flag Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.Geography, "Specific to a geography item", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXClassification, "All Geography Classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXGeography, "All Geography Relationships", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXResourceItem, "All geography resource items", activityMasterSystem);

	}
	
	private void createAddressConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Address Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.Address, "Addresses are a physical location in a certain geography", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXGeography, "Any classifications for Address Geography groupings", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXResourceItem, "Any classification for resource items", activityMasterSystem);
		
	}
	
	private void createArrangementConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Arrangement Concepts", progressMonitor);
		
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.Arrangement, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, "Any relationships for the arrangement and the involved party", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXClassification, "Any classifications for arrangements", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangementType, "Any classifications for the type specified in the arrangement",
				activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangement, "Any classifications for the Relationships between Arrangement and X Arrangements",
				activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXInvolvedParty, "Any Address Relationship with Involved Parties", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXProduct, "Arrangement classifications with product", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXResourceItem, "Any Arrangement Relationship with a Resource Item", activityMasterSystem);
		service.createDataConcept(ArrangementXRules, "Classifications for an arrangements link to a set of rules", activityMasterSystem);
		service.createDataConcept(ArrangementXRulesTypes, "Classifications for an arrangements link to a set of rules", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementType, "A type of arrangement or agreement", activityMasterSystem);
		
	}
	
	private void createClassificationsConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Classification Concepts", progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConcept, "A designation of a table", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, "All classifications for the data concepts", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, "Resource Items for data concepts", activityMasterSystem);
		
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.Classification, "Standard Table Based Classification", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXClassification, "The Classification Hierarchy", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXResourceItem,
				"Any classification type that exists for classification and resource items, usually icons etc", activityMasterSystem);
		
	}
	
	private void createEventsConcepts(IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Data Concept System", "Event Concepts", progressMonitor);
		
		//Event
		service.createDataConcept(EnterpriseClassificationDataConcepts.Event, "An event is an audit of any change in the system, post installation", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventType, "A specific type of event ", activityMasterSystem);
		service.createDataConcept(EventXEventType, "Classifications for events", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXClassification, "All classifications that relate to events", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXAddress, "Any classifications that relate to Events and Classifications, such as occured at",
				activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXArrangement, "Any classifications for event and arrangements", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXInvolvedParty,
				"Any classifications for involved parties and events like bought, purchased, called, emailed", activityMasterSystem);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXGeography, "Geographical mappings for events", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXProduct, "Any Product that exists for X product classifications", activityMasterSystem);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXResourceItem, "Any event resource items, usually json or data received in some force", activityMasterSystem);
		service.createDataConcept(EventXRules, "An events link to a set of rules", activityMasterSystem);
		
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
