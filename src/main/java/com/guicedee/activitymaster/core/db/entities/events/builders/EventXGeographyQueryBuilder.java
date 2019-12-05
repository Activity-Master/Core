package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXGeography;
import com.guicedee.activitymaster.core.db.entities.events.EventXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXGeography_;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;

import javax.persistence.metamodel.Attribute;

public class EventXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Geography, EventXGeographyQueryBuilder,
				                                              EventXGeography, Long, EventXGeographySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXGeography_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXGeography_.geographyID;
	}
}
