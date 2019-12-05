package com.guicedee.activitymaster.core.db.entities.activeflag.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

@SuppressWarnings("Duplicates")
public class ActiveFlagQueryBuilder
		extends QueryBuilderNameDescription<ActiveFlagQueryBuilder, ActiveFlag, Long, ActiveFlagSecurityToken>
{

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inActiveRange(IEnterprise<?> enterprise)
	{
		return this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inVisibleRange(IEnterprise<?> enterprise)
	{
		return this;
	}
}
