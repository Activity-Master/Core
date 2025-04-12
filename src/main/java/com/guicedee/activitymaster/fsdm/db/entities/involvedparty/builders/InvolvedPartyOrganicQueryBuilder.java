package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganic;

import java.util.UUID;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, UUID,
				InvolvedPartyOrganicSecurityTokenQueryBuilder>
{
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
