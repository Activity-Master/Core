package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementTypeSecurityTokenQueryBuilder, ArrangementTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementTypeSecurityToken_.base;
	}
}
