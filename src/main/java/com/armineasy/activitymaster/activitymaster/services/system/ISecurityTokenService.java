package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISecurityTokenService
{

	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	SecurityToken getEveryoneGroup(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	SecurityToken getEverywhereGroup(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	SecurityToken getGuestsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	SecurityToken getRegisteredGuestsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	SecurityToken getVisitorsGuestsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	SecurityToken getAdministratorsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	SecurityToken getSystemsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	SecurityToken getPluginsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	SecurityToken getApplicationsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken);
}
