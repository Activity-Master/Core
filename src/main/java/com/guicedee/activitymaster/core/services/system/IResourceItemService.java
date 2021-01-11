package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface IResourceItemService<J extends IResourceItemService<J>>
{
	IResourceItemType<?> createType(IResourceType<?> value, ISystems<?> system, UUID... identityToken);
	
	IResourceItemType<?> createType(String value, String description, ISystems<?> system, UUID... identityToken);
	
	IResourceItem<?> create(IResourceType<?> identityResourceType, String resourceItemDataValue,
	                        ISystems<?> system, UUID... identityToken);
	
	IResourceItem<?> create(String identityResourceType, String resourceItemDataValue,
	                        ISystems<?> system, UUID... identityToken);
	
	IResourceItem<?> create(String identityResourceType, String resourceItemDataValue, String originalSourceSystemUniqueID,
	                        LocalDateTime effectiveFromDate,
	                        ISystems<?> system, UUID... identityToken);
	
	IResourceItem<?> findByClassification(IResourceType<?> resourceType,
	                                      IResourceItemClassification<?> classification,
	                                      String value,
	                                      ISystems<?> systems,
	                                      UUID... identityToken);
	
	IResourceItem<?> findByClassification(IResourceType<?> resourceType,
	                                      String classification,
	                                      String value,
	                                      ISystems<?> systems,
	                                      UUID... identityToken);
	
	IResourceItem<?> findByUUID(UUID uuid,
	                            ISystems<?> systems,
	                            UUID... identityToken);
	
	IResourceItem<?> findByOriginalSourceUniqueID(String originalSourceUniqueID,
	                                              ISystems<?> systems,
	                                              UUID... identityToken);
	
	byte[] getDataForResourceItemValue(IRelationshipValue<IResourceItem<?>, IResourceData<?>, ?> data);
	

	IResourceItemType<?> findResourceItemType(IResourceType<?> type, ISystems<?> system, UUID... identityToken);
	
	IResourceItemType<?> findResourceItemType(String type, ISystems<?> system, UUID... identityToken);
	
	List<IResourceItem<?>> findByResourceItemType(String type, ISystems<?> systems, UUID... identityToken);
	
	List<IResourceItem<?>> findByResourceItemType(@CacheKey String type, String value, @CacheKey ISystems<?> systems, @CacheKey UUID... identityToken);
}
