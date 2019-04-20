package com.armineasy.activitymaster.activitymaster.services.classifications.events;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

public interface IEventTypeClassification<J extends Enum & IEventTypeClassification<J>> extends IClassificationValue<J>
{

}
