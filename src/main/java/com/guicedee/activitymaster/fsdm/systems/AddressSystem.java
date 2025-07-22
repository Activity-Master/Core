package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IAddressService.*;


@Log4j2
public class AddressSystem
	extends ActivityMasterDefaultSystem<AddressSystem>
	implements IActivityMasterSystem<AddressSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(enterprise, AddressSystemName, "The system for the address management")
		                                        .await().atMost(Duration.ofMinutes(1));
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise))
		              .await().atMost(Duration.ofMinutes(1));
		return iSystems;
	}

	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Address System", "Starting Address Checks");
	}

	@Override
	public Uni<Void> postStartup(IEnterprise<?,?> enterprise)
	{
		log.info("Starting reactive postStartup for Address System");

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
			result -> log.info("Address System postStartup completed successfully"),
			error -> log.error("Error in Address System postStartup: {}", error.getMessage(), error)
		);
		return postStartupChain.replaceWith(Uni.createFrom().voidItem());
	}


	@Override
	public int totalTasks()
	{
		return 15;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 7;
	}

	@Override
	public String getSystemName()
	{
		return AddressSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for address management";
	}
}
