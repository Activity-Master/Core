package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = 0, taskCount = 6)
@Log4j2
public class ProductsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Override
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of product classifications");

		// Create the base product classification first
		return service.create(session, ProductClassifications.Products, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all product-related classification creation operations to the list
				// These all have the same parent (ProductClassifications.Products or ProductClassifications.ProductGroup)
				operations.add(service.create(session, ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products));
				operations.add(service.create(session, ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup));
				operations.add(service.create(session, ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup));
				operations.add(service.create(session, ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup));

				log.info("Running {} product classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating product classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Products System", "Loaded Product Classifications...", 5))
					.map(result -> true); // Return Boolean
			});
	}
}
