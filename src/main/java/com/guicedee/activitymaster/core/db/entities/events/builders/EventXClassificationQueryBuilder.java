package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Classification, EventXClassificationQueryBuilder,
				                                              EventXClassification, java.util.UUID>
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
