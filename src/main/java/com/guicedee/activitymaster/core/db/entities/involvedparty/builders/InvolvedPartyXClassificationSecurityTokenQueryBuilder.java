package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXClassificationSecurityTokenQueryBuilder, InvolvedPartyXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXClassificationSecurityToken_.base;
	}
}
