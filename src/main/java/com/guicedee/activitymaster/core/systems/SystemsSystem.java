package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.InvolvedPartyService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.types.IPTypes;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.activitymaster.core.services.types.NameTypes;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SystemsSystem
		implements IActivityMasterSystem<SystemsSystem>
{

	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	private static final Map<IEnterprise<?>, Systems> systemsMap = new HashMap<>();
	private static final Logger log = Logger.getLogger(SystemsSystem.class.getName());

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(SystemsService.class)
		            .create(enterprise, SystemsService.ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master");

		GuiceContext.get(SystemsService.class)
		            .create(enterprise, SystemsService.ActivityMasterWebSystemName, "The Web Administration Application for Activity Master", "Activity Master Web");
	}

	@Override
	public int totalTasks()
	{
		return 2;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedParty<?> createInvolvedPartyForNewSystem(ISystems newSystem, UUID... identityToken)
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
			IInvolvedParty<?> ip = ipService.create(newSystem, Pair.of(IdentificationTypes.IdentificationTypeUUID, newSystemUUID.toString()), false, activityMasterSystemUUID);
			ip.addOrReuse(IdentificationTypes.IdentificationTypeSystemID, newSystem.getId()
			                                                                       .toString(), newSystem, activityMasterSystemUUID);
			ip.addOrReuse(IPTypes.TypeSystem, newSystemUUID.toString(), newSystem, activityMasterSystemUUID);
			ip.addOrReuse(NameTypes.PreferredNameType, newSystem.getName(), newSystem, activityMasterSystemUUID);
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
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		final String systemName = "Systems System";
		final String systemDesc = "The system for managing other systems";
		Systems sys = (Systems) GuiceContext.get(SystemsService.class)
		                                    .findSystem(enterprise, systemName);
		UUID securityToken = null;
		if (sys == null)
		{
			sys = (Systems) GuiceContext.get(SystemsService.class)
			                                    .create(enterprise, systemName, systemDesc, systemName);

			securityToken = GuiceContext.get(ISystemsService.class)
			                            .registerNewSystem(enterprise, sys);
		}
		else
		{
			securityToken = GuiceContext.get(SystemsService.class)
			                            .getSecurityIdentityToken(sys);
		}
		systemTokens.put(enterprise, securityToken);
		systemsMap.put(enterprise, sys);
	}

	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{

	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
