package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementSecurityTokenQueryBuilder, ArrangementSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementSecurityToken_.base;
	}
}
