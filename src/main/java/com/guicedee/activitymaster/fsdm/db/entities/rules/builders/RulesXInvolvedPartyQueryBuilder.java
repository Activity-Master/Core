package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, InvolvedParty, RulesXInvolvedPartyQueryBuilder,
		RulesXInvolvedParty, java.util.UUID>
{
	@Override
	public SingularAttribute<RulesXInvolvedParty, Rules> getPrimaryAttribute()
	{
		return RulesXInvolvedParty_.rulesID;
	}

	@Override
	public SingularAttribute<RulesXInvolvedParty, InvolvedParty> getSecondaryAttribute()
	{
		return RulesXInvolvedParty_.involvedPartyID;
	}
}
