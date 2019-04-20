package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyNonOrganicSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyNonOrganicSecurityTokenQueryBuilder, InvolvedPartyNonOrganicSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyNonOrganicSecurityToken_.base;
	}
}
