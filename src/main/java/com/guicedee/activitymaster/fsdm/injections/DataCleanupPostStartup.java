package com.guicedee.activitymaster.fsdm.injections;

import com.google.inject.Inject;
import com.guicedee.guicedinjection.interfaces.IGuicePostStartup;
import lombok.extern.java.Log;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log
public class DataCleanupPostStartup implements IGuicePostStartup<DataCleanupPostStartup>
{
	@Inject
	private DataCleanupService dataCleanupService;
	
	@Override
	public List<CompletableFuture<Boolean>> postLoad()
	{
		return List.of(CompletableFuture.supplyAsync(() -> {
			try
			{
				dataCleanupService.cleanup();
				return true;
			}catch (Throwable T)
			{
				T.printStackTrace();
				return false;
			}
		}, getExecutorService()));
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 50;
	}
}
