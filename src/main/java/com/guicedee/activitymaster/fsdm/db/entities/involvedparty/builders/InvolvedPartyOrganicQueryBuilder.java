package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganic;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, String,
				InvolvedPartyOrganicSecurityTokenQueryBuilder>
{
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
