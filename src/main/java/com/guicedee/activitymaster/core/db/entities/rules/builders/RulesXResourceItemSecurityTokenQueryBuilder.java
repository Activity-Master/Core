package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class RulesXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXResourceItemSecurityTokenQueryBuilder, RulesXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXResourceItemSecurityToken_.base;
	}
}
