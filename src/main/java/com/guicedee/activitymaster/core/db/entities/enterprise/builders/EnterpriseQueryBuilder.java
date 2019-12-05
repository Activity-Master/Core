package com.guicedee.activitymaster.core.db.entities.enterprise.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseSecurityToken;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

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
