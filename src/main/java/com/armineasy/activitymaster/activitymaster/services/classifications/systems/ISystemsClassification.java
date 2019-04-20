package com.armineasy.activitymaster.activitymaster.services.classifications.systems;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

public interface ISystemsClassification<J extends Enum & ISystemsClassification<J>> extends IClassificationValue<J>
{

}
