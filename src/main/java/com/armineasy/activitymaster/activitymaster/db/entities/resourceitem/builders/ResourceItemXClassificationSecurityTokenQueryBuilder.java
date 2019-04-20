package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXClassificationSecurityTokenQueryBuilder, ResourceItemXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXClassificationSecurityToken_.base;
	}
}
