package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXProductSecurityToken_;

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
