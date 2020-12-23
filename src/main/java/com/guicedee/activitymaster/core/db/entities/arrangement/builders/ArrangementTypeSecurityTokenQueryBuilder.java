package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementTypeSecurityTokenQueryBuilder, ArrangementTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementTypeSecurityToken_.base;
	}
}
