package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXClassificationSecurityTokenQueryBuilder, ArrangementXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXClassificationSecurityToken_.base;
	}
}
