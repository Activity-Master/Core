package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXProductSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class RulesXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<RulesXProductSecurityTokenQueryBuilder, RulesXProductSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return RulesXProductSecurityToken_.base;
	}
}
