package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementTypeXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ArrangementTypeXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementTypeXClassificationSecurityTokenQueryBuilder, ArrangementTypeXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementTypeXClassificationSecurityToken_.base;
	}
}
