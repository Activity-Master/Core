package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IEventClassification<J extends Enum<J> & IEventClassification<J>> extends IClassificationValue<J>
{

}
