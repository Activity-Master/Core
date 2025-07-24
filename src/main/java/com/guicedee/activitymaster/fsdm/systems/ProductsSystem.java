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
	public void createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		// Get the ActivityMaster system
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));

		// Create base product classification
		service.create(session, ProductClassifications.Products, activityMasterSystem)
		      .await().atMost(Duration.ofMinutes(1));

		logProgress("Products System", "Creating product classifications...");

		// Create product-related classifications in parallel since they all have the same parent
		List<Uni<?>> productClassifications = new ArrayList<>();
		productClassifications.add(service.create(session, ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products));
		productClassifications.add(service.create(session, ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup));
		productClassifications.add(service.create(session, ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup));
		productClassifications.add(service.create(session, ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup));

		// Execute all product classifications in parallel and wait for completion
		Uni.combine().all().unis(productClassifications)
		             .discardItems()
		             .onFailure().invoke(error -> 
		                 log.error("Error creating product classifications: {}", error.getMessage(), error))
		             .await().atMost(Duration.ofMinutes(1));

		logProgress("Products System", "Loaded Product Classifications...", 4);
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
