package com.guicedee.activitymaster.fsdm.services;

import lombok.extern.java.Log;

@Log
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
		log.info("Wiping Caches");
		/*CacheManager cacheManager = com.guicedee.client.IGuiceContext.get(CacheManager.class);
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
		}*/
	}
}
