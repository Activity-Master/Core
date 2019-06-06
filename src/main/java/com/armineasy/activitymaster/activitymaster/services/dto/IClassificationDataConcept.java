package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;

import java.io.Serializable;

public interface IClassificationDataConcept<J extends IClassificationDataConcept<J>>
		extends Serializable, IContainsClassifications<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification, IClassificationClassificationDataConceptType<?>>,
				        IContainsResourceItems<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem>,
				        IActivityMasterEntity<ClassificationDataConcept>

{
}
