package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassification;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassification_;

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
