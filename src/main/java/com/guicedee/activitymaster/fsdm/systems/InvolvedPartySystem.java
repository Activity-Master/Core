package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.client.types.classifications.types.*;
import com.guicedee.guicedinjection.GuiceContext;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.*;


public class InvolvedPartySystem
		extends ActivityMasterDefaultSystem<InvolvedPartySystem>
		implements IActivityMasterSystem<InvolvedPartySystem>
{
	@Inject
	private IInvolvedPartyService<?> service;
	
	@Inject
	private ISystemsService<?> systemsService;
	
	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise)
	{
		return systemsService
		              .create(enterprise, getSystemName(), getSystemDescription());
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Involved Party System", "Starting Checks for Required Values");
		createIdentificationTypes(enterprise);
		createNameTypes(enterprise);
		createTypes(enterprise);
		createDefaultUsers(enterprise);
	}
	
	private void createIdentificationTypes(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> system = IActivityMasterService.getISystem(ActivityMasterSystemName);
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeDriversLicense, "Describes an Individuals Drivers Licence Number");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypePassportNumber, "Describes an Individuals Passport Number");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeTaxNumber, "Tax ID Number");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeVATNumber, "Describes an Organisation’s VAT Registration number.");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeRegistrationNumber, "Describes an Organisation’s Registration number.");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeIdentityNumber, "An Individual Green-Bard Coded ID Document Number");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeEmailAddress, "An individuals email address");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeCellPhoneNumber, "An individuals cell phone number");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeSocialSecurityNumber, "An individuals social security number");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeUserName, "An individuals username");
		//service.createIdentificationType(system, IdentificationTypes.IdentificationTypePassword, "An individuals password");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeUUID, "A given unique system identifier");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeSessionID, "A Given JavascriptSession ID");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeSystemID, "A unique system identifier granted to each each system on registration");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
		                                 "An identifier marking the involved party as the creator of the enterprise");
		service.createIdentificationType(system, IdentificationTypes.IdentificationTypeUnassigned, "This involved party is unassigned and should be classified");
		
		logProgress("Involved Party System", "Loaded Identification Types", 16);
	}
	
	private void createNameTypes(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> system = IActivityMasterService.getISystem(ActivityMasterSystemName);
		service.createNameType(NameTypes.FirstNameType, "The first name of an individual or entity", system);
		service.createNameType(NameTypes.FullNameType, "The full name of an individual or entity", system);
		service.createNameType(NameTypes.PreferredNameType, "An Alias for the Involved Party", system);
		service.createNameType(NameTypes.BirthNameType, "A Given Birth/Maiden Name for an Individual", system);
		service.createNameType(NameTypes.LegalNameType, "The Legal Name associated with an Involved Party", system);
		service.createNameType(NameTypes.CommonNameType, "The Common Name associated with an Individual", system);
		service.createNameType(NameTypes.SalutationType, "The Salutation Associated with an Individual", system);
		service.createNameType(NameTypes.MiddleNameType, "The Middle Name of an Individual", system);
		service.createNameType(NameTypes.InitialsType, "The initials of an Individual", system);
		service.createNameType(NameTypes.SurnameType, "The last name of an Individual", system);
		service.createNameType(NameTypes.QualificationType, "The Qualification of an Individual", system);
		service.createNameType(NameTypes.SuffixType, "The Suffix on an Individual's Name - e.g. \"Jnr, Snr\"", system);
		
		logProgress("Involved Party System", "Loaded Name Types", 12);
	}
	
	private void createTypes(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> system = IActivityMasterService.getISystem(ActivityMasterSystemName);
		service.createType(system, IPTypes.TypeIndividual, "Defines any involved party as a physical person");
		service.createType(system, IPTypes.TypeOrganisation, "Defines any involved party as an organisation");
		service.createType(system, IPTypes.TypeSystem, "Defines any involved party as a physical system");
		service.createType(system, IPTypes.TypeDevice, "Defines any involved party as a physical system");
		service.createType(system, IPTypes.TypeApplication, "Defines any involved party as an application");
		service.createType(system, IPTypes.TypeAbstraction, "Represents an abstract entity such as Time");
		service.createType(system, IPTypes.TypeUnknown, "The type of Involved Party is unknown.");
		logProgress("Involved Party System", "Loaded Types", 6);
	}
	
	private void createDefaultUsers(IEnterprise<?,?> enterprise)
	{
		InvolvedPartyService service = GuiceContext.get(InvolvedPartyService.class);
	}
	
	
	@Override
	public int totalTasks()
	{
		return 5;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 5;
	}
	
	@Override
	public String getSystemName()
	{
		return InvolvedPartySystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for managing Involved Parties";
	}
}
