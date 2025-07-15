package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassification;
import com.guicedee.client.IGuiceContext;
//import jakarta.transaction.Transactional;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class SecurityTokenSystem
		extends ActivityMasterDefaultSystem<SecurityTokenSystem>
		implements IActivityMasterSystem<SecurityTokenSystem>
{

	@Inject
	private IClassificationService<?> classificationService;

	@Inject
	private ISecurityTokenService<?> securityTokenService;

	@Inject
	private ISystemsService<?> systemsService;

	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise)
	{
		// Note: Using reactive programming internally but maintaining synchronous interface
		log.info("Registering security token system for enterprise: {}", enterprise.getName());

		// Create the system
		ISystems<?, ?> iSystems = systemsService
		              .create(enterprise, getSystemName(), getSystemDescription())
		              .onItem().invoke(system -> log.debug("Created system: {}", system.getName()))
		              .onFailure().invoke(error -> log.error("Error creating system: {}", error.getMessage(), error))
		              .await().atMost(Duration.ofMinutes(1));

		// Register the system
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise))
		              .onItem().invoke(() -> log.debug("Registered system: {}", getSystemName()))
		              .onFailure().invoke(error -> log.error("Error registering system: {}", error.getMessage(), error))
		              .await().atMost(Duration.ofMinutes(1));

		log.info("Successfully registered security token system for enterprise: {}", enterprise.getName());
		return iSystems;
	}

	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Security Token Service", "Starting Security Structure Checks/Install");

		// Note: Using reactive programming internally but maintaining synchronous interface
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));

		SecurityTokenSystem instance = IGuiceContext.get(SecurityTokenSystem.class);

		// Call defaultsCreation which handles the core security setup
		instance.defaultsCreation(enterprise);

		logProgress("Security Management", "Setting Security Configurator to Activity Master");

		// Get all the systems that are created before this one
		EnterpriseSystem enterpriseSystem = com.guicedee.client.IGuiceContext.get(EnterpriseSystem.class);
		ActiveFlagSystem afs = com.guicedee.client.IGuiceContext.get(ActiveFlagSystem.class);
		ClassificationsSystem cfs = com.guicedee.client.IGuiceContext.get(ClassificationsSystem.class);
		ClassificationsDataConceptSystem cdcs = com.guicedee.client.IGuiceContext.get(ClassificationsDataConceptSystem.class);
		SystemsSystem ss = com.guicedee.client.IGuiceContext.get(SystemsSystem.class);
		InvolvedPartySystem ips = com.guicedee.client.IGuiceContext.get(InvolvedPartySystem.class);

		// Create a list of operations to run in parallel
		List<Uni<?>> registerOperations = new ArrayList<>();

		// Add all system registration operations to the list
		registerOperations.add(systemsService.registerNewSystem(enterprise, enterpriseSystem.getSystem(enterprise)));
		registerOperations.add(systemsService.registerNewSystem(enterprise, afs.getSystem(enterprise)));
		registerOperations.add(systemsService.registerNewSystem(enterprise, ss.getSystem(enterprise)));
		registerOperations.add(systemsService.registerNewSystem(enterprise, cdcs.getSystem(enterprise)));
		registerOperations.add(systemsService.registerNewSystem(enterprise, cfs.getSystem(enterprise)));
		registerOperations.add(systemsService.registerNewSystem(enterprise, ips.getSystem(enterprise)));

		log.info("Running {} system registration operations in parallel", registerOperations.size());

		// Execute all operations in parallel
		Uni.combine().all().unis(registerOperations)
		   .discardItems()
		   .onFailure().invoke(error -> log.error("Error registering systems: {}", error.getMessage(), error))
		   .await().atMost(Duration.ofMinutes(2));

		logProgress("Security Token Service", "Enabling Security System");
		System.out.println("Enabling Authentication Modules");
		//todo enable security
	//	com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
		//            .setSecurityEnabled(true);
	}

	//@Transactional
	void defaultsCreation(IEnterprise<?,?> enterprise)
	{
		// Note: Using reactive programming internally but maintaining synchronous interface
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));

		log.info("Starting security defaults creation for enterprise: {}", enterprise.getName());

		// Step 1: Create security defaults
		createSecurityDefaults(enterprise.getName(), activityMasterSystem);

		// Step 2: Create security tokens
		SecurityToken rootToken = createSecurityTokens(enterprise.getName(), enterprise);

		// Step 3: Create groups and folders
		createGroupsAndFolders(enterprise, rootToken);

		// Step 4: Apply defaults to new enterprise
		applyDefaultsToNewEnterprise(enterprise);

		// Step 5: Create ActivityMaster involved party
		createActivityMasterInvolvedParty(enterprise);

		// Step 6: Apply defaults after ActivityMaster
		applyDefaultsToNewEnterpriseAfterActivityMaster(enterprise);

		log.info("Completed security defaults creation for enterprise: {}", enterprise.getName());
	}

	//@Transactional
	void registerNewSystem(IEnterprise<?,?> enterprise, ISystems<?,?> creatingSystem)
	{
		// Note: Using reactive programming internally but maintaining synchronous interface
		systemsService.registerNewSystem(enterprise, creatingSystem)
		              .onFailure().invoke(error -> log.error("Error registering system {}: {}", 
		                                                    creatingSystem.getName(), error.getMessage(), error))
		              .await().atMost(Duration.ofMinutes(1));

		log.debug("Successfully registered system: {}", creatingSystem.getName());
	}

	void createSecurityDefaults(String enterpriseName, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		log.info("Creating security defaults for enterprise: {}", enterpriseName);

		// Create the enterprise classification
		IClassification<?, ?> entClassification = classificationService.create(enterpriseName, system, identityToken)
		                                                              .await().atMost(Duration.ofMinutes(1));

		log.debug("Created enterprise classification: {}", entClassification.getName());

		// Create security-related classifications in parallel
		List<Uni<?>> securityClassifications = new ArrayList<>();

		// Add all classification creation operations to the list
		securityClassifications.add(classificationService.create(SecurityTokenClassifications.UserGroup.toString(),
				SecurityTokenClassifications.UserGroup.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 1, entClassification, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.User.toString(),
				SecurityTokenClassifications.User.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.Guests.toString(),
				SecurityTokenClassifications.Guests.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.Visitors.toString(),
				SecurityTokenClassifications.Visitors.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.Registered.toString(),
				SecurityTokenClassifications.Registered.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.Application.toString(),
				SecurityTokenClassifications.Application.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 3, identityToken));

		securityClassifications.add(classificationService.create(UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 4, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.Plugin.toString(),
				SecurityTokenClassifications.Plugin.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 5, identityToken));

		securityClassifications.add(classificationService.create(SecurityTokenClassifications.Identity.toString(),
				"A security token identity",
				EnterpriseClassificationDataConcepts.SecurityTokenXClassification, system, 1, entClassification, identityToken));

		log.info("Running {} security classification creation operations in parallel", securityClassifications.size());

		// Execute all operations in parallel
		Uni.combine().all().unis(securityClassifications)
		   .discardItems()
		   .onFailure().invoke(error -> log.error("Error creating security classifications: {}", error.getMessage(), error))
		   .await().atMost(Duration.ofMinutes(2));

		logProgress("Security Token Service", "Security Classifications Installed", 11);
	}

	//@Transactional
	SecurityToken createSecurityTokens(String enterpriseName, IEnterprise<?,?> enterprise)
	{
		log.info("Creating security tokens for enterprise: {}", enterpriseName);

		// Note: Using reactive programming internally but maintaining synchronous interface
		UUID uuid = getSystemToken(enterprise);
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));

		// Prepare the description
		String description = enterprise.getDescription().isEmpty() 
		                    ? "An enterprise-wide project" 
		                    : enterprise.getDescription();

		// Create the root token
		SecurityToken rootToken = (SecurityToken) securityTokenService.create(enterpriseName,
				enterprise.getName(), description, activityMasterSystem)
				.onItem().invoke(token -> log.debug("Created root token: {}", token.getName()))
				.onFailure().invoke(error -> log.error("Error creating root token: {}", error.getMessage(), error))
				.await().atMost(Duration.ofMinutes(1));

		// Grant access to the token
		securityTokenService.grantAccessToToken(rootToken, rootToken, false, false, false, false, activityMasterSystem)
		                   .onFailure().invoke(error -> log.error("Error granting access to token: {}", error.getMessage(), error))
		                   .await().atMost(Duration.ofMinutes(1));

		// Add classification to the enterprise
		try {
			enterprise.addOrUpdateClassification(EnterpriseClassifications.EnterpriseIdentity, 
			                                    null, 
			                                    rootToken.getSecurityToken(), 
			                                    activityMasterSystem, 
			                                    uuid);
			log.debug("Added classification to enterprise: {}", EnterpriseClassifications.EnterpriseIdentity);
		} catch (Exception e) {
			log.error("Error adding classification to enterprise: {}", e.getMessage(), e);
		}

		logProgress("Security Token Service", "Enterprise Security Validated", 3);
		log.info("Successfully created security tokens for enterprise: {}", enterpriseName);

		return rootToken;
	}

	@SuppressWarnings("Duplicates")
	void createGroupsAndFolders(IEnterprise<?,?> enterprise, SecurityToken rootToken)
	{
		log.info("Creating groups and folders for enterprise: {}", enterprise.getName());

		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));
		// Create the everyone token
		SecurityToken everyoneToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				UserGroupSecurityTokenClassifications.Everyone.toString(),
				UserGroupSecurityTokenClassifications.Everyone.classificationDescription(),
				activityMasterSystem)
				.await().atMost(Duration.ofMinutes(1));
		SecurityToken everywhereToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				UserGroupSecurityTokenClassifications.Everywhere.toString(),
				UserGroupSecurityTokenClassifications.Everywhere.classificationDescription(),
				activityMasterSystem);
		SecurityToken administratorsToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				UserGroupSecurityTokenClassifications.Administrators.toString(),
				UserGroupSecurityTokenClassifications.Administrators.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				SecurityTokenClassifications.Guests.toString(),
				SecurityTokenClassifications.Guests.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsVisitorsToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				SecurityTokenClassifications.Visitors.toString(),
				SecurityTokenClassifications.Visitors.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsRegisteredToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				SecurityTokenClassifications.Registered.toString(),
				SecurityTokenClassifications.Registered.classificationDescription(),
				activityMasterSystem);

		SecurityToken applicationToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.Application.toString(),
				UserGroupSecurityTokenClassifications.Applications.toString(),
				UserGroupSecurityTokenClassifications.Applications.classificationDescription(),
				activityMasterSystem);
		SecurityToken systemsToken = (SecurityToken) securityTokenService.create(
				UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.classificationDescription(),
				activityMasterSystem);

		SecurityToken pluginToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.Plugin.toString(),
				UserGroupSecurityTokenClassifications.Plugins.toString(),
				UserGroupSecurityTokenClassifications.Plugins.classificationDescription(),
				activityMasterSystem);

		SecurityToken activityMasterToken = (SecurityToken) securityTokenService.create(
				UserGroupSecurityTokenClassifications.System.toString(),
				"Activity Master System", "Defines the activity master as a system", activityMasterSystem);

		logProgress("Security Token Service", "Base Security Tokens", 11);

		activityMasterSystem.addOrReuseClassification(SystemsClassifications.SystemIdentity,
				activityMasterToken.getSecurityToken(),
				activityMasterSystem)
				.onItem().invoke(result -> log.debug("Added classification to activity master system: {}", SystemsClassifications.SystemIdentity))
				.onFailure().invoke(error -> log.error("Error adding classification to activity master system: {}", error.getMessage(), error))
				.await().atMost(Duration.ofMinutes(1));

		// Get the UserGroup classification
		IClassification<?, ?> userGroupClassification = classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem)
		                                                                   .await().atMost(Duration.ofMinutes(1));

		// Link tokens with classifications
		securityTokenService.link(rootToken, everyoneToken, userGroupClassification)
		                   .await().atMost(Duration.ofMinutes(1));
		securityTokenService.link(rootToken, everywhereToken, userGroupClassification)
		                   .await().atMost(Duration.ofMinutes(1));
		securityTokenService.link(everyoneToken, administratorsToken, userGroupClassification)
		                   .await().atMost(Duration.ofMinutes(1));
	/*	link(everyoneToken, usersToken,
		     classificationService.find(UserGroup, SecurityTokenXSecurityToken.class, activityMasterSystem));
*/
		// Get classifications for linking tokens
		IClassification<?, ?> userGroupClass = classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem)
		                                                          .await().atMost(Duration.ofMinutes(1));
		IClassification<?, ?> applicationClass = classificationService.find(SecurityTokenClassifications.Application, activityMasterSystem)
		                                                            .await().atMost(Duration.ofMinutes(1));
		IClassification<?, ?> systemClass = classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem)
		                                                       .await().atMost(Duration.ofMinutes(1));
		IClassification<?, ?> pluginClass = classificationService.find(SecurityTokenClassifications.Plugin, activityMasterSystem)
		                                                       .await().atMost(Duration.ofMinutes(1));

		// Link tokens with their classifications
		securityTokenService.link(everyoneToken, usersGuestsToken, userGroupClass)
		                   .await().atMost(Duration.ofMinutes(1));
		securityTokenService.link(usersGuestsToken, usersGuestsRegisteredToken, userGroupClass)
		                   .await().atMost(Duration.ofMinutes(1));
		securityTokenService.link(usersGuestsToken, usersGuestsVisitorsToken, userGroupClass)
		                   .await().atMost(Duration.ofMinutes(1));

		securityTokenService.link(rootToken, applicationToken, applicationClass)
		                   .await().atMost(Duration.ofMinutes(1));
		securityTokenService.link(rootToken, systemsToken, systemClass)
		                   .await().atMost(Duration.ofMinutes(1));
		securityTokenService.link(rootToken, pluginToken, pluginClass)
		                   .await().atMost(Duration.ofMinutes(1));

		securityTokenService.link(systemsToken, activityMasterToken, systemClass)
		                   .await().atMost(Duration.ofMinutes(1));

		logProgress("Security Token Service", "Security Hierarchy Confirmed", 11);

		// Process all grantAccessToToken operations in parallel
		log.info("Starting parallel processing of security token access grants");

		// Create lists to hold all the operations for each token type
		List<Uni<?>> administratorsOperations = new ArrayList<>();
		List<Uni<?>> usersGuestsOperations = new ArrayList<>();
		List<Uni<?>> usersGuestsRegisteredOperations = new ArrayList<>();
		List<Uni<?>> usersGuestsVisitorsOperations = new ArrayList<>();
		List<Uni<?>> everyoneOperations = new ArrayList<>();
		List<Uni<?>> everywhereOperations = new ArrayList<>();
		List<Uni<?>> applicationOperations = new ArrayList<>();
		List<Uni<?>> systemsOperations = new ArrayList<>();
		List<Uni<?>> pluginOperations = new ArrayList<>();

		// Add operations for administratorsToken
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, rootToken, true, true, true, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, everyoneToken, true, true, false, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, administratorsToken, true, true, false, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, applicationToken, true, true, false, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, everywhereToken, true, true, true, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, systemsToken, true, true, false, true, activityMasterSystem));
		administratorsOperations.add(securityTokenService.grantAccessToToken(administratorsToken, pluginToken, true, true, false, true, activityMasterSystem));

		// Add operations for usersGuestsToken
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, rootToken, false, false, false, false, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, everyoneToken, false, false, false, false, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, administratorsToken, false, false, false, false, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, applicationToken, false, false, false, false, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, everywhereToken, false, false, false, true, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, usersGuestsToken, false, false, false, true, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, systemsToken, false, false, false, false, activityMasterSystem));
		usersGuestsOperations.add(securityTokenService.grantAccessToToken(usersGuestsToken, pluginToken, false, false, false, false, activityMasterSystem));

		// Add operations for usersGuestsRegisteredToken
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, rootToken, false, false, false, false, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, everyoneToken, false, false, false, false, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, administratorsToken, false, false, false, false, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, applicationToken, false, false, false, false, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, everywhereToken, false, false, false, true, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, usersGuestsToken, false, false, false, true, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, systemsToken, false, false, false, false, activityMasterSystem));
		usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, pluginToken, false, false, false, false, activityMasterSystem));

		// Add operations for usersGuestsVisitorsToken
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, rootToken, false, false, false, false, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, everyoneToken, false, false, false, false, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, administratorsToken, false, false, false, false, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, applicationToken, false, false, false, false, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, everywhereToken, false, false, false, true, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, usersGuestsToken, false, false, false, true, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, systemsToken, false, false, false, false, activityMasterSystem));
		usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, pluginToken, false, false, false, false, activityMasterSystem));

		// Add operations for everyoneToken
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, rootToken, false, false, false, true, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, everyoneToken, false, false, false, false, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, administratorsToken, false, false, false, false, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, applicationToken, false, false, false, true, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, everywhereToken, true, true, false, true, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, systemsToken, false, false, false, true, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, pluginToken, false, false, false, true, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, usersGuestsToken, false, false, false, false, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem));
		everyoneOperations.add(securityTokenService.grantAccessToToken(everyoneToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem));

		// Add operations for everywhereToken
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, rootToken, false, false, false, true, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, everyoneToken, false, false, false, true, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, administratorsToken, false, false, false, false, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, applicationToken, false, false, false, false, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, systemsToken, false, false, false, false, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, everywhereToken, false, false, false, true, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, pluginToken, false, false, false, false, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, usersGuestsToken, false, false, false, false, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem));
		everywhereOperations.add(securityTokenService.grantAccessToToken(everywhereToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem));

		// Add operations for applicationToken
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, rootToken, false, false, false, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, everyoneToken, true, true, false, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, administratorsToken, true, false, false, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, applicationToken, true, true, false, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, everywhereToken, true, true, false, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, systemsToken, true, true, true, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, pluginToken, true, true, true, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem));
		applicationOperations.add(securityTokenService.grantAccessToToken(applicationToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem));

		// Add operations for systemsToken
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, rootToken, false, false, false, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, everyoneToken, true, true, false, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, administratorsToken, true, false, false, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, applicationToken, true, true, false, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, systemsToken, true, true, true, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, pluginToken, true, true, true, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem));
		systemsOperations.add(securityTokenService.grantAccessToToken(systemsToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem));

		// Add operations for pluginToken
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, rootToken, false, false, false, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, everyoneToken, true, true, false, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, administratorsToken, true, false, false, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, applicationToken, true, true, false, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, systemsToken, true, true, true, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, pluginToken, true, true, true, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem));
		pluginOperations.add(securityTokenService.grantAccessToToken(pluginToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem));

		// Combine all operations into a single list
		List<Uni<?>> allOperations = new ArrayList<>();
		allOperations.addAll(administratorsOperations);
		allOperations.addAll(usersGuestsOperations);
		allOperations.addAll(usersGuestsRegisteredOperations);
		allOperations.addAll(usersGuestsVisitorsOperations);
		allOperations.addAll(everyoneOperations);
		allOperations.addAll(everywhereOperations);
		allOperations.addAll(applicationOperations);
		allOperations.addAll(systemsOperations);
		allOperations.addAll(pluginOperations);

		log.info("Running {} token access grant operations in parallel", allOperations.size());

		// Execute all operations in parallel and wait for them to complete
		Uni.combine().all().unis(allOperations)
		   .discardItems()
		   .onItem().invoke(() -> log.info("All token access grant operations completed successfully"))
		   .onFailure().invoke(error -> log.error("Error granting token access: {}", error.getMessage(), error))
		   .await().atMost(Duration.ofMinutes(5));

		logProgress("Security Token Service", "Default Security Confirmed", 37);
	}

	void applyDefaultsToNewEnterprise(IEnterprise<?,?> enterprise)
	{
		// Note: Using reactive programming internally but maintaining synchronous interface
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));
		logProgress("Security Token Service", "Checking Default Security for all enterprise default items");

		logProgress("Security Token Service", "Starting basic security checks", 1);
		createDefaultSecurityForTable(new ActiveFlag(), activityMasterSystem);
		//logProgress("Security Token Service", "Security Tokens securities", 1);
		//createDefaultSecurity(new SecurityToken(), system);
		logProgress("Security Token Service", "Systems securities", 1);
		createDefaultSecurityForTable(new Systems(), activityMasterSystem);
		logProgress("Security Token Service", "System Classifications securities", 1);
		createDefaultSecurityForTable(new SystemsXClassification(), activityMasterSystem);
		logProgress("Security Token Service", "Classification Data Concepts security checks", 1);
		createDefaultSecurityForTable(new ClassificationDataConcept(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Classifications checks", 1);
		createDefaultSecurityForTable(new Classification(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Hierarchy checks", 1);
		createDefaultSecurityForTable(new ClassificationXClassification(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Identification Type checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyIdentificationType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Name Types checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyNameType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Party Type checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Organic Types checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyOrganicType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Final Enterprise checks", 1);
		createDefaultSecurityForTable(new EnterpriseXClassification(), activityMasterSystem);

		createDefaultSecurityForTable((WarehouseCoreTable<?, ?, ?,?>) enterprise, activityMasterSystem);
		logProgress("Security Token Service", "Completed Checks", 1);
	}

	void createActivityMasterInvolvedParty(IEnterprise<?,?> enterprise)
	{
		// Note: Using reactive programming internally but maintaining synchronous interface
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));
		com.guicedee.client.IGuiceContext.get(SystemsSystem.class)
				.createInvolvedPartyForNewSystem(activityMasterSystem);
	}

	void applyDefaultsToNewEnterpriseAfterActivityMaster(IEnterprise<?,?> enterprise)
	{
		// Note: Using reactive programming internally but maintaining synchronous interface
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));
		logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1);
		createDefaultSecurityForTable(new InvolvedParty(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Organic Relationship checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyOrganic(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Non Organic checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyNonOrganic(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyIdentificationType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Classifications Relationship checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyXClassification(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Name Type Relationship checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyNameType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Involved Party Type Relationship checks", 1);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Events checks", 1);
		createDefaultSecurityForTable(new EventType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Resource Item Types checks", 1);
		createDefaultSecurityForTable(new ResourceItemType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Arrangement Types checks", 1);
		createDefaultSecurityForTable(new ArrangementType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting Arrangement checks", 1);
		createDefaultSecurityForTable(new Arrangement(), activityMasterSystem);
		logProgress("Security Token Service", "Starting ArrangementXType checks", 1);
		createDefaultSecurityForTable(new ArrangementXArrangementType(), activityMasterSystem);
		logProgress("Security Token Service", "Starting ArrangementXClassification checks", 1);
		createDefaultSecurityForTable(new ArrangementXClassification(), activityMasterSystem);
		logProgress("Security Token Service", "Starting ArrangementXResourceItem checks", 1);
		createDefaultSecurityForTable(new ArrangementXResourceItem(), activityMasterSystem);
		logProgress("Security Token Service", "Starting ArrangementXInvolvedParty checks", 1);
		createDefaultSecurityForTable(new ArrangementXInvolvedParty(), activityMasterSystem);
		logProgress("Security Token Service", "Starting ArrangementXProduct checks", 1);
		createDefaultSecurityForTable(new ArrangementXProduct(), activityMasterSystem);
	}

	void createDefaultSecurityForTable(WarehouseCoreTable<?, ?, ?, ?> table, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		// This method is currently disabled but could be implemented using reactive programming
		// The commented code below shows how it might be implemented

		/*
		// Note: Using reactive programming internally but maintaining synchronous interface
		table.builder()
		     .withEnterprise(system.getEnterpriseID())
		     .whereNoSecurityIsApplied()
		     .inDateRange()
		     .getAll()
		     .onItem().invoke(items -> log.debug("Found {} items without security", items.size()))
		     .chain(items -> {
		         // Create a list of operations to run in parallel
		         List<Uni<?>> securityOperations = new ArrayList<>();

		         for (WarehouseCoreTable next : items) {
		             logProgress("Security Token Service", "Checking - " + next.getClass().getSimpleName(), 0);
		             // Add security creation operation to the list
		             securityOperations.add(next.createDefaultSecurity(system, identityToken));
		         }

		         if (securityOperations.isEmpty()) {
		             return Uni.createFrom().voidItem();
		         }

		         // Execute all operations in parallel
		         return Uni.combine().all().unis(securityOperations)
		                  .discardItems()
		                  .onFailure().invoke(error -> log.error("Error creating default security: {}", 
		                                                        error.getMessage(), error));
		     })
		     .await().atMost(Duration.ofMinutes(2));
		*/
	}

	@Override
	public int totalTasks()
	{
		return 190;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 6;
	}

	@Override
	public String getSystemName()
	{
		return SecurityTokenSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing Security Tokens";
	}
}
