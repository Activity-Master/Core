package com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;

public interface IResourceItemClassification<J extends Enum & IResourceItemClassification<J>> extends IClassificationValue<J>
{
}
