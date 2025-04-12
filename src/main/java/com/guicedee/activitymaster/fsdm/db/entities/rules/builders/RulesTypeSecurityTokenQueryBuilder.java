package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class RulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeSecurityTokenQueryBuilder, RulesTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeSecurityToken_.base;
	}
}
