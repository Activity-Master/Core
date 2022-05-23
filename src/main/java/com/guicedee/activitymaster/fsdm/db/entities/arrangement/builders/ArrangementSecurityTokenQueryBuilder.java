package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementSecurityTokenQueryBuilder, ArrangementSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementSecurityToken_.base;
	}
}
