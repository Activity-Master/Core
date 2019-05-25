package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.implementations.SecurityTokenService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.types.IPTypes;
import com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes;
import com.armineasy.activitymaster.activitymaster.services.types.NameTypes;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.pairing.Pair;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static com.armineasy.activitymaster.activitymaster.ActivityMasterStatics.*;
import static com.armineasy.activitymaster.activitymaster.implementations.SystemsService.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IPTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.NameTypes.*;

@Log
@Singleton
public class SystemsSystem
		implements IActivityMasterSystem<SystemsSystem>
{

	private static final Map<Enterprise, UUID> systemTokens = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(SystemsService.class)
		            .create(enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master");

		GuiceContext.get(SystemsService.class)
		            .create(enterprise, ActivityMasterWebSystemName, "The Web Administration Application for Activity Master", "Activity Master Web");
	}

	@Override
	public int totalTasks()
	{
		return 2;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,timeout = transactionTimeout)
	public UUID registerNewSystem(Enterprise enterprise, ISystems<?> newSystem)
	{
		//Create Security Token for the created system row
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		SecurityTokenService securityTokenService = GuiceContext.get(SecurityTokenService.class);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		UUID activityMasterSystemUUID = GuiceContext.get(SystemsService.class)
		                                            .getSecurityIdentityToken(activityMasterSystem);

		SecurityToken newSystemsSecurityToken = securityTokenService.create(UserGroupSecurityTokenClassifications.System,
		                                                                    newSystem.getName(), newSystem.getDescription(), newSystem);

		SecurityToken systemsToken = securityTokenService.create(UserGroupSecurityTokenClassifications.System,
		                                                         UserGroupSecurityTokenClassifications.System.classificationName(),
		                                                         UserGroupSecurityTokenClassifications.System.classificationDescription(), activityMasterSystem);

		securityTokenService.link(systemsToken, newSystemsSecurityToken,
		                          classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem.getEnterpriseID(),
		                                                     activityMasterSystemUUID));
		//Add the systems classifications so the UUID can be fetched
		newSystem.addClassification(SystemsClassifications.SystemIdentity, newSystemsSecurityToken.getSecurityToken(), newSystem,
		                            activityMasterSystemUUID);

		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(newSystem, activityMasterSystemUUID);

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			newSystemsSecurityToken.createDefaultSecurity(activityMasterSystem,activityMasterSystemUUID);
		}

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			systemsToken.createDefaultSecurity(activityMasterSystem,activityMasterSystemUUID);
		}

		GuiceContext.get(SystemsSystem.class)
		            .createInvolvedPartyForNewSystem(newSystem);

		return newSystemUUID;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public InvolvedParty createInvolvedPartyForNewSystem(ISystems newSystem,UUID...identityToken)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(newSystem.getEnterpriseID());
		UUID activityMasterSystemUUID = GuiceContext.get(SystemsService.class)
		                                            .getSecurityIdentityToken(activityMasterSystem);

		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(newSystem, activityMasterSystemUUID);
		if (newSystemUUID == null)
		{
			throw new ActivityMasterException("No UUID for newly created system");
		}
		InvolvedPartyService ipService = GuiceContext.get(InvolvedPartyService.class);
		try
		{
			InvolvedParty ip = ipService.create(newSystem, Pair.of(IdentificationTypeUUID,newSystemUUID.toString()), false,activityMasterSystemUUID);
			ip.addIdentificationType(IdentificationTypeSystemID, newSystem,newSystem.getId().toString(), activityMasterSystemUUID);
			ip.addType(TypeSystem,newSystem, newSystemUUID.toString(),activityMasterSystemUUID);
			ip.addNameType(PreferredNameType,newSystem, newSystem.getName(),activityMasterSystemUUID);
			return ip;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Cannot build InvolvedParty", e);
		}

		return null;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 2;
	}


	@Override
	public void postUpdate(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Systems System",
		                                        "The system for managing Systems", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<Enterprise, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
