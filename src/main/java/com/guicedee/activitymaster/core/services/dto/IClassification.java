package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.hierarchies.ClassificationHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.capabilities.*;

public interface IClassification<J extends IClassification<J>>
		extends IContainsHierarchy<Classification, ClassificationXClassification, ClassificationHierarchyView>,
				        IContainsResourceItems<Classification, ResourceItem, ClassificationXResourceItem, IResourceItemClassification<?>, IClassification<?>, IResourceItem<?>,Classification>,
				        IActivityMasterEntity<Classification>,
				        IContainsEnterprise<J>,
				        INameAndDescription<Classification>
{

}
