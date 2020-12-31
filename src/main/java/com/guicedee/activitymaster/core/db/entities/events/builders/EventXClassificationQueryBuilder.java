package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassification;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXClassification_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Classification, EventXClassificationQueryBuilder,
				                                              EventXClassification, java.util.UUID, EventXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<EventXClassification, Event> getPrimaryAttribute()
	{
		return EventXClassification_.eventID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return EventXClassification_.classificationID;
	}
}
