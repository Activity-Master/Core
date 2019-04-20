package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNonOrganic;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken;

public class InvolvedPartyNonOrganicQueryBuilder
		extends QueryBuilder<InvolvedPartyNonOrganicQueryBuilder, InvolvedPartyNonOrganic, Long, InvolvedPartyNonOrganicSecurityToken>
{

	@Override
	protected boolean isIdGenerated()
	{
		return false;
	}
}
