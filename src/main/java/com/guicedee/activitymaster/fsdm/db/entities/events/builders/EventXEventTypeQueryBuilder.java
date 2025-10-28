package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class EventXEventTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Event,
		EventType,
		EventXEventTypeQueryBuilder,
		EventXEventType,
		UUID,
		EventXEventTypeSecurityTokenQueryBuilder
		>
{
	@Override
	public SingularAttribute<EventXEventType, Event> getPrimaryAttribute()
	{
		return EventXEventType_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXEventType, EventType> getSecondaryAttribute()
	{
		return EventXEventType_.eventTypeID;
	}
	
	@Override
	public EventXEventTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(EventXEventType_.EVENT_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(EventType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
		}
		return this;
	}
}
