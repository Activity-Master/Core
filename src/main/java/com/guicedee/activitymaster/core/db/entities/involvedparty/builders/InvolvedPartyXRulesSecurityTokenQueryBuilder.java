package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXRulesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXRulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXRulesSecurityTokenQueryBuilder, InvolvedPartyXRulesSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXRulesSecurityToken_.base;
	}
}
