package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganic;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken;

public class InvolvedPartyOrganicQueryBuilder
		extends QueryBuilder<InvolvedPartyOrganicQueryBuilder, InvolvedPartyOrganic, Long, InvolvedPartyOrganicSecurityToken>
{

	@Override
	protected boolean isIdGenerated()
	{
		return false;
	}
}
