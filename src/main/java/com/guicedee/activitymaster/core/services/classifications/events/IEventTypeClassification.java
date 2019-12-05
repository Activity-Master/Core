package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;

public interface IEventTypeClassification<J extends Enum<J> & IEventTypeClassification<J>> extends IClassificationValue<J>, IEventTypeValue<J>
{

}
