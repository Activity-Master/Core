package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangementSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangementSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXArrangementSecurityTokenQueryBuilder, ArrangementXArrangementSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXArrangementSecurityToken_.base;
	}
}
