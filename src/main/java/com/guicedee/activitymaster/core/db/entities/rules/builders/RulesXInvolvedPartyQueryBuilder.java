package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.metamodel.Attribute;

public class RulesXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, InvolvedParty, RulesXInvolvedPartyQueryBuilder,
		RulesXInvolvedParty, java.util.UUID, RulesXInvolvedPartySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesXInvolvedParty_.rulesID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesXInvolvedParty_.involvedPartyID;
	}
}
