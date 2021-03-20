package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Product, EventXProductQueryBuilder,
				                                              EventXProduct, java.util.UUID>
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
