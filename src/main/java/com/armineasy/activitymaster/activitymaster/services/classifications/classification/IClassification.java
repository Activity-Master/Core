package com.armineasy.activitymaster.activitymaster.services.classifications.classification;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

public interface IClassification<J extends Enum & IClassification<J>> extends IClassificationValue<J>
{
}
