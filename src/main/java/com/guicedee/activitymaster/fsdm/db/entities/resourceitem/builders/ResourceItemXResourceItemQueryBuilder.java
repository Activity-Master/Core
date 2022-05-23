package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ResourceItemXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItem, ResourceItem, ResourceItemXResourceItemQueryBuilder,
		ResourceItemXResourceItem, java.lang.String>
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
