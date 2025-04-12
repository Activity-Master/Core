package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ArrangementXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, ResourceItem, ArrangementXResourceItemQueryBuilder,
		ArrangementXResourceItem, UUID,ArrangementXResourceItemSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ArrangementXResourceItem, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXResourceItem_.arrangementID;
	}
	
	@Override
	public SingularAttribute<ArrangementXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ArrangementXResourceItem_.resourceItemID;
	}
}
