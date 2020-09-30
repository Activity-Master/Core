package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class RulesXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXClassificationSecurityTokenQueryBuilder, RulesXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXClassificationSecurityToken_.base;
	}
}
