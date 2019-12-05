package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemData;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataXClassification;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;

public interface IResourceData<J extends IResourceData<J>>
	extends IContainsClassifications<ResourceItemData, Classification, ResourceItemDataXClassification, IResourceItemClassification<?>,IResourceData<?>,IClassification<?>, ResourceItemData>
	, IContainsEnterprise<IResourceData<J>>
	, IActivityMasterEntity<IResourceData<J>>
{
}
