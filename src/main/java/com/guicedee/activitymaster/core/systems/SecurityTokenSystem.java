package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.SystemsService;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.db.entities.events.EventType;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
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
import com.guicedee.activitymaster.core.services.system.*;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.UUID;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.system.ISecurityTokenService.*;
import static com.guicedee.guicedinjection.GuiceContext.*;


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
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, getSystemName(), getSystemDescription());
	}
	
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Security Token Service", "Starting Security Structure Checks/Install", progressMonitor);
		
		createSecurityDefaults(enterprise, activityMasterSystem, progressMonitor);
		SecurityToken rootToken = createSecurityTokens(enterprise, enterprise, progressMonitor);
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
	}

	void createSecurityDefaults(IEnterpriseName<?> enterpriseName, ISystems<?> system, IActivityMasterProgressMonitor progressMonitor, UUID... identityToken)
	{
		classificationService.create(enterpriseName, system,  0, identityToken);
		classificationService.create(SecurityTokenClassifications.UserGroup, system,  1, Classifications.Security, identityToken);
		classificationService.create(SecurityTokenClassifications.User, system,  2, Classifications.Security, identityToken);
		classificationService.create(SecurityTokenClassifications.Guests, system,  2, Classifications.Security, identityToken);
		classificationService.create(SecurityTokenClassifications.Visitors, system,  2, Classifications.Security, identityToken);
		classificationService.create(SecurityTokenClassifications.Registered, system,  2, Classifications.Security, identityToken);
		classificationService.create(SecurityTokenClassifications.Application, system,  3, Classifications.Security, identityToken);
		classificationService.create(UserGroupSecurityTokenClassifications.System, system,  4, Classifications.Security, identityToken);
		classificationService.create(SecurityTokenClassifications.Plugin, system,  5, Classifications.Security, identityToken);

		classificationService.create(SecurityTokenClassifications.Identity, system, Classifications.Security);

		logProgress("Security Token Service", "Security Classifications Installed", 11, progressMonitor);
	}

	SecurityToken createSecurityTokens(IEnterpriseName<?> enterpriseName, IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		UUID uuid = getSystemToken(enterprise);
		
		SecurityToken rootToken = (SecurityToken) securityTokenService.create(enterpriseName,
		                                                        enterprise.getName(), enterprise.getDescription()
		                                                                                        .isEmpty() ? "An enterprise-wide project" : enterprise.getDescription(),
		                                                        activityMasterSystem);

		securityTokenService.grantAccessToToken(rootToken, rootToken, false, false, false, false, activityMasterSystem);

		enterprise.addOrUpdate(EnterpriseClassifications.EnterpriseIdentity,null, rootToken.getSecurityToken(), activityMasterSystem, uuid);

		logProgress("Security Token Service", "Enterprise Security Validated", 3, progressMonitor);

		return rootToken;
	}

	@SuppressWarnings("Duplicates")
	void createGroupsAndFolders(IEnterprise<?> enterprise, SecurityToken rootToken, IActivityMasterProgressMonitor progressMonitor)
	{
		SecurityToken everyoneToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup,
				UserGroupSecurityTokenClassifications.Everyone.classificationName(),
				UserGroupSecurityTokenClassifications.Everyone.classificationDescription(),
				activityMasterSystem);
		SecurityToken everywhereToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup,
				UserGroupSecurityTokenClassifications.Everywhere.classificationName(),
				UserGroupSecurityTokenClassifications.Everywhere.classificationDescription(),
				activityMasterSystem);
		SecurityToken administratorsToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup,
				UserGroupSecurityTokenClassifications.Administrators.classificationName(),
				UserGroupSecurityTokenClassifications.Administrators.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup,
				SecurityTokenClassifications.Guests.classificationName(),
				SecurityTokenClassifications.Guests.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsVisitorsToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup,
				SecurityTokenClassifications.Visitors.classificationName(),
				SecurityTokenClassifications.Visitors.classificationDescription(),
				activityMasterSystem);
		SecurityToken usersGuestsRegisteredToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.UserGroup,
				SecurityTokenClassifications.Registered.classificationName(),
				SecurityTokenClassifications.Registered.classificationDescription(),
				activityMasterSystem);

		SecurityToken applicationToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.Application,
				UserGroupSecurityTokenClassifications.Applications.classificationName(),
				UserGroupSecurityTokenClassifications.Applications.classificationDescription(),
				activityMasterSystem);
		SecurityToken systemsToken = (SecurityToken) securityTokenService.create(
				UserGroupSecurityTokenClassifications.System,
				UserGroupSecurityTokenClassifications.System.classificationName(),
				UserGroupSecurityTokenClassifications.System.classificationDescription(),
				activityMasterSystem);

		SecurityToken pluginToken = (SecurityToken) securityTokenService.create(
				SecurityTokenClassifications.Plugin,
				UserGroupSecurityTokenClassifications.Plugins.classificationName(),
				UserGroupSecurityTokenClassifications.Plugins.classificationDescription(),
				activityMasterSystem);

		SecurityToken activityMasterToken = (SecurityToken) securityTokenService.create(
				UserGroupSecurityTokenClassifications.System,
				"Activity Master System", "Defines the activity master as a system", activityMasterSystem);

		logProgress("Security Token Service", "Base Security Tokens", 11, progressMonitor);

		activityMasterSystem.addOrReuse(SystemsClassifications.SystemIdentity,
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

		logProgress("Security Token Service", "Security Hierarchy Confirmed", 11, progressMonitor);

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

		logProgress("Security Token Service", "Default Security Confirmed", 37, progressMonitor);
	}

	void applyDefaultsToNewEnterprise(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Security Token Service", "Checking Default Security for all enterprise default items", progressMonitor);

		logProgress("Security Token Service", "Starting basic security checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ActiveFlag(), activityMasterSystem, progressMonitor);
		//logProgress("Security Token Service", "Security Tokens securities", 1, progressMonitor);
		//createDefaultSecurity(new SecurityToken(), system, progressMonitor);
		logProgress("Security Token Service", "Systems securities", 1, progressMonitor);
		createDefaultSecurityForTable(new Systems(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "System Classifications securities", 1, progressMonitor);
		createDefaultSecurityForTable(new SystemXClassification(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Classification Data Concepts security checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ClassificationDataConcept(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Classifications checks", 1, progressMonitor);
		createDefaultSecurityForTable(new Classification(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Hierarchy checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ClassificationXClassification(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Identification Type checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyIdentificationType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Name Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyNameType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Party Type checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Organic Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyOrganicType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Final Enterprise checks", 1, progressMonitor);
		createDefaultSecurityForTable(new EnterpriseXClassification(), activityMasterSystem, progressMonitor);

		createDefaultSecurityForTable((WarehouseCoreTable<?, ?, ?, ?>) enterprise, activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Completed Checks", 1, progressMonitor);
	}

	void createActivityMasterInvolvedParty(IEnterprise<?> enterprise)
	{
		get(SystemsSystem.class)
				.createInvolvedPartyForNewSystem(activityMasterSystem);
	}

	void applyDefaultsToNewEnterpriseAfterActivityMaster(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedParty(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Organic Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyOrganic(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Non Organic checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyNonOrganic(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyIdentificationType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Classifications Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXClassification(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Name Type Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyNameType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Involved Party Type Relationship checks", 1, progressMonitor);
		createDefaultSecurityForTable(new InvolvedPartyXInvolvedPartyType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Events checks", 1, progressMonitor);
		createDefaultSecurityForTable(new EventType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Resource Item Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ResourceItemType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Arrangement Types checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting Arrangement checks", 1, progressMonitor);
		createDefaultSecurityForTable(new Arrangement(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXType checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXArrangementType(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXClassification checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXClassification(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXResourceItem checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXResourceItem(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXInvolvedParty checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXInvolvedParty(), activityMasterSystem, progressMonitor);
		logProgress("Security Token Service", "Starting ArrangementXProduct checks", 1, progressMonitor);
		createDefaultSecurityForTable(new ArrangementXProduct(), activityMasterSystem, progressMonitor);
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
