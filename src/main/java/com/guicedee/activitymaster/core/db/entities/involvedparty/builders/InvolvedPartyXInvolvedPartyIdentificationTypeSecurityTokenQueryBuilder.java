package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken_.base;
	}
}
