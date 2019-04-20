package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISystemsService
{
	Systems getActivityMaster(Enterprise enterprise, UUID... token);

	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	Systems findSystem(@CacheKey Enterprise enterprise, @CacheKey UUID token, UUID... identityToken);
}
