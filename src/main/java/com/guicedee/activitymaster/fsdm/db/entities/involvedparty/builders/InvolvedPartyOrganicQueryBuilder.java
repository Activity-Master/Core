package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganic;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilderTable<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, java.lang.String,
		InvolvedPartyOrganicSecurityTokenQueryBuilder>
{
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
