package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class EventXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, InvolvedParty, EventXInvolvedPartyQueryBuilder,
		EventXInvolvedParty, UUID,EventXInvolvedPartySecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<EventXInvolvedParty, Event> getPrimaryAttribute()
	{
		return EventXInvolvedParty_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXInvolvedParty, InvolvedParty> getSecondaryAttribute()
	{
		return EventXInvolvedParty_.involvedPartyID;
	}
}
