package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
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

import java.util.UUID;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.*;


public class SecurityTokenSystem
		extends ActivityMasterDefaultSystem<SecurityTokenSystem>
		implements IActivityMasterSystem<SecurityTokenSystem>
{
	private static final Logger log = Logger.getLogger(SecurityTokenSystem.class.getName());
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	private ISecurityTokenService<?> securityTokenService;

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
		logProgress("Security Token Service", "Starting Security Structure Checks/Install");
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
		createSecurityDefaults(enterprise.getName(), activityMasterSystem);
		SecurityToken rootToken = createSecurityTokens(enterprise.getName(), enterprise);
		createGroupsAndFolders(enterprise, rootToken);
		applyDefaultsToNewEnterprise(enterprise);
		createActivityMasterInvolvedParty(enterprise);
		applyDefaultsToNewEnterpriseAfterActivityMaster(enterprise);
		
		UUID newSystemUUID = com.guicedee.client.IGuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(activityMasterSystem);
		
		SecurityToken activityMasterToken = (SecurityToken) com.guicedee.client.IGuiceContext.get(SystemsService.class)
		                                                                .getSecurityToken(newSystemUUID.toString(), activityMasterSystem, newSystemUUID);
		
		logProgress("Security Management", "Setting Security Configurator to Activity Master");
		
		//Load all the systems system tokens that are created before I am
		EnterpriseSystem enterpriseSystem = com.guicedee.client.IGuiceContext.get(EnterpriseSystem.class);
		ActiveFlagSystem afs = com.guicedee.client.IGuiceContext.get(ActiveFlagSystem.class);
		ClassificationsSystem cfs = com.guicedee.client.IGuiceContext.get(ClassificationsSystem.class);
		ClassificationsDataConceptSystem cdcs = com.guicedee.client.IGuiceContext.get(ClassificationsDataConceptSystem.class);
		SystemsSystem ss = com.guicedee.client.IGuiceContext.get(SystemsSystem.class);
		InvolvedPartySystem ips = com.guicedee.client.IGuiceContext.get(InvolvedPartySystem.class);
		systemsService
		              .registerNewSystem(enterprise, enterpriseSystem.getSystem(enterprise));
		systemsService
		              .registerNewSystem(enterprise, afs.getSystem(enterprise));
		systemsService
		              .registerNewSystem(enterprise, ss.getSystem(enterprise));
		systemsService
		              .registerNewSystem(enterprise, cdcs.getSystem(enterprise));
		systemsService
		              .registerNewSystem(enterprise, cfs.getSystem(enterprise));
		systemsService
		              .registerNewSystem(enterprise, ips.getSystem(enterprise));
		
		logProgress("Security Token Service", "Enabling Security System");
		System.out.println("Enabling Authentication Modules");
		com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(true);
	}
	
	void createSecurityDefaults(String enterpriseName, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		IClassification<?, ?> entClassification = classificationService.create(enterpriseName, system, identityToken);
		
		classificationService.create(SecurityTokenClassifications.UserGroup.toString(),SecurityTokenClassifications.UserGroup.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 1,entClassification,  identityToken);
		
		classificationService.create(SecurityTokenClassifications.User.toString(),SecurityTokenClassifications.User.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 2,  identityToken);
		
		classificationService.create(SecurityTokenClassifications.Guests.toString(),SecurityTokenClassifications.Guests.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 2,  identityToken);
		
		classificationService.create(SecurityTokenClassifications.Visitors.toString(),SecurityTokenClassifications.Visitors.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 2,  identityToken);
		
		classificationService.create(SecurityTokenClassifications.Registered.toString(),SecurityTokenClassifications.Registered.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 2,  identityToken);
		
		classificationService.create(SecurityTokenClassifications.Application.toString(),SecurityTokenClassifications.Application.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 3,  identityToken);
		
		classificationService.create(UserGroupSecurityTokenClassifications.System.toString(),UserGroupSecurityTokenClassifications.System.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 4,  identityToken);
		
		classificationService.create(SecurityTokenClassifications.Plugin.toString(),SecurityTokenClassifications.Plugin.toString(),
				EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken ,system, 5,  identityToken);

		classificationService.create(SecurityTokenClassifications.Identity.toString(),"A security token identity",EnterpriseClassificationDataConcepts.SecurityTokenXClassification,
				system,1,entClassification, identityToken);
		
		logProgress("Security Token Service", "Security Classifications Installed", 11);
	}
	
	SecurityToken createSecurityTokens(String enterpriseName, IEnterprise<?,?> enterprise)
	{
		UUID uuid = getSystemToken(enterprise);
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
		SecurityToken rootToken = (SecurityToken) securityTokenService.create(enterpriseName,
				enterprise.getName(), enterprise.getDescription()
				                                .isEmpty() ? "An enterprise-wide project" : enterprise.getDescription(),
				activityMasterSystem);
		
		securityTokenService.grantAccessToToken(rootToken, rootToken, false, false, false, false, activityMasterSystem);
		
		enterprise.addOrUpdateClassification(EnterpriseClassifications.EnterpriseIdentity, null, rootToken.getSecurityToken(), activityMasterSystem, uuid);
		
		logProgress("Security Token Service", "Enterprise Security Validated", 3);
		
		return rootToken;
	}
	
	@SuppressWarnings("Duplicates")
	void createGroupsAndFolders(IEnterprise<?,?> enterprise, SecurityToken rootToken)
	{
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
		SecurityToken everyoneToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup.toString(),
				UserGroupSecurityTokenClassifications.Everyone.toString(),
				UserGroupSecurityTokenClassifications.Everyone.classificationDescription(),
				activityMasterSystem);
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
				activityMasterSystem);
		
		securityTokenService.link(rootToken, everyoneToken,
				classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem));
		securityTokenService.link(rootToken, everywhereToken,
				classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem));
		securityTokenService.link(everyoneToken, administratorsToken,
				classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem));
	/*	link(everyoneToken, usersToken,
		     classificationService.find(UserGroup, SecurityTokenXSecurityToken.class, activityMasterSystem));
*/
		securityTokenService.link(everyoneToken, usersGuestsToken,
				classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem));
		securityTokenService.link(usersGuestsToken, usersGuestsRegisteredToken,
				classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem));
		securityTokenService.link(usersGuestsToken, usersGuestsVisitorsToken,
				classificationService.find(SecurityTokenClassifications.UserGroup, activityMasterSystem));
		
		securityTokenService.link(rootToken, applicationToken,
				classificationService.find(SecurityTokenClassifications.Application, activityMasterSystem));
		securityTokenService.link(rootToken, systemsToken,
				classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem));
		securityTokenService.link(rootToken, pluginToken,
				classificationService.find(SecurityTokenClassifications.Plugin, activityMasterSystem));
		
		securityTokenService.link(systemsToken, activityMasterToken,
				classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem));
		
		logProgress("Security Token Service", "Security Hierarchy Confirmed", 11);
		
		//mark folders as unable to be deleted by anyone including administrators
		securityTokenService.grantAccessToToken(administratorsToken, rootToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, everyoneToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, administratorsToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, applicationToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, everywhereToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, systemsToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(administratorsToken, pluginToken, true, true, false, true, activityMasterSystem);
		
		//Allow default access to everyone
		securityTokenService.grantAccessToToken(usersGuestsToken, rootToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, everyoneToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, administratorsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, applicationToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, everywhereToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, usersGuestsToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, systemsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsToken, pluginToken, false, false, false, false, activityMasterSystem);
		
		//Allow default access to everyone
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, rootToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, everyoneToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, administratorsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, applicationToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, everywhereToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, usersGuestsToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, systemsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsRegisteredToken, pluginToken, false, false, false, false, activityMasterSystem);
		
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, rootToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, everyoneToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, administratorsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, applicationToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, everywhereToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, usersGuestsToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, systemsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(usersGuestsVisitorsToken, pluginToken, false, false, false, false, activityMasterSystem);
		
		//Apply default guest access right
		securityTokenService.grantAccessToToken(everyoneToken, rootToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, everyoneToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, administratorsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, applicationToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, everywhereToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, systemsToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, pluginToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, usersGuestsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everyoneToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem);
		
		securityTokenService.grantAccessToToken(everywhereToken, rootToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, everyoneToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, administratorsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, applicationToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, systemsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, everywhereToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, pluginToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, usersGuestsToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem);
		securityTokenService.grantAccessToToken(everywhereToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem);
		
		//Apply default application access right
		securityTokenService.grantAccessToToken(applicationToken, rootToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, everyoneToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, administratorsToken, true, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, applicationToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, everywhereToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, systemsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, pluginToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(applicationToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem);
		//Apply default systems access right
		securityTokenService.grantAccessToToken(systemsToken, rootToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, everyoneToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, administratorsToken, true, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, applicationToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, systemsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, pluginToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem);
		//Apply default plugins access right
		securityTokenService.grantAccessToToken(pluginToken, rootToken, false, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, everyoneToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, administratorsToken, true, false, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, applicationToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, systemsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, pluginToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem);
		securityTokenService.grantAccessToToken(pluginToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem);
		
		logProgress("Security Token Service", "Default Security Confirmed", 37);
	}
	
	void applyDefaultsToNewEnterprise(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
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
		
		createDefaultSecurityForTable((WarehouseCoreTable<?, ?, ?>) enterprise, activityMasterSystem);
		logProgress("Security Token Service", "Completed Checks", 1);
	}
	
	void createActivityMasterInvolvedParty(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
		com.guicedee.client.IGuiceContext.get(SystemsSystem.class)
				.createInvolvedPartyForNewSystem(activityMasterSystem);
	}
	
	void applyDefaultsToNewEnterpriseAfterActivityMaster(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
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
	
	void createDefaultSecurityForTable(WarehouseCoreTable< ?, ?, ?> table, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		/*for (WarehouseCoreTable next : table.builder()
		                                    .withEnterprise(system.getEnterpriseID())
		                                    .whereNoSecurityIsApplied()
		                                    .inDateRange()
		                                    .getAll())
		{
			logProgress("Security Token Service", "Checking - " +
			                                      next.getClass()
			                                          .getSimpleName(), 0);
			next.createDefaultSecurity(system, identityToken);
		}*/
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
