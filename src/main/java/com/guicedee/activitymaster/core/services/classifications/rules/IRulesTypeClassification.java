package com.guicedee.activitymaster.core.services.classifications.rules;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;

public interface IRulesTypeClassification<J extends Enum<J> & IRulesTypeClassification<J>> extends IClassificationValue<J>,
                                                                                                   IRulesTypeValue<J>
{

}
