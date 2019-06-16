package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.ArrangementInvolvedPartyClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.ArrangementProductClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.ArrangementTypeClassifications.*;

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
		service.create(InvolvedPartyArrangements, activityMasterSystem);
		service.create(PurchasedBy, activityMasterSystem,InvolvedPartyArrangements);
		service.create(SoldBy, activityMasterSystem,InvolvedPartyArrangements);
		service.create(OwnedBy, activityMasterSystem,InvolvedPartyArrangements);
		service.create(ManagedBy, activityMasterSystem,InvolvedPartyArrangements);

		logProgress("Classifications System", "Loaded Arrangement InvolvedParty Classifications...", 4, progressMonitor);

		//purchase classifications
		service.create(ArrangementPurchase,activityMasterSystem);
		service.create(PurchaseName,activityMasterSystem,ArrangementPurchase);
		service.create(PurchaseInvoiceName, activityMasterSystem,ArrangementPurchase);
		service.create(PurchaseCost, activityMasterSystem,ArrangementPurchase);
		service.create(PurchaseVat, activityMasterSystem,ArrangementPurchase);
		service.create(PurchaseTotalCost, activityMasterSystem,ArrangementPurchase);
		service.create(PurchaseInvoiceDate, activityMasterSystem,ArrangementPurchase);
		service.create(PurchasePaidDate, activityMasterSystem,ArrangementPurchase);
		service.create(PurchasePromotionCode,  activityMasterSystem,ArrangementPurchase);
		service.create(PurchaseStatus, activityMasterSystem,ArrangementPurchase);
		logProgress("Classifications System", "Loaded Arrangement Product Classifications...", 9, progressMonitor);

		service.create(ArrangementProductTypes, activityMasterSystem);
		service.create(ProductPurchase, activityMasterSystem,ArrangementProductTypes);
		service.create(ProductQuote, activityMasterSystem,ArrangementProductTypes);
		service.create(ProductLead, activityMasterSystem,ArrangementProductTypes);
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
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}
	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
