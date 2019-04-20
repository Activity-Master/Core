package com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseSecurityToken;

public class EnterpriseQueryBuilder
		extends QueryBuilderNameDescription<EnterpriseQueryBuilder, Enterprise, Long, EnterpriseSecurityToken>
{
	@Override
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public EnterpriseQueryBuilder withEnterprise(Enterprise enterprise)
	{
		findByName(enterprise.getName());
		return this;
	}
}
