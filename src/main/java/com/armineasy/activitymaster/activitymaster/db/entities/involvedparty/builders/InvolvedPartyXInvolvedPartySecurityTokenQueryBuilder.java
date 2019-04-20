package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder, InvolvedPartyXInvolvedPartySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXInvolvedPartySecurityToken_.base;
	}
}
