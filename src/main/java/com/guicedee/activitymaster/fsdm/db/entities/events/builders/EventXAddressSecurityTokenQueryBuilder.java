package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXAddressSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXAddressSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class EventXAddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXAddressSecurityTokenQueryBuilder, EventXAddressSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXAddressSecurityToken_.base;
	}
}
