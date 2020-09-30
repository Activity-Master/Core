package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganic;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilderTable<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, Long, InvolvedPartyNonOrganicSecurityToken>
{

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
