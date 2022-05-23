package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyTypeSecurityTokenQueryBuilder, InvolvedPartyTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyTypeSecurityToken_.base;
	}
}
