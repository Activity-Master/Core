package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IRulesService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.List;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;


public class RulesService<J extends RulesService<J>> implements IRulesService<J>
{
	@Override
	public IRules<?> createRules(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken)
	{
		Rules rules = new Rules();
		rules.setEnterpriseID((Enterprise) system.getEnterpriseID());
		rules.setSystemID((Systems) system);
		rules.setOriginalSourceSystemID((Systems) system);
		rules.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(system.getEnterpriseID(), identityToken));
		rules.persist();
		rules.createDefaultSecurity(system, identityToken);
		rules.add(NoClassification, rulesType, STRING_EMPTY, system, identityToken);
		return rules;
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
		                   .withEnterprise(system.getEnterpriseID())
		                   .inActiveRange(system.getEnterpriseID())
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(rulesType);
			et.setDescription(description);
			et.setSystemID((Systems) system);
			et.setEnterpriseID((Enterprise) system.getEnterpriseID());
			et.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                            .getActiveFlag(system.getEnterpriseID(), identityToken));
			et.setOriginalSourceSystemID((Systems) system);
			et.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				et.createDefaultSecurity(system, identityToken);
			}
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
		return findRulesTypes(rulesType.classificationValue(), system, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "RulesTypesString")
	public IRulesType<?> findRulesTypes(@CacheKey String rulesType, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		Enterprise enterprise = (Enterprise) system.getEnterprise();
		return new RulesType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(enterprise, identityToken)
		                      .get()
		                      .orElseThrow();
	}
	
	@Override
	public List<IRulesType<?>> findRulesTypes(String classifications, String value, ISystems<?> system, UUID... identityToken)
	{
		Enterprise enterprise = (Enterprise) system.getEnterprise();
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classifications, enterprise, identityToken);
		@SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes"})
		List all = new RulesType().builder()
		                          .withClassification((Classification) classification, value)
		                          .withEnterprise(enterprise)
		                          .inActiveRange(enterprise, identityToken)
		                          .inDateRange()
		                          .canRead(enterprise, identityToken)
		                          .getAll();
		//noinspection unchecked
		return all;
	}
}
