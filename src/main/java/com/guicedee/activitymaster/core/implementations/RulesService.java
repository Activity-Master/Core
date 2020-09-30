package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.IRulesType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IRulesService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;


public class RulesService<J extends RulesService<J>> implements IRulesService<J>
{
	@Override
	public IRules<?> createRules(IRulesTypeValue<?> rulesType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Rules rules = new Rules();
		rules.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		rules.setSystemID((Systems) originatingSystem);
		rules.setOriginalSourceSystemID((Systems) originatingSystem);
		rules.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		rules.persist();
		rules.createDefaultSecurity(originatingSystem, identityToken);
		rules.add(NoClassification, rulesType, STRING_EMPTY, originatingSystem, identityToken);
		return rules;
	}
	
	@Override
	public IRulesType<?> createRulesType(IRulesTypeValue<?> rulesType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		RulesType et = new RulesType();
		
		boolean exists = et.builder()
		                   .withName(rulesType.name())
		                   .withEnterprise(originatingSystem.getEnterpriseID())
		                   .inActiveRange(originatingSystem.getEnterpriseID())
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(rulesType.name());
			et.setDescription(rulesType.classificationValue());
			et.setSystemID((Systems) originatingSystem);
			et.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			et.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                            .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			et.setOriginalSourceSystemID((Systems) originatingSystem);
			et.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				et.createDefaultSecurity(originatingSystem, identityToken);
			}
			return et;
		}
		else
		{
			return findRulesType(rulesType, originatingSystem.getEnterprise(), identityToken);
		}
	}
	
	@Override
	@CacheResult(cacheName = "RulesTypes")
	public IRulesType<?> findRulesType(@CacheKey IRulesTypeValue<?> rulesType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return findRulesType(rulesType.classificationValue(), enterprise, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "RulesTypesString")
	public IRulesType<?> findRulesType(@CacheKey String rulesType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return new RulesType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(enterprise, identityToken)
		                      .get()
		                      .orElseThrow();
	}
}
