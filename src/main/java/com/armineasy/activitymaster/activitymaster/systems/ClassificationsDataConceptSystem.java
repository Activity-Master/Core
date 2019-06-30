package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationsDataConceptService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public class ClassificationsDataConceptSystem
		implements IActivityMasterSystem<ClassificationsDataConceptSystem>
{
	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();

	@SuppressWarnings("Duplicates")
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> activityMaster = GuiceContext.get(SystemsService.class)
		                                         .getActivityMaster(enterprise);

		logProgress("Classification Data Concept System", "Checking/Creating Base Concepts", progressMonitor);

		ClassificationsDataConceptService service = GuiceContext.get(ClassificationsDataConceptService.class);
		service.createDataConcept(GlobalClassificationsDataConceptName, "Any general classification", activityMaster);
		service.createDataConcept(NoClassificationDataConceptName, "No classification is applicable", activityMaster);

		service.createDataConcept(SecurityTokenXClassification, "Security Token Classifications", activityMaster);
		service.createDataConcept(SecurityTokenXSecurityToken, "Security Token Hierarchy", activityMaster);

		logProgress("Classification Data Concept System", "Loading base concepts", 4, progressMonitor);

		service.createDataConcept(ActiveFlagXClassification, "Any active flag classifications", activityMaster);
		service.createDataConcept(Address, "Addresses are a physical location in a certain geography", activityMaster);
		service.createDataConcept(AddressXGeography, "Any classifications for Address Geography groupings", activityMaster);
		service.createDataConcept(AddressXResourceItem, "Any classification for resource items", activityMaster);
		service.createDataConcept(ArrangementXClassification, "Any classifications for arrangements", activityMaster);
		service.createDataConcept(ArrangementXArrangementType, "Any classifications for the type specified in the arrangement", activityMaster);
		service.createDataConcept(ArrangementXArrangement, "Any classifications for the Relationships between Arrangement and X Arrangements", activityMaster);
		service.createDataConcept(AddressXInvolvedParty, "Any Address Relationship with Involved Parties", activityMaster);
		service.createDataConcept(ArrangementXResourceItem, "Any Arrangement Relationship with a Resource Item", activityMaster);
		service.createDataConcept(ArrangementXProduct, "Arrangement classifications with product", activityMaster);
		service.createDataConcept(ClassificationXResourceItem, "Any classification type that exists for classification and resource items, usually icons etc", activityMaster);
		service.createDataConcept(EventXClassification, "All classifications that relate to events", activityMaster);
		service.createDataConcept(EventXAddress, "Any classifications that relate to Events and Classifications, such as occured at", activityMaster);
		service.createDataConcept(EventXArrangement, "Any classifications for event and arrangements", activityMaster);
		service.createDataConcept(EventXInvolvedParty, "Any classifications for involved parties and events like bought, purchased, called, emailed", activityMaster);
		service.createDataConcept(EventXProduct, "Any Product that exists for X product classifications", activityMaster);
		service.createDataConcept(EventXResourceItem, "Any event resource items, usually json or data received in some force", activityMaster);
		service.createDataConcept(GeographyXClassification, "All Geography Classifications", activityMaster);
		service.createDataConcept(GeographyXGeography, "All Geography Relationships", activityMaster);
		service.createDataConcept(GeographyXResourceItem, "All geography resource items", activityMaster);

		logProgress("Classification Data Concept System", "Still loading base concepts..", 20, progressMonitor);

		service.createDataConcept(InvolvedPartyXInvolvedPartyIdentificationType, "All classifications for identification type and involved party relationships", activityMaster);
		service.createDataConcept(InvolvedPartyXProduct, "All classifications for involved party and product relationships", activityMaster);
		service.createDataConcept(InvolvedPartyXResourceItem, "All classifications for involved party and resource item relationships", activityMaster);
		service.createDataConcept(ProductXProduct, "Product Hierarchy", activityMaster);
		service.createDataConcept(ProductXResourceItem, "All product and resource item relationship classifications", activityMaster);
		service.createDataConcept(ResourceItemXClassification, "all resource item classifications", activityMaster);
		service.createDataConcept(ArrangementXInvolvedParty, "Any relationships for the arrangement and the involved party", activityMaster);
		service.createDataConcept(ClassificationDataConceptXClassification, "All classifications for the data concepts", activityMaster);
		service.createDataConcept(ClassificationDataConceptXResourceItem, "Resource Items for data concepts", activityMaster);
		service.createDataConcept(ClassificationXClassification, "The Classification Hierarchy", activityMaster);
		service.createDataConcept(EnterpriseXClassification, "Classifications for an enterprise", activityMaster);
		service.createDataConcept(EventXEventType, "Classifications for events", activityMaster);
		service.createDataConcept(InvolvedPartyXAddress, "All involved party identification types", activityMaster);
		service.createDataConcept(InvolvedPartyXClassification, "All involved party custom classifications", activityMaster);
		service.createDataConcept(InvolvedPartyXInvolvedParty, "Involved Party Hierarchy", activityMaster);
		service.createDataConcept(InvolvedPartyXInvolvedPartyNameType, "Involved Party Name Types", activityMaster);
		service.createDataConcept(InvolvedPartyXInvolvedPartyType, "Involved Party Types", activityMaster);
		service.createDataConcept(ProductXClassification, "All product classifications", activityMaster);
		service.createDataConcept(ProductXProductType, "Product Types", activityMaster);
		service.createDataConcept(ResourceItemDataXClassification, "Resource Item Data Classifications", activityMaster);
		service.createDataConcept(YesNoXClassification, "Yes No Classifications", activityMaster);

		logProgress("Classification Data Concept System", "Loaded all Base Concepts", 21, progressMonitor);

		service.createDataConcept(ActiveFlag, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(AddressXClassification, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Arrangement, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(ArrangementType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Classification, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(ClassificationDataConcept, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Enterprise, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Event, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EventType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(EventXGeography, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Geography, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedParty, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedPartyIdentificationType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedPartyNameType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedPartyNonOrganic, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedPartyOrganic, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedPartyOrganicType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(InvolvedPartyType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Product, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(ResourceItem, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(ResourceItemData, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(ResourceItemType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(ResourceItemXResourceItemType, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(Systems, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(SystemXClassification, "Standard Table Based Classification", activityMaster);
		service.createDataConcept(YesNo, "Standard Table Based Classification", activityMaster);

		service.createDataConcept(SecurityToken, "Standard Table Based Classification", activityMaster);

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
	public void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Classification Data Concept System", "The system for handling classification data concepts", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
