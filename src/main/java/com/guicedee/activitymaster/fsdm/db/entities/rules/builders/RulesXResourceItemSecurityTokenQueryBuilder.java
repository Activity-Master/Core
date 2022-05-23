package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXResourceItemSecurityTokenQueryBuilder, RulesXResourceItemSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXResourceItemSecurityToken_.base;
	}
}
