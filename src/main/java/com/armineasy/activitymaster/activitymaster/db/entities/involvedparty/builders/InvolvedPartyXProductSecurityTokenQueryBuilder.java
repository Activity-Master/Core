package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProductSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXProductSecurityTokenQueryBuilder, InvolvedPartyXProductSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXProductSecurityToken_.base;
	}
}
