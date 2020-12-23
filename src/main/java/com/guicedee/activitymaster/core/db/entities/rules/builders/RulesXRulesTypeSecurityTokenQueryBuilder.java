package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRulesTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRulesTypeSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class RulesXRulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXRulesTypeSecurityTokenQueryBuilder, RulesXRulesTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXRulesTypeSecurityToken_.base;
	}
}
