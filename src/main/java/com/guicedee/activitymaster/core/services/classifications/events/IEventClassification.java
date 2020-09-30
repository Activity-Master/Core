package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;

public interface IEventClassification<J extends Enum<J> & IEventClassification<J>>
		extends IClassificationValue<J>,
		        IEventTypeValue<J>
{

}
