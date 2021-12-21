package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartySecurityToken_;
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
