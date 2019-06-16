package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;

public interface IEventClassification<J extends Enum & IEventClassification<J>> extends IClassificationValue<J>
{

}
