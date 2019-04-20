package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemDataXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemDataXClassificationSecurityTokenQueryBuilder, ResourceItemDataXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemDataXClassificationSecurityToken_.base;
	}
}
