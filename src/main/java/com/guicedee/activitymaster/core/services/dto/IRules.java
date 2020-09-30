package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.capabilities.IContainsRulesTypes;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;

public interface IRules<J extends IRules<J>>
		extends IContainsClassifications<Rules, Classification, RulesXClassification, IRulesClassification<?>, IRules<?>, IClassification<?>, Rules>,
		        IContainsResourceItems<Rules, ResourceItem, RulesXResourceItem, IClassificationValue<?>, IRules<?>, IResourceItem<?>, Rules>,
		        IContainsRulesTypes<Rules, RulesType, RulesXRulesType, IRulesClassification<?>, IRulesTypeValue<?>, IRules<?>, IRulesType<?>, Rules>,
		        IActivityMasterEntity<Rules>
{
}
