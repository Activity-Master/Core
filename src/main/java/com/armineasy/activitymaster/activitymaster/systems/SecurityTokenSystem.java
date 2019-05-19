package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.implementations.*;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.classification.Classifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.EnterpriseClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Log
public class SecurityTokenSystem
		implements IActivityMasterSystem<SecurityTokenSystem>
{

	private static final Map<Enterprise, UUID> systemTokens = new HashMap<>();

	@Override
	public void createDefaults(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Security Token Service", "Starting Security Structure Checks/Install", progressMonitor);
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		IEnterpriseName<?> enterpriseName = GuiceContext.get(ActivityMasterConfiguration.class).getEnterpriseName();
		if(enterpriseName == null)
		{
			throw new ActivityMasterException("IEnterpriseName is not set for security root. Make sure to set it in ActivityMasterConfiguration from a created type");
		}

		createSecurityDefaults(GuiceContext.get(ActivityMasterConfiguration.class).getEnterpriseName(),activityMasterSystem,progressMonitor);
		SecurityToken rootToken = createSecurityTokens(enterpriseName, enterprise, progressMonitor);
		createGroupsAndFolders(enterprise, rootToken, progressMonitor);
		applyDefaultsToNewEnterprise(enterprise, progressMonitor);
		createActivityMasterInvolvedParty(enterprise);
		applyDefaultsToNewEnterpriseAfterActivityMaster(enterprise, progressMonitor);


		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(activityMasterSystem);

		SecurityToken activityMasterToken = GuiceContext.get(SystemsService.class)
		                                                .getSecurityToken(newSystemUUID, activityMasterSystem, newSystemUUID);

		logProgress("Security Management", "Setting Security Configurator to Activity Master", progressMonitor);
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setToken(activityMasterToken);

		logProgress("Security Token Service", "Enabling Security System", progressMonitor);
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(true);
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setDoubleCheckDisabled(true);
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createSecurityDefaults(IEnterpriseName<?> enterpriseName, Systems originatingSystem, IActivityMasterProgressMonitor progressMonitor, UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(enterpriseName, originatingSystem, (short) 0, identityToken);

		service.create(UserGroup, originatingSystem, (short) 1,Security, identityToken);
		service.create(User, originatingSystem, (short) 2,Security, identityToken);
		service.create(Guests, originatingSystem, (short) 2,Security, identityToken);
		service.create(Visitors, originatingSystem, (short) 2,Security, identityToken);
		service.create(Registered, originatingSystem, (short) 2,Security, identityToken);
		service.create(Application, originatingSystem, (short) 3,Security, identityToken);
		service.create(UserGroupSecurityTokenClassifications.System, originatingSystem, (short) 4,Security, identityToken);
		service.create(Plugin, originatingSystem, (short) 5,Security, identityToken);

		service.create(Identity, originatingSystem,Security);


		logProgress("Security Token Service", "Security Classifications Installed", 11, progressMonitor);
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	SecurityToken createSecurityTokens(IEnterpriseName<?> enterpriseName,Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		UUID uuid = SecurityTokenSystem.getSystemTokens()
		                               .get(enterprise);

		SecurityTokenService system = GuiceContext.get(SecurityTokenService.class);

		SecurityToken rootToken = system.create(enterpriseName,
		                                        enterprise.getName(), enterprise.getDescription()
		                                                                        .isEmpty() ? "An enterprise-wide project" : enterprise.getDescription(), activityMasterSystem);

		system.grantAccessToToken(rootToken, rootToken, false, false, false, false, activityMasterSystem);

		enterprise.addClassification(EnterpriseIdentity, rootToken.getSecurityToken(), activityMasterSystem, uuid);

		logProgress("Security Token Service", "Enterprise Security Validated", 3, progressMonitor);

		return rootToken;
	}

	@SuppressWarnings("Duplicates")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createGroupsAndFolders(Enterprise enterprise, SecurityToken rootToken, IActivityMasterProgressMonitor progressMonitor)
	{
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);

		SecurityTokenService system = GuiceContext.get(SecurityTokenService.class);
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		SecurityToken everyoneToken = system.create(
				UserGroup,
				Everyone.classificationName(),
				Everyone.classificationDescription(),
				activityMasterSystem);
		SecurityToken everywhereToken = system.create(
				UserGroup,
				Everywhere.classificationName(),
				Everywhere.classificationDescription(),
				activityMasterSystem);
		SecurityToken administratorsToken = system.create(
				UserGroup,
				Administrators.classificationName(),
				Administrators.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsToken = system.create(
				UserGroup,
				Guests.classificationName(),
				Guests.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsVisitorsToken = system.create(
				UserGroup,
				Visitors.classificationName(),
				Visitors.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsRegisteredToken = system.create(
				UserGroup,
				Registered.classificationName(),
				Registered.classificationDescription(),
				activityMasterSystem);

		SecurityToken applicationToken = system.create(
				Application,
				Applications.classificationName(),
				Applications.classificationDescription(),
				activityMasterSystem);
		SecurityToken systemsToken = system.create(
				UserGroupSecurityTokenClassifications.System,
				System.classificationName(),
				System.classificationDescription(),
				activityMasterSystem);

		SecurityToken pluginToken = system.create(
				Plugin,
				Plugins.classificationName(),
				Plugins.classificationDescription(),
				activityMasterSystem);


		SecurityToken activityMasterToken = system.create(
				UserGroupSecurityTokenClassifications.System,
				"Activity Master System", "Defines the activity master as a system", activityMasterSystem);


		logProgress("Security Token Service", "Base Security Tokens", 11, progressMonitor);

		activityMasterSystem.addClassification(SystemsClassifications.SystemIdentity,
		                                       activityMasterToken.getSecurityToken(),
		                                       activityMasterSystem);

		system.link(rootToken, everyoneToken,
		            classificationService.find(UserGroup, enterprise));
		system.link(rootToken, everywhereToken,
		            classificationService.find(UserGroup, enterprise));
		system.link(everyoneToken, administratorsToken,
		            classificationService.find(UserGroup, enterprise));
	/*	link(everyoneToken, usersToken,
		     classificationService.find(UserGroup, SecurityTokenXSecurityToken.class, activityMasterSystem));
*/
		system.link(everyoneToken, usersGuestsToken,
		            classificationService.find(UserGroup, enterprise));
		system.link(usersGuestsToken, usersGuestsRegisteredToken,
		            classificationService.find(UserGroup, enterprise));
		system.link(usersGuestsToken, usersGuestsVisitorsToken,
		            classificationService.find(UserGroup, enterprise));

		system.link(rootToken, applicationToken,
		            classificationService.find(Application, enterprise));
		system.link(rootToken, systemsToken,
		            classificationService.find(UserGroupSecurityTokenClassifications.System, enterprise));
		system.link(rootToken, pluginToken,
		            classificationService.find(Plugin, enterprise));

		system.link(systemsToken, activityMasterToken,
		            classificationService.find(UserGroupSecurityTokenClassifications.System, enterprise));

		logProgress("Security Token Service", "Security Hierarchy Confirmed", 11, progressMonitor);

		//mark folders as unable to be deleted by anyone including administrators
		system.grantAccessToToken(administratorsToken, rootToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, everyoneToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, administratorsToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, applicationToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, everywhereToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, systemsToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(administratorsToken, pluginToken, true, true, false, true, activityMasterSystem);


		//Allow default access to everyone
		system.grantAccessToToken(usersGuestsToken, rootToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, everyoneToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, administratorsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, applicationToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, everywhereToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, usersGuestsToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, systemsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsToken, pluginToken, false, false, false, false, activityMasterSystem);

		//Allow default access to everyone
		system.grantAccessToToken(usersGuestsRegisteredToken, rootToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, everyoneToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, administratorsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, applicationToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, everywhereToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, usersGuestsToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, systemsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsRegisteredToken, pluginToken, false, false, false, false, activityMasterSystem);

		system.grantAccessToToken(usersGuestsVisitorsToken, rootToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, everyoneToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, administratorsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, applicationToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, everywhereToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, usersGuestsToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, systemsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(usersGuestsVisitorsToken, pluginToken, false, false, false, false, activityMasterSystem);

		//Apply default guest access right
		system.grantAccessToToken(everyoneToken, rootToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, everyoneToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, administratorsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, applicationToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, everywhereToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, systemsToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, pluginToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, usersGuestsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everyoneToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem);

		system.grantAccessToToken(everywhereToken, rootToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, everyoneToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, administratorsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, applicationToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, systemsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, everywhereToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, pluginToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, usersGuestsToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem);
		system.grantAccessToToken(everywhereToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem);

		//Apply default application access right
		system.grantAccessToToken(applicationToken, rootToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, everyoneToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, administratorsToken, true, false, false, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, applicationToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, everywhereToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, systemsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, pluginToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(applicationToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem);
		//Apply default systems access right
		system.grantAccessToToken(systemsToken, rootToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, everyoneToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, administratorsToken, true, false, false, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, applicationToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, systemsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, pluginToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem);
		//Apply default plugins access right
		system.grantAccessToToken(pluginToken, rootToken, false, false, false, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, everyoneToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, administratorsToken, true, false, false, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, applicationToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, systemsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, pluginToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, usersGuestsToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem);
		system.grantAccessToToken(pluginToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem);

		logProgress("Security Token Service", "Default Security Confirmed", 37, progressMonitor);
	}

	void applyDefaultsToNewEnterprise(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems system = get(SystemsService.class).getActivityMaster(enterprise);

		logProgress("Security Token Service", "Checking Default Security for all enterprise default items", progressMonitor);

		logProgress("Security Token Service", "Starting basic security checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ActiveFlag(), system, progressMonitor);
		//logProgress("Security Token Service", "Security Tokens securities", 1, progressMonitor);
		//createDefaultSecurity(new SecurityToken(), system, progressMonitor);
		logProgress("Security Token Service", "Systems securities", 1, progressMonitor);
		createDefaultSecurityForTable(new Systems(), system, progressMonitor);
		logProgress("Security Token Service", "System Classifications securities", 1, progressMonitor);
		createDefaultSecurityForTable(new SystemXClassification(), system, progressMonitor);
		logProgress("Security Token Service", "Classification Data Concepts security checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ClassificationDataConcept(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Classifications checks", 1, progressMonitor);
		createDefaultSecurityForTable(new Classification(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Yes No checks", 1, progressMonitor);
		createDefaultSecurityForTable(new YesNo(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Hierarchy checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ClassificationXClassification(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Identification Type checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyIdentificationType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Name Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyNameType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Party Type checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Organic Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyOrganicType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Final Enterprise checks", 1, progressMonitor);
		createDefaultSecurityForTable(new EnterpriseXClassification(), system, progressMonitor);


		createDefaultSecurityForTable(enterprise, system, progressMonitor);
		logProgress("Security Token Service", "Completed Checks", 1, progressMonitor);
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createActivityMasterInvolvedParty(Enterprise enterprise)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		GuiceContext.get(SystemsSystem.class)
		            .createInvolvedPartyForNewSystem(activityMasterSystem);
	}

	void applyDefaultsToNewEnterpriseAfterActivityMaster(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems system = get(SystemsService.class).getActivityMaster(enterprise);
		UUID token = get(SystemsService.class).getSecurityIdentityToken(system);

		logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedParty(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Organic Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyOrganic(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Non Organic checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyNonOrganic(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyIdentificationType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Classifications Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXClassification(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Name Type Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyNameType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Type Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Events checks", 1, progressMonitor);
		createDefaultSecurityForTable(new EventType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Resource Item Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ResourceItemType(), system, progressMonitor);
	}

	void createDefaultSecurityForTable(WarehouseCoreTable<?, ?, ?, ?> table, Systems system, IActivityMasterProgressMonitor progressMonitor,UUID...identityToken)
	{
		for (WarehouseCoreTable next : table.builder()
		                                    .withEnterprise(system.getEnterpriseID())
		                                    .whereNoSecurityIsApplied(system.getEnterpriseID())
		                                    .inDateRange()
		                                    .getAll())
		{
			/*logProgress("Security Token Service", "Checking - " +
			                                      next.getClass()
			                                          .getSimpleName(), progressMonitor);
			*/next.createDefaultSecurity(system,identityToken);
		}

	}

	@Override
	public int totalTasks()
	{
		return 77;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 23;
	}


	@Override
	public void postUpdate(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Security Tokens System",
		                                        "The system for managing Security Tokens", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<Enterprise, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
