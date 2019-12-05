package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemDataSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemDataSecurityTokenQueryBuilder, ResourceItemDataSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemDataSecurityToken_.base;
	}
}
