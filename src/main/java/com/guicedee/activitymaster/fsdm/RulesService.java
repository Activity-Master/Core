package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;


public class RulesService
		implements IRulesService<RulesService>
{
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	public IRules<?,?> get()
	{
		return new Rules();
	}
	
	@Override
	public IRules<?,?> createRules(String rulesType, String name, String description, ISystems<?,?> system, UUID... identityToken)
	{
		return createRules(rulesType,null, name, description, system, identityToken);
	}
	
	@Override
	public IRules<?,?> createRules(String rulesType, UUID key, String name, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		boolean exists = new Rules().builder()
		                            .withName(name)
		                            .inActiveRange()
		                            .inDateRange()
		                            .withEnterprise(enterprise)
		                            .getCount() > 0;
		if (exists)
		{
			return new Rules().builder()
			                  .withName(name)
			                  .inActiveRange()
			                  .inDateRange()
			                  .withEnterprise(enterprise)
			                  .get()
			                  .orElseThrow();
		}
		
		Rules rules = new Rules();
		rules.setId(key);
		rules.setName(name);
		rules.setDescription(description);
		
		rules.setEnterpriseID(enterprise);
		rules.setSystemID(system);
		rules.setOriginalSourceSystemID(system);
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
		rules.setActiveFlagID(activeFlag);
		rules.persist();
		rules.createDefaultSecurity(system, identityToken);
		
		IRulesType<?,?> pType = createRulesType(rulesType, rulesType, system, identityToken);
		
		rules.addRuleTypes(pType.getName(),STRING_EMPTY,NoClassification.toString(),  system, identityToken);
		
		return rules;
	}
	
	@Override
	public IRules<?,?> find(UUID identity)
	{
		return new Rules().builder()
		                  .find(identity)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRulesType<?,?> findType(UUID identity)
	{
		return new RulesType().builder()
		                  .find(identity)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRules<?,?> findRules(String name, IEnterprise<?,?> enterprise, UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(name)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRules<?,?> findRules(String productName, IClassification<?,?> classification, IEnterprise<?,?> enterprise, UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(productName)
		                  .withClassification(classification)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRulesType<?,?> createRulesType(String rulesType, ISystems<?,?> system, UUID... identityToken)
	{
		return createRulesType(rulesType, rulesType, system, identityToken);
	}
	
	@Override
	public IRulesType<?,?> createRulesType(String rulesType, String description, ISystems<?,?> system, UUID... identityToken)
	{
		return createRulesType(rulesType,null, description, system, identityToken);
	}
	
	@Override
	public IRulesType<?,?> createRulesType(String rulesType, UUID key, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		RulesType et = new RulesType();
		
		boolean exists = et.builder()
		                   .withName(rulesType)
		                   .withEnterprise(enterprise)
		                   .inActiveRange()
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setId(key);
			et.setName(rulesType);
			et.setDescription(description);
			et.setSystemID(system);
			et.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID(system);
			et.persist();
				et.createDefaultSecurity(system, identityToken);
			
			return et;
		}
		else
		{
			return findRulesTypes(rulesType, system, identityToken);
		}
	}
	
	@Override
	@CacheResult(cacheName = "RulesTypesString")
	public IRulesType<?,?> findRulesTypes(@CacheKey String rulesType, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return new RulesType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange()
		                      .inDateRange()
		                //      .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow();
	}
	
	@Override
	public List<IRulesType<?,?>> findRulesTypes(String classifications, String value, ISystems<?,?> system, UUID... identityToken)
	{
		IClassification<?,?> classification = classificationService.find(classifications, system, identityToken);
		@SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes"})
		List all = new RulesType().builder()
		                          .withClassification((Classification) classification, value)
		                          .withEnterprise(enterprise)
		                          .inActiveRange()
		                          .inDateRange()
		                     //     .canRead(system, identityToken)
		                          .getAll();
		//noinspection unchecked
		return all;
	}
	
	@Override
	public List<IRules<?,?>> findByRulesTypes(IRulesType<?,?> rulesType, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink(null,(RulesType) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(RulesXRulesType::getRulesID)
		                                .collect(Collectors.toList());
		return res;
	}
	
	@Override
	public List<IRulesType<?,?>> findRuleTypesByRules(IRules<?,?> rulesType, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType,null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(RulesXRulesType::getRulesTypeID)
		                                .collect(Collectors.toList());
		return res;
	}
	
	@Override
	public List<IRelationshipValue<IRules<?,?>,IRulesType<?,?>,?>> findRuleTypeValuesByRules(IRules<?,?> rulesType, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType,null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll();
		return res;
	}
	
	@Override
	public List<IRules<?,?>> findRulesByProduct(IProduct<?,?> product, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		List res = new RulesXProduct().builder()
		                              .withClassification(classificationName, value, system, identityToken)
		                              .findLink(null,(Product) product, value)
		                              .withEnterprise(enterprise)
		                              .inActiveRange()
		                              .inDateRange()
		                              .canRead(system, identityToken)
		                              .getAll()
		                              .stream()
		                              .map(RulesXProduct::getRulesID)
		                              .collect(Collectors.toList());
		return res;
	}
	
	
	@Override
	public List<IRelationshipValue<IRules<?,?>,IResourceItem<?,?>,?>> findRulesByResourceItem(IResourceItem<?,?> resourceItem, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		List res = new RulesXResourceItem().builder()
		                              .withClassification(classificationName,  system)
		                              .findLink(null,(ResourceItem) resourceItem, value)
		                              .withEnterprise(enterprise)
		                              .inActiveRange()
		                              .inDateRange()
		                              .canRead(system, identityToken)
		                              .getAll();
		return res;
	}
}
