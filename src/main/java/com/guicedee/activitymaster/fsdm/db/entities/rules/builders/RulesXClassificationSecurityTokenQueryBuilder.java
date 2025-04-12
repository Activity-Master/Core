package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class RulesXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXClassificationSecurityTokenQueryBuilder, RulesXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXClassificationSecurityToken_.base;
	}
}
