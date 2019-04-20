package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyTypeSecurityTokenQueryBuilder, InvolvedPartyTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyTypeSecurityToken_.base;
	}
}
