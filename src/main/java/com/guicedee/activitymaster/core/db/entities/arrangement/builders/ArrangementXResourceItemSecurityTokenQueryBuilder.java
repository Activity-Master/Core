package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXResourceItemSecurityTokenQueryBuilder, ArrangementXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXResourceItemSecurityToken_.base;
	}
}
