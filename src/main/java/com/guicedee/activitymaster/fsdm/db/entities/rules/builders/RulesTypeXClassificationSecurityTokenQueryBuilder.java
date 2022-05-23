package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesTypeXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesTypeXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeXClassificationSecurityTokenQueryBuilder, RulesTypeXClassificationSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeXClassificationSecurityToken_.base;
	}
}
