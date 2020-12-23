package com.guicedee.activitymaster.core.db.entities.activeflag.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

@SuppressWarnings("Duplicates")
public class ActiveFlagQueryBuilder
		extends QueryBuilderNameDescription<ActiveFlagQueryBuilder, ActiveFlag, java.util.UUID, ActiveFlagSecurityToken>
{

	@jakarta.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inActiveRange(IEnterprise<?> enterprise)
	{
		return this;
	}

	@jakarta.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inVisibleRange(IEnterprise<?> enterprise)
	{
		return this;
	}
}
