package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ResourceItemXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXResourceItemSecurityTokenQueryBuilder, ResourceItemXResourceItemSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXResourceItemSecurityToken_.base;
	}
}
