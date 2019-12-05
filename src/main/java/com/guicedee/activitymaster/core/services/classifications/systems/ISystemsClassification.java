package com.guicedee.activitymaster.core.services.classifications.systems;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface ISystemsClassification<J extends Enum<J> & ISystemsClassification<J>> extends IClassificationValue<J>
{

}
