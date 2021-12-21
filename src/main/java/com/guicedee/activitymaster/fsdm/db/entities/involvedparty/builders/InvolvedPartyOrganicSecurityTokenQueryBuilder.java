package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyOrganicSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyOrganicSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyOrganicSecurityTokenQueryBuilder, InvolvedPartyOrganicSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyOrganicSecurityToken_.base;
	}
}
