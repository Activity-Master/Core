package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyOrganicTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyOrganicTypeSecurityTokenQueryBuilder, InvolvedPartyOrganicTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyOrganicTypeSecurityToken_.base;
	}
}
