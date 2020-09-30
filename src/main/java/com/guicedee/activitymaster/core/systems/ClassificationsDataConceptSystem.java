package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.ClassificationsDataConceptService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.activitymaster.core.services.system.IActivityMasterService.*;

@Singleton
public class ClassificationsDataConceptSystem
		extends ActivityMasterDefaultSystem<ClassificationsDataConceptSystem>
		implements IActivityMasterSystem<ClassificationsDataConceptSystem>
{
	@SuppressWarnings("Duplicates")
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		wipeCaches();
		ISystems<?> activityMaster = GuiceContext.get(SystemsService.class)
		                                         .getActivityMaster(enterprise);
		
		logProgress("Classification Data Concept System", "Checking/Creating Base Concepts", progressMonitor);
		
		ClassificationsDataConceptService service = GuiceContext.get(ClassificationsDataConceptService.class);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName, "Any general classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, "No classification is applicable", activityMaster);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXClassification, "Security Token Classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, "Security Token Hierarchy", activityMaster);
		
		logProgress("Classification Data Concept System", "Loading base concepts", 4, progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlagXClassification, "Any active flag classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Address, "Addresses are a physical location in a certain geography", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXGeography, "Any classifications for Address Geography groupings", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXResourceItem, "Any classification for resource items", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXClassification, "Any classifications for arrangements", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangementType, "Any classifications for the type specified in the arrangement",
		                          activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangement, "Any classifications for the Relationships between Arrangement and X Arrangements",
		                          activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXInvolvedParty, "Any Address Relationship with Involved Parties", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXResourceItem, "Any Arrangement Relationship with a Resource Item", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXProduct, "Arrangement classifications with product", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXResourceItem,
		                          "Any classification type that exists for classification and resource items, usually icons etc", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXClassification, "All classifications that relate to events", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXAddress, "Any classifications that relate to Events and Classifications, such as occured at",
		                          activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXArrangement, "Any classifications for event and arrangements", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXInvolvedParty,
		                          "Any classifications for involved parties and events like bought, purchased, called, emailed", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXProduct, "Any Product that exists for X product classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXResourceItem, "Any event resource items, usually json or data received in some force", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXClassification, "All Geography Classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXGeography, "All Geography Relationships", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXResourceItem, "All geography resource items", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Geography, "Specific to a geography item", activityMaster);
		
		logProgress("Classification Data Concept System", "Still loading base concepts..", 20, progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
		                          "All classifications for identification type and involved party relationships", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, "All classifications for involved party and product relationships", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, "All classifications for involved party and resource item relationships",
		                          activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProduct, "Product Hierarchy", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXResourceItem, "All product and resource item relationship classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXClassification, "all resource item classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, "Any relationships for the arrangement and the involved party", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, "All classifications for the data concepts", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, "Resource Items for data concepts", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXClassification, "The Classification Hierarchy", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EnterpriseXClassification, "Classifications for an enterprise", activityMaster);
		service.createDataConcept(EventXEventType, "Classifications for events", activityMaster);
		service.createDataConcept(InvolvedPartyXAddress, "All involved party identification types", activityMaster);
		service.createDataConcept(InvolvedPartyXClassification, "All involved party custom classifications", activityMaster);
		service.createDataConcept(InvolvedPartyXInvolvedParty, "Involved Party Hierarchy", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, "Involved Party Name Types", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, "Involved Party Types", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXClassification, "All product classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProductType, "Product Types", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, "Resource Item Data Classifications", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.YesNoXClassification, "Yes No Classifications", activityMaster);
		
		logProgress("Classification Data Concept System", "Loaded all Base Concepts", 21, progressMonitor);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlag, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXClassification, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Arrangement, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Classification, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConcept, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Enterprise, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Event, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.EventXGeography, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Geography, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedParty, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNameType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Product, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItem, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemData, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.Systems, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.SystemXClassification, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EnterpriseClassificationDataConcepts.YesNo, "Standard Table Based Classification", activityMaster);
		
		service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityToken, "Standard Table Based Classification", activityMaster);
		
		logProgress("Classification Data Concept System", "Loaded all Table Base Concepts", 4, progressMonitor);
	}
	
	@Override
	public int totalTasks()
	{
		return 45;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 3;
	}
	
	@Override
	public String getSystemName()
	{
		return "Classification Data Concept System";
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for handling classification data concepts";
	}
	
}
