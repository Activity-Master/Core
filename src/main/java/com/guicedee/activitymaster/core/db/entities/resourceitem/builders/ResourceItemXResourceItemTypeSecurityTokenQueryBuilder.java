package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemXResourceItemTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXResourceItemTypeSecurityTokenQueryBuilder, ResourceItemXResourceItemTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXResourceItemTypeSecurityToken_.base;
	}
}
