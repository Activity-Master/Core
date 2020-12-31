package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.services.dto.IActiveFlag;

import jakarta.validation.constraints.NotNull;

public interface IContainsActiveFlags<J extends IContainsActiveFlags<J>>
{
	IActiveFlag<?> getActiveFlagID();
	
	@NotNull String getOriginalSourceSystemUniqueID();
	
}
