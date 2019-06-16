package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IEventTypeValue;

public interface IEventTypeClassification<J extends Enum & IEventTypeClassification<J>> extends IClassificationValue<J>, IEventTypeValue<J>
{

}
