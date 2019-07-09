package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.ActivityMasterStatics.*;

public interface ISystemsService
{
	ISystems<?> getActivityMaster(IEnterprise<?> enterprise, UUID... token);

	ISystems<?> findSystem(IEnterprise<?> enterprise,UUID token, UUID... identityToken);

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,timeout = transactionTimeout)
	UUID registerNewSystem(IEnterprise<?> enterprise, ISystems<?> newSystem);

	ISystems<?> create(IEnterprise<?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken);
}
