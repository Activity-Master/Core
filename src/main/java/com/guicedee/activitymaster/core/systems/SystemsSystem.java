package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.InvolvedPartyService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.core.services.types.IPTypes;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.activitymaster.core.services.types.NameTypes;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.core.implementations.SystemsService.*;
import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;

@Singleton
public class SystemsSystem
		extends ActivityMasterDefaultSystem<SystemsSystem>
		implements IActivityMasterSystem<SystemsSystem>
{
	private static final Logger log = Logger.getLogger(SystemsSystem.class.getName());

	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		GuiceContext.get(SystemsService.class)
		            .create(enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master");

		GuiceContext.get(SystemsService.class)
		            .create(enterprise, SystemsService.ActivityMasterWebSystemName, "The Web Administration Application for Activity Master", "Activity Master Web");
		
		ActivityMasterConfiguration.getCreatingNew()
		                           .set(false);
	}

	@Override
	public int totalTasks()
	{
		return 2;
	}

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
			ip.addOrReuseIdentificationType(IdentificationTypes.IdentificationTypeSystemID,NoClassification.name(), newSystem.getId()
			                                                                       .toString(), newSystem.getEnterprise(), activityMasterSystemUUID);
			ip.addOrReuseType(IPTypes.TypeSystem, NoClassification.name(),newSystemUUID.toString(), newSystem.getEnterprise(), activityMasterSystemUUID);
			ip.addOrReuseNameType(NameTypes.PreferredNameType, NoClassification.name(), newSystem.getName(), newSystem.getEnterprise(), activityMasterSystemUUID);
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
	public String getSystemName()
	{
		return "Systems System";
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing other systems";
	}

}
