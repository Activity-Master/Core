package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.IRulesType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IRulesService<J extends IRulesService<J>>
{
	IRules<?> createRules(IRulesTypeValue<?> rulesType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IRulesType<?> createRulesType(IRulesTypeValue<?> rulesType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IRulesType<?> findRulesType(IRulesTypeValue<?> rulesType, IEnterprise<?> enterprise, UUID... identityToken);

	IRulesType<?> findRulesType(String rulesType, IEnterprise<?> enterprise, UUID... identityToken);
}
