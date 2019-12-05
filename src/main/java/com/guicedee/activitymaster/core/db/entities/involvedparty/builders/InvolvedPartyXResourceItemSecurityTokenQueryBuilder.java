package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXResourceItemSecurityTokenQueryBuilder, InvolvedPartyXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXResourceItemSecurityToken_.base;
	}
}
