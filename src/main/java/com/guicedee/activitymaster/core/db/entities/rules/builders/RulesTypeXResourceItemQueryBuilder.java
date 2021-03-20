package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesTypeXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<RulesType, ResourceItem, RulesTypeXResourceItemQueryBuilder,
		RulesTypeXResourceItem, java.util.UUID>
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
