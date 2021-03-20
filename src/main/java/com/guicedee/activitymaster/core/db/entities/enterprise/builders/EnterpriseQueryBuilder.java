package com.guicedee.activitymaster.core.db.entities.enterprise.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;

public class EnterpriseQueryBuilder
		extends QueryBuilderNameDescription<EnterpriseQueryBuilder, Enterprise, java.util.UUID>
		implements
		           IEnterpriseQueryBuilder<EnterpriseQueryBuilder,Enterprise>
{
	@jakarta.validation.constraints.NotNull
	public EnterpriseQueryBuilder withEnterprise(Enterprise enterprise)
	{
		withName(enterprise.getName());
		return this;
	}
}
