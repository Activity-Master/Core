package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXRulesTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXRulesTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXRulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXRulesTypeSecurityTokenQueryBuilder, ArrangementXRulesTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXRulesTypeSecurityToken_.base;
	}
}
