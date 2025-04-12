package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesXInvolvedPartySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class RulesXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXInvolvedPartySecurityTokenQueryBuilder, RulesXInvolvedPartySecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXInvolvedPartySecurityToken_.base;
	}
}
