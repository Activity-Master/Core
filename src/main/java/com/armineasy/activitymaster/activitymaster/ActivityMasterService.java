package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.EnterpriseService;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.implementations.SecurityTokenService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.IProgressable;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActivityMasterService;
import com.armineasy.activitymaster.activitymaster.services.system.IEnterpriseService;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.IDefaultService;
import com.jwebmp.guicedinjection.pairing.Pair;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IPTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.NameTypes.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

public class ActivityMasterService
		implements IProgressable, IActivityMasterService
{
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 6000)
	public Enterprise startNewEnterprise(IEnterpriseName<?> enterpriseName,
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
	protected InvolvedParty createAdminAndCreatorUserForEnterprise(IEnterpriseName<?> enterpriseName, String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);
		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .getEnterprise(enterpriseName);

		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1, progressMonitor);

		Systems activityMasterSystem = get(SystemsService.class).getActivityMaster(enterprise);
		UUID token = get(SystemsService.class).getSecurityIdentityToken(activityMasterSystem);
		SecurityToken administratorsGroup = get(SecurityTokenService.class).getAdministratorsFolder(enterprise);

		InvolvedPartyService service = get(InvolvedPartyService.class);

		Pair<IIdentificationType, String> pair = new Pair<>(
				IdentificationTypeEnterpriseCreatorRole,
				adminUserName);
		Optional<InvolvedParty> exists = new InvolvedParty().builder()
		                                                    .findByIdentificationType(enterprise,
		                                                                              IdentificationTypeEnterpriseCreatorRole,
		                                                                              adminUserName)
		                                                    .get();

		InvolvedParty administratorUser;
		if (exists.isEmpty())
		{
			InvolvedParty adminUser = service.create(activityMasterSystem, pair, true);

			adminUser.addIdentificationType(IdentificationTypeUserName, activityMasterSystem, adminUserName, token);
			adminUser.addType(TypeIndividual, activityMasterSystem, "Artificial Individual", token);
			adminUser.addNameType(PreferredNameType, activityMasterSystem, "Enterprise Creator", token);
			adminUser.addNameType(FirstNameType, activityMasterSystem, "Administrator", token);

			SecurityToken myToken = get(SecurityTokenService.class).create(Identity,
			                                                               adminUserName,
			                                                               "The creator of the enterprise", activityMasterSystem, administratorsGroup, token);

			adminUser.addIdentificationType(IdentificationTypeEnterpriseCreatorRole, activityMasterSystem, adminUserName, token);
			adminUser.addIdentificationType(IdentificationTypeUUID, activityMasterSystem, myToken.getSecurityToken(), token);

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


	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));

		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .getEnterprise(enterpriseName);
		for (IActivityMasterSystem allSystem : allSystems)
		{
			logProgress("System Loading", "Starting up system " + allSystem.getClass()
			                                                                         .getName(), progressMonitor);
			allSystem.postUpdate(enterprise, progressMonitor);
		}
		ActivityMasterConfiguration.get()
		                           .setDoubleCheckDisabled(true);
	}
}
