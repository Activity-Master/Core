package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXArrangementSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXArrangementSecurityToken_;
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
