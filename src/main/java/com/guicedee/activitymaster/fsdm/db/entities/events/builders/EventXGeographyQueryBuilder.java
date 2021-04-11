package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Geography, EventXGeographyQueryBuilder,
				                                              EventXGeography, java.util.UUID>
{
	@Override
	public SingularAttribute<EventXGeography, Event> getPrimaryAttribute()
	{
		return EventXGeography_.eventID;
	}

	@Override
	public SingularAttribute<EventXGeography, Geography> getSecondaryAttribute()
	{
		return EventXGeography_.geographyID;
	}
}
