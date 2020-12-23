package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRulesSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class RulesXRulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXRulesSecurityTokenQueryBuilder, RulesXRulesSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXRulesSecurityToken_.base;
	}
}
