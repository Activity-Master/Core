package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganic;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilder<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, Long, InvolvedPartyNonOrganicSecurityToken>
{

	@Override
	protected boolean isIdGenerated()
	{
		return false;
	}
}
