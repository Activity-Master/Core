package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXInvolvedParty_;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, InvolvedParty, EventXInvolvedPartyQueryBuilder,
				                                              EventXInvolvedParty, java.util.UUID, EventXInvolvedPartySecurityToken>
{
	@Override
	public SingularAttribute<EventXInvolvedParty, Event> getPrimaryAttribute()
	{
		return EventXInvolvedParty_.eventID;
	}

	@Override
	public  SingularAttribute<EventXInvolvedParty, InvolvedParty> getSecondaryAttribute()
	{
		return EventXInvolvedParty_.involvedPartyID;
	}
}
