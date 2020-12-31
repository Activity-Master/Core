package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXProduct;
import com.guicedee.activitymaster.core.db.entities.events.EventXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXProduct_;
import com.guicedee.activitymaster.core.db.entities.product.Product;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Product, EventXProductQueryBuilder,
				                                              EventXProduct, java.util.UUID, EventXProductSecurityToken>
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
