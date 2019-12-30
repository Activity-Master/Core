package com.guicedee.activitymaster.core;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.implementations.EnterpriseService;
import com.guicedee.activitymaster.core.implementations.InvolvedPartyService;
import com.guicedee.activitymaster.core.implementations.SecurityTokenService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.IProgressable;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IActivityMasterService;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications;
import com.guicedee.activitymaster.core.services.types.IPTypes;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import com.guicedee.logger.LogFactory;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.logging.Level;

import static com.guicedee.activitymaster.core.services.types.NameTypes.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@Singleton
public class ActivityMasterService
		implements IProgressable, IActivityMasterService
{
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 6000)
	public IEnterprise<?> startNewEnterprise(IEnterpriseName<?> enterpriseName,
	                                         @NotNull String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{

		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		get(ActivityMasterConfiguration.class)
				.setAsyncEnabled(false);
		get(ActivityMasterConfiguration.class)
				.setEnterpriseName(enterpriseName);
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));

		int totalTasks = allSystems.stream()
		                           .mapToInt(IActivityMasterSystem::totalTasks)
		                           .sum() + 1;

		logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks, progressMonitor);
		Enterprise enterprise = get(EnterpriseService.class).create(enterpriseName.classificationName(), enterpriseName.classificationDescription(), progressMonitor);
		get(ActivityMasterConfiguration.class)
				.getEnterpriseName()
				.setEnterprise(enterprise);

		runUpdatesOnEnterprise(enterpriseName, progressMonitor);

		//Create Involved Party for Enterprise
		createAdminAndCreatorUserForEnterprise(enterpriseName, adminUserName, adminPassword, progressMonitor);

		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(true);
		return enterprise;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	protected IInvolvedParty<?> createAdminAndCreatorUserForEnterprise(IEnterpriseName<?> enterpriseName, String adminUserName,
	                                                                   @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		IEnterprise<?> enterprise = get(IEnterpriseService.class)
				                            .getEnterprise(enterpriseName);

		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1, progressMonitor);

		ISystems<?> activityMasterSystem = get(SystemsService.class).getActivityMaster(enterprise);
		UUID token = get(SystemsService.class).getSecurityIdentityToken(activityMasterSystem);
		SecurityToken administratorsGroup = (SecurityToken) get(SecurityTokenService.class).getAdministratorsFolder(enterprise);

		InvolvedPartyService service = get(InvolvedPartyService.class);

		Pair<IIdentificationType<?>, String> pair = new Pair<>(
				IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
				new Passwords().integerEncrypt(adminUserName.getBytes()));
		Optional<InvolvedParty> exists = new InvolvedParty().builder()
		                                                    .findByIdentificationType(enterprise,
		                                                                              IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
		                                                                              new Passwords().integerEncrypt(adminUserName.getBytes()))
		                                                    .get();

		IInvolvedParty<?> administratorUser;
		if (exists.isEmpty())
		{
			IInvolvedParty<?> adminUser = service.create(activityMasterSystem, pair, true);

			adminUser.addOrReuse(IdentificationTypes.IdentificationTypeUserName, new Passwords().integerEncrypt(adminUserName.getBytes()), activityMasterSystem, token);

			adminUser.addOrReuse(IPTypes.TypeIndividual, "Creator Individual", activityMasterSystem, token);
			adminUser.addOrReuse(PreferredNameType, "Enterprise Creator", activityMasterSystem, token);
			adminUser.addOrReuse(FirstNameType, "Administrator", activityMasterSystem, token);

			SecurityToken myToken = (SecurityToken) get(SecurityTokenService.class).create(SecurityTokenClassifications.Identity,
			                                                                               adminUserName,
			                                                                               "The creator of the enterprise", activityMasterSystem, administratorsGroup, token);

			adminUser.addOrReuse(IdentificationTypes.IdentificationTypeEnterpriseCreatorRole, new Passwords().integerEncrypt(adminUserName.getBytes()), activityMasterSystem,
			                     token);
			adminUser.addOrReuse(IdentificationTypes.IdentificationTypeUUID, myToken.getSecurityToken(), activityMasterSystem, token);

			service.addUpdateUsernamePassword(null, adminUserName, adminPassword, adminUser, activityMasterSystem, token);
			adminUser.createDefaultSecurity(activityMasterSystem, token);
			administratorUser = adminUser;
		}
		else
		{
			administratorUser = exists.get();
		}
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(true);
		return administratorUser;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void runUpdatesOnEnterprise(@NotNull IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);

		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
		IEnterprise<?> enterprise = get(IEnterpriseService.class)
				                            .getEnterprise(enterpriseName);
		//Find all systems required for first time installation/updates
		int total = allSystems.size();
		int current = 0;
		//progressMonitor.setCurrentTask(0);
		//progressMonitor.setTotalTasks(total);
		for (Iterator<IActivityMasterSystem> iterator = allSystems.iterator(); iterator.hasNext(); )
		{
			IActivityMasterSystem allSystem = iterator.next();
			logProgress("Running System", allSystem.getClass()
			                                       .getSimpleName(), progressMonitor);
			String nameC = cleanName(allSystem.getClass()
			                                  .getSimpleName());
			allSystem.createDefaults(enterprise, progressMonitor);
			logProgress("Completed with System", nameC, 1, progressMonitor);
		}

		logProgress("System Configuration", "Starting System Startups", 1, progressMonitor);
		loadSystems(enterpriseName, progressMonitor);
		logProgress("System Configuration", "Enterprise All Updates. Updating Systems", 1, progressMonitor);
		loadUpdates(enterpriseName, progressMonitor);
		logProgress("System Configuration", "Done", 1, progressMonitor);
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(true);
	}

	public void runScript(String script)
	{
		javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);
		try (java.sql.Connection c = ds.getConnection();
		     java.sql.Statement st = c.createStatement())
		{
			st.executeUpdate(script);
		}
		catch (java.sql.SQLException e)
		{
			LogFactory.getLog("ActivityMasterService")
			          .log(Level.SEVERE, "Unable to execute updates to hierarchy", e);
		}
	}

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 5000)
	public void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));

		IEnterprise<?> enterprise = get(IEnterpriseService.class)
				                            .getEnterprise(enterpriseName);
		for (IActivityMasterSystem allSystem : allSystems)
		{
			logProgress("System Loading", "Starting up system " + allSystem.getClass()
			                                                               .getName(), 1, progressMonitor);
			allSystem.postStartup(enterprise, progressMonitor);
		}
		ActivityMasterConfiguration.get()
		                           .setDoubleCheckDisabled(true);
	}

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 5000)
	public void loadUpdates(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
		IEnterprise<?> enterprise = get(IEnterpriseService.class)
				                            .getEnterprise(enterpriseName);
		if (progressMonitor != null)
		{
			progressMonitor.setTotalTasks(allSystems.size() * 3);
		}

		for (IActivityMasterSystem<?> allSystem : allSystems)
		{
			logProgress("System Updating", "Updating system " + allSystem.getClass()
			                                                             .getName(), 1, progressMonitor);
			allSystem.loadUpdates(enterprise, progressMonitor);
		}
		if (progressMonitor != null)
		{
			int cur = progressMonitor.getTotalTasks() - progressMonitor.getCurrentTask();
			logProgress("System Updating", "Completed Updating Systems", cur, progressMonitor);
		}
		ActivityMasterConfiguration.get()
		                           .setDoubleCheckDisabled(true);
	}
}
