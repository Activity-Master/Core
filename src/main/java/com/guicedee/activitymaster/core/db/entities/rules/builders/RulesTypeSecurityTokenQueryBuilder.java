package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class RulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeSecurityTokenQueryBuilder, RulesTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeSecurityToken_.base;
	}
}
