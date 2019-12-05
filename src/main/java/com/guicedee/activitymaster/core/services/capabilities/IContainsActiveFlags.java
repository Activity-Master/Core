package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.services.dto.IActiveFlag;

public interface IContainsActiveFlags<J extends IContainsActiveFlags<J>>
{
	IActiveFlag<?> getActiveFlagID();
}
