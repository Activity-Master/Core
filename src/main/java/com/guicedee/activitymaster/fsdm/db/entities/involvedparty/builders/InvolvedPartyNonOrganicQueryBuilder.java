package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNonOrganic;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilderTable<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, java.lang.String>
{
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
