package com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlagQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;

@SuppressWarnings("Duplicates")
public class ActiveFlagQueryBuilder
		extends QueryBuilderNameDescription<ActiveFlagQueryBuilder, ActiveFlag, java.util.UUID>
		implements IActiveFlagQueryBuilder<ActiveFlagQueryBuilder, ActiveFlag>
{
	
	@jakarta.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inActiveRange(IEnterprise<?, ?> enterprise)
	{
		return this;
	}
	
	@jakarta.validation.constraints.NotNull
	public ActiveFlagQueryBuilder inVisibleRange(IEnterprise<?, ?> enterprise)
	{
		return this;
	}
}
