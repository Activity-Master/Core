package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXArrangementTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXArrangementTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXArrangementTypeSecurityTokenQueryBuilder, ArrangementXArrangementTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXArrangementTypeSecurityToken_.base;
	}
}
