package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataSecurityToken_;

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
