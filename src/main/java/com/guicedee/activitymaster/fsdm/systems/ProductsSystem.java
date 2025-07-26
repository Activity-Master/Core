package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IProductService.*;


@Log4j2
public class ProductsSystem
		extends ActivityMasterDefaultSystem<ProductsSystem>
		implements IActivityMasterSystem<ProductsSystem>
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	private ISystemsService<?> systemsService;
	
	@Inject
	private Mutiny.SessionFactory sessionFactory;

	@Override
	public ISystems<?,?>  registerSystem(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(session, enterprise, getSystemName(), getSystemDescription())
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
		logProgress("Products System", "Starting Products Checks");
		log.info("Creating product defaults in a new session and transaction");
		
		// Use sessionFactory.withTransaction to create a new session
		return sessionFactory.withTransaction((newSession, tx) -> {
			// Get the ActivityMaster system
			return systemsService.findSystem(newSession, enterprise, ActivityMasterSystemName)
				.chain(activityMasterSystem -> {
					// Create base product classification
					return service.create(newSession, ProductClassifications.Products, activityMasterSystem)
						.chain(baseClassification -> {
							logProgress("Products System", "Creating product classifications...");

							// Create product-related classifications in parallel since they all have the same parent
							List<Uni<?>> productClassifications = new ArrayList<>();
							productClassifications.add(service.create(newSession, ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products));
							productClassifications.add(service.create(newSession, ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup));
							productClassifications.add(service.create(newSession, ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup));
							productClassifications.add(service.create(newSession, ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup));

							// Execute all product classifications in parallel
							return Uni.combine().all().unis(productClassifications)
								.discardItems()
								.onFailure().invoke(error -> 
									log.error("Error creating product classifications: {}", error.getMessage(), error))
								.invoke(v -> logProgress("Products System", "Loaded Product Classifications...", 4));
						});
				});
		}).replaceWithVoid();
	}

	@Override
	public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
	{
		log.info("Starting reactive postStartup for Products System");

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
		return Integer.MIN_VALUE + 11;
	}

	@Override
	public String getSystemName()
	{
		return ProductSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing Products";
	}
}
