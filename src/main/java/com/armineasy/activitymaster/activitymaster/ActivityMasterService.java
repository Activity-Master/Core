package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.*;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.IProgressable;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.EnterpriseClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.system.IActivityMasterService;
import com.armineasy.activitymaster.activitymaster.services.system.IEnterpriseService;
import com.armineasy.activitymaster.activitymaster.services.types.IPTypes;
import com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes;
import com.armineasy.activitymaster.activitymaster.services.types.NameTypes;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.IDefaultService;
import com.jwebmp.guicedinjection.interfaces.JobService;
import com.jwebmp.guicedinjection.pairing.Pair;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IPTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.NameTypes.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

public class ActivityMasterService
		implements IProgressable, IActivityMasterService
{

	public Enterprise startNewEnterprise(IEnterpriseName<?> enterpriseName,
	                                     @NotNull String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{

		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);
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
		createAdminAndCreatorUserForEnterprise(enterpriseName, adminUserName, adminPassword,progressMonitor);

		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(true);
		return enterprise;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	protected InvolvedParty createAdminAndCreatorUserForEnterprise(IEnterpriseName<?> enterpriseName, String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);
		Enterprise enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .findEnterprise(enterpriseName)
		                                    .orElseThrow();

		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1, progressMonitor);

		Systems activityMasterSystem = get(SystemsService.class).getActivityMaster(enterprise);
		UUID token = get(SystemsService.class).getSecurityIdentityToken(activityMasterSystem);
		SecurityToken administratorsGroup = get(SecurityTokenService.class).getAdministratorsFolder(enterprise);

		InvolvedPartyService service = get(InvolvedPartyService.class);
		//TODO Create the root admin user (if doesn't exist...)
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
			InvolvedParty adminUser = service.create(activityMasterSystem,pair, true);

			adminUser.addIdentificationType(IdentificationTypeUserName, activityMasterSystem, adminUserName, token);
			adminUser.addType(TypeIndividual,activityMasterSystem, "Artificial Individual", token);
			adminUser.addNameType(PreferredNameType,activityMasterSystem, "Enterprise Creator", token);
			adminUser.addNameType(FirstNameType,activityMasterSystem, "Administrator", token);

			SecurityToken myToken = get(SecurityTokenService.class).create(Identity,
			                                                               adminUserName,
			                                                               "The creator of the enterprise", activityMasterSystem, administratorsGroup, token);

			adminUser.addIdentificationType(IdentificationTypeEnterpriseCreatorRole,activityMasterSystem, adminUserName, token);
			adminUser.addIdentificationType(IdentificationTypeUUID,activityMasterSystem, myToken.getSecurityToken(), token);

			service.addUpdateUsernamePassword(adminUserName, adminPassword, adminUser, activityMasterSystem, token);
			adminUser.createDefaultSecurity(activityMasterSystem);
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




	public void runUpdatesOnEnterprise(@NotNull IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);

		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
		Enterprise enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .findEnterprise(enterpriseName)
		                                    .orElseThrow();
		//Find all systems required for first time installation/updates
		for (IActivityMasterSystem allSystem : allSystems)
		{
			logProgress("Running System", allSystem.getClass()
			                                        .getSimpleName(), progressMonitor);
			String nameC = allSystem.getClass()
			                        .getSimpleName();
			if (nameC.indexOf("$$EnhancerByGuice$$") > 0)
			{
				nameC = nameC.substring(0, nameC.indexOf("$$EnhancerByGuice$$"));
			}
			allSystem.createDefaults(enterprise, progressMonitor);
			logProgress("Completed with System", nameC, progressMonitor);
		}

		loadSystems(enterpriseName, progressMonitor);

		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(true);
	}

	@Override
	public void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Set<IActivityMasterSystem> allSystems = IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
		Enterprise enterprise = GuiceContext.get(IEnterpriseService.class)
		                                    .findEnterprise(enterpriseName)
		                                    .orElseThrow();
		for (IActivityMasterSystem allSystem : allSystems)
		{
			logProgress("System Loading", "Starting up system " + allSystem.getClass().getName(), progressMonitor);
			allSystem.postUpdate(enterprise, progressMonitor);
		}
	}
}
