package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganic;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilder<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, Long, InvolvedPartyOrganicSecurityToken>
{

	@Override
	protected boolean isIdGenerated()
	{
		return false;
	}
}
