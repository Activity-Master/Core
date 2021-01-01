package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import com.guicedee.activitymaster.core.updates.ISystemUpdate;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IEnterpriseService
{
	void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
	LocalDate getEnterpriseLastUpdateDate(IEnterprise<?> enterprise);
	
	Map<LocalDate, Class<? extends ISystemUpdate>> getUpdates(LocalDate lastUpdateDate);
	
	List<IEnterprise<?>> findEnterprisesWithClassification(Classification classification);
	
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseNameString")
	IEnterprise<?> getEnterprise(@CacheKey String name);
	
	/**
	 * Gets an enterprise or throws an exception.
	 *
	 * Result is cached
	 *
	 * @param name the name of the enterprise
	 *
	 * @return The enterprise
	 */
	IEnterprise<?> getEnterprise(IEnterpriseName<?> name);

	Set<IEnterpriseName<?>> getIEnterprises();

	@CacheResult
	IEnterpriseName<?> getIEnterprise(@CacheKey IEnterprise<?> enterprise);

	@CacheResult
	IEnterprise<?> getIEnterpriseFromName(@CacheKey IEnterpriseName<?> enterprise);
}
