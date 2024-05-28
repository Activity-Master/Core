package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;

public class RulesQueryBuilder
		extends QueryBuilderSCD<RulesQueryBuilder, Rules, String,RulesSecurityTokenQueryBuilder>
		implements IRulesQueryBuilder<RulesQueryBuilder, Rules>,
		           IQueryBuilderNamesAndDescriptions<RulesQueryBuilder,Rules,String>
{

}
