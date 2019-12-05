package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.classifications.arrangement.ArrangementInvolvedPartyClassifications;
import com.guicedee.activitymaster.core.services.classifications.arrangement.ArrangementProductClassifications;
import com.guicedee.activitymaster.core.services.classifications.arrangement.ArrangementTypeClassifications;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArrangementsSystem
		implements IActivityMasterSystem<ArrangementsSystem>
{
	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
	public int totalTasks()
	{
		return 0;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 8;
	}
	@Override
	public void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Arrangements System", "The system for the arrangements management", "");
		UUID securityToken = GuiceContext.get(ISystemsService.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}
	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
