package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNonOrganicSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyNonOrganicSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyNonOrganicSecurityTokenQueryBuilder, InvolvedPartyNonOrganicSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyNonOrganicSecurityToken_.base;
	}
}
