package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangement_;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangementsSecurityToken;

import javax.persistence.metamodel.Attribute;

public class EventXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Arrangement, EventXArrangementQueryBuilder,
				                                              EventXArrangement, Long, EventXArrangementsSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXArrangement_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXArrangement_.arrangementID;
	}
}
