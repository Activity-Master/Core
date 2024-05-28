package com.guicedee.activitymaster.fsdm.db.entities.systems.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystemsQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;

public class SystemsQueryBuilder
		extends QueryBuilderCore<SystemsQueryBuilder, Systems, String>
		implements IQueryBuilderClassifications<SystemsQueryBuilder, Systems, java.lang.String>,
		           ISystemsQueryBuilder<SystemsQueryBuilder, Systems>,
		           IQueryBuilderDefault<SystemsQueryBuilder, Systems, java.lang.String>,
		           IQueryBuilderEnterprise<SystemsQueryBuilder, Systems, java.lang.String>,
		           IQueryBuilderNamesAndDescriptions<SystemsQueryBuilder,Systems,String>
{
}
