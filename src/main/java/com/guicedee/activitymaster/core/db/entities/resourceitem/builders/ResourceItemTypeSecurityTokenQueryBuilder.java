package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemTypeSecurityTokenQueryBuilder, ResourceItemTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemTypeSecurityToken_.base;
	}
}
