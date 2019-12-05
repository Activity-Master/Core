package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXAddressSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXAddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXAddressSecurityTokenQueryBuilder, EventXAddressSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXAddressSecurityToken_.base;
	}
}
