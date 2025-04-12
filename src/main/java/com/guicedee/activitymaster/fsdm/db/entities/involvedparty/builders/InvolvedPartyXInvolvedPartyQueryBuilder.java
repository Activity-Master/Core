package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXInvolvedPartyQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, InvolvedParty, InvolvedPartyXInvolvedPartyQueryBuilder,
		InvolvedPartyXInvolvedParty, UUID,InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedParty, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedParty_.parentInvolvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedParty, InvolvedParty> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedParty_.childInvolvedPartyID;
	}
}
