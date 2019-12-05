package com.guicedee.activitymaster.core.services.classifications.activeflag;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IActiveFlagClassification<J extends Enum<J> & IActiveFlagClassification<J>> extends IClassificationValue<J>
{
}
