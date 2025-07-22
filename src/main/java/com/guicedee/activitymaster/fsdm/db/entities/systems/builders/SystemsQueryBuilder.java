package com.guicedee.activitymaster.fsdm.db.entities.systems.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystemsQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;

import java.util.UUID;

public class SystemsQueryBuilder
		extends QueryBuilderCore<SystemsQueryBuilder, Systems, UUID>
		implements IQueryBuilderClassifications<SystemsQueryBuilder, Systems, UUID>,
		           ISystemsQueryBuilder<SystemsQueryBuilder, Systems>,
		           IQueryBuilderDefault<SystemsQueryBuilder, Systems, UUID>,
		           IQueryBuilderEnterprise<SystemsQueryBuilder, Systems, UUID>,
		           IQueryBuilderNamesAndDescriptions<SystemsQueryBuilder,Systems,java.util.UUID>
{
}
