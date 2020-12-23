package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.classifications.securitytokens.ISecurityTokenClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

public interface ISecurityTokenService<J extends ISecurityTokenService<J>>
{

	ISecurityToken<?> create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems<?> system);

	ISecurityToken<?> create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems<?> system, ISecurityToken<?> parent, UUID... identityToken);

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
