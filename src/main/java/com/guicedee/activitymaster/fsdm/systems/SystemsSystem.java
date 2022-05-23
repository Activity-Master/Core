package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.*;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;


public class SystemsSystem
		extends ActivityMasterDefaultSystem<SystemsSystem>
		implements IActivityMasterSystem<SystemsSystem>
{
	private static final Logger log = Logger.getLogger(SystemsSystem.class.getName());
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public ISystems<?, ?> registerSystem(IEnterprise<?, ?> enterprise)
	{
		Map<IEnterprise<?, ?>, ISystems<?, ?>> systemsMap = new HashMap<>();
		ISystems<?, ?> entSystem = systemsService.get()
		                                         .create(enterprise, EnterpriseSystemName, "The system for handling enterprises");
		systemsMap.put(enterprise, entSystem);
		//	Pair p = Pair.of(EnterpriseSystem.class, systemsMap);
		
		
		Map<IEnterprise<?, ?>, ISystems<?, ?>> flagSystemsMap = new HashMap<>();
		ISystems<?, ?> flagSystem = systemsService.get()
		                                          .create(enterprise, ActivateFlagSystemName, "The system for the active flag management");
		flagSystemsMap.put(enterprise, flagSystem);
		//	Pair ap = Pair.of(ActiveFlagSystem.class, flagSystemsMap);
		
		Map<IEnterprise<?, ?>, ISystems<?, ?>> actSystemsMap = new HashMap<>();
		ISystems<?, ?> activityMasterSystem = systemsService.get()
		                                                    .create(enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master");
		actSystemsMap.put(enterprise, activityMasterSystem);
		//	Pair pAct = Pair.of(SystemsSystem.class, actSystemsMap);
		
		return activityMasterSystem;
	}
	
	@Override
	public void createDefaults(IEnterprise<?, ?> enterprise)
	{
	
	}
	
	@Override
	public int totalTasks()
	{
		return 2;
	}
	
	public IInvolvedParty<?, ?> createInvolvedPartyForNewSystem(ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		ISystems<?, ?> activityMasterSystem = systemsService.get()
		                                                    .getActivityMaster(system.getEnterpriseID());
		UUID activityMasterSystemUUID = systemsService.get()
		                                              .getSecurityIdentityToken(activityMasterSystem);
		
		UUID newSystemUUID = systemsService.get()
		                                   .getSecurityIdentityToken(system, activityMasterSystemUUID);
		if (newSystemUUID == null)
		{
			throw new ActivityMasterException("No UUID for newly created system");
		}
		InvolvedPartyService ipService = GuiceContext.get(InvolvedPartyService.class);
		try
		{
			IInvolvedParty<?, ?> ip = ipService.create(system, Pair.of(IdentificationTypes.IdentificationTypeUUID.toString(), newSystemUUID.toString()), false, activityMasterSystemUUID);
			
			IInvolvedPartyIdentificationType<?, ?> involvedPartyIdentificationType = ipService.findInvolvedPartyIdentificationType(IdentificationTypes.IdentificationTypeSystemID.toString(), system, identityToken);
			ip.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(), involvedPartyIdentificationType, system.getId()
			                                                                                                                 .toString(), system, activityMasterSystemUUID);
			
			IInvolvedPartyType<?, ?> ipType = ipService.findType(IPTypes.TypeSystem.toString(), system, identityToken);
			ip.addOrReuseInvolvedPartyType(NoClassification.toString(), ipType, newSystemUUID.toString(), system, activityMasterSystemUUID);
			IInvolvedPartyNameType<?, ?> nameType = ipService.findInvolvedPartyNameType(NameTypes.PreferredNameType.toString(), system, identityToken);
			ip.addOrReuseInvolvedPartyNameType(NoClassification.toString(), nameType, system.getName(), system, activityMasterSystemUUID);
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
