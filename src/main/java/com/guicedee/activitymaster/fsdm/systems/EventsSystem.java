package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.Date;

import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;


@Log4j2
public class EventsSystem
		extends ActivityMasterDefaultSystem<EventsSystem>
		implements IActivityMasterSystem<EventsSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private IEventService<?> eventService;

	@Inject
	private IResourceItemService<?> resourceItemServiceProvider;

	@Inject
	private IClassificationService<?> classificationServiceProvider;
	
	@Inject
	private Mutiny.SessionFactory sessionFactory;

	@Override
	public ISystems<?,?> registerSystem(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(session, enterprise, getSystemName(), getSystemDescription())
		                                        .await().atMost(java.time.Duration.ofMinutes(1));
		getSystem(session, enterprise).chain(system ->{
					return systemsService
								   .registerNewSystem(session, enterprise, system);
		}).await().atMost(Duration.ofMinutes(1));
		return iSystems;
	}

	@Override
	public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		logProgress("Loading Events", "Events creating default types");
		logProgress("Loading Time", "Loading in Today");
		log.info("Creating events defaults in a new session and transaction");

		// Get the day synchronously since ITimeSystem is not reactive yet
		com.guicedee.client.IGuiceContext.get(ITimeSystem.class)
		            .getDay(new Date());

		// Use sessionFactory.withTransaction to create a new session
		return sessionFactory.withTransaction((newSession, tx) -> {
			// Start reactive chain with getting the ActivityMaster system
			return systemsService.findSystem(newSession, enterprise, ActivityMasterSystemName)
				.chain(activityMasterSystem -> {
					logProgress("Loading Logging Types", "Creating Log Types");
					
					// Get system token once and reuse it
					return getSystemToken(newSession, enterprise)
						.chain(systemToken -> {
							// Create base classifications sequentially
							return classificationServiceProvider.create(
								newSession, "LogItemTypes", "The log item event registered types", 
								Classification, activityMasterSystem, systemToken)
								.chain(logItemTypes -> {
									return classificationServiceProvider.create(
										newSession, "EventStatus", "The status of the event", 
										EventXClassification, activityMasterSystem, systemToken)
										.chain(eventStatus -> {
											// Create LogItemTypes classifications in parallel
											java.util.List<Uni<?>> logItemTypeOperations = new java.util.ArrayList<>();
											for (LogItemTypes value : LogItemTypes.values()) {
												logItemTypeOperations.add(classificationServiceProvider.create(
													newSession, value, activityMasterSystem, "LogItemTypes", systemToken));
											}

											// Execute all LogItemTypes classifications in parallel
											return Uni.combine().all().unis(logItemTypeOperations)
												.discardItems()
												.onFailure().invoke(error -> 
													log.error("Error creating LogItemTypes classifications: {}", error.getMessage(), error))
												.chain(v -> {
													// Create LogItem resource type
													return resourceItemServiceProvider.createType(
														newSession, "LogItem", "An attached log item", 
														activityMasterSystem, systemToken)
														.invoke(result -> {
															logProgress("Loading Time", "Creating Hours and Minutes");
															
															//todo Create time synchronously since ITimeSystem is not reactive yet
															com.guicedee.client.IGuiceContext.get(ITimeSystem.class)
																.createTime();
														});
												});
										});
								});
						});
				});
		}).replaceWithVoid();
	}

	@Override
	public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
	{
		log.info("Starting reactive postStartup for Events System");

		// Create a reactive chain for the postStartup operations
		// Get the system
		return systemsService.findSystem(session, enterprise, getSystemName())
				.onItem()
				.ifNull()
				.failWith(() -> new RuntimeException("System not found: " + getSystemName()))
				.chain(system -> {
					log.debug("Found system: {}", system.getName());
					// Get the security token
					return systemsService.getSecurityIdentityToken(session, system)
							.onItem()
							.ifNull()
							.failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
							.map(token -> {
								log.debug("Found security token for system: {}", system.getName());
								return null; // Return Void
							});
				})
				.replaceWith(Uni.createFrom()
						.voidItem());
	}

	@Override
	public int totalTasks()
	{
		return 0;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 9;
	}

	@Override
	public String getSystemName()
	{
		return EventSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing events";
	}
}
