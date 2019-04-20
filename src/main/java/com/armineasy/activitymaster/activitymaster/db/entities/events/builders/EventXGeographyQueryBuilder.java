package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXGeography_;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;

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
