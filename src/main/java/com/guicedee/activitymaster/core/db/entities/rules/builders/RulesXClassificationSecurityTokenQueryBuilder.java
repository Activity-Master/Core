package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class RulesXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXClassificationSecurityTokenQueryBuilder, RulesXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXClassificationSecurityToken_.base;
	}
}
