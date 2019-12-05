package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemType;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.capabilities.*;

public interface IResourceItem<J extends IResourceItem<J>>
		extends IContainsClassifications<ResourceItem, Classification, ResourceItemXClassification, IResourceItemClassification<?>, IResourceItem<?>, IClassification<?>, ResourceItem>,
				        IContainsResourceItemTypes<ResourceItem, ResourceItemType, ResourceItemXResourceItemType, IResourceType<?>, ResourceItem>,
				        IActivityMasterEntity<ResourceItem>,
				        IContainsActiveFlags<ResourceItem>,
				        IContainsData<ResourceItem>,
				        IContainsEnterprise<ResourceItem>
{
}
