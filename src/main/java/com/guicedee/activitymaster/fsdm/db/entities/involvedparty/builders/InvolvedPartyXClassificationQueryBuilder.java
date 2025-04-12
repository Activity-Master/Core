package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Classification, InvolvedPartyXClassificationQueryBuilder,
		InvolvedPartyXClassification, UUID,InvolvedPartyXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXClassification, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXClassification_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return InvolvedPartyXClassification_.classificationID;
	}
}
