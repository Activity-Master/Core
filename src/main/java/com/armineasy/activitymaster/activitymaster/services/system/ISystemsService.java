package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISystemsService
{
	ISystems getActivityMaster(IEnterprise enterprise, UUID... token);

	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	ISystems findSystem(@CacheKey IEnterprise enterprise, @CacheKey UUID token, @CacheKey UUID... identityToken);
}
