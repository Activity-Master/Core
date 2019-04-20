package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.types.IPTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.NameTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.OrganicPartyTypes.*;

public class InvolvedPartySystem
		implements IActivityMasterSystem<InvolvedPartySystem>
{

	private static final Map<Enterprise, UUID> systemTokens = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Involved Party System", "Starting Checks for Required Values", progressMonitor);
		createIdentificationTypes(enterprise, progressMonitor);
		createNameTypes(enterprise, progressMonitor);
		createTypes(enterprise, progressMonitor);
		createDefaultUsers(enterprise, progressMonitor);
		createOrganicTypes(enterprise, progressMonitor);
	}

	private void createIdentificationTypes(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);

		service.createIdentificationType(enterprise, IdentificationTypeDriversLicense, "Describes an Individuals Drivers Licence Number");
		service.createIdentificationType(enterprise, IdentificationTypePassportNumber, "Describes an Individuals Passport Number");
		service.createIdentificationType(enterprise, IdentificationTypeTaxNumber, "Tax ID Number");
		service.createIdentificationType(enterprise, IdentificationTypeVATNumber, "Describes an Organisation’s VAT Registration number.");
		service.createIdentificationType(enterprise, IdentificationTypeIdentityNumber, "An Individual Green-Bard Coded ID Document Number");
		service.createIdentificationType(enterprise, IdentificationTypeEmailAddress, "An individuals email address");
		service.createIdentificationType(enterprise, IdentificationTypeCellPhoneNumber, "An individuals cell phone number");
		service.createIdentificationType(enterprise, IdentificationTypeSocialSecurityNumber, "An individuals social security number");
		service.createIdentificationType(enterprise, IdentificationTypeUserName, "An individuals username");
		service.createIdentificationType(enterprise, IdentificationTypePassword, "An individuals password");
		service.createIdentificationType(enterprise, IdentificationTypeUUID, "A given unique system identifier");
		service.createIdentificationType(enterprise, IdentificationTypeSessionID, "A Given JavascriptSession ID");
		service.createIdentificationType(enterprise, IdentificationTypeSystemID, "A unique system identifier granted to each each system on registration");
		service.createIdentificationType(enterprise, IdentificationTypeEnterpriseCreatorRole, "An identifier marking the involved party as the creator of the enterprise");
		service.createIdentificationType(enterprise, IdentificationTypeUnassigned, "This involved party is unassigned and should be classified");

		logProgress("Involved Party System", "Loaded Identification Types", 16, progressMonitor);
	}

	private void createNameTypes(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);

		service.createNameType(FirstNameType, "The first name of an individual or entity", enterprise);
		service.createNameType(FullNameType, "The full name of an individual or entity", enterprise);
		service.createNameType(PreferredNameType, "An Alias for the Involved Party", enterprise);
		service.createNameType(BirthNameType, "A Given Birth/Maiden Name for an Individual", enterprise);
		service.createNameType(LegalNameType, "The Legal Name associated with an Involved Party", enterprise);
		service.createNameType(CommonNameType, "The Common Name associated with an Individual", enterprise);
		service.createNameType(SalutationType, "The Salutation Associated with an Individual", enterprise);
		service.createNameType(MiddleNameType, "The Middle Name of an Individual", enterprise);
		service.createNameType(InitialsType, "The initials of an Individual", enterprise);
		service.createNameType(SurnameType, "The last name of an Individual", enterprise);
		service.createNameType(QualificationType, "The Qualification of an Individual", enterprise);
		service.createNameType(SuffixType, "The Suffix on an Individual's Name - e.g. \"Jnr, Snr\"", enterprise);

		service.createNameType(Username, "A unique username assigned to the Involved Party", enterprise);
		service.createNameType(EmailAddress, "A unique Email Address used to identify the Involved Party", enterprise);

		logProgress("Involved Party System", "Loaded Name Types", 12, progressMonitor);
	}

	private void createTypes(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);

		service.createType(enterprise, TypeIndividual, "Defines any involved party as a physical person");
		service.createType(enterprise, TypeOrganisation, "Defines any involved party as an organisation");
		service.createType(enterprise, TypeSystem, "Defines any involved party as a physical system");
		service.createType(enterprise, TypeApplication, "Defines any involved party as an application");
		service.createType(enterprise, TypeAbstraction, "Represents an abstract entity such as Time");
		service.createType(enterprise, TypeUnknown, "The type of Involved Party is unknown.");
		logProgress("Involved Party System", "Loaded Types", 6, progressMonitor);
	}

	private void createDefaultUsers(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
	}

	private void createOrganicTypes(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);

		service.createOrganicType(enterprise, OrganicCustomerType, "Defines a customer");
		service.createOrganicType(enterprise, OrganicEmployeeType, "Defines an employee of the Enterprise");
		service.createOrganicType(enterprise, OrganicUserType, "Defines a user of the given enterprise");
		service.createOrganicType(enterprise, OrganicAgentType, "Defines an agent of the enterprise");
		service.createOrganicType(enterprise, OrganicClientType, "Defines a client of the enterprise");
		service.createOrganicType(enterprise, OrganicUnknownType, "The type of Organic Party is unknown");
		logProgress("Involved Party System", "Loaded Organic Types", 6, progressMonitor);
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
	public void postUpdate(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Involved Party System",
		                                        "The system for managing Involved Parties", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<Enterprise, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
