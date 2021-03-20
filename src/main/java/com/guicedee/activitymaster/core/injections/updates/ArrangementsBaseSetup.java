package com.guicedee.activitymaster.core.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.administration.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.administration.ISystemUpdate;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.*;
import com.guicedee.activitymaster.core.updates.DatedUpdate;

import static com.guicedee.activitymaster.core.SystemsService.*;

@DatedUpdate(date = "2016/02/01", taskCount = 3)
public class ArrangementsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Override
	public void update(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		
		logProgress("Arrangements", "Creating Base...", progressMonitor);
		
		//arrangement relationships with parties
		service.create(ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements, activityMasterSystem);
		service.create(ArrangementInvolvedPartyClassifications.PurchasedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		service.create(ArrangementInvolvedPartyClassifications.SoldBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		service.create(ArrangementInvolvedPartyClassifications.OwnedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		service.create(ArrangementInvolvedPartyClassifications.ManagedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		
		logProgress("Arrangements", "Loaded Arrangement InvolvedParty Classifications...", 1, progressMonitor);
		
		//purchase classifications
		service.create(ArrangementProductClassifications.ArrangementPurchase, activityMasterSystem);
		service.create(ArrangementProductClassifications.PurchaseName, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchaseInvoiceName, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchaseCost, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchaseVat, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchaseTotalCost, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchaseInvoiceDate, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchasePaidDate, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchasePromotionCode, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		service.create(ArrangementProductClassifications.PurchaseStatus, activityMasterSystem, ArrangementProductClassifications.ArrangementPurchase);
		logProgress("Arrangements", "Loaded Arrangement Product Classifications...", 1, progressMonitor);
		
		service.create(ArrangementTypeClassifications.ArrangementProductTypes, activityMasterSystem);
		service.create(ArrangementTypeClassifications.ProductPurchase, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		service.create(ArrangementTypeClassifications.ProductBid, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		service.create(ArrangementTypeClassifications.ProductInterest, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		service.create(ArrangementTypeClassifications.ProductQuote, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		service.create(ArrangementTypeClassifications.ProductLead, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		logProgress("Arrangements", "Loaded Arrangement Type Classifications...", 1, progressMonitor);
	}
	
}
