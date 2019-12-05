package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken_.base;
	}
}
