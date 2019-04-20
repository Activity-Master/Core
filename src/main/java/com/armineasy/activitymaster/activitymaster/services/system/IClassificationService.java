package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IClassificationService
{

	@SuppressWarnings("Duplicates")
	@CacheResult(cacheName = "ClassificationFindWithIClassificationStringConceptValue")
	Classification find(@CacheKey IClassificationValue<?> name, @CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	Classification getHierarchyType(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "IdentityTypeClassification")
	Classification getIdentityType(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);
}
