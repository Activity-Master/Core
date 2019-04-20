package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISecurityTokenService
{

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);
}
