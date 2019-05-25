package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

public interface IContainsEnterprise<J extends IContainsEnterprise<J>>
{
	public Enterprise getEnterpriseID();

	default IEnterprise<?> getEnterprise()
	{
		return getEnterpriseID();
	}
}
