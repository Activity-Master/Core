package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ArrangementXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXClassificationSecurityTokenQueryBuilder, ArrangementXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXClassificationSecurityToken_.base;
	}
}
