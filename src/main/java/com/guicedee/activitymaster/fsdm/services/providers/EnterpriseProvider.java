package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.systems.SecurityTokenSystem;
//import jakarta.transaction.Transactional;
import lombok.extern.java.Log;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Log
public class EnterpriseProvider implements Provider<IEnterprise<Enterprise, EnterpriseQueryBuilder>>
{
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	@Inject
	private Provider<IEnterpriseService<?>> enterpriseService;
	
	public static IEnterprise<Enterprise, EnterpriseQueryBuilder> loadedEnterprise = null;
	
	@Override
	//@Transactional
	public IEnterprise<Enterprise, EnterpriseQueryBuilder> get()
	{
		if (loadedEnterprise != null)
		{
			return loadedEnterprise;
		}
		ActivityMasterConfiguration activityMasterConfiguration = configuration.get();
		if (!Strings.isNullOrEmpty(activityMasterConfiguration.getApplicationEnterpriseName()))
		{
			enterpriseService.get().isEnterpriseReady()
					.chain(enterprise->{
						EnterpriseProvider.loadedEnterprise = (IEnterprise<Enterprise, EnterpriseQueryBuilder>) enterprise;
						return null;
					})
					.await().atMost(Duration.of(50L, ChronoUnit.SECONDS));
		}
		if (loadedEnterprise == null)
		{
			return null;
		}
		return loadedEnterprise;
	}
}
