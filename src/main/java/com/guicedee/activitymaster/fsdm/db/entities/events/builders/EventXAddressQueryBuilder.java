package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Address, EventXAddressQueryBuilder,
		EventXAddress, java.lang.String,EventXAddressSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<EventXAddress, Event> getPrimaryAttribute()
	{
		return EventXAddress_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXAddress, Address> getSecondaryAttribute()
	{
		return EventXAddress_.addressID;
	}
}
