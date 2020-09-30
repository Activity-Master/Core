package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

public interface IProductType<J extends IProductType<J>> extends INameAndDescription<J>
	,IProductTypeValue
{

}
