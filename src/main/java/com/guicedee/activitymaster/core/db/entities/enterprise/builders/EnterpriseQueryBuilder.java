package com.guicedee.activitymaster.core.db.entities.enterprise.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseSecurityToken;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassification;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

public class EnterpriseQueryBuilder
		extends QueryBuilderNameDescription<EnterpriseQueryBuilder, Enterprise, Long, EnterpriseSecurityToken>
		implements IContainsClassificationsQueryBuilder<EnterpriseQueryBuilder, Enterprise, Long, EnterpriseXClassification>
{
	@Override
	@javax.validation.constraints.NotNull
	public EnterpriseQueryBuilder withEnterprise(IEnterprise<?> enterprise)
	{
		withName(enterprise.getName());
		return this;
	}
}
