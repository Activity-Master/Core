package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.cache.impl.HazelcastClientCacheManager;

import jakarta.cache.CacheManager;

public interface IActivityMasterService
{
	void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);
	
	void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
	void runScript(String script);

	void updatePartitionBases();
	
}
