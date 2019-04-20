package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNameTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNameTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyNameTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyNameTypeSecurityTokenQueryBuilder, InvolvedPartyNameTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyNameTypeSecurityToken_.base;
	}
}
