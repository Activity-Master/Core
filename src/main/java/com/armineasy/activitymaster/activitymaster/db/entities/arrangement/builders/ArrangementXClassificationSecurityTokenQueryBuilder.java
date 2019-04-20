package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXClassificationSecurityTokenQueryBuilder, ArrangementXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXClassificationSecurityToken_.base;
	}
}
