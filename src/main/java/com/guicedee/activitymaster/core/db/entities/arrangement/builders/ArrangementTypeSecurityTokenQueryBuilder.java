package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementTypeSecurityTokenQueryBuilder, ArrangementTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementTypeSecurityToken_.base;
	}
}
