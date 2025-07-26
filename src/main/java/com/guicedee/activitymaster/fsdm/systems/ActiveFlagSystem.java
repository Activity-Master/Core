package com.guicedee.activitymaster.fsdm.systems;

import com.entityassist.enumerations.ActiveFlag;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;


@Log4j2
public class ActiveFlagSystem
		extends ActivityMasterDefaultSystem<ActiveFlagSystem>
		implements IActivityMasterSystem<ActiveFlagSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private IActiveFlagService<?> activeFlagService;
	
	@Inject
	private Mutiny.SessionFactory sessionFactory;
	@Override
	public ISystems<?,?> registerSystem(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(session, enterprise, ActivateFlagSystemName, "The system for the active flag management")
		                                        .await().atMost(Duration.ofMinutes(1));

		getSystem(session, enterprise).chain(system ->{
					return systemsService
								   .registerNewSystem(session, enterprise, system);
		}).await().atMost(Duration.ofMinutes(1));
		return iSystems;
	}

	@Override
	public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		logProgress("Active Flag Service", "Loading Active Flags");
		log.info("Creating active flags in a new session and transaction");

		return sessionFactory.withTransaction((newSession, tx) -> {
			// Create a list of all active flags
			ActiveFlag[] activeFlags = ActiveFlag.values();

			// Create the first active flag and then chain the rest
			Uni<Void> createChain = null;

			for (ActiveFlag activeFlag : activeFlags) {
				if (createChain == null) {
					// First flag
					createChain = ((ActiveFlagService)activeFlagService)
						.create(newSession, enterprise, activeFlag.name(), activeFlag.getDescription())
						.map(result -> null); // Convert to Void
				} else {
					// Chain subsequent flags
					final Uni<Void> finalChain = createChain;
					createChain = finalChain.chain(v -> 
						((ActiveFlagService)activeFlagService)
							.create(newSession, enterprise, activeFlag.name(), activeFlag.getDescription())
							.map(result -> null) // Convert to Void
					);
				}
			}

			// Return the reactive chain or an empty one if no flags were created
			return createChain != null ? createChain : Uni.createFrom().voidItem();
		}).replaceWithVoid();
	}

	@Override
	public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting reactive postStartup for Active Flag System");

		// Create a reactive chain for the postStartup operations
			// Get the system
			return systemsService.findSystem(session, enterprise, getSystemName())
				.onItem().ifNull().failWith(() -> new RuntimeException("System not found: " + getSystemName()))
				.chain(system -> {
					log.debug("Found system: {}", system.getName());
					// Get the security token
					return systemsService.getSecurityIdentityToken(session, system)
						.onItem().ifNull().failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
						.map(token -> {
							log.debug("Found security token for system: {}", system.getName());
							return null; // Return Void
						});
				}).replaceWith(Uni.createFrom().voidItem());
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
