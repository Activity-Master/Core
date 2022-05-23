package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXRulesTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXRulesTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesXRulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXRulesTypeSecurityTokenQueryBuilder, RulesXRulesTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXRulesTypeSecurityToken_.base;
	}
}
