package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.List;
import java.util.Optional;

public interface IEnterpriseService
{
	List<Enterprise> findEnterprisesWithClassification(Classification classification);
	/**
	 * Gets an enterprise or throws an exception.
	 *
	 * Result is cached
	 *
	 * @param name the name of the enterprise
	 *
	 * @return The enterprise
	 */
	Enterprise getEnterprise(@CacheKey IEnterpriseName<?> name);
}
