package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.core.InvolvedPartyService;
import com.guicedee.activitymaster.core.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.types.*;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;


public class SystemsSystem
		extends ActivityMasterDefaultSystem<SystemsSystem>
		implements IActivityMasterSystem<SystemsSystem>
{
	private static final Logger log = Logger.getLogger(SystemsSystem.class.getName());
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master");
		systemsService.get()
		              .create(enterprise, SystemsService.ActivityMasterWebSystemName, "The Web Administration Application for Activity Master", "Activity Master Web");
	}
	
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	
	}
	
	@Override
	public int totalTasks()
	{
		return 2;
	}
	
	public IInvolvedParty<?> createInvolvedPartyForNewSystem(ISystems<?> newSystem, UUID... identityToken)
	{
		ISystems<?> activityMasterSystem = systemsService.get()
		                                                 .getActivityMaster(newSystem.getEnterpriseID());
		UUID activityMasterSystemUUID = systemsService.get()
		                                              .getSecurityIdentityToken(activityMasterSystem);
		
		UUID newSystemUUID = systemsService.get()
		                                   .getSecurityIdentityToken(newSystem, activityMasterSystemUUID);
		if (newSystemUUID == null)
		{
			throw new ActivityMasterException("No UUID for newly created system");
		}
		InvolvedPartyService ipService = GuiceContext.get(InvolvedPartyService.class);
		try
		{
			IInvolvedParty<?> ip = ipService.create(newSystem, Pair.of(IdentificationTypes.IdentificationTypeUUID, newSystemUUID.toString()), false, activityMasterSystemUUID);
			ip.addOrReuseIdentificationType(IdentificationTypes.IdentificationTypeSystemID, NoClassification.name(), newSystem.getId()
			                                                                                                                  .toString(), newSystem, activityMasterSystemUUID);
			ip.addOrReuseType(IPTypes.TypeSystem, NoClassification.name(), newSystemUUID.toString(), newSystem, activityMasterSystemUUID);
			ip.addOrReuseNameType(NameTypes.PreferredNameType, NoClassification.name(), newSystem.getName(), newSystem, activityMasterSystemUUID);
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
		return ActivityMasterSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The Core Enterprise Activity Monitoring Application";
	}
	
}
