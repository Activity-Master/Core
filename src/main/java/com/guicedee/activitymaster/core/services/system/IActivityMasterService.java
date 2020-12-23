package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import com.guicedee.logger.LogFactory;
import com.hazelcast.client.cache.impl.HazelcastClientCacheManager;

import jakarta.cache.CacheManager;

public interface IActivityMasterService
{
	void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);

	void loadUpdates(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);

    void runScript(String script);

	void updatePartitionBases();
	
	/**
	 * Wipe all caches? Haven't serialVersion'd to incremental yet so probably a good idea
	 */
	static void wipeCaches()
	{
		LogFactory.getLog("Startup")
		          .info("Wiping Caches");
		CacheManager cacheManager = GuiceContext.get(CacheManager.class);
		HazelcastClientCacheManager hazelcastClientCacheManager = (HazelcastClientCacheManager) cacheManager;
		for (String cacheName : cacheManager.getCacheNames())
		{
			cacheManager.getCache(cacheName)
			            .clear();
			hazelcastClientCacheManager.removeCache(cacheName,false);
		}
	}
}
