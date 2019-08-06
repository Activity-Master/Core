package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.implementations.EnterpriseService;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.implementations.SecurityTokenService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.IProgressable;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IInvolvedParty;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.security.Passwords;
import com.armineasy.activitymaster.activitymaster.services.system.IActivityMasterService;
import com.armineasy.activitymaster.activitymaster.services.system.IEnterpriseService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.IDefaultService;
import com.jwebmp.guicedinjection.pairing.Pair;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;
import com.jwebmp.logger.LogFactory;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.logging.Level;

import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IPTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.NameTypes.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Singleton
public class ActivityMasterService
		implements IProgressable, IActivityMasterService
{
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 6000)
	public IEnterprise<?> startNewEnterprise(IEnterpriseName<?> enterpriseName,
	                                                  @NotNull String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{

		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setAsyncEnabled(false);
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setEnterpriseName(enterpriseName);
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));

		int totalTasks = allSystems.stream()
		                           .mapToInt(IActivityMasterSystem::totalTasks)
		                           .sum() + 1;

		logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks, progressMonitor);
		Enterprise enterprise = get(EnterpriseService.class).create(enterpriseName.classificationName(), enterpriseName.classificationDescription(), progressMonitor);
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .getEnterpriseName()
		            .setEnterprise(enterprise);

		runUpdatesOnEnterprise(enterpriseName, progressMonitor);

		//Create Involved Party for Enterprise
		createAdminAndCreatorUserForEnterprise(enterpriseName, adminUserName, adminPassword, progressMonitor);

		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(true);
		return enterprise;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	protected IInvolvedParty<?> createAdminAndCreatorUserForEnterprise(IEnterpriseName<?> enterpriseName, String adminUserName,
	                                                               @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);
		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .getEnterprise(enterpriseName);

		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1, progressMonitor);

		ISystems<?> activityMasterSystem = get(SystemsService.class).getActivityMaster(enterprise);
		UUID token = get(SystemsService.class).getSecurityIdentityToken(activityMasterSystem);
		SecurityToken administratorsGroup = (SecurityToken) get(SecurityTokenService.class).getAdministratorsFolder(enterprise);

		InvolvedPartyService service = get(InvolvedPartyService.class);

		Pair<IIdentificationType<?>, String> pair = new Pair<>(
				IdentificationTypeEnterpriseCreatorRole,
				new Passwords().integerEncrypt(adminUserName.getBytes()));
		Optional<InvolvedParty> exists = new InvolvedParty().builder()
		                                                    .findByIdentificationType(enterprise,
		                                                                              IdentificationTypeEnterpriseCreatorRole,
		                                                                              new Passwords().integerEncrypt(adminUserName.getBytes()))
		                                                    .get();

		IInvolvedParty<?> administratorUser;
		if (exists.isEmpty())
		{
			IInvolvedParty<?> adminUser = service.create(activityMasterSystem, pair, true);

			adminUser.addOrReuse(IdentificationTypeUserName, new Passwords().integerEncrypt(adminUserName.getBytes()),activityMasterSystem, token);

			adminUser.addOrReuse(TypeIndividual, "Artificial Individual",activityMasterSystem, token);
			adminUser.addOrReuse(PreferredNameType,  "Enterprise Creator",activityMasterSystem, token);
			adminUser.addOrReuse(FirstNameType, "Administrator",activityMasterSystem, token);

			SecurityToken myToken = (SecurityToken) get(SecurityTokenService.class).create(Identity,
			                                                                               adminUserName,
			                                                                               "The creator of the enterprise", activityMasterSystem, administratorsGroup, token);

			adminUser.addOrReuse(IdentificationTypeEnterpriseCreatorRole,new Passwords().integerEncrypt(adminUserName.getBytes()), activityMasterSystem,  token);
			adminUser.addOrReuse(IdentificationTypeUUID, myToken.getSecurityToken(),activityMasterSystem, token);

			service.addUpdateUsernamePassword(null, adminUserName, adminPassword, adminUser, activityMasterSystem, token);
			adminUser.createDefaultSecurity(activityMasterSystem, token);
			administratorUser = adminUser;
		}
		else
		{
			administratorUser = exists.get();
		}
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(true);
		return administratorUser;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void runUpdatesOnEnterprise(@NotNull IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);

		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
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
			                                       .getSimpleName(),progressMonitor);
			String nameC = cleanName(allSystem.getClass()
			                                  .getSimpleName());
			allSystem.createDefaults(enterprise, progressMonitor);
			logProgress("Completed with System", nameC,1, progressMonitor);
		}

		loadSystems(enterpriseName, progressMonitor);

		GuiceContext.get(ActivityMasterConfiguration.class)
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
			LogFactory.getLog("ActivityMasterService").log(Level.SEVERE, "Unable to execute updates to hierarchy", e);
		}
	}

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,timeout = 5000)
	public void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));

		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .getEnterprise(enterpriseName);
		for (IActivityMasterSystem allSystem : allSystems)
		{
			logProgress("System Loading", "Starting up system " + allSystem.getClass()
			                                                                         .getName(),1, progressMonitor);
			allSystem.postUpdate(enterprise, progressMonitor);
		}
		ActivityMasterConfiguration.get()
		                           .setDoubleCheckDisabled(true);
	}
}
