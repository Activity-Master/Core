package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import java.util.List;

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
}
