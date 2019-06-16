package com.armineasy.activitymaster.activitymaster.services.classifications.product;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;

public interface IProductClassification<J extends Enum & IProductClassification<J>> extends IClassificationValue<J>
{

}
