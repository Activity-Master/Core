package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXRulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXRulesTypeSecurityTokenQueryBuilder, ArrangementXRulesTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXRulesTypeSecurityToken_.base;
	}
}
