package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;

public interface IResourceItem<J extends IResourceItem<J>>
	extends IContainsClassifications<ResourceItem, Classification, ResourceItemXClassification, IResourceItemClassification<?>, ResourceItem>,
			        IContainsResourceItemTypes<ResourceItem, ResourceItemType, ResourceItemXResourceItemType, IResourceType<?>,ResourceItem>,
			        IActivityMasterEntity<ResourceItem>,
			        IContainsActiveFlags<ResourceItem>,
			        IContainsEnterprise<ResourceItem>
{
}
