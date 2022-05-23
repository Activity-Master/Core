package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesSecurityTokenQueryBuilder, RulesSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesSecurityToken_.base;
	}
}
