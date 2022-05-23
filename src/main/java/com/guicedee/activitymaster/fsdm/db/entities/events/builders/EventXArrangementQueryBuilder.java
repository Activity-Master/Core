package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Arrangement, EventXArrangementQueryBuilder,
		EventXArrangement, java.lang.String>
{
	@Override
	public SingularAttribute<EventXArrangement, Event> getPrimaryAttribute()
	{
		return EventXArrangement_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXArrangement, Arrangement> getSecondaryAttribute()
	{
		return EventXArrangement_.arrangementID;
	}
}
