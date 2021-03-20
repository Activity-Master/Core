package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.client.services.builders.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystemsQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;

import java.util.UUID;

public class SystemsQueryBuilder
		extends QueryBuilderNameDescription<SystemsQueryBuilder, Systems, java.util.UUID>
		implements IQueryBuilderClassifications<SystemsQueryBuilder,Systems, UUID>,
		           ISystemsQueryBuilder<SystemsQueryBuilder, Systems>,
		           IQueryBuilderDefault<SystemsQueryBuilder,Systems,UUID>,
		           IQueryBuilderEnterprise<SystemsQueryBuilder,Systems,UUID>
{
}
