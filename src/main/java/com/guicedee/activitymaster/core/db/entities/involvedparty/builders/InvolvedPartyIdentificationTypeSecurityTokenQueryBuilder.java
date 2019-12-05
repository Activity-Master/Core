package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyIdentificationTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyIdentificationTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, InvolvedPartyIdentificationTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyIdentificationTypeSecurityToken_.base;
	}
}
