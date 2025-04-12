package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXRulesSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXRulesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class RulesXRulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXRulesSecurityTokenQueryBuilder, RulesXRulesSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXRulesSecurityToken_.base;
	}
}
