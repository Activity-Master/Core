package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Classification, InvolvedPartyXClassificationQueryBuilder,
						                                              InvolvedPartyXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<InvolvedPartyXClassification, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXClassification_.involvedPartyID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return InvolvedPartyXClassification_.classificationID;
	}
}
