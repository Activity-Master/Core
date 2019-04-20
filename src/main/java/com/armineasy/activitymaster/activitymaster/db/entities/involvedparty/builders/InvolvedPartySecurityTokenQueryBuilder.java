package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartySecurityTokenQueryBuilder, InvolvedPartySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartySecurityToken_.base;
	}
}
