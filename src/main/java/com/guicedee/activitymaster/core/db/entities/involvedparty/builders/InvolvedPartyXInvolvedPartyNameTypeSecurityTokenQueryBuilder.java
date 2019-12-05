package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyNameTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyNameTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder, InvolvedPartyXInvolvedPartyNameTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameTypeSecurityToken_.base;
	}
}
