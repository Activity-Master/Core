package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNonOrganic;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilderTable<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, java.util.UUID>
{

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
