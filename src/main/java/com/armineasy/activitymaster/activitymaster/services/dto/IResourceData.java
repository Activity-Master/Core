package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemData;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;

public interface IResourceData<J extends IResourceData<J>>
	extends IContainsClassifications<ResourceItemData, Classification, ResourceItemDataXClassification, IResourceItemClassification<?>,IResourceData<?>,IClassification<?>, ResourceItemData>
{
}
