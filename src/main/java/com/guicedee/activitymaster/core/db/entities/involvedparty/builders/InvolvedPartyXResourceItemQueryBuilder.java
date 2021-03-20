package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class InvolvedPartyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, ResourceItem, InvolvedPartyXResourceItemQueryBuilder,
						                                              InvolvedPartyXResourceItem, java.util.UUID>
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
