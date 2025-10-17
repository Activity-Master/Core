package com.guicedee.activitymaster.fsdm.services;

import lombok.extern.log4j.Log4j2;

@Log4j2
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
