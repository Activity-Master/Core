package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISecurityToken;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISecurityTokenService<J extends ISecurityTokenService<J>>
{

	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	ISecurityToken<?> getEveryoneGroup(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	ISecurityToken<?> getEverywhereGroup(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	ISecurityToken<?> getGuestsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	ISecurityToken<?> getRegisteredGuestsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	ISecurityToken<?> getVisitorsGuestsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	ISecurityToken<?> getAdministratorsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	ISecurityToken<?> getSystemsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	ISecurityToken<?> getPluginsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	ISecurityToken<?> getApplicationsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	ISecurityToken<?> getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);
}
