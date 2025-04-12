package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXRulesSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXRulesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class InvolvedPartyXRulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXRulesSecurityTokenQueryBuilder, InvolvedPartyXRulesSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXRulesSecurityToken_.base;
	}
}
