package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeSecurityTokenQueryBuilder, RulesTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeSecurityToken_.base;
	}
}
