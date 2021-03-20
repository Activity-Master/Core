package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Arrangement, EventXArrangementQueryBuilder,
				                                              EventXArrangement, java.util.UUID>
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
