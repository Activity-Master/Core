package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyTypeSecurityToken_;

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
