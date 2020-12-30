package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesTypeXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesTypeXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesTypeXClassificationSecurityTokenQueryBuilder, RulesTypeXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesTypeXClassificationSecurityToken_.base;
	}
}
