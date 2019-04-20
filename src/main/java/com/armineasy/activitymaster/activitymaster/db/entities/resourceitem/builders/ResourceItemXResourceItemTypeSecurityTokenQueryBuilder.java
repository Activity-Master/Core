package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken_;

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
