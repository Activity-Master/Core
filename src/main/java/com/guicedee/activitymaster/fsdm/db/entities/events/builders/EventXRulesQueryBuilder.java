package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class EventXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Rules, EventXRulesQueryBuilder,
		EventXRules, UUID,EventXRulesSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<EventXRules, Event> getPrimaryAttribute()
	{
		return EventXRules_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXRules, Rules> getSecondaryAttribute()
	{
		return EventXRules_.rulesID;
	}
}
