package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISecurityTokenService
{

	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	SecurityToken getEveryoneGroup(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	SecurityToken getEverywhereGroup(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	SecurityToken getGuestsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	SecurityToken getRegisteredGuestsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	SecurityToken getVisitorsGuestsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	SecurityToken getAdministratorsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	SecurityToken getSystemsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	SecurityToken getPluginsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	SecurityToken getApplicationsFolder(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);
}
