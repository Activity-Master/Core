package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXProductSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXProductSecurityTokenQueryBuilder, ArrangementXProductSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXProductSecurityToken_.base;
	}
}
