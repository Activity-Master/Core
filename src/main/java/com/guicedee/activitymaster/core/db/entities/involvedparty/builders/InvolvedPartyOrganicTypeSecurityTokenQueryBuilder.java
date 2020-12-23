package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyOrganicTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyOrganicTypeSecurityTokenQueryBuilder, InvolvedPartyOrganicTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyOrganicTypeSecurityToken_.base;
	}
}
