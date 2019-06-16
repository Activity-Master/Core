package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.services.dto.IActiveFlag;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

public interface IContainsActiveFlags<J extends IContainsActiveFlags<J>>
{
	IActiveFlag<?> getActiveFlagID();
}
