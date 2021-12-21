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
		try
		{
			for (String cacheName : cacheManager.getCacheNames())
			{
				try
				{
					cacheManager.getCache(cacheName)
					            .clear();
				}
				catch (Exception e)
				{
					//ignore failed clear
				}
			}
		}
		catch (Throwable T)
		{
			//ignore failed clear
		}
	}
}
