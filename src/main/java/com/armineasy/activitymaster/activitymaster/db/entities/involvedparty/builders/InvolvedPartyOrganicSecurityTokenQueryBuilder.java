package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyOrganicSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyOrganicSecurityTokenQueryBuilder, InvolvedPartyOrganicSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyOrganicSecurityToken_.base;
	}
}
