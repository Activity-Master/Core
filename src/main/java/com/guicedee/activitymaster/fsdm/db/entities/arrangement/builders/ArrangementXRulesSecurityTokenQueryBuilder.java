package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXRulesSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXRulesSecurityToken_;
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
