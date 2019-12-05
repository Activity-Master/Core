package com.guicedee.activitymaster.core.services.classifications.resourceitems;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IResourceItemClassification<J extends Enum<J> & IResourceItemClassification<J>>
		extends IClassificationValue<J>
{
}
