package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken_;

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
