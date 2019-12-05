package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;

public interface IResourceItemType<J extends IResourceItemType<J>>
		extends INameAndDescription<ResourceItemType>,
				        IContainsEnterprise<ResourceItemType>,
				        IActivityMasterEntity<ResourceItemType>,
				        IContainsActiveFlags<ResourceItemType>
{
}
