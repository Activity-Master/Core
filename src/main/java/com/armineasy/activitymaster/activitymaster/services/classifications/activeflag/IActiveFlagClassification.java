package com.armineasy.activitymaster.activitymaster.services.classifications.activeflag;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

public interface IActiveFlagClassification<J extends Enum & IActiveFlagClassification<J>> extends IClassificationValue<J>
{
}
