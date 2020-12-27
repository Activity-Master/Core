package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXRulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXRulesSecurityTokenQueryBuilder, ArrangementXRulesSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXRulesSecurityToken_.base;
	}
}
