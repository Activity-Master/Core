package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken_;

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
