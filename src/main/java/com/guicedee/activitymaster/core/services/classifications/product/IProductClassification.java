package com.guicedee.activitymaster.core.services.classifications.product;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IProductClassification<J extends Enum<J> & IProductClassification<J>> extends IClassificationValue<J>
{

}
