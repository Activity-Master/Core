package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.services.dto.IEnterprise;

public interface IContainsEnterprise<J extends IContainsEnterprise<J>>
{
	public IEnterprise<?> getEnterpriseID();

	default IEnterprise<?> getEnterprise()
	{
		return getEnterpriseID();
	}
}
