package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassification;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesTypeClassification;

public interface IRulesType<J extends IRulesType<J>> extends INameAndDescription<J>,
                                                             IActivityMasterEntity<RulesType>,
                                                             IContainsClassifications<RulesType, Classification, RulesTypeXClassification, IRulesTypeClassification<?>,IRulesType<?>, IClassification<?>,RulesType>,
                                                             IHasActiveFlags<J>,
                                                             IContainsEnterprise<J>
{

}
