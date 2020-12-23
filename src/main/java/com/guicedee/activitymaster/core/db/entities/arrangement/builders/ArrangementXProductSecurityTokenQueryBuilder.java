package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProductSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXProductSecurityTokenQueryBuilder, ArrangementXProductSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXProductSecurityToken_.base;
	}
}
