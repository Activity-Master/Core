package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.implementations.SecurityTokenService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IInvolvedParty;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.pairing.Pair;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.armineasy.activitymaster.activitymaster.ActivityMasterStatics.*;
import static com.armineasy.activitymaster.activitymaster.implementations.SystemsService.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IPTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.armineasy.activitymaster.activitymaster.services.types.NameTypes.*;

@Singleton
public class SystemsSystem
		implements IActivityMasterSystem<SystemsSystem>
{

	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	private static final Logger log = Logger.getLogger(SystemsSystem.class.getName());

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedParty<?> createInvolvedPartyForNewSystem(ISystems newSystem,UUID...identityToken)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
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
			IInvolvedParty<?> ip = ipService.create(newSystem, Pair.of(IdentificationTypeUUID, newSystemUUID.toString()), false, activityMasterSystemUUID);
			ip.addOrReuse(IdentificationTypeSystemID, newSystem.getId().toString(),newSystem, activityMasterSystemUUID);
			ip.addOrReuse(TypeSystem, newSystemUUID.toString(),newSystem,  activityMasterSystemUUID);
			ip.addOrReuse(PreferredNameType, newSystem.getName(),newSystem,  activityMasterSystemUUID);
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
	public void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Systems System",
		                                        "The system for managing Systems", "");
		UUID securityToken = GuiceContext.get(ISystemsService.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
