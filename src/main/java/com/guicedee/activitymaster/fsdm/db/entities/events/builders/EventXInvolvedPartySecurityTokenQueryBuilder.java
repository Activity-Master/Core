package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXInvolvedPartySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXInvolvedPartySecurityTokenQueryBuilder, EventXInvolvedPartySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXInvolvedPartySecurityToken_.base;
	}
}
