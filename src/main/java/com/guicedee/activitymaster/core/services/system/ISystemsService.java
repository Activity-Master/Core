package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

import static com.guicedee.activitymaster.core.ActivityMasterStatics.*;

public interface ISystemsService
{
	ISystems<?> getActivityMaster(IEnterprise<?> enterprise, UUID... token);

	ISystems<?> findSystem(IEnterprise<?> enterprise, String systemName, UUID... token);

	ISystems<?> findSystem(IEnterprise<?> enterprise, UUID token, UUID... identityToken);

	UUID registerNewSystem(IEnterprise<?> enterprise, ISystems<?> newSystem);

	ISystems<?> create(IEnterprise<?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken);

	UUID getSecurityIdentityToken(ISystems<?> system, UUID... identityToken);
}
