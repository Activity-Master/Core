package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartySecurityToken_;

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
