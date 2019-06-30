package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;

import java.io.Serializable;

public interface IClassificationDataConcept<J extends IClassificationDataConcept<J>>
		extends Serializable,
				        //IContainsClassifications<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification, IClassificationClassificationDataConceptTypes<?>>,
				        IContainsResourceItems<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem, IResourceType<?>, IResourceItemClassification<?>,ClassificationDataConcept>,
				        IActivityMasterEntity<ClassificationDataConcept>

{
}
