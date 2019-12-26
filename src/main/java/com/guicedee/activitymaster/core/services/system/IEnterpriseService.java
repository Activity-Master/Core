package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.List;
import java.util.Set;

public interface IEnterpriseService
{
	List<IEnterprise<?>> findEnterprisesWithClassification(Classification classification);
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

	@SuppressWarnings({"unchecked", "rawtypes"})
	Set<IEnterpriseName<?>> getIEnterprises();

	@CacheResult
	IEnterpriseName<?> getIEnterprise(@CacheKey IEnterprise<?> enterprise);

	@CacheResult
	IEnterprise<?> getIEnterpriseFromName(@CacheKey IEnterpriseName<?> enterprise);
}
