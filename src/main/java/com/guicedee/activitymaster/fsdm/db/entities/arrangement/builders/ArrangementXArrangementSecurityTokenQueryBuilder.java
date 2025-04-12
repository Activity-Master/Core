package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXArrangementSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXArrangementSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ArrangementXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXArrangementSecurityTokenQueryBuilder, ArrangementXArrangementSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXArrangementSecurityToken_.base;
	}
}
