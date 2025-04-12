package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;

import java.util.UUID;

public class RulesQueryBuilder
		extends QueryBuilderSCD<RulesQueryBuilder, Rules, UUID,RulesSecurityTokenQueryBuilder>
		implements IRulesQueryBuilder<RulesQueryBuilder, Rules>,
		           IQueryBuilderNamesAndDescriptions<RulesQueryBuilder,Rules, UUID>
{

}
