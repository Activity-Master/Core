package com.guicedee.activitymaster.fsdm.services;

import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import jakarta.cache.CacheManager;

public class ActivityMasterSystemsManager
{
	private ActivityMasterSystemsManager()
	{
	}
	
	/**
	 * Wipe all caches? Haven't serialVersion'd to incremental yet so probably a good idea
	 */
	public static void wipeCaches()
	{
		LogFactory.getLog("ActivityMasterSystemsManager")
		          .info("Wiping Caches");
		CacheManager cacheManager = GuiceContext.get(CacheManager.class);
		for (String cacheName : cacheManager.getCacheNames())
		{
			cacheManager.getCache(cacheName)
			            .clear();
		}
	}
}
