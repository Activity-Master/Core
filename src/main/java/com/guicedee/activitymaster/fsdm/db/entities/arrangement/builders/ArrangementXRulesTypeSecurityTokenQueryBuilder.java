package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXRulesTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXRulesTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ArrangementXRulesTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXRulesTypeSecurityTokenQueryBuilder, ArrangementXRulesTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXRulesTypeSecurityToken_.base;
	}
}
