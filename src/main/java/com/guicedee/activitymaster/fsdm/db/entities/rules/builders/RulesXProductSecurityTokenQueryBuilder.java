package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXProductSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXProductSecurityTokenQueryBuilder, RulesXProductSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXProductSecurityToken_.base;
	}
}
