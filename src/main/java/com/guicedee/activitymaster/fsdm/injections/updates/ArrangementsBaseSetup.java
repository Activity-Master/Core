package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -300, taskCount = 3)
@Log4j2
public class ArrangementsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Override
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of arrangement classifications");
		logProgress("Arrangements", "Creating Base...");

		// Create the base InvolvedPartyArrangements classification first
		return service.create(session, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements, activityMasterSystem)
			.chain(baseInvolvedPartyArrangement -> {
				// Create a list of operations to run in parallel for involved party arrangement classifications
				List<Uni<?>> involvedPartyOperations = new ArrayList<>();

				// Add all involved party arrangement classification creation operations to the list
				involvedPartyOperations.add(service.create(session, ArrangementInvolvedPartyClassifications.PurchasedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements));
				involvedPartyOperations.add(service.create(session, ArrangementInvolvedPartyClassifications.SoldBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements));
				involvedPartyOperations.add(service.create(session, ArrangementInvolvedPartyClassifications.OwnedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements));
				involvedPartyOperations.add(service.create(session, ArrangementInvolvedPartyClassifications.ManagedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements));

				log.info("Running {} involved party arrangement classification creation operations in parallel", involvedPartyOperations.size());

				// Run all involved party operations in parallel
				return Uni.combine().all().unis(involvedPartyOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating involved party arrangement classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Arrangements", "Loaded Arrangement InvolvedParty Classifications...", 1))
					.chain(() -> {
						// Now create the ArrangementPurchase classification
						return service.create(session, ArrangementProductClassifications.ArrangementPurchase, activityMasterSystem)
							.chain(baseArrangementPurchase -> {
								// Create a list of operations to run in parallel for purchase classifications
								List<Uni<?>> purchaseOperations = new ArrayList<>();

								// Add purchase-related classification creation operations to the list
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseName, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseInvoiceName, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseCost, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseVat, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseTotalCost, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseInvoiceDate, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchasePaidDate, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchasePromotionCode, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));
								purchaseOperations.add(service.create(session, ArrangementProductClassifications.PurchaseStatus, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase));

								log.info("Running {} purchase classification creation operations in parallel", purchaseOperations.size());

								// Run all purchase operations in parallel
								return Uni.combine().all().unis(purchaseOperations)
									.discardItems()
									.onFailure().invoke(error -> log.error("Error creating purchase classifications: {}", error.getMessage(), error))
									.invoke(() -> logProgress("Arrangements", "Loaded Arrangement Product Classifications...", 1))
									.chain(() -> {
										// Finally, create ArrangementProductTypes classification and its children
										return service.create(session, ArrangementTypeClassifications.ArrangementProductTypes, activityMasterSystem)
											.chain(baseArrangementProductTypes -> {
												// Create a list of operations for product type classifications
												List<Uni<?>> productTypeOperations = new ArrayList<>();
												productTypeOperations.add(service.create(session, ArrangementTypeClassifications.ProductPurchase, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes));
												productTypeOperations.add(service.create(session, ArrangementTypeClassifications.ProductBid, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes));
												productTypeOperations.add(service.create(session, ArrangementTypeClassifications.ProductInterest, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes));
												productTypeOperations.add(service.create(session, ArrangementTypeClassifications.ProductQuote, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes));
												productTypeOperations.add(service.create(session, ArrangementTypeClassifications.ProductLead, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes));

												log.info("Running {} product type classification creation operations in parallel", productTypeOperations.size());

												return Uni.combine().all().unis(productTypeOperations)
													.discardItems()
													.onFailure().invoke(error -> log.error("Error creating product type classifications: {}", error.getMessage(), error))
													.invoke(() -> logProgress("Arrangements", "Loaded Arrangement Type Classifications...", 1))
													.map(result -> true); // Return Boolean
											});
									});
							});
					});
			});
	}

}
