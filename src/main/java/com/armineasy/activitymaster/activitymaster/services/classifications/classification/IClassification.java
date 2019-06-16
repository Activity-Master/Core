package com.armineasy.activitymaster.activitymaster.services.classifications.classification;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;

public interface IClassification<J extends Enum & IClassification<J>> extends IClassificationValue<J>
{
}
