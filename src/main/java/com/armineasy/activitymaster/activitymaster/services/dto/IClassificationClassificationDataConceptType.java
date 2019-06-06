package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

public interface IClassificationClassificationDataConceptType<J extends Enum & IClassificationClassificationDataConceptType<J>>
		extends IClassificationValue<J>, IClassificationDataConceptValue<J>
{
}
