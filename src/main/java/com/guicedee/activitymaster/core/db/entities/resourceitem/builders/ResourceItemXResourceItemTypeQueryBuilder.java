package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;

import javax.persistence.metamodel.Attribute;

public class ResourceItemXResourceItemTypeQueryBuilder
		extends QueryBuilderRelationship<ResourceItem, ResourceItemType,
						                                ResourceItemXResourceItemTypeQueryBuilder,
				                                ResourceItemXResourceItemType, Long,
				                                ResourceItemXResourceItemTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ResourceItemXResourceItemType_.resourceItemID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ResourceItemXResourceItemType_.resourceItemTypeID;
	}
}
