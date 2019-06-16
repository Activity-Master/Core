package com.armineasy.activitymaster.activitymaster.services.classifications.arrangement;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;

public interface IArrangementClassification<J extends Enum & IArrangementClassification<J>> extends IClassificationValue<J>
{

}
