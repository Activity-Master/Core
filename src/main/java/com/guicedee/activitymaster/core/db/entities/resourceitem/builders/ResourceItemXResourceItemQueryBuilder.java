package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItem_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ResourceItemXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItem, ResourceItem, ResourceItemXResourceItemQueryBuilder,
		ResourceItemXResourceItem, java.util.UUID, ResourceItemXResourceItemSecurityToken>
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
