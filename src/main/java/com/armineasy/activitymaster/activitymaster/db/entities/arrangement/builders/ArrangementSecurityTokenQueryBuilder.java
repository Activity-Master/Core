package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementSecurityTokenQueryBuilder, ArrangementSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementSecurityToken_.base;
	}
}
