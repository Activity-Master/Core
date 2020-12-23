package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ArrangementTypeXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementTypeXClassificationSecurityTokenQueryBuilder, ArrangementTypeXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementTypeXClassificationSecurityToken_.base;
	}
}
