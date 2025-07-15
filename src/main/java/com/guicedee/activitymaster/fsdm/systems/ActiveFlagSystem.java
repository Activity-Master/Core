package com.guicedee.activitymaster.fsdm.systems;

import com.entityassist.enumerations.ActiveFlag;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.*;


@Log4j2
public class ActiveFlagSystem
		extends ActivityMasterDefaultSystem<ActiveFlagSystem>
		implements IActivityMasterSystem<ActiveFlagSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private IActiveFlagService<?> activeFlagService;
	@Override
	public ISystems<?,?> registerSystem(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(enterprise, ActivateFlagSystemName, "The system for the active flag management")
		                                        .await().atMost(Duration.ofMinutes(1));
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise))
		              .await().atMost(Duration.ofMinutes(1));
		return iSystems;
	}

	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Active Flag Service", "Loading Active Flags");

		// Note: This method is called synchronously, but we're using reactive programming internally
		// We'll use await().atMost() to block until all active flags are created

		// Create a list of all active flags
		ActiveFlag[] activeFlags = ActiveFlag.values();

		// Create the first active flag and then chain the rest
		Uni<Void> createChain = null;

		for (ActiveFlag activeFlag : activeFlags) {
			if (createChain == null) {
				// First flag
				createChain = ((ActiveFlagService)activeFlagService)
					.create(enterprise, activeFlag.name(), activeFlag.getDescription())
					.map(result -> null); // Convert to Void
			} else {
				// Chain subsequent flags
				final Uni<Void> finalChain = createChain;
				createChain = finalChain.chain(v -> 
					((ActiveFlagService)activeFlagService)
						.create(enterprise, activeFlag.name(), activeFlag.getDescription())
						.map(result -> null) // Convert to Void
				);
			}
		}

		// Wait for all flags to be created
		if (createChain != null) {
			createChain.await().atMost(Duration.ofMinutes(2));
		}
	}

	@Override
	public void postStartup(IEnterprise<?,?> enterprise)
	{
		log.info("Starting reactive postStartup for Active Flag System");

		// Create a reactive chain for the postStartup operations
		Uni<Void> postStartupChain = ReactiveTransactionUtil.withTransaction(session -> {
			// Get the system
			return systemsService.findSystem(enterprise, getSystemName())
				.onItem().ifNull().failWith(() -> new RuntimeException("System not found: " + getSystemName()))
				.chain(system -> {
					log.debug("Found system: {}", system.getName());
					// Get the security token
					return systemsService.getSecurityIdentityToken(system)
						.onItem().ifNull().failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
						.map(token -> {
							log.debug("Found security token for system: {}", system.getName());
							return null; // Return Void
						});
				});
		});

		// Subscribe to the reactive chain
		postStartupChain.subscribe().with(
			result -> log.info("Active Flag System postStartup completed successfully"),
			error -> log.error("Error in Active Flag System postStartup: {}", error.getMessage(), error)
		);
	}

	@Override
	public int totalTasks()
	{
		return ActiveFlag.values().length;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 1;
	}

	@Override
	public String getSystemName()
	{
		return ActivateFlagSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for the active flag management";
	}
}
