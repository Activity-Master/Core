package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder, InvolvedPartyXInvolvedPartyTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXInvolvedPartyTypeSecurityToken_.base;
	}
}
