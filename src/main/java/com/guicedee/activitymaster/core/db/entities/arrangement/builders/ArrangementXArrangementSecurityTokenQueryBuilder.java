package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangementSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangementSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXArrangementSecurityTokenQueryBuilder, ArrangementXArrangementSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXArrangementSecurityToken_.base;
	}
}
