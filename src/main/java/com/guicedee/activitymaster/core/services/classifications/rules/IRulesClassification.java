package com.guicedee.activitymaster.core.services.classifications.rules;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IRulesClassification<J extends Enum<J> & IRulesClassification<J>> extends IClassificationValue<J>
{

}
