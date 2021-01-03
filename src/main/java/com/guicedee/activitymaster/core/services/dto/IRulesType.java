package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesTypeClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IRulesType<J extends IRulesType<J>> extends IContainsNameAndDescription<J>,
                                                             IActivityMasterEntity<RulesType>,
                                                             IContainsClassifications<RulesType, Classification, RulesTypeXClassification, IRulesTypeClassification<?>,IRulesType<?>, IClassification<?>,RulesType>,
                                                             IContainsActiveFlags<J>,
                                                             IContainsEnterprise<J>,
                                                             IContainsResourceItems<RulesType, ResourceItem, RulesTypeXResourceItem, IClassificationValue<?>,IRulesType<?>,IResourceItem<?>,J>
{

}
