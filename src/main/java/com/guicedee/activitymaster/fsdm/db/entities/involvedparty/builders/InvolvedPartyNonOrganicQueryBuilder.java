package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNonOrganic;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, java.lang.String,
		InvolvedPartyNonOrganicSecurityTokenQueryBuilder>
{
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
