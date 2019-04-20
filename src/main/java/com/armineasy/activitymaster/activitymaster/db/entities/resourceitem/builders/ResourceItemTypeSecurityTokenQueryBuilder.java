package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemTypeSecurityToken_;

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
