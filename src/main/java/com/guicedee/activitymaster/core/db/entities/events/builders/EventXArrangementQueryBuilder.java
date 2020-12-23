package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangement;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangement_;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangementsSecurityToken;

import jakarta.persistence.metamodel.Attribute;

public class EventXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Arrangement, EventXArrangementQueryBuilder,
				                                              EventXArrangement, java.util.UUID, EventXArrangementsSecurityToken>
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
