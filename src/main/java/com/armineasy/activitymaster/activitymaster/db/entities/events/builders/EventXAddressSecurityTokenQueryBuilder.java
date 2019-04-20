package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXAddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXAddressSecurityToken_;

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
