package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.ClassificationHierarchyView;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;

public interface IClassification<J extends IClassification<J>>
		extends IContainsHierarchy<Classification, ClassificationXClassification, ClassificationHierarchyView>,
				        IContainsResourceItems<Classification, ResourceItem, ClassificationXResourceItem>,
				        IActivityMasterEntity<Classification>,
				        IContainsEnterprise<J>,
				        INameAndDescription<Classification>
{

}
