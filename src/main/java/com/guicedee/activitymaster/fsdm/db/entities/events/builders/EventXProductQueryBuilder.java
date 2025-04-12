package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class EventXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Product, EventXProductQueryBuilder,
		EventXProduct, UUID,EventXProductSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<EventXProduct, Event> getPrimaryAttribute()
	{
		return EventXProduct_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXProduct, Product> getSecondaryAttribute()
	{
		return EventXProduct_.productID;
	}
}
