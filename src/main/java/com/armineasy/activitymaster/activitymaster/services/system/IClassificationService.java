package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IClassificationService<J extends IClassificationService<J>>
{

	@SuppressWarnings("Duplicates")
	@CacheResult(cacheName = "ClassificationFindWithIClassificationStringConceptValue")
	IClassification<?> find(@CacheKey IClassificationValue<?> name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	IClassification<?> getHierarchyType(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

	@CacheResult(cacheName = "IdentityTypeClassification")
	IClassification<?> getIdentityType(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);
}
