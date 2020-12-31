package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedParty;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedPartySecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXInvolvedParty_;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, InvolvedParty, ArrangementXInvolvedPartyQueryBuilder,
						                                              ArrangementXInvolvedParty, java.util.UUID, ArrangementXInvolvedPartySecurityToken>
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
