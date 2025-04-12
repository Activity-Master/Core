package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganicTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class InvolvedPartyOrganicTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyOrganicTypeSecurityTokenQueryBuilder, InvolvedPartyOrganicTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyOrganicTypeSecurityToken_.base;
	}
}
