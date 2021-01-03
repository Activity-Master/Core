package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesTypeXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeXResourceItemSecurityTokenQueryBuilder, RulesTypeXResourceItemSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeXResourceItemSecurityToken_.base;
	}
}
