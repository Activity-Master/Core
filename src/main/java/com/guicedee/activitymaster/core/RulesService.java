package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IRulesService;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;


public class RulesService<J extends RulesService<J>>
		implements IRulesService<J>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?> activeFlag;
	
	@Inject
	private IEnterprise<?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Override
	public IRules<?> createRules(String rulesType, String name, String description, ISystems<?> system, UUID... identityToken)
	{
		boolean exists = new Rules().builder()
		                            .withName(name)
		                            .inActiveRange(enterprise, identityToken)
		                            .inDateRange()
		                            .withEnterprise(enterprise)
		                            .getCount() > 0;
		if (exists)
		{
			return new Rules().builder()
			                  .withName(name)
			                  .inActiveRange(enterprise, identityToken)
			                  .inDateRange()
			                  .withEnterprise(enterprise)
			                  .get()
			                  .orElseThrow();
		}
		
		Rules rules = new Rules();
		rules.setName(name);
		rules.setDescription(description);
		
		rules.setEnterpriseID((Enterprise) enterprise);
		rules.setSystemID((Systems) system);
		rules.setOriginalSourceSystemID((Systems) system);
		rules.setActiveFlagID(activeFlag);
		rules.persist();
		rules.createDefaultSecurity(system, identityToken);
		
		IRulesType<?> pType = createRulesType(rulesType, rulesType, system, identityToken);
		
		rules.addRulesType(NoClassification, (IRulesTypeValue<?>) pType, STRING_EMPTY, system, identityToken);
		
		return rules;
	}
	
	@Override
	public IRules<?> find(UUID identity)
	{
		return new Rules().builder()
		                  .find(identity)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRules<?> findRules(String name, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(name)
		                  .inActiveRange(enterprise, identityToken)
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRules<?> findRules(String productName, IClassification<?> classification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(productName)
		                  .withClassification(classification)
		                  .inActiveRange(enterprise, identityToken)
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRulesType<?> createRulesType(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken)
	{
		return createRulesType(rulesType.classificationName(), rulesType.classificationDescription(), system, identityToken);
	}
	
	@Override
	public IRulesType<?> createRulesType(String rulesType, String description, ISystems<?> system, UUID... identityToken)
	{
		RulesType et = new RulesType();
		
		boolean exists = et.builder()
		                   .withName(rulesType)
		                   .withEnterprise(enterprise)
		                   .inActiveRange(enterprise)
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(rulesType);
			et.setDescription(description);
			et.setSystemID((Systems) system);
			et.setEnterpriseID((Enterprise) enterprise);
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID((Systems) system);
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
	@CacheResult(cacheName = "RulesTypes")
	public IRulesType<?> findRulesTypes(@CacheKey IRulesTypeValue<?> rulesType, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return findRulesTypes(rulesType.classificationName(), system, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "RulesTypesString")
	public IRulesType<?> findRulesTypes(@CacheKey String rulesType, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return new RulesType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow();
	}
	
	@Override
	public List<IRulesType<?>> findRulesTypes(String classifications, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassification<?> classification = classificationService.find(classifications, system, identityToken);
		@SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes"})
		List all = new RulesType().builder()
		                          .withClassification((Classification) classification, value)
		                          .withEnterprise(enterprise)
		                          .inActiveRange(enterprise, identityToken)
		                          .inDateRange()
		                          .canRead(system, identityToken)
		                          .getAll();
		//noinspection unchecked
		return all;
	}
	
	@Override
	public List<IRules<?>> findByRulesTypes(IRulesType<?> rulesType, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findChildLink((RulesType) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange(enterprise, identityToken)
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(RulesXRulesType::getRulesID)
		                                .collect(Collectors.toList());
		return res;
	}
	
	@Override
	public List<IRulesType<?>> findRuleTypesByRules(IRules<?> rulesType, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findParentLink((Rules) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange(enterprise, identityToken)
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(RulesXRulesType::getRulesTypeID)
		                                .collect(Collectors.toList());
		return res;
	}
	
	@Override
	public List<IRelationshipValue<IRules<?>,IRulesType<?>,?>> findRuleTypeValuesByRules(IRules<?> rulesType, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findParentLink((Rules) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange(enterprise, identityToken)
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll();
		return res;
	}
	
	@Override
	public List<IRules<?>> findRulesByProduct(IProduct<?> product, String classificationName, String value, ISystems<?> system, UUID... identityToken)
	{
		List res = new RulesXProduct().builder()
		                              .withClassification(classificationName, value, system, identityToken)
		                              .findChildLink((Product) product, value)
		                              .withEnterprise(enterprise)
		                              .inActiveRange(enterprise, identityToken)
		                              .inDateRange()
		                              .canRead(system, identityToken)
		                              .getAll()
		                              .stream()
		                              .map(RulesXProduct::getRulesID)
		                              .collect(Collectors.toList());
		return res;
	}
}
