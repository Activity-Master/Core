package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXProductSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class RulesXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXProductSecurityTokenQueryBuilder, RulesXProductSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXProductSecurityToken_.base;
	}
}
