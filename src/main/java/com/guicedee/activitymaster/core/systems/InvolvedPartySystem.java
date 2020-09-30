package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.InvolvedPartyService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.core.services.types.IPTypes;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.activitymaster.core.services.types.NameTypes;
import com.guicedee.activitymaster.core.services.types.OrganicPartyTypes;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

@Singleton
public class InvolvedPartySystem
		extends ActivityMasterDefaultSystem<InvolvedPartySystem>
		implements IActivityMasterSystem<InvolvedPartySystem>
{
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Involved Party System", "Starting Checks for Required Values", progressMonitor);
		createIdentificationTypes(enterprise, progressMonitor);
		createNameTypes(enterprise, progressMonitor);
		createTypes(enterprise, progressMonitor);
		createDefaultUsers(enterprise, progressMonitor);
		createOrganicTypes(enterprise, progressMonitor);
	}
	
	private void createIdentificationTypes(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
		
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeDriversLicense, "Describes an Individuals Drivers Licence Number");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypePassportNumber, "Describes an Individuals Passport Number");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeTaxNumber, "Tax ID Number");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeVATNumber, "Describes an Organisation’s VAT Registration number.");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeRegistrationNumber, "Describes an Organisation’s Registration number.");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeIdentityNumber, "An Individual Green-Bard Coded ID Document Number");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeEmailAddress, "An individuals email address");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeCellPhoneNumber, "An individuals cell phone number");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeSocialSecurityNumber, "An individuals social security number");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeUserName, "An individuals username");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypePassword, "An individuals password");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeUUID, "A given unique system identifier");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeSessionID, "A Given JavascriptSession ID");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeSystemID, "A unique system identifier granted to each each system on registration");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
		                                 "An identifier marking the involved party as the creator of the enterprise");
		service.createIdentificationType(enterprise, IdentificationTypes.IdentificationTypeUnassigned, "This involved party is unassigned and should be classified");
		
		logProgress("Involved Party System", "Loaded Identification Types", 16, progressMonitor);
	}
	
	private void createNameTypes(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
		
		service.createNameType(NameTypes.FirstNameType, "The first name of an individual or entity", enterprise);
		service.createNameType(NameTypes.FullNameType, "The full name of an individual or entity", enterprise);
		service.createNameType(NameTypes.PreferredNameType, "An Alias for the Involved Party", enterprise);
		service.createNameType(NameTypes.BirthNameType, "A Given Birth/Maiden Name for an Individual", enterprise);
		service.createNameType(NameTypes.LegalNameType, "The Legal Name associated with an Involved Party", enterprise);
		service.createNameType(NameTypes.CommonNameType, "The Common Name associated with an Individual", enterprise);
		service.createNameType(NameTypes.SalutationType, "The Salutation Associated with an Individual", enterprise);
		service.createNameType(NameTypes.MiddleNameType, "The Middle Name of an Individual", enterprise);
		service.createNameType(NameTypes.InitialsType, "The initials of an Individual", enterprise);
		service.createNameType(NameTypes.SurnameType, "The last name of an Individual", enterprise);
		service.createNameType(NameTypes.QualificationType, "The Qualification of an Individual", enterprise);
		service.createNameType(NameTypes.SuffixType, "The Suffix on an Individual's Name - e.g. \"Jnr, Snr\"", enterprise);
		
		logProgress("Involved Party System", "Loaded Name Types", 12, progressMonitor);
	}
	
	private void createTypes(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
		
		service.createType(enterprise, IPTypes.TypeIndividual, "Defines any involved party as a physical person");
		service.createType(enterprise, IPTypes.TypeOrganisation, "Defines any involved party as an organisation");
		service.createType(enterprise, IPTypes.TypeSystem, "Defines any involved party as a physical system");
		service.createType(enterprise, IPTypes.TypeApplication, "Defines any involved party as an application");
		service.createType(enterprise, IPTypes.TypeAbstraction, "Represents an abstract entity such as Time");
		service.createType(enterprise, IPTypes.TypeUnknown, "The type of Involved Party is unknown.");
		logProgress("Involved Party System", "Loaded Types", 6, progressMonitor);
	}
	
	private void createDefaultUsers(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
	}
	
	private void createOrganicTypes(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
		
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicCustomerType, "Defines a customer");
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicEmployeeType, "Defines an employee of the Enterprise");
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicUserType, "Defines a user of the given enterprise");
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicAgentType, "Defines an agent of the enterprise");
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicClientType, "Defines a client of the enterprise");
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicUnknownType, "The type of Organic Party is unknown");
		logProgress("Involved Party System", "Loaded Organic Types", 6, progressMonitor);
		
		service.createOrganicType(enterprise, OrganicPartyTypes.OrganicCustomerType, "Defines a customer");
	}
	
	@Override
	public int totalTasks()
	{
		return 40;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 7;
	}
	
	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	
	}
	
	@Override
	public String getSystemName()
	{
		return "Involved Party System";
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for managing Involved Parties";
	}
	
}
