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
		log.info("Starting sequential creation of arrangement classifications with session: {}", session.hashCode());
		logProgress("Arrangements", "Creating Base...");

		// Create the base InvolvedPartyArrangements classification first
		return service.create(session, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements, activityMasterSystem)
			.chain(baseInvolvedPartyArrangement -> {
				log.info("📋 Creating involved party arrangement classifications sequentially");
				
				// Chain involved party arrangement classification creation operations sequentially
				return service.create(session, ArrangementInvolvedPartyClassifications.PurchasedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements)
					.chain(() -> service.create(session, ArrangementInvolvedPartyClassifications.SoldBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements))
					.chain(() -> service.create(session, ArrangementInvolvedPartyClassifications.OwnedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements))
					.chain(() -> service.create(session, ArrangementInvolvedPartyClassifications.ManagedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all involved party arrangement classifications");
						logProgress("Arrangements", "Loaded Arrangement InvolvedParty Classifications...", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating involved party arrangement classifications: {}", error.getMessage(), error))
					.chain(() -> {
						// Now create the ArrangementPurchase classification
						return service.create(session, ArrangementProductClassifications.ArrangementPurchase, activityMasterSystem)
							.chain(baseArrangementPurchase -> {
								log.info("📋 Creating purchase classifications sequentially");
								
								// Chain purchase-related classification creation operations sequentially
								return service.create(session, ArrangementProductClassifications.PurchaseName, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase)
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchaseInvoiceName, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchaseCost, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchaseVat, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchaseTotalCost, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchaseInvoiceDate, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchasePaidDate, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchasePromotionCode, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.chain(() -> service.create(session, ArrangementProductClassifications.PurchaseStatus, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase))
									.onItem().invoke(() -> {
										log.info("✅ Successfully created all purchase classifications");
										logProgress("Arrangements", "Loaded Arrangement Product Classifications...", 1);
									})
									.onFailure().invoke(error -> log.error("❌ Error creating purchase classifications: {}", error.getMessage(), error))
									.chain(() -> {
										// Finally, create ArrangementProductTypes classification and its children
										return service.create(session, ArrangementTypeClassifications.ArrangementProductTypes, activityMasterSystem)
											.chain(baseArrangementProductTypes -> {
												log.info("📋 Creating product type classifications sequentially");
												
												// Chain product type classification creation operations sequentially
												return service.create(session, ArrangementTypeClassifications.ProductPurchase, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes)
													.chain(() -> service.create(session, ArrangementTypeClassifications.ProductBid, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes))
													.chain(() -> service.create(session, ArrangementTypeClassifications.ProductInterest, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes))
													.chain(() -> service.create(session, ArrangementTypeClassifications.ProductQuote, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes))
													.chain(() -> service.create(session, ArrangementTypeClassifications.ProductLead, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes))
													.onItem().invoke(() -> {
														log.info("✅ Successfully created all product type classifications");
														logProgress("Arrangements", "Loaded Arrangement Type Classifications...", 1);
													})
													.onFailure().invoke(error -> log.error("❌ Error creating product type classifications: {}", error.getMessage(), error))
													.map(result -> true); // Return Boolean
											});
									});
							});
					});
			});
	}
}