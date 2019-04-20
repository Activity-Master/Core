package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXClassification_;

import javax.persistence.metamodel.Attribute;

public class EventXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Classification, EventXClassificationQueryBuilder,
				                                              EventXClassification, Long, EventXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXClassification_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXClassification_.classificationID;
	}
}
