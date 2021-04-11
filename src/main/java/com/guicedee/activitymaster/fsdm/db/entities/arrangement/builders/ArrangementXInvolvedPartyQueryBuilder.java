package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, InvolvedParty, ArrangementXInvolvedPartyQueryBuilder,
						                                              ArrangementXInvolvedParty, java.util.UUID>
{
	@Override
	public SingularAttribute<ArrangementXInvolvedParty, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXInvolvedParty_.arrangementID;
	}

	@Override
	public SingularAttribute<ArrangementXInvolvedParty, InvolvedParty> getSecondaryAttribute()
	{
		return ArrangementXInvolvedParty_.involvedPartyID;
	}
}
