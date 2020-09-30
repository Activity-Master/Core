package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassification;

public class RulesQueryBuilder
		extends QueryBuilderSCDNameDescription<RulesQueryBuilder, Rules, Long, RulesSecurityToken>
		implements IContainsClassificationsQueryBuilder<RulesQueryBuilder,Rules,Long, RulesXClassification>
{

}
