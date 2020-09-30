package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class RulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesSecurityTokenQueryBuilder, RulesSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesSecurityToken_.base;
	}
}
