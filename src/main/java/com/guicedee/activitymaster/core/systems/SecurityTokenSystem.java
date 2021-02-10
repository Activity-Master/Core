package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassification;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.db.entities.events.EventType;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SecurityTokenService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.core.services.classifications.systems.SystemsClassifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.UUID;
import java.util.logging.Logger;

import static com.guicedee.guicedinjection.GuiceContext.*;

@Singleton
public class SecurityTokenSystem
		extends ActivityMasterDefaultSystem<SecurityTokenSystem>
		implements IActivityMasterSystem<SecurityTokenSystem>
{
	private static final Logger log = Logger.getLogger(SecurityTokenSystem.class.getName());

	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Security Token Service", "Starting Security Structure Checks/Install", progressMonitor);
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		IEnterpriseName<?> enterpriseName = GuiceContext.get(ActivityMasterConfiguration.class)
		                                                .getEnterpriseName();
		if (enterpriseName == null)
		{
			throw new ActivityMasterException("IEnterpriseName is not set for security root. Make sure to set it in ActivityMasterConfiguration from a created type");
		}

		createSecurityDefaults(GuiceContext.get(ActivityMasterConfiguration.class)
		                                   .getEnterpriseName(), activityMasterSystem, progressMonitor);
		SecurityToken rootToken = createSecurityTokens(enterpriseName, enterprise, progressMonitor);
		createGroupsAndFolders(enterprise, rootToken, progressMonitor);
		applyDefaultsToNewEnterprise(enterprise, progressMonitor);
		createActivityMasterInvolvedParty(enterprise);
		applyDefaultsToNewEnterpriseAfterActivityMaster(enterprise, progressMonitor);

		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(activityMasterSystem);

		SecurityToken activityMasterToken = (SecurityToken) GuiceContext.get(SystemsService.class)
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

	void createSecurityDefaults(IEnterpriseName<?> enterpriseName, ISystems<?> system, IActivityMasterProgressMonitor progressMonitor, UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(enterpriseName, system,  0, identityToken);
		service.create(SecurityTokenClassifications.UserGroup, system,  1, Classifications.Security, identityToken);
		service.create(SecurityTokenClassifications.User, system,  2, Classifications.Security, identityToken);
		service.create(SecurityTokenClassifications.Guests, system,  2, Classifications.Security, identityToken);
		service.create(SecurityTokenClassifications.Visitors, system,  2, Classifications.Security, identityToken);
		service.create(SecurityTokenClassifications.Registered, system,  2, Classifications.Security, identityToken);
		service.create(SecurityTokenClassifications.Application, system,  3, Classifications.Security, identityToken);
		service.create(UserGroupSecurityTokenClassifications.System, system,  4, Classifications.Security, identityToken);
		service.create(SecurityTokenClassifications.Plugin, system,  5, Classifications.Security, identityToken);

		service.create(SecurityTokenClassifications.Identity, system, Classifications.Security);

		logProgress("Security Token Service", "Security Classifications Installed", 11, progressMonitor);
	}

	SecurityToken createSecurityTokens(IEnterpriseName<?> enterpriseName, IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		UUID uuid = getSystemToken(enterprise);

		SecurityTokenService system = GuiceContext.get(SecurityTokenService.class);

		SecurityToken rootToken = (SecurityToken) system.create(enterpriseName,
		                                                        enterprise.getName(), enterprise.getDescription()
		                                                                                        .isEmpty() ? "An enterprise-wide project" : enterprise.getDescription(),
		                                                        activityMasterSystem);

		system.grantAccessToToken(rootToken, rootToken, false, false, false, false, activityMasterSystem);

		enterprise.addOrUpdate(EnterpriseClassifications.EnterpriseIdentity,null, rootToken.getSecurityToken(), activityMasterSystem, uuid);

		logProgress("Security Token Service", "Enterprise Security Validated", 3, progressMonitor);

		return rootToken;
	}

	@SuppressWarnings("Duplicates")
	void createGroupsAndFolders(IEnterprise<?> enterprise, SecurityToken rootToken, IActivityMasterProgressMonitor progressMonitor)
	{
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);

		SecurityTokenService systemService = GuiceContext.get(SecurityTokenService.class);
		ISystems<?> system = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);

		SecurityToken everyoneToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.UserGroup,
				UserGroupSecurityTokenClassifications.Everyone.classificationName(),
				UserGroupSecurityTokenClassifications.Everyone.classificationDescription(),
				system);
		SecurityToken everywhereToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.UserGroup,
				UserGroupSecurityTokenClassifications.Everywhere.classificationName(),
				UserGroupSecurityTokenClassifications.Everywhere.classificationDescription(),
				system);
		SecurityToken administratorsToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.UserGroup,
				UserGroupSecurityTokenClassifications.Administrators.classificationName(),
				UserGroupSecurityTokenClassifications.Administrators.classificationDescription(),
				system);
		SecurityToken usersGuestsToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.UserGroup,
				SecurityTokenClassifications.Guests.classificationName(),
				SecurityTokenClassifications.Guests.classificationDescription(),
				system);
		SecurityToken usersGuestsVisitorsToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.UserGroup,
				SecurityTokenClassifications.Visitors.classificationName(),
				SecurityTokenClassifications.Visitors.classificationDescription(),
				system);
		SecurityToken usersGuestsRegisteredToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.UserGroup,
				SecurityTokenClassifications.Registered.classificationName(),
				SecurityTokenClassifications.Registered.classificationDescription(),
				system);

		SecurityToken applicationToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.Application,
				UserGroupSecurityTokenClassifications.Applications.classificationName(),
				UserGroupSecurityTokenClassifications.Applications.classificationDescription(),
				system);
		SecurityToken systemsToken = (SecurityToken) systemService.create(
				UserGroupSecurityTokenClassifications.System,
				UserGroupSecurityTokenClassifications.System.classificationName(),
				UserGroupSecurityTokenClassifications.System.classificationDescription(),
				system);

		SecurityToken pluginToken = (SecurityToken) systemService.create(
				SecurityTokenClassifications.Plugin,
				UserGroupSecurityTokenClassifications.Plugins.classificationName(),
				UserGroupSecurityTokenClassifications.Plugins.classificationDescription(),
				system);

		SecurityToken activityMasterToken = (SecurityToken) systemService.create(
				UserGroupSecurityTokenClassifications.System,
				"Activity Master System", "Defines the activity master as a system", system);

		logProgress("Security Token Service", "Base Security Tokens", 11, progressMonitor);

		system.addOrReuse(SystemsClassifications.SystemIdentity,
		                                activityMasterToken.getSecurityToken(),
		                                system);

		systemService.link(rootToken, everyoneToken,
		            classificationService.find(SecurityTokenClassifications.UserGroup, system));
		systemService.link(rootToken, everywhereToken,
		            classificationService.find(SecurityTokenClassifications.UserGroup, system));
		systemService.link(everyoneToken, administratorsToken,
		            classificationService.find(SecurityTokenClassifications.UserGroup, system));
	/*	link(everyoneToken, usersToken,
		     classificationService.find(UserGroup, SecurityTokenXSecurityToken.class, activityMasterSystem));
*/
		systemService.link(everyoneToken, usersGuestsToken,
		            classificationService.find(SecurityTokenClassifications.UserGroup, system));
		systemService.link(usersGuestsToken, usersGuestsRegisteredToken,
		            classificationService.find(SecurityTokenClassifications.UserGroup, system));
		systemService.link(usersGuestsToken, usersGuestsVisitorsToken,
		            classificationService.find(SecurityTokenClassifications.UserGroup, system));

		systemService.link(rootToken, applicationToken,
		            classificationService.find(SecurityTokenClassifications.Application, system));
		systemService.link(rootToken, systemsToken,
		            classificationService.find(UserGroupSecurityTokenClassifications.System, system));
		systemService.link(rootToken, pluginToken,
		            classificationService.find(SecurityTokenClassifications.Plugin, system));

		systemService.link(systemsToken, activityMasterToken,
		            classificationService.find(UserGroupSecurityTokenClassifications.System, system));

		logProgress("Security Token Service", "Security Hierarchy Confirmed", 11, progressMonitor);

		//mark folders as unable to be deleted by anyone including administrators
		systemService.grantAccessToToken(administratorsToken, rootToken, true, true, true, true, system);
		systemService.grantAccessToToken(administratorsToken, everyoneToken, true, true, false, true, system);
		systemService.grantAccessToToken(administratorsToken, administratorsToken, true, true, false, true, system);
		systemService.grantAccessToToken(administratorsToken, applicationToken, true, true, false, true, system);
		systemService.grantAccessToToken(administratorsToken, everywhereToken, true, true, true, true, system);
		systemService.grantAccessToToken(administratorsToken, usersGuestsToken, true, true, true, true, system);
		systemService.grantAccessToToken(administratorsToken, systemsToken, true, true, false, true, system);
		systemService.grantAccessToToken(administratorsToken, pluginToken, true, true, false, true, system);

		//Allow default access to everyone
		systemService.grantAccessToToken(usersGuestsToken, rootToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsToken, everyoneToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsToken, administratorsToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsToken, applicationToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsToken, everywhereToken, false, false, false, true, system);
		systemService.grantAccessToToken(usersGuestsToken, usersGuestsToken, false, false, false, true, system);
		systemService.grantAccessToToken(usersGuestsToken, systemsToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsToken, pluginToken, false, false, false, false, system);

		//Allow default access to everyone
		systemService.grantAccessToToken(usersGuestsRegisteredToken, rootToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, everyoneToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, administratorsToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, applicationToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, everywhereToken, false, false, false, true, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, usersGuestsToken, false, false, false, true, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, systemsToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsRegisteredToken, pluginToken, false, false, false, false, system);

		systemService.grantAccessToToken(usersGuestsVisitorsToken, rootToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, everyoneToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, administratorsToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, applicationToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, everywhereToken, false, false, false, true, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, usersGuestsToken, false, false, false, true, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, systemsToken, false, false, false, false, system);
		systemService.grantAccessToToken(usersGuestsVisitorsToken, pluginToken, false, false, false, false, system);

		//Apply default guest access right
		systemService.grantAccessToToken(everyoneToken, rootToken, false, false, false, true, system);
		systemService.grantAccessToToken(everyoneToken, everyoneToken, false, false, false, false, system);
		systemService.grantAccessToToken(everyoneToken, administratorsToken, false, false, false, false, system);
		systemService.grantAccessToToken(everyoneToken, applicationToken, false, false, false, true, system);
		systemService.grantAccessToToken(everyoneToken, everywhereToken, true, true, false, true, system);
		systemService.grantAccessToToken(everyoneToken, systemsToken, false, false, false, true, system);
		systemService.grantAccessToToken(everyoneToken, pluginToken, false, false, false, true, system);
		systemService.grantAccessToToken(everyoneToken, usersGuestsToken, false, false, false, false, system);
		systemService.grantAccessToToken(everyoneToken, usersGuestsRegisteredToken, false, false, false, false, system);
		systemService.grantAccessToToken(everyoneToken, usersGuestsVisitorsToken, false, false, false, false, system);

		systemService.grantAccessToToken(everywhereToken, rootToken, false, false, false, true, system);
		systemService.grantAccessToToken(everywhereToken, everyoneToken, false, false, false, true, system);
		systemService.grantAccessToToken(everywhereToken, administratorsToken, false, false, false, false, system);
		systemService.grantAccessToToken(everywhereToken, applicationToken, false, false, false, false, system);
		systemService.grantAccessToToken(everywhereToken, systemsToken, false, false, false, false, system);
		systemService.grantAccessToToken(everywhereToken, everywhereToken, false, false, false, true, system);
		systemService.grantAccessToToken(everywhereToken, pluginToken, false, false, false, false, system);
		systemService.grantAccessToToken(everywhereToken, usersGuestsToken, false, false, false, false, system);
		systemService.grantAccessToToken(everywhereToken, usersGuestsRegisteredToken, false, false, false, false, system);
		systemService.grantAccessToToken(everywhereToken, usersGuestsVisitorsToken, false, false, false, false, system);

		//Apply default application access right
		systemService.grantAccessToToken(applicationToken, rootToken, false, false, false, true, system);
		systemService.grantAccessToToken(applicationToken, everyoneToken, true, true, false, true, system);
		systemService.grantAccessToToken(applicationToken, administratorsToken, true, false, false, true, system);
		systemService.grantAccessToToken(applicationToken, applicationToken, true, true, false, true, system);
		systemService.grantAccessToToken(applicationToken, everywhereToken, true, true, false, true, system);
		systemService.grantAccessToToken(applicationToken, systemsToken, true, true, true, true, system);
		systemService.grantAccessToToken(applicationToken, pluginToken, true, true, true, true, system);
		systemService.grantAccessToToken(applicationToken, usersGuestsToken, true, true, true, true, system);
		systemService.grantAccessToToken(applicationToken, usersGuestsRegisteredToken, true, true, true, true, system);
		systemService.grantAccessToToken(applicationToken, usersGuestsVisitorsToken, true, true, true, true, system);
		//Apply default systems access right
		systemService.grantAccessToToken(systemsToken, rootToken, false, false, false, true, system);
		systemService.grantAccessToToken(systemsToken, everyoneToken, true, true, false, true, system);
		systemService.grantAccessToToken(systemsToken, administratorsToken, true, false, false, true, system);
		systemService.grantAccessToToken(systemsToken, applicationToken, true, true, false, true, system);
		systemService.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, system);
		systemService.grantAccessToToken(systemsToken, systemsToken, true, true, true, true, system);
		systemService.grantAccessToToken(systemsToken, pluginToken, true, true, true, true, system);
		systemService.grantAccessToToken(systemsToken, usersGuestsToken, true, true, true, true, system);
		systemService.grantAccessToToken(systemsToken, usersGuestsRegisteredToken, true, true, true, true, system);
		systemService.grantAccessToToken(systemsToken, usersGuestsVisitorsToken, true, true, true, true, system);
		//Apply default plugins access right
		systemService.grantAccessToToken(pluginToken, rootToken, false, false, false, true, system);
		systemService.grantAccessToToken(pluginToken, everyoneToken, true, true, false, true, system);
		systemService.grantAccessToToken(pluginToken, administratorsToken, true, false, false, true, system);
		systemService.grantAccessToToken(pluginToken, applicationToken, true, true, false, true, system);
		systemService.grantAccessToToken(systemsToken, everywhereToken, true, true, false, true, system);
		systemService.grantAccessToToken(pluginToken, systemsToken, true, true, true, true, system);
		systemService.grantAccessToToken(pluginToken, pluginToken, true, true, true, true, system);
		systemService.grantAccessToToken(pluginToken, usersGuestsToken, true, true, true, true, system);
		systemService.grantAccessToToken(pluginToken, usersGuestsRegisteredToken, true, true, true, true, system);
		systemService.grantAccessToToken(pluginToken, usersGuestsVisitorsToken, true, true, true, true, system);

		logProgress("Security Token Service", "Default Security Confirmed", 37, progressMonitor);
	}

	void applyDefaultsToNewEnterprise(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> system = get(SystemsService.class).getActivityMaster(enterprise);

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

		createDefaultSecurityForTable((WarehouseCoreTable<?, ?, ?, ?>) enterprise, system, progressMonitor);
		logProgress("Security Token Service", "Completed Checks", 1, progressMonitor);
	}

	void createActivityMasterInvolvedParty(IEnterprise<?> enterprise)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		get(SystemsSystem.class)
				.createInvolvedPartyForNewSystem(activityMasterSystem);
	}

	void applyDefaultsToNewEnterpriseAfterActivityMaster(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> system = get(SystemsService.class).getActivityMaster(enterprise);
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
		logProgress("Security Token Service", "Starting Arrangement Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting Arrangement checks", 1, progressMonitor);
		createDefaultSecurityForTable(new Arrangement(), system, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXType checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXArrangementType(), system, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXClassification checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXClassification(), system, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXResourceItem checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXResourceItem(), system, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXInvolvedParty checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXInvolvedParty(), system, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXProduct checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXProduct(), system, progressMonitor);
	}

	void createDefaultSecurityForTable(WarehouseCoreTable<?, ?, ?, ?> table, ISystems<?> system, IActivityMasterProgressMonitor progressMonitor, UUID... identityToken)
	{
		for (WarehouseCoreTable next : table.builder()
		                                    .withEnterprise(system.getEnterpriseID())
		                                    .whereNoSecurityIsApplied()
		                                    .inDateRange()
		                                    .getAll())
		{
			logProgress("Security Token Service", "Checking - " +
			                                      next.getClass()
			                                          .getSimpleName(),0, progressMonitor);
			next.createDefaultSecurity(system, identityToken);
		}
	}

	@Override
	public int totalTasks()
	{
		return 190;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 23;
	}

	@Override
	public String getSystemName()
	{
		return "Security Tokens System";
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing Security Tokens";
	}
}
