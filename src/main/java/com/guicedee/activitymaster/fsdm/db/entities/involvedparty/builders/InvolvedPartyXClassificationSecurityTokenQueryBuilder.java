package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class InvolvedPartyXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXClassificationSecurityTokenQueryBuilder, InvolvedPartyXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXClassificationSecurityToken_.base;
	}
}
