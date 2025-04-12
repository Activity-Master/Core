package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyIdentificationTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyIdentificationTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder, InvolvedPartyIdentificationTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyIdentificationTypeSecurityToken_.base;
	}
}
