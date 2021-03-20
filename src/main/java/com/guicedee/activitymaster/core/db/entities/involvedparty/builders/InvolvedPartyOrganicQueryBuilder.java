package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganic;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilderTable<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, java.util.UUID>
{

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
