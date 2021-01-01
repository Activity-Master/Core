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

	ISecurityToken<?> getEveryoneGroup(ISystems<?> system,  UUID... identityToken);


	ISecurityToken<?> getEverywhereGroup( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getGuestsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getRegisteredGuestsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getVisitorsGuestsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getAdministratorsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getSystemsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getPluginsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getApplicationsFolder( ISystems<?> system,  UUID... identityToken);

	ISecurityToken<?> getSecurityToken( UUID identifyingToken,  ISystems<?> system,  UUID... identityToken);
}
