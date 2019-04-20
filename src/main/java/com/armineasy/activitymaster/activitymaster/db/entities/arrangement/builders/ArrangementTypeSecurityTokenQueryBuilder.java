package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementTypeSecurityTokenQueryBuilder, ArrangementTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementTypeSecurityToken_.base;
	}
}
