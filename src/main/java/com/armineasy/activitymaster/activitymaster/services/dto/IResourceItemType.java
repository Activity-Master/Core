package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;

public interface IResourceItemType<J extends IResourceItemType<J>>
		extends INameAndDescription<ResourceItemType>,
				        IContainsEnterprise<ResourceItemType>,
				        IActivityMasterEntity<ResourceItemType>,
				        IContainsActiveFlags<ResourceItemType>
{
}
