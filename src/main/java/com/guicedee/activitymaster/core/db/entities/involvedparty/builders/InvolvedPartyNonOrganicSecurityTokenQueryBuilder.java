package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyNonOrganicSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyNonOrganicSecurityTokenQueryBuilder, InvolvedPartyNonOrganicSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyNonOrganicSecurityToken_.base;
	}
}
