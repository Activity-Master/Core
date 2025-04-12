package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class RulesXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, ResourceItem, RulesXResourceItemQueryBuilder,
		RulesXResourceItem, UUID,RulesXResourceItemSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<RulesXResourceItem, Rules> getPrimaryAttribute()
	{
		return RulesXResourceItem_.rulesID;
	}
	
	@Override
	public SingularAttribute<RulesXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return RulesXResourceItem_.resourceItemID;
	}
}
