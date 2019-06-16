package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceTypeValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;


public interface IResourceItemService
{

	@Transactional(entityManagerAnnotation =  ActivityMasterDB.class)
	ResourceItem create(IResourceTypeValue<?> identityResourceType, String mimeType,
	                    ISystems system, UUID... identityToken);

	@CacheResult(cacheName = "ResourceItemFindByClassification")
	ResourceItem findByClassification(@CacheKey IResourceTypeValue<?> resourceType,
	                                  @CacheKey IResourceItemClassification<?> classification,
	                                  @CacheKey String value,
	                                  @CacheKey ISystems systems,
	                                  @CacheKey UUID... identityToken);
}
