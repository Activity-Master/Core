package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

import java.io.Serializable;

public interface IClassificationDataConcept<J extends IClassificationDataConcept<J>>
		extends Serializable,
				        //IContainsClassifications<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification, IClassificationClassificationDataConceptTypes<?>>,
				        IContainsResourceItems<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem, IClassificationValue<?>,IClassificationDataConcept<?>, IResourceItem<?>, ClassificationDataConcept>,
				        IActivityMasterEntity<ClassificationDataConcept>

{
}
