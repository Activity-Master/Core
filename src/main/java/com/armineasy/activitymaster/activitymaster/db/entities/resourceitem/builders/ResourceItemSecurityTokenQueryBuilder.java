package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemSecurityTokenQueryBuilder, ResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemSecurityToken_.base;
	}
}
