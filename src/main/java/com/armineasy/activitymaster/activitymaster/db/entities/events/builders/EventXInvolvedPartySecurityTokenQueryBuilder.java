package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXInvolvedPartySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXInvolvedPartySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXInvolvedPartySecurityTokenQueryBuilder, EventXInvolvedPartySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXInvolvedPartySecurityToken_.base;
	}
}
