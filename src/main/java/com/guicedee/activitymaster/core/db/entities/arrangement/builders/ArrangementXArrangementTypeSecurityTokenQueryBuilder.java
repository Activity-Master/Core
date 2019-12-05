package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangementTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangementTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXArrangementTypeSecurityTokenQueryBuilder, ArrangementXArrangementTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXArrangementTypeSecurityToken_.base;
	}
}
