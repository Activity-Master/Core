package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXProductSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXProductSecurityTokenQueryBuilder, InvolvedPartyXProductSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXProductSecurityToken_.base;
	}
}
