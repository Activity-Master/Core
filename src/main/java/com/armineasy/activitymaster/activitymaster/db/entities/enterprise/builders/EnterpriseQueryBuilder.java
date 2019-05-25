package com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseSecurityToken;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

public class EnterpriseQueryBuilder
		extends QueryBuilderNameDescription<EnterpriseQueryBuilder, Enterprise, Long, EnterpriseSecurityToken>
{
	@Override
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public EnterpriseQueryBuilder withEnterprise(IEnterprise enterprise)
	{
		findByName(enterprise.getName());
		return this;
	}
}
