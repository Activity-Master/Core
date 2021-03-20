package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.rules.IRulesQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;

public class RulesQueryBuilder
		extends QueryBuilderSCDNameDescription<RulesQueryBuilder, Rules, java.util.UUID>
		implements IRulesQueryBuilder<RulesQueryBuilder,Rules>
{

}
