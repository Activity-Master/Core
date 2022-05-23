package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXResourceItemSecurityTokenQueryBuilder, ArrangementXResourceItemSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXResourceItemSecurityToken_.base;
	}
}
