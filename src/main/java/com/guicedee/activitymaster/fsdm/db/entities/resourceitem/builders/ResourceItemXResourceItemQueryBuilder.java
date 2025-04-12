package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ResourceItemXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItem, ResourceItem, ResourceItemXResourceItemQueryBuilder,
		ResourceItemXResourceItem, UUID,ResourceItemXResourceItemSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ResourceItemXResourceItem, ResourceItem> getPrimaryAttribute()
	{
		return ResourceItemXResourceItem_.parentResourceItemID;
	}
	
	@Override
	public SingularAttribute<ResourceItemXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ResourceItemXResourceItem_.childResourceItemID;
	}
}
