package com.guicedee.activitymaster.fsdm.db.entities.systems.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystemsQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;

public class SystemsQueryBuilder
		extends QueryBuilderNameDescription<SystemsQueryBuilder, Systems, java.lang.String>
		implements IQueryBuilderClassifications<SystemsQueryBuilder, Systems, java.lang.String>,
		           ISystemsQueryBuilder<SystemsQueryBuilder, Systems>,
		           IQueryBuilderDefault<SystemsQueryBuilder, Systems, java.lang.String>,
		           IQueryBuilderEnterprise<SystemsQueryBuilder, Systems, java.lang.String>
{
}
