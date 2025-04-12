package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementSecurityTokenQueryBuilder, ArrangementSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementSecurityToken_.base;
	}
}
