package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;


public interface IResourceItemService<J extends IResourceItemService<J>>
{
	IResourceItemType<?> createType(IResourceType<?> value, ISystems<?> system, UUID... identityToken);

	IResourceItem<?> create(IResourceType<?> identityResourceType, String mimeType,
	                        ISystems system, UUID... identityToken);

	IResourceItem<?> findByClassification( IResourceType<?> resourceType,
	                                   IResourceItemClassification<?> classification,
	                                   String value,
	                                   ISystems systems,
	                                   UUID... identityToken);

	IResourceItemType<?> findResourceItemType( IResourceType<?> type,  ISystems systems,  UUID... identityToken);
}
