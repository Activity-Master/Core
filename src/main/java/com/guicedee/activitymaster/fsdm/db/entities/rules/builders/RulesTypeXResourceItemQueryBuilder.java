package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesTypeXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<RulesType, ResourceItem, RulesTypeXResourceItemQueryBuilder,
		RulesTypeXResourceItem, java.lang.String>
{
	@Override
	public SingularAttribute<RulesTypeXResourceItem, RulesType> getPrimaryAttribute()
	{
		return RulesTypeXResourceItem_.rulesTypeID;
	}
	
	@Override
	public SingularAttribute<RulesTypeXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return RulesTypeXResourceItem_.resourceItemID;
	}
}
