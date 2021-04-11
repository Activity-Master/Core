package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.Attribute;

public class RulesXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXInvolvedPartySecurityTokenQueryBuilder, RulesXInvolvedPartySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXInvolvedPartySecurityToken_.base;
	}
}
