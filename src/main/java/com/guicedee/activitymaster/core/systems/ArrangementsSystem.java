package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.arrangement.ArrangementInvolvedPartyClassifications;
import com.guicedee.activitymaster.core.services.classifications.arrangement.ArrangementProductClassifications;
import com.guicedee.activitymaster.core.services.classifications.arrangement.ArrangementTypeClassifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

@Singleton
public class ArrangementsSystem
		extends ActivityMasterDefaultSystem<ArrangementsSystem>
		implements IActivityMasterSystem<ArrangementsSystem>
{
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	
	}
	
	@Override
	public int totalTasks()
	{
		return 4;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 8;
	}
	
	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		logProgress("Classifications System", "Checking/Creating Defaults...", progressMonitor);
		
		//arrangement relationships with parties
		service.create(ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements, activityMasterSystem);
		service.create(ArrangementInvolvedPartyClassifications.PurchasedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		service.create(ArrangementInvolvedPartyClassifications.SoldBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		service.create(ArrangementInvolvedPartyClassifications.OwnedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		service.create(ArrangementInvolvedPartyClassifications.ManagedBy, activityMasterSystem, ArrangementInvolvedPartyClassifications.InvolvedPartyArrangements);
		
		logProgress("Classifications System", "Loaded Arrangement InvolvedParty Classifications...", 4, progressMonitor);
		
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
		logProgress("Classifications System", "Loaded Arrangement Product Classifications...", 9, progressMonitor);
		
		service.create(ArrangementTypeClassifications.ArrangementProductTypes, activityMasterSystem);
		service.create(ArrangementTypeClassifications.ProductPurchase, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		service.create(ArrangementTypeClassifications.ProductQuote, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		service.create(ArrangementTypeClassifications.ProductLead, activityMasterSystem, ArrangementTypeClassifications.ArrangementProductTypes);
		logProgress("Classifications System", "Loaded Arrangement Type Classifications...", 1, progressMonitor);
		
		
	}
	
	@Override
	public String getSystemName()
	{
		return "Arrangements System";
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for the arrangement management";
	}
}
