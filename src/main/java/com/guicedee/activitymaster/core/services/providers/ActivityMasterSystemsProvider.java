package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Provider;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IDefaultService;

import java.util.ServiceLoader;
import java.util.Set;

public class ActivityMasterSystemsProvider implements Provider<Set<IActivityMasterSystem>>
{
	@Override
	public Set<IActivityMasterSystem> get()
	{
		return IDefaultService.loaderToSet(ServiceLoader.load(IActivityMasterSystem.class));
	}
}
