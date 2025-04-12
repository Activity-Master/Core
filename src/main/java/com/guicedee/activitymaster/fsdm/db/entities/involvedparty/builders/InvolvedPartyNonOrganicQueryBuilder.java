package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNonOrganic;

import java.util.UUID;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, UUID,
		InvolvedPartyNonOrganicSecurityTokenQueryBuilder>
{
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
