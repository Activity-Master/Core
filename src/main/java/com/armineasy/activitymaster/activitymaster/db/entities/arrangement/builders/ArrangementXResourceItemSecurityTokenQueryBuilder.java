package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXResourceItemSecurityTokenQueryBuilder, ArrangementXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXResourceItemSecurityToken_.base;
	}
}
