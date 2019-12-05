package com.guicedee.activitymaster.core.services.classifications.arrangement;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IArrangementClassification<J extends Enum<J> & IArrangementClassification<J>> extends IClassificationValue<J>
{

}
