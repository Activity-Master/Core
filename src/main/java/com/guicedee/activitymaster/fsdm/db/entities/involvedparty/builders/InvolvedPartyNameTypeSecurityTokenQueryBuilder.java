package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNameTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNameTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyNameTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyNameTypeSecurityTokenQueryBuilder, InvolvedPartyNameTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyNameTypeSecurityToken_.base;
	}
}
