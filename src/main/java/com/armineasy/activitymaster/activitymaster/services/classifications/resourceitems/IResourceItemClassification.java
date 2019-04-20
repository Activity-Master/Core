package com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

public interface IResourceItemClassification<J extends Enum & IResourceItemClassification<J>> extends IClassificationValue<J>
{
}
