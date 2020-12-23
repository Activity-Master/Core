package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemSecurityTokenQueryBuilder, ResourceItemSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemSecurityToken_.base;
	}
}
