package com.guicedee.activitymaster.fsdm.injections;

import com.google.inject.Inject;
//import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

@Log
public class DataCleanupPostStartup
{
	@Inject
	private DataCleanupService dataCleanupService;
	
	//@Transactional
	public void postLoad()
	{
		dataCleanupService.cleanup();
		
	}
	
}
