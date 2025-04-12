package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class InvolvedPartyXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<InvolvedPartyXResourceItemSecurityTokenQueryBuilder, InvolvedPartyXResourceItemSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return InvolvedPartyXResourceItemSecurityToken_.base;
	}
}
