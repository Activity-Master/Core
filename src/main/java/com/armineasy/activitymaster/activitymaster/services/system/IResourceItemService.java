package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;


public interface IResourceItemService<J extends IResourceItemService<J>>
{
	IResourceItemType<?> createType(IResourceType<?> value, ISystems<?> system, UUID... identityToken);

	IResourceItem<?> create(IResourceType<?> identityResourceType, String mimeType,
	                        ISystems<?> system, UUID... identityToken);

	IResourceItem<?> findByClassification( IResourceType<?> resourceType,
	                                   IResourceItemClassification<?> classification,
	                                   String value,
	                                   ISystems<?> systems,
	                                   UUID... identityToken);

	byte[] getDataForResourceItemValue(IRelationshipValue<IResourceItem<?>, IResourceData<?>, ?> data);

	IResourceItemType<?> findResourceItemType(IResourceType<?> type, ISystems<?> systems, UUID... identityToken);
}
