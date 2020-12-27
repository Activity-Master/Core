package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.hierarchies.ResourceItemHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;

public interface IResourceItem<J extends IResourceItem<J>>
		extends IContainsClassifications<ResourceItem, Classification, ResourceItemXClassification, IClassificationValue<?>, IResourceItem<?>, IClassification<?>, ResourceItem>,
		        IContainsResourceItemTypes<ResourceItem, ResourceItemType, ResourceItemXResourceItemType, IResourceType<?>, ResourceItem>,
		        IActivityMasterEntity<ResourceItem>,
		        IHasActiveFlags<ResourceItem>,
		        IContainsData<ResourceItem>,
		        IContainsHierarchy<ResourceItem, ResourceItemXResourceItem, ResourceItemHierarchyView,IResourceItem<?>>,
		        IContainsResourceItems<ResourceItem,ResourceItem,ResourceItemXResourceItem,IClassificationValue<?>,IResourceItem<?>,IResourceItem<?>,ResourceItem>,
		        IContainsEnterprise<ResourceItem>
{
	String getResourceItemDataType();
}
