package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangementTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangementTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXArrangementTypeSecurityTokenQueryBuilder, ArrangementXArrangementTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXArrangementTypeSecurityToken_.base;
	}
}
