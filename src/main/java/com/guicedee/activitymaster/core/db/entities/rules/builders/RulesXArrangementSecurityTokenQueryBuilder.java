package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXArrangementsSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXArrangementsSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXArrangementSecurityTokenQueryBuilder, RulesXArrangementsSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXArrangementsSecurityToken_.base;
	}
}
