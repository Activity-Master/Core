package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXArrangementsSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXArrangementsSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class RulesXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXArrangementSecurityTokenQueryBuilder, RulesXArrangementsSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXArrangementsSecurityToken_.base;
	}
}
