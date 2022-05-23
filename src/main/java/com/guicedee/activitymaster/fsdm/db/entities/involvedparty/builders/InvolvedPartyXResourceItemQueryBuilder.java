package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, ResourceItem, InvolvedPartyXResourceItemQueryBuilder,
		InvolvedPartyXResourceItem, java.lang.String>
{
	@Override
	public SingularAttribute<InvolvedPartyXResourceItem, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXResourceItem_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return InvolvedPartyXResourceItem_.resourceItemID;
	}
}
