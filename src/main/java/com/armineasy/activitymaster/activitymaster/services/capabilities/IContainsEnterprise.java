package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

public interface IContainsEnterprise<J extends IContainsEnterprise<J>>
{
	public IEnterprise<?> getEnterpriseID();

	default IEnterprise<?> getEnterprise()
	{
		return getEnterpriseID();
	}
}
