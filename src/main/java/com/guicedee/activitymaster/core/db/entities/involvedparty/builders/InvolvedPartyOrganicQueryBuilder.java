package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganic;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilderTable<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, Long, InvolvedPartyOrganicSecurityToken>
{

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
