package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;

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
