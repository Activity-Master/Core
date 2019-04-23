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

	Enterprise getEnterprise(@CacheKey IEnterpriseName<?> name);
}
