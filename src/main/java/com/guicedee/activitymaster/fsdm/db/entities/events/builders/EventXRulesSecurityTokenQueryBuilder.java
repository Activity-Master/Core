package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXRulesSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXRulesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class EventXRulesSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXRulesSecurityTokenQueryBuilder, EventXRulesSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXRulesSecurityToken_.base;
	}
}
