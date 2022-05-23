package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesTypeXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeXResourceItemSecurityTokenQueryBuilder, RulesTypeXResourceItemSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeXResourceItemSecurityToken_.base;
	}
}
