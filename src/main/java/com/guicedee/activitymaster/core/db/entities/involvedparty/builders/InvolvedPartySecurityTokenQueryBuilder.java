package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartySecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartySecurityTokenQueryBuilder, InvolvedPartySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartySecurityToken_.base;
	}
}
