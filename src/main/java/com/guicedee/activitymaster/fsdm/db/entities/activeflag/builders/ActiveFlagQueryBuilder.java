package com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlagQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;

import java.util.UUID;

@SuppressWarnings("Duplicates")
public class ActiveFlagQueryBuilder
		extends QueryBuilderCore<ActiveFlagQueryBuilder, ActiveFlag, UUID>
		implements IActiveFlagQueryBuilder<ActiveFlagQueryBuilder, ActiveFlag>,
		           IQueryBuilderNamesAndDescriptions<ActiveFlagQueryBuilder,ActiveFlag,java.util.UUID>
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
