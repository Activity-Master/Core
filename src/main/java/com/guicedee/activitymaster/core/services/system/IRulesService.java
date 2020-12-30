package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.IRulesType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.List;
import java.util.UUID;

public interface IRulesService<J extends IRulesService<J>>
{
	IRules<?> createRules(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken);
	
	IRulesType<?> createRulesType(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken);
	
	IRulesType<?> createRulesType(String rulesType, String description, ISystems<?> system, UUID... identityToken);
	
	IRulesType<?> findRulesTypes(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken);

	IRulesType<?> findRulesTypes(String rulesType, ISystems<?> system, UUID... identityToken);
	
	List<IRulesType<?>> findRulesTypes(String classifications, String value, ISystems<?> system, UUID... identityToken);
}
