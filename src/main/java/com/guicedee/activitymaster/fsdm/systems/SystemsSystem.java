package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.*;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;


@Log4j2
public class SystemsSystem
		extends ActivityMasterDefaultSystem<SystemsSystem>
		implements IActivityMasterSystem<SystemsSystem>
{

	@Inject
	private Provider<ISystemsService<?>> systemsService;

	/**
	 * Creates all the applicable systems that are required before the actual activity master system can be created.
	 * After the user groups setup then security controls kick in
	 * @param enterprise
	 * @return
	 */
	@Override
	public ISystems<?, ?> registerSystem(IEnterprise<?, ?> enterprise)
	{
		log.info("Registering systems for enterprise: {}", enterprise.getName());

		// Create Enterprise System
		log.debug("Creating Enterprise System");
		ISystems<?, ?> entSystem = systemsService.get()
		                                         .create(enterprise, EnterpriseSystemName, "The system for handling enterprises")
		                                         .onItem().invoke(system -> log.debug("Created Enterprise System: {}", system.getName()))
		                                         .onFailure().invoke(error -> log.error("Error creating Enterprise System: {}", error.getMessage(), error))
		                                         .await().atMost(Duration.ofMinutes(1));

		// Create Active Flag System
		log.debug("Creating Active Flag System");
		ISystems<?, ?> flagSystem = systemsService.get()
		                                          .create(enterprise, ActivateFlagSystemName, "The system for the active flag management")
		                                          .onItem().invoke(system -> log.debug("Created Active Flag System: {}", system.getName()))
		                                          .onFailure().invoke(error -> log.error("Error creating Active Flag System: {}", error.getMessage(), error))
		                                          .await().atMost(Duration.ofMinutes(1));

		// Create Activity Master System
		log.debug("Creating Activity Master System");
		ISystems<?, ?> activityMasterSystem = systemsService.get()
		                                                    .create(enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master")
		                                                    .onItem().invoke(system -> log.debug("Created Activity Master System: {}", system.getName()))
		                                                    .onFailure().invoke(error -> log.error("Error creating Activity Master System: {}", error.getMessage(), error))
		                                                    .await().atMost(Duration.ofMinutes(1));

		log.info("Successfully registered systems for enterprise: {}", enterprise.getName());
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
		log.info("Creating involved party for system: {}", system.getName());

		// Get Activity Master System
		ISystems<?, ?> activityMasterSystem = systemsService.get()
		                                                    .getActivityMaster(system.getEnterpriseID())
		                                                    .onItem().invoke(s -> log.debug("Got Activity Master System"))
		                                                    .onFailure().invoke(error -> log.error("Error getting Activity Master System: {}", error.getMessage(), error))
		                                                    .await().atMost(Duration.ofMinutes(1));

		// Get Activity Master System UUID
		UUID activityMasterSystemUUID = systemsService.get()
		                                              .getSecurityIdentityToken(activityMasterSystem)
		                                              .onItem().invoke(uuid -> log.debug("Got Activity Master System UUID: {}", uuid))
		                                              .onFailure().invoke(error -> log.error("Error getting Activity Master System UUID: {}", error.getMessage(), error))
		                                              .await().atMost(Duration.ofMinutes(1));

		// Get System UUID
		UUID newSystemUUID = systemsService.get()
		                                   .getSecurityIdentityToken(system, activityMasterSystemUUID)
		                                   .onItem().invoke(uuid -> log.debug("Got System UUID: {}", uuid))
		                                   .onFailure().invoke(error -> log.error("Error getting System UUID: {}", error.getMessage(), error))
		                                   .await().atMost(Duration.ofMinutes(1));

		if (newSystemUUID == null)
		{
			log.error("No UUID for newly created system: {}", system.getName());
			throw new ActivityMasterException("No UUID for newly created system");
		}

		InvolvedPartyService ipService = com.guicedee.client.IGuiceContext.get(InvolvedPartyService.class);

		try
		{
			// Create the involved party
			IInvolvedParty<?, ?> involvedParty = ipService.create(system, Pair.of(IdentificationTypes.IdentificationTypeUUID.toString(), 
			                                                                      newSystemUUID.toString()), 
			                                                      false, activityMasterSystemUUID)
			                                             .onItem().invoke(ip -> log.debug("Created involved party for system: {}", system.getName()))
			                                             .onFailure().invoke(error -> log.error("Error creating involved party: {}", error.getMessage(), error))
			                                             .await().atMost(Duration.ofMinutes(2));

			// Find identification type
			IInvolvedPartyIdentificationType<?, ?> involvedPartyIdentificationType = ipService.findInvolvedPartyIdentificationType(
			                                                                                  IdentificationTypes.IdentificationTypeSystemID.toString(), 
			                                                                                  system, identityToken)
			                                                                                 .onItem().invoke(type -> log.debug("Found identification type"))
			                                                                                 .onFailure().invoke(error -> log.error("Error finding identification type: {}", error.getMessage(), error))
			                                                                                 .await().atMost(Duration.ofMinutes(1));

			// Add identification type to involved party
			log.debug("Adding identification type to involved party");
			involvedParty.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(), 
			                                                        involvedPartyIdentificationType, 
			                                                        system.getId().toString(), 
			                                                        system, 
			                                                        activityMasterSystemUUID);

			// Find party type
			IInvolvedPartyType<?, ?> ipType = ipService.findType(IPTypes.TypeSystem.toString(), system, identityToken)
			                                          .onItem().invoke(type -> log.debug("Found party type"))
			                                          .onFailure().invoke(error -> log.error("Error finding party type: {}", error.getMessage(), error))
			                                          .await().atMost(Duration.ofMinutes(1));

			// Add party type to involved party
			log.debug("Adding party type to involved party");
			involvedParty.addOrReuseInvolvedPartyType(NoClassification.toString(), 
			                                         ipType, 
			                                         newSystemUUID.toString(), 
			                                         system, 
			                                         activityMasterSystemUUID);

			// Find name type
			IInvolvedPartyNameType<?, ?> nameType = ipService.findInvolvedPartyNameType(NameTypes.PreferredNameType.toString(), system, identityToken)
			                                                .onItem().invoke(type -> log.debug("Found name type"))
			                                                .onFailure().invoke(error -> log.error("Error finding name type: {}", error.getMessage(), error))
			                                                .await().atMost(Duration.ofMinutes(1));

			// Add name type to involved party
			log.debug("Adding name type to involved party");
			involvedParty.addOrReuseInvolvedPartyNameType(NoClassification.toString(), 
			                                             nameType, 
			                                             system.getName(), 
			                                             system, 
			                                             activityMasterSystemUUID);

			log.info("Successfully created involved party for system: {}", system.getName());
			return involvedParty;
		}
		catch (Exception e)
		{
			log.error("Cannot build InvolvedParty for system {}: {}", system.getName(), e.getMessage(), e);
			return null;
		}
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
