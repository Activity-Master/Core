package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItem_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItem, ResourceItem, ResourceItemXResourceItemQueryBuilder,
		ResourceItemXResourceItem, Long, ResourceItemXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ResourceItemXResourceItem_.parentResourceItemID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return ResourceItemXResourceItem_.childResourceItemID;
	}
}
